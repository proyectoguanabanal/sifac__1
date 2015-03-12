/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.PreexistingEntityException;
import controladores.exceptions.RollbackFailureException;
import entidades.Permiso;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.PermisoRol;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class PermisoJpaController implements Serializable {

    public PermisoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Permiso permiso) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (permiso.getPermisoRolList() == null) {
            permiso.setPermisoRolList(new ArrayList<PermisoRol>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<PermisoRol> attachedPermisoRolList = new ArrayList<PermisoRol>();
            for (PermisoRol permisoRolListPermisoRolToAttach : permiso.getPermisoRolList()) {
                permisoRolListPermisoRolToAttach = em.getReference(permisoRolListPermisoRolToAttach.getClass(), permisoRolListPermisoRolToAttach.getCodPermisoRol());
                attachedPermisoRolList.add(permisoRolListPermisoRolToAttach);
            }
            permiso.setPermisoRolList(attachedPermisoRolList);
            em.persist(permiso);
            for (PermisoRol permisoRolListPermisoRol : permiso.getPermisoRolList()) {
                Permiso oldCodPermisoOfPermisoRolListPermisoRol = permisoRolListPermisoRol.getCodPermiso();
                permisoRolListPermisoRol.setCodPermiso(permiso);
                permisoRolListPermisoRol = em.merge(permisoRolListPermisoRol);
                if (oldCodPermisoOfPermisoRolListPermisoRol != null) {
                    oldCodPermisoOfPermisoRolListPermisoRol.getPermisoRolList().remove(permisoRolListPermisoRol);
                    oldCodPermisoOfPermisoRolListPermisoRol = em.merge(oldCodPermisoOfPermisoRolListPermisoRol);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPermiso(permiso.getCodPermiso()) != null) {
                throw new PreexistingEntityException("Permiso " + permiso + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Permiso permiso) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Permiso persistentPermiso = em.find(Permiso.class, permiso.getCodPermiso());
            List<PermisoRol> permisoRolListOld = persistentPermiso.getPermisoRolList();
            List<PermisoRol> permisoRolListNew = permiso.getPermisoRolList();
            List<String> illegalOrphanMessages = null;
            for (PermisoRol permisoRolListOldPermisoRol : permisoRolListOld) {
                if (!permisoRolListNew.contains(permisoRolListOldPermisoRol)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PermisoRol " + permisoRolListOldPermisoRol + " since its codPermiso field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PermisoRol> attachedPermisoRolListNew = new ArrayList<PermisoRol>();
            for (PermisoRol permisoRolListNewPermisoRolToAttach : permisoRolListNew) {
                permisoRolListNewPermisoRolToAttach = em.getReference(permisoRolListNewPermisoRolToAttach.getClass(), permisoRolListNewPermisoRolToAttach.getCodPermisoRol());
                attachedPermisoRolListNew.add(permisoRolListNewPermisoRolToAttach);
            }
            permisoRolListNew = attachedPermisoRolListNew;
            permiso.setPermisoRolList(permisoRolListNew);
            permiso = em.merge(permiso);
            for (PermisoRol permisoRolListNewPermisoRol : permisoRolListNew) {
                if (!permisoRolListOld.contains(permisoRolListNewPermisoRol)) {
                    Permiso oldCodPermisoOfPermisoRolListNewPermisoRol = permisoRolListNewPermisoRol.getCodPermiso();
                    permisoRolListNewPermisoRol.setCodPermiso(permiso);
                    permisoRolListNewPermisoRol = em.merge(permisoRolListNewPermisoRol);
                    if (oldCodPermisoOfPermisoRolListNewPermisoRol != null && !oldCodPermisoOfPermisoRolListNewPermisoRol.equals(permiso)) {
                        oldCodPermisoOfPermisoRolListNewPermisoRol.getPermisoRolList().remove(permisoRolListNewPermisoRol);
                        oldCodPermisoOfPermisoRolListNewPermisoRol = em.merge(oldCodPermisoOfPermisoRolListNewPermisoRol);
                    }
                }
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
                Integer id = permiso.getCodPermiso();
                if (findPermiso(id) == null) {
                    throw new NonexistentEntityException("The permiso with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Permiso permiso;
            try {
                permiso = em.getReference(Permiso.class, id);
                permiso.getCodPermiso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permiso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PermisoRol> permisoRolListOrphanCheck = permiso.getPermisoRolList();
            for (PermisoRol permisoRolListOrphanCheckPermisoRol : permisoRolListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Permiso (" + permiso + ") cannot be destroyed since the PermisoRol " + permisoRolListOrphanCheckPermisoRol + " in its permisoRolList field has a non-nullable codPermiso field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(permiso);
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

    public List<Permiso> findPermisoEntities() {
        return findPermisoEntities(true, -1, -1);
    }

    public List<Permiso> findPermisoEntities(int maxResults, int firstResult) {
        return findPermisoEntities(false, maxResults, firstResult);
    }

    private List<Permiso> findPermisoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Permiso.class));
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

    public Permiso findPermiso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Permiso.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermisoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Permiso> rt = cq.from(Permiso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
