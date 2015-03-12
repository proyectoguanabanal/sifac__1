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
import entidades.Reconexion;
import entidades.Factura;
import entidades.FacturaReconexion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class FacturaReconexionJpaController implements Serializable {

    public FacturaReconexionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FacturaReconexion facturaReconexion) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Reconexion codReconexion = facturaReconexion.getCodReconexion();
            if (codReconexion != null) {
                codReconexion = em.getReference(codReconexion.getClass(), codReconexion.getCodReconexion());
                facturaReconexion.setCodReconexion(codReconexion);
            }
            Factura codFactura = facturaReconexion.getCodFactura();
            if (codFactura != null) {
                codFactura = em.getReference(codFactura.getClass(), codFactura.getCodFactura());
                facturaReconexion.setCodFactura(codFactura);
            }
            em.persist(facturaReconexion);
            if (codReconexion != null) {
                codReconexion.getFacturaReconexionList().add(facturaReconexion);
                codReconexion = em.merge(codReconexion);
            }
            if (codFactura != null) {
                codFactura.getFacturaReconexionList().add(facturaReconexion);
                codFactura = em.merge(codFactura);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findFacturaReconexion(facturaReconexion.getCodFacturaReconexion()) != null) {
                throw new PreexistingEntityException("FacturaReconexion " + facturaReconexion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FacturaReconexion facturaReconexion) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            FacturaReconexion persistentFacturaReconexion = em.find(FacturaReconexion.class, facturaReconexion.getCodFacturaReconexion());
            Reconexion codReconexionOld = persistentFacturaReconexion.getCodReconexion();
            Reconexion codReconexionNew = facturaReconexion.getCodReconexion();
            Factura codFacturaOld = persistentFacturaReconexion.getCodFactura();
            Factura codFacturaNew = facturaReconexion.getCodFactura();
            if (codReconexionNew != null) {
                codReconexionNew = em.getReference(codReconexionNew.getClass(), codReconexionNew.getCodReconexion());
                facturaReconexion.setCodReconexion(codReconexionNew);
            }
            if (codFacturaNew != null) {
                codFacturaNew = em.getReference(codFacturaNew.getClass(), codFacturaNew.getCodFactura());
                facturaReconexion.setCodFactura(codFacturaNew);
            }
            facturaReconexion = em.merge(facturaReconexion);
            if (codReconexionOld != null && !codReconexionOld.equals(codReconexionNew)) {
                codReconexionOld.getFacturaReconexionList().remove(facturaReconexion);
                codReconexionOld = em.merge(codReconexionOld);
            }
            if (codReconexionNew != null && !codReconexionNew.equals(codReconexionOld)) {
                codReconexionNew.getFacturaReconexionList().add(facturaReconexion);
                codReconexionNew = em.merge(codReconexionNew);
            }
            if (codFacturaOld != null && !codFacturaOld.equals(codFacturaNew)) {
                codFacturaOld.getFacturaReconexionList().remove(facturaReconexion);
                codFacturaOld = em.merge(codFacturaOld);
            }
            if (codFacturaNew != null && !codFacturaNew.equals(codFacturaOld)) {
                codFacturaNew.getFacturaReconexionList().add(facturaReconexion);
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
                Integer id = facturaReconexion.getCodFacturaReconexion();
                if (findFacturaReconexion(id) == null) {
                    throw new NonexistentEntityException("The facturaReconexion with id " + id + " no longer exists.");
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
            FacturaReconexion facturaReconexion;
            try {
                facturaReconexion = em.getReference(FacturaReconexion.class, id);
                facturaReconexion.getCodFacturaReconexion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The facturaReconexion with id " + id + " no longer exists.", enfe);
            }
            Reconexion codReconexion = facturaReconexion.getCodReconexion();
            if (codReconexion != null) {
                codReconexion.getFacturaReconexionList().remove(facturaReconexion);
                codReconexion = em.merge(codReconexion);
            }
            Factura codFactura = facturaReconexion.getCodFactura();
            if (codFactura != null) {
                codFactura.getFacturaReconexionList().remove(facturaReconexion);
                codFactura = em.merge(codFactura);
            }
            em.remove(facturaReconexion);
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

    public List<FacturaReconexion> findFacturaReconexionEntities() {
        return findFacturaReconexionEntities(true, -1, -1);
    }

    public List<FacturaReconexion> findFacturaReconexionEntities(int maxResults, int firstResult) {
        return findFacturaReconexionEntities(false, maxResults, firstResult);
    }

    private List<FacturaReconexion> findFacturaReconexionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FacturaReconexion.class));
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

    public FacturaReconexion findFacturaReconexion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FacturaReconexion.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaReconexionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FacturaReconexion> rt = cq.from(FacturaReconexion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
