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
import entidades.Usuario;
import entidades.Rol;
import entidades.UsuarioRol;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class UsuarioRolJpaController implements Serializable {

    public UsuarioRolJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuarioRol usuarioRol) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario codUsuario = usuarioRol.getCodUsuario();
            if (codUsuario != null) {
                codUsuario = em.getReference(codUsuario.getClass(), codUsuario.getCodUsuario());
                usuarioRol.setCodUsuario(codUsuario);
            }
            Rol codRol = usuarioRol.getCodRol();
            if (codRol != null) {
                codRol = em.getReference(codRol.getClass(), codRol.getCodRol());
                usuarioRol.setCodRol(codRol);
            }
            em.persist(usuarioRol);
            if (codUsuario != null) {
                codUsuario.getUsuarioRolList().add(usuarioRol);
                codUsuario = em.merge(codUsuario);
            }
            if (codRol != null) {
                codRol.getUsuarioRolList().add(usuarioRol);
                codRol = em.merge(codRol);
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

    public void edit(UsuarioRol usuarioRol) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UsuarioRol persistentUsuarioRol = em.find(UsuarioRol.class, usuarioRol.getCodUsuarioRol());
            Usuario codUsuarioOld = persistentUsuarioRol.getCodUsuario();
            Usuario codUsuarioNew = usuarioRol.getCodUsuario();
            Rol codRolOld = persistentUsuarioRol.getCodRol();
            Rol codRolNew = usuarioRol.getCodRol();
            if (codUsuarioNew != null) {
                codUsuarioNew = em.getReference(codUsuarioNew.getClass(), codUsuarioNew.getCodUsuario());
                usuarioRol.setCodUsuario(codUsuarioNew);
            }
            if (codRolNew != null) {
                codRolNew = em.getReference(codRolNew.getClass(), codRolNew.getCodRol());
                usuarioRol.setCodRol(codRolNew);
            }
            usuarioRol = em.merge(usuarioRol);
            if (codUsuarioOld != null && !codUsuarioOld.equals(codUsuarioNew)) {
                codUsuarioOld.getUsuarioRolList().remove(usuarioRol);
                codUsuarioOld = em.merge(codUsuarioOld);
            }
            if (codUsuarioNew != null && !codUsuarioNew.equals(codUsuarioOld)) {
                codUsuarioNew.getUsuarioRolList().add(usuarioRol);
                codUsuarioNew = em.merge(codUsuarioNew);
            }
            if (codRolOld != null && !codRolOld.equals(codRolNew)) {
                codRolOld.getUsuarioRolList().remove(usuarioRol);
                codRolOld = em.merge(codRolOld);
            }
            if (codRolNew != null && !codRolNew.equals(codRolOld)) {
                codRolNew.getUsuarioRolList().add(usuarioRol);
                codRolNew = em.merge(codRolNew);
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
                Integer id = usuarioRol.getCodUsuarioRol();
                if (findUsuarioRol(id) == null) {
                    throw new NonexistentEntityException("The usuarioRol with id " + id + " no longer exists.");
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
            UsuarioRol usuarioRol;
            try {
                usuarioRol = em.getReference(UsuarioRol.class, id);
                usuarioRol.getCodUsuarioRol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioRol with id " + id + " no longer exists.", enfe);
            }
            Usuario codUsuario = usuarioRol.getCodUsuario();
            if (codUsuario != null) {
                codUsuario.getUsuarioRolList().remove(usuarioRol);
                codUsuario = em.merge(codUsuario);
            }
            Rol codRol = usuarioRol.getCodRol();
            if (codRol != null) {
                codRol.getUsuarioRolList().remove(usuarioRol);
                codRol = em.merge(codRol);
            }
            em.remove(usuarioRol);
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

    public List<UsuarioRol> findUsuarioRolEntities() {
        return findUsuarioRolEntities(true, -1, -1);
    }

    public List<UsuarioRol> findUsuarioRolEntities(int maxResults, int firstResult) {
        return findUsuarioRolEntities(false, maxResults, firstResult);
    }

    private List<UsuarioRol> findUsuarioRolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioRol.class));
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

    public UsuarioRol findUsuarioRol(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioRol.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioRolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioRol> rt = cq.from(UsuarioRol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
