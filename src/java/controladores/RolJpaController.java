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
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.PermisoRol;
import entidades.Rol;
import java.util.ArrayList;
import java.util.List;
import entidades.UsuarioRol;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class RolJpaController implements Serializable {

    public RolJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rol rol) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (rol.getPermisoRolList() == null) {
            rol.setPermisoRolList(new ArrayList<PermisoRol>());
        }
        if (rol.getUsuarioRolList() == null) {
            rol.setUsuarioRolList(new ArrayList<UsuarioRol>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<PermisoRol> attachedPermisoRolList = new ArrayList<PermisoRol>();
            for (PermisoRol permisoRolListPermisoRolToAttach : rol.getPermisoRolList()) {
                permisoRolListPermisoRolToAttach = em.getReference(permisoRolListPermisoRolToAttach.getClass(), permisoRolListPermisoRolToAttach.getCodPermisoRol());
                attachedPermisoRolList.add(permisoRolListPermisoRolToAttach);
            }
            rol.setPermisoRolList(attachedPermisoRolList);
            List<UsuarioRol> attachedUsuarioRolList = new ArrayList<UsuarioRol>();
            for (UsuarioRol usuarioRolListUsuarioRolToAttach : rol.getUsuarioRolList()) {
                usuarioRolListUsuarioRolToAttach = em.getReference(usuarioRolListUsuarioRolToAttach.getClass(), usuarioRolListUsuarioRolToAttach.getCodUsuarioRol());
                attachedUsuarioRolList.add(usuarioRolListUsuarioRolToAttach);
            }
            rol.setUsuarioRolList(attachedUsuarioRolList);
            em.persist(rol);
            for (PermisoRol permisoRolListPermisoRol : rol.getPermisoRolList()) {
                Rol oldCodRolOfPermisoRolListPermisoRol = permisoRolListPermisoRol.getCodRol();
                permisoRolListPermisoRol.setCodRol(rol);
                permisoRolListPermisoRol = em.merge(permisoRolListPermisoRol);
                if (oldCodRolOfPermisoRolListPermisoRol != null) {
                    oldCodRolOfPermisoRolListPermisoRol.getPermisoRolList().remove(permisoRolListPermisoRol);
                    oldCodRolOfPermisoRolListPermisoRol = em.merge(oldCodRolOfPermisoRolListPermisoRol);
                }
            }
            for (UsuarioRol usuarioRolListUsuarioRol : rol.getUsuarioRolList()) {
                Rol oldCodRolOfUsuarioRolListUsuarioRol = usuarioRolListUsuarioRol.getCodRol();
                usuarioRolListUsuarioRol.setCodRol(rol);
                usuarioRolListUsuarioRol = em.merge(usuarioRolListUsuarioRol);
                if (oldCodRolOfUsuarioRolListUsuarioRol != null) {
                    oldCodRolOfUsuarioRolListUsuarioRol.getUsuarioRolList().remove(usuarioRolListUsuarioRol);
                    oldCodRolOfUsuarioRolListUsuarioRol = em.merge(oldCodRolOfUsuarioRolListUsuarioRol);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRol(rol.getCodRol()) != null) {
                throw new PreexistingEntityException("Rol " + rol + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rol rol) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Rol persistentRol = em.find(Rol.class, rol.getCodRol());
            List<PermisoRol> permisoRolListOld = persistentRol.getPermisoRolList();
            List<PermisoRol> permisoRolListNew = rol.getPermisoRolList();
            List<UsuarioRol> usuarioRolListOld = persistentRol.getUsuarioRolList();
            List<UsuarioRol> usuarioRolListNew = rol.getUsuarioRolList();
            List<String> illegalOrphanMessages = null;
            for (PermisoRol permisoRolListOldPermisoRol : permisoRolListOld) {
                if (!permisoRolListNew.contains(permisoRolListOldPermisoRol)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PermisoRol " + permisoRolListOldPermisoRol + " since its codRol field is not nullable.");
                }
            }
            for (UsuarioRol usuarioRolListOldUsuarioRol : usuarioRolListOld) {
                if (!usuarioRolListNew.contains(usuarioRolListOldUsuarioRol)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioRol " + usuarioRolListOldUsuarioRol + " since its codRol field is not nullable.");
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
            rol.setPermisoRolList(permisoRolListNew);
            List<UsuarioRol> attachedUsuarioRolListNew = new ArrayList<UsuarioRol>();
            for (UsuarioRol usuarioRolListNewUsuarioRolToAttach : usuarioRolListNew) {
                usuarioRolListNewUsuarioRolToAttach = em.getReference(usuarioRolListNewUsuarioRolToAttach.getClass(), usuarioRolListNewUsuarioRolToAttach.getCodUsuarioRol());
                attachedUsuarioRolListNew.add(usuarioRolListNewUsuarioRolToAttach);
            }
            usuarioRolListNew = attachedUsuarioRolListNew;
            rol.setUsuarioRolList(usuarioRolListNew);
            rol = em.merge(rol);
            for (PermisoRol permisoRolListNewPermisoRol : permisoRolListNew) {
                if (!permisoRolListOld.contains(permisoRolListNewPermisoRol)) {
                    Rol oldCodRolOfPermisoRolListNewPermisoRol = permisoRolListNewPermisoRol.getCodRol();
                    permisoRolListNewPermisoRol.setCodRol(rol);
                    permisoRolListNewPermisoRol = em.merge(permisoRolListNewPermisoRol);
                    if (oldCodRolOfPermisoRolListNewPermisoRol != null && !oldCodRolOfPermisoRolListNewPermisoRol.equals(rol)) {
                        oldCodRolOfPermisoRolListNewPermisoRol.getPermisoRolList().remove(permisoRolListNewPermisoRol);
                        oldCodRolOfPermisoRolListNewPermisoRol = em.merge(oldCodRolOfPermisoRolListNewPermisoRol);
                    }
                }
            }
            for (UsuarioRol usuarioRolListNewUsuarioRol : usuarioRolListNew) {
                if (!usuarioRolListOld.contains(usuarioRolListNewUsuarioRol)) {
                    Rol oldCodRolOfUsuarioRolListNewUsuarioRol = usuarioRolListNewUsuarioRol.getCodRol();
                    usuarioRolListNewUsuarioRol.setCodRol(rol);
                    usuarioRolListNewUsuarioRol = em.merge(usuarioRolListNewUsuarioRol);
                    if (oldCodRolOfUsuarioRolListNewUsuarioRol != null && !oldCodRolOfUsuarioRolListNewUsuarioRol.equals(rol)) {
                        oldCodRolOfUsuarioRolListNewUsuarioRol.getUsuarioRolList().remove(usuarioRolListNewUsuarioRol);
                        oldCodRolOfUsuarioRolListNewUsuarioRol = em.merge(oldCodRolOfUsuarioRolListNewUsuarioRol);
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
                Integer id = rol.getCodRol();
                if (findRol(id) == null) {
                    throw new NonexistentEntityException("The rol with id " + id + " no longer exists.");
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
            Rol rol;
            try {
                rol = em.getReference(Rol.class, id);
                rol.getCodRol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rol with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PermisoRol> permisoRolListOrphanCheck = rol.getPermisoRolList();
            for (PermisoRol permisoRolListOrphanCheckPermisoRol : permisoRolListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Rol (" + rol + ") cannot be destroyed since the PermisoRol " + permisoRolListOrphanCheckPermisoRol + " in its permisoRolList field has a non-nullable codRol field.");
            }
            List<UsuarioRol> usuarioRolListOrphanCheck = rol.getUsuarioRolList();
            for (UsuarioRol usuarioRolListOrphanCheckUsuarioRol : usuarioRolListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Rol (" + rol + ") cannot be destroyed since the UsuarioRol " + usuarioRolListOrphanCheckUsuarioRol + " in its usuarioRolList field has a non-nullable codRol field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(rol);
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

    public List<Rol> findRolEntities() {
        return findRolEntities(true, -1, -1);
    }

    public List<Rol> findRolEntities(int maxResults, int firstResult) {
        return findRolEntities(false, maxResults, firstResult);
    }

    private List<Rol> findRolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rol.class));
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

    public Rol findRol(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rol.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rol> rt = cq.from(Rol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
