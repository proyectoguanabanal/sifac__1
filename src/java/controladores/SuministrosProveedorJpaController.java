/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Suministros;
import entidades.Proveedor;
import entidades.SuministrosProveedor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class SuministrosProveedorJpaController implements Serializable {

    public SuministrosProveedorJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SuministrosProveedor suministrosProveedor) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Suministros codSuministros = suministrosProveedor.getCodSuministros();
            if (codSuministros != null) {
                codSuministros = em.getReference(codSuministros.getClass(), codSuministros.getCodSuministros());
                suministrosProveedor.setCodSuministros(codSuministros);
            }
            Proveedor codProveedor = suministrosProveedor.getCodProveedor();
            if (codProveedor != null) {
                codProveedor = em.getReference(codProveedor.getClass(), codProveedor.getCodProveedor());
                suministrosProveedor.setCodProveedor(codProveedor);
            }
            em.persist(suministrosProveedor);
            if (codSuministros != null) {
                codSuministros.getSuministrosProveedorList().add(suministrosProveedor);
                codSuministros = em.merge(codSuministros);
            }
            if (codProveedor != null) {
                codProveedor.getSuministrosProveedorList().add(suministrosProveedor);
                codProveedor = em.merge(codProveedor);
            }
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

    public void edit(SuministrosProveedor suministrosProveedor) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            SuministrosProveedor persistentSuministrosProveedor = em.find(SuministrosProveedor.class, suministrosProveedor.getCodSuministrosProveedor());
            Suministros codSuministrosOld = persistentSuministrosProveedor.getCodSuministros();
            Suministros codSuministrosNew = suministrosProveedor.getCodSuministros();
            Proveedor codProveedorOld = persistentSuministrosProveedor.getCodProveedor();
            Proveedor codProveedorNew = suministrosProveedor.getCodProveedor();
            if (codSuministrosNew != null) {
                codSuministrosNew = em.getReference(codSuministrosNew.getClass(), codSuministrosNew.getCodSuministros());
                suministrosProveedor.setCodSuministros(codSuministrosNew);
            }
            if (codProveedorNew != null) {
                codProveedorNew = em.getReference(codProveedorNew.getClass(), codProveedorNew.getCodProveedor());
                suministrosProveedor.setCodProveedor(codProveedorNew);
            }
            suministrosProveedor = em.merge(suministrosProveedor);
            if (codSuministrosOld != null && !codSuministrosOld.equals(codSuministrosNew)) {
                codSuministrosOld.getSuministrosProveedorList().remove(suministrosProveedor);
                codSuministrosOld = em.merge(codSuministrosOld);
            }
            if (codSuministrosNew != null && !codSuministrosNew.equals(codSuministrosOld)) {
                codSuministrosNew.getSuministrosProveedorList().add(suministrosProveedor);
                codSuministrosNew = em.merge(codSuministrosNew);
            }
            if (codProveedorOld != null && !codProveedorOld.equals(codProveedorNew)) {
                codProveedorOld.getSuministrosProveedorList().remove(suministrosProveedor);
                codProveedorOld = em.merge(codProveedorOld);
            }
            if (codProveedorNew != null && !codProveedorNew.equals(codProveedorOld)) {
                codProveedorNew.getSuministrosProveedorList().add(suministrosProveedor);
                codProveedorNew = em.merge(codProveedorNew);
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
                Integer id = suministrosProveedor.getCodSuministrosProveedor();
                if (findSuministrosProveedor(id) == null) {
                    throw new NonexistentEntityException("The suministrosProveedor with id " + id + " no longer exists.");
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
            SuministrosProveedor suministrosProveedor;
            try {
                suministrosProveedor = em.getReference(SuministrosProveedor.class, id);
                suministrosProveedor.getCodSuministrosProveedor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The suministrosProveedor with id " + id + " no longer exists.", enfe);
            }
            Suministros codSuministros = suministrosProveedor.getCodSuministros();
            if (codSuministros != null) {
                codSuministros.getSuministrosProveedorList().remove(suministrosProveedor);
                codSuministros = em.merge(codSuministros);
            }
            Proveedor codProveedor = suministrosProveedor.getCodProveedor();
            if (codProveedor != null) {
                codProveedor.getSuministrosProveedorList().remove(suministrosProveedor);
                codProveedor = em.merge(codProveedor);
            }
            em.remove(suministrosProveedor);
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

    public List<SuministrosProveedor> findSuministrosProveedorEntities() {
        return findSuministrosProveedorEntities(true, -1, -1);
    }

    public List<SuministrosProveedor> findSuministrosProveedorEntities(int maxResults, int firstResult) {
        return findSuministrosProveedorEntities(false, maxResults, firstResult);
    }

    private List<SuministrosProveedor> findSuministrosProveedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SuministrosProveedor.class));
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

    public SuministrosProveedor findSuministrosProveedor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SuministrosProveedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getSuministrosProveedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SuministrosProveedor> rt = cq.from(SuministrosProveedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
