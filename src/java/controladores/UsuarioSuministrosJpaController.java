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
import entidades.Usuario;
import entidades.Suministros;
import entidades.UsuarioSuministros;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class UsuarioSuministrosJpaController implements Serializable {

    public UsuarioSuministrosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuarioSuministros usuarioSuministros) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario codUsuario = usuarioSuministros.getCodUsuario();
            if (codUsuario != null) {
                codUsuario = em.getReference(codUsuario.getClass(), codUsuario.getCodUsuario());
                usuarioSuministros.setCodUsuario(codUsuario);
            }
            Suministros codSuministros = usuarioSuministros.getCodSuministros();
            if (codSuministros != null) {
                codSuministros = em.getReference(codSuministros.getClass(), codSuministros.getCodSuministros());
                usuarioSuministros.setCodSuministros(codSuministros);
            }
            em.persist(usuarioSuministros);
            if (codUsuario != null) {
                codUsuario.getUsuarioSuministrosList().add(usuarioSuministros);
                codUsuario = em.merge(codUsuario);
            }
            if (codSuministros != null) {
                codSuministros.getUsuarioSuministrosList().add(usuarioSuministros);
                codSuministros = em.merge(codSuministros);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUsuarioSuministros(usuarioSuministros.getCodUsuarioSuministro()) != null) {
                throw new PreexistingEntityException("UsuarioSuministros " + usuarioSuministros + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuarioSuministros usuarioSuministros) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UsuarioSuministros persistentUsuarioSuministros = em.find(UsuarioSuministros.class, usuarioSuministros.getCodUsuarioSuministro());
            Usuario codUsuarioOld = persistentUsuarioSuministros.getCodUsuario();
            Usuario codUsuarioNew = usuarioSuministros.getCodUsuario();
            Suministros codSuministrosOld = persistentUsuarioSuministros.getCodSuministros();
            Suministros codSuministrosNew = usuarioSuministros.getCodSuministros();
            if (codUsuarioNew != null) {
                codUsuarioNew = em.getReference(codUsuarioNew.getClass(), codUsuarioNew.getCodUsuario());
                usuarioSuministros.setCodUsuario(codUsuarioNew);
            }
            if (codSuministrosNew != null) {
                codSuministrosNew = em.getReference(codSuministrosNew.getClass(), codSuministrosNew.getCodSuministros());
                usuarioSuministros.setCodSuministros(codSuministrosNew);
            }
            usuarioSuministros = em.merge(usuarioSuministros);
            if (codUsuarioOld != null && !codUsuarioOld.equals(codUsuarioNew)) {
                codUsuarioOld.getUsuarioSuministrosList().remove(usuarioSuministros);
                codUsuarioOld = em.merge(codUsuarioOld);
            }
            if (codUsuarioNew != null && !codUsuarioNew.equals(codUsuarioOld)) {
                codUsuarioNew.getUsuarioSuministrosList().add(usuarioSuministros);
                codUsuarioNew = em.merge(codUsuarioNew);
            }
            if (codSuministrosOld != null && !codSuministrosOld.equals(codSuministrosNew)) {
                codSuministrosOld.getUsuarioSuministrosList().remove(usuarioSuministros);
                codSuministrosOld = em.merge(codSuministrosOld);
            }
            if (codSuministrosNew != null && !codSuministrosNew.equals(codSuministrosOld)) {
                codSuministrosNew.getUsuarioSuministrosList().add(usuarioSuministros);
                codSuministrosNew = em.merge(codSuministrosNew);
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
                Integer id = usuarioSuministros.getCodUsuarioSuministro();
                if (findUsuarioSuministros(id) == null) {
                    throw new NonexistentEntityException("The usuarioSuministros with id " + id + " no longer exists.");
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
            UsuarioSuministros usuarioSuministros;
            try {
                usuarioSuministros = em.getReference(UsuarioSuministros.class, id);
                usuarioSuministros.getCodUsuarioSuministro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioSuministros with id " + id + " no longer exists.", enfe);
            }
            Usuario codUsuario = usuarioSuministros.getCodUsuario();
            if (codUsuario != null) {
                codUsuario.getUsuarioSuministrosList().remove(usuarioSuministros);
                codUsuario = em.merge(codUsuario);
            }
            Suministros codSuministros = usuarioSuministros.getCodSuministros();
            if (codSuministros != null) {
                codSuministros.getUsuarioSuministrosList().remove(usuarioSuministros);
                codSuministros = em.merge(codSuministros);
            }
            em.remove(usuarioSuministros);
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

    public List<UsuarioSuministros> findUsuarioSuministrosEntities() {
        return findUsuarioSuministrosEntities(true, -1, -1);
    }

    public List<UsuarioSuministros> findUsuarioSuministrosEntities(int maxResults, int firstResult) {
        return findUsuarioSuministrosEntities(false, maxResults, firstResult);
    }

    private List<UsuarioSuministros> findUsuarioSuministrosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioSuministros.class));
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

    public UsuarioSuministros findUsuarioSuministros(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioSuministros.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioSuministrosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioSuministros> rt = cq.from(UsuarioSuministros.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
