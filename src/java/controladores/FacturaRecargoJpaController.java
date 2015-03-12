/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.PreexistingEntityException;
import controladores.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Recargo;
import entidades.Factura;
import entidades.FacturaRecargo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class FacturaRecargoJpaController implements Serializable {

    public FacturaRecargoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FacturaRecargo facturaRecargo) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Recargo codRecargo = facturaRecargo.getCodRecargo();
            if (codRecargo != null) {
                codRecargo = em.getReference(codRecargo.getClass(), codRecargo.getCodRecargo());
                facturaRecargo.setCodRecargo(codRecargo);
            }
            Factura codFactura = facturaRecargo.getCodFactura();
            if (codFactura != null) {
                codFactura = em.getReference(codFactura.getClass(), codFactura.getCodFactura());
                facturaRecargo.setCodFactura(codFactura);
            }
            em.persist(facturaRecargo);
            if (codRecargo != null) {
                codRecargo.getFacturaRecargoList().add(facturaRecargo);
                codRecargo = em.merge(codRecargo);
            }
            if (codFactura != null) {
                codFactura.getFacturaRecargoList().add(facturaRecargo);
                codFactura = em.merge(codFactura);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findFacturaRecargo(facturaRecargo.getCodFacturaRecargo()) != null) {
                throw new PreexistingEntityException("FacturaRecargo " + facturaRecargo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FacturaRecargo facturaRecargo) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            FacturaRecargo persistentFacturaRecargo = em.find(FacturaRecargo.class, facturaRecargo.getCodFacturaRecargo());
            Recargo codRecargoOld = persistentFacturaRecargo.getCodRecargo();
            Recargo codRecargoNew = facturaRecargo.getCodRecargo();
            Factura codFacturaOld = persistentFacturaRecargo.getCodFactura();
            Factura codFacturaNew = facturaRecargo.getCodFactura();
            if (codRecargoNew != null) {
                codRecargoNew = em.getReference(codRecargoNew.getClass(), codRecargoNew.getCodRecargo());
                facturaRecargo.setCodRecargo(codRecargoNew);
            }
            if (codFacturaNew != null) {
                codFacturaNew = em.getReference(codFacturaNew.getClass(), codFacturaNew.getCodFactura());
                facturaRecargo.setCodFactura(codFacturaNew);
            }
            facturaRecargo = em.merge(facturaRecargo);
            if (codRecargoOld != null && !codRecargoOld.equals(codRecargoNew)) {
                codRecargoOld.getFacturaRecargoList().remove(facturaRecargo);
                codRecargoOld = em.merge(codRecargoOld);
            }
            if (codRecargoNew != null && !codRecargoNew.equals(codRecargoOld)) {
                codRecargoNew.getFacturaRecargoList().add(facturaRecargo);
                codRecargoNew = em.merge(codRecargoNew);
            }
            if (codFacturaOld != null && !codFacturaOld.equals(codFacturaNew)) {
                codFacturaOld.getFacturaRecargoList().remove(facturaRecargo);
                codFacturaOld = em.merge(codFacturaOld);
            }
            if (codFacturaNew != null && !codFacturaNew.equals(codFacturaOld)) {
                codFacturaNew.getFacturaRecargoList().add(facturaRecargo);
                codFacturaNew = em.merge(codFacturaNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = facturaRecargo.getCodFacturaRecargo();
                if (findFacturaRecargo(id) == null) {
                    throw new NonexistentEntityException("The facturaRecargo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            FacturaRecargo facturaRecargo;
            try {
                facturaRecargo = em.getReference(FacturaRecargo.class, id);
                facturaRecargo.getCodFacturaRecargo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The facturaRecargo with id " + id + " no longer exists.", enfe);
            }
            Recargo codRecargo = facturaRecargo.getCodRecargo();
            if (codRecargo != null) {
                codRecargo.getFacturaRecargoList().remove(facturaRecargo);
                codRecargo = em.merge(codRecargo);
            }
            Factura codFactura = facturaRecargo.getCodFactura();
            if (codFactura != null) {
                codFactura.getFacturaRecargoList().remove(facturaRecargo);
                codFactura = em.merge(codFactura);
            }
            em.remove(facturaRecargo);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FacturaRecargo> findFacturaRecargoEntities() {
        return findFacturaRecargoEntities(true, -1, -1);
    }

    public List<FacturaRecargo> findFacturaRecargoEntities(int maxResults, int firstResult) {
        return findFacturaRecargoEntities(false, maxResults, firstResult);
    }

    private List<FacturaRecargo> findFacturaRecargoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FacturaRecargo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public FacturaRecargo findFacturaRecargo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FacturaRecargo.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaRecargoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FacturaRecargo> rt = cq.from(FacturaRecargo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
