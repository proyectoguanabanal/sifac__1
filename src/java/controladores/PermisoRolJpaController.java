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
import entidades.Rol;
import entidades.Permiso;
import entidades.PermisoRol;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class PermisoRolJpaController implements Serializable {

    public PermisoRolJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PermisoRol permisoRol) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Rol codRol = permisoRol.getCodRol();
            if (codRol != null) {
                codRol = em.getReference(codRol.getClass(), codRol.getCodRol());
                permisoRol.setCodRol(codRol);
            }
            Permiso codPermiso = permisoRol.getCodPermiso();
            if (codPermiso != null) {
                codPermiso = em.getReference(codPermiso.getClass(), codPermiso.getCodPermiso());
                permisoRol.setCodPermiso(codPermiso);
            }
            em.persist(permisoRol);
            if (codRol != null) {
                codRol.getPermisoRolList().add(permisoRol);
                codRol = em.merge(codRol);
            }
            if (codPermiso != null) {
                codPermiso.getPermisoRolList().add(permisoRol);
                codPermiso = em.merge(codPermiso);
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

    public void edit(PermisoRol permisoRol) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PermisoRol persistentPermisoRol = em.find(PermisoRol.class, permisoRol.getCodPermisoRol());
            Rol codRolOld = persistentPermisoRol.getCodRol();
            Rol codRolNew = permisoRol.getCodRol();
            Permiso codPermisoOld = persistentPermisoRol.getCodPermiso();
            Permiso codPermisoNew = permisoRol.getCodPermiso();
            if (codRolNew != null) {
                codRolNew = em.getReference(codRolNew.getClass(), codRolNew.getCodRol());
                permisoRol.setCodRol(codRolNew);
            }
            if (codPermisoNew != null) {
                codPermisoNew = em.getReference(codPermisoNew.getClass(), codPermisoNew.getCodPermiso());
                permisoRol.setCodPermiso(codPermisoNew);
            }
            permisoRol = em.merge(permisoRol);
            if (codRolOld != null && !codRolOld.equals(codRolNew)) {
                codRolOld.getPermisoRolList().remove(permisoRol);
                codRolOld = em.merge(codRolOld);
            }
            if (codRolNew != null && !codRolNew.equals(codRolOld)) {
                codRolNew.getPermisoRolList().add(permisoRol);
                codRolNew = em.merge(codRolNew);
            }
            if (codPermisoOld != null && !codPermisoOld.equals(codPermisoNew)) {
                codPermisoOld.getPermisoRolList().remove(permisoRol);
                codPermisoOld = em.merge(codPermisoOld);
            }
            if (codPermisoNew != null && !codPermisoNew.equals(codPermisoOld)) {
                codPermisoNew.getPermisoRolList().add(permisoRol);
                codPermisoNew = em.merge(codPermisoNew);
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
                Integer id = permisoRol.getCodPermisoRol();
                if (findPermisoRol(id) == null) {
                    throw new NonexistentEntityException("The permisoRol with id " + id + " no longer exists.");
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
            PermisoRol permisoRol;
            try {
                permisoRol = em.getReference(PermisoRol.class, id);
                permisoRol.getCodPermisoRol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permisoRol with id " + id + " no longer exists.", enfe);
            }
            Rol codRol = permisoRol.getCodRol();
            if (codRol != null) {
                codRol.getPermisoRolList().remove(permisoRol);
                codRol = em.merge(codRol);
            }
            Permiso codPermiso = permisoRol.getCodPermiso();
            if (codPermiso != null) {
                codPermiso.getPermisoRolList().remove(permisoRol);
                codPermiso = em.merge(codPermiso);
            }
            em.remove(permisoRol);
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

    public List<PermisoRol> findPermisoRolEntities() {
        return findPermisoRolEntities(true, -1, -1);
    }

    public List<PermisoRol> findPermisoRolEntities(int maxResults, int firstResult) {
        return findPermisoRolEntities(false, maxResults, firstResult);
    }

    private List<PermisoRol> findPermisoRolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PermisoRol.class));
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

    public PermisoRol findPermisoRol(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PermisoRol.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermisoRolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PermisoRol> rt = cq.from(PermisoRol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
