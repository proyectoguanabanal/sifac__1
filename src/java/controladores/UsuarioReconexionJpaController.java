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
import entidades.Reconexion;
import entidades.UsuarioReconexion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class UsuarioReconexionJpaController implements Serializable {

    public UsuarioReconexionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuarioReconexion usuarioReconexion) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario codUsuario = usuarioReconexion.getCodUsuario();
            if (codUsuario != null) {
                codUsuario = em.getReference(codUsuario.getClass(), codUsuario.getCodUsuario());
                usuarioReconexion.setCodUsuario(codUsuario);
            }
            Reconexion codReconexion = usuarioReconexion.getCodReconexion();
            if (codReconexion != null) {
                codReconexion = em.getReference(codReconexion.getClass(), codReconexion.getCodReconexion());
                usuarioReconexion.setCodReconexion(codReconexion);
            }
            em.persist(usuarioReconexion);
            if (codUsuario != null) {
                codUsuario.getUsuarioReconexionList().add(usuarioReconexion);
                codUsuario = em.merge(codUsuario);
            }
            if (codReconexion != null) {
                codReconexion.getUsuarioReconexionList().add(usuarioReconexion);
                codReconexion = em.merge(codReconexion);
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

    public void edit(UsuarioReconexion usuarioReconexion) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UsuarioReconexion persistentUsuarioReconexion = em.find(UsuarioReconexion.class, usuarioReconexion.getCodUsuarioReconexion());
            Usuario codUsuarioOld = persistentUsuarioReconexion.getCodUsuario();
            Usuario codUsuarioNew = usuarioReconexion.getCodUsuario();
            Reconexion codReconexionOld = persistentUsuarioReconexion.getCodReconexion();
            Reconexion codReconexionNew = usuarioReconexion.getCodReconexion();
            if (codUsuarioNew != null) {
                codUsuarioNew = em.getReference(codUsuarioNew.getClass(), codUsuarioNew.getCodUsuario());
                usuarioReconexion.setCodUsuario(codUsuarioNew);
            }
            if (codReconexionNew != null) {
                codReconexionNew = em.getReference(codReconexionNew.getClass(), codReconexionNew.getCodReconexion());
                usuarioReconexion.setCodReconexion(codReconexionNew);
            }
            usuarioReconexion = em.merge(usuarioReconexion);
            if (codUsuarioOld != null && !codUsuarioOld.equals(codUsuarioNew)) {
                codUsuarioOld.getUsuarioReconexionList().remove(usuarioReconexion);
                codUsuarioOld = em.merge(codUsuarioOld);
            }
            if (codUsuarioNew != null && !codUsuarioNew.equals(codUsuarioOld)) {
                codUsuarioNew.getUsuarioReconexionList().add(usuarioReconexion);
                codUsuarioNew = em.merge(codUsuarioNew);
            }
            if (codReconexionOld != null && !codReconexionOld.equals(codReconexionNew)) {
                codReconexionOld.getUsuarioReconexionList().remove(usuarioReconexion);
                codReconexionOld = em.merge(codReconexionOld);
            }
            if (codReconexionNew != null && !codReconexionNew.equals(codReconexionOld)) {
                codReconexionNew.getUsuarioReconexionList().add(usuarioReconexion);
                codReconexionNew = em.merge(codReconexionNew);
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
                Integer id = usuarioReconexion.getCodUsuarioReconexion();
                if (findUsuarioReconexion(id) == null) {
                    throw new NonexistentEntityException("The usuarioReconexion with id " + id + " no longer exists.");
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
            UsuarioReconexion usuarioReconexion;
            try {
                usuarioReconexion = em.getReference(UsuarioReconexion.class, id);
                usuarioReconexion.getCodUsuarioReconexion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioReconexion with id " + id + " no longer exists.", enfe);
            }
            Usuario codUsuario = usuarioReconexion.getCodUsuario();
            if (codUsuario != null) {
                codUsuario.getUsuarioReconexionList().remove(usuarioReconexion);
                codUsuario = em.merge(codUsuario);
            }
            Reconexion codReconexion = usuarioReconexion.getCodReconexion();
            if (codReconexion != null) {
                codReconexion.getUsuarioReconexionList().remove(usuarioReconexion);
                codReconexion = em.merge(codReconexion);
            }
            em.remove(usuarioReconexion);
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

    public List<UsuarioReconexion> findUsuarioReconexionEntities() {
        return findUsuarioReconexionEntities(true, -1, -1);
    }

    public List<UsuarioReconexion> findUsuarioReconexionEntities(int maxResults, int firstResult) {
        return findUsuarioReconexionEntities(false, maxResults, firstResult);
    }

    private List<UsuarioReconexion> findUsuarioReconexionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioReconexion.class));
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

    public UsuarioReconexion findUsuarioReconexion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioReconexion.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioReconexionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioReconexion> rt = cq.from(UsuarioReconexion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
