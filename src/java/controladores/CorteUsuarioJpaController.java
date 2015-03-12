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
import entidades.Corte;
import entidades.CorteUsuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class CorteUsuarioJpaController implements Serializable {

    public CorteUsuarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CorteUsuario corteUsuario) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario codUsuario = corteUsuario.getCodUsuario();
            if (codUsuario != null) {
                codUsuario = em.getReference(codUsuario.getClass(), codUsuario.getCodUsuario());
                corteUsuario.setCodUsuario(codUsuario);
            }
            Corte codCorte = corteUsuario.getCodCorte();
            if (codCorte != null) {
                codCorte = em.getReference(codCorte.getClass(), codCorte.getCodCorte());
                corteUsuario.setCodCorte(codCorte);
            }
            em.persist(corteUsuario);
            if (codUsuario != null) {
                codUsuario.getCorteUsuarioList().add(corteUsuario);
                codUsuario = em.merge(codUsuario);
            }
            if (codCorte != null) {
                codCorte.getCorteUsuarioList().add(corteUsuario);
                codCorte = em.merge(codCorte);
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

    public void edit(CorteUsuario corteUsuario) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            CorteUsuario persistentCorteUsuario = em.find(CorteUsuario.class, corteUsuario.getCodCorteUsuario());
            Usuario codUsuarioOld = persistentCorteUsuario.getCodUsuario();
            Usuario codUsuarioNew = corteUsuario.getCodUsuario();
            Corte codCorteOld = persistentCorteUsuario.getCodCorte();
            Corte codCorteNew = corteUsuario.getCodCorte();
            if (codUsuarioNew != null) {
                codUsuarioNew = em.getReference(codUsuarioNew.getClass(), codUsuarioNew.getCodUsuario());
                corteUsuario.setCodUsuario(codUsuarioNew);
            }
            if (codCorteNew != null) {
                codCorteNew = em.getReference(codCorteNew.getClass(), codCorteNew.getCodCorte());
                corteUsuario.setCodCorte(codCorteNew);
            }
            corteUsuario = em.merge(corteUsuario);
            if (codUsuarioOld != null && !codUsuarioOld.equals(codUsuarioNew)) {
                codUsuarioOld.getCorteUsuarioList().remove(corteUsuario);
                codUsuarioOld = em.merge(codUsuarioOld);
            }
            if (codUsuarioNew != null && !codUsuarioNew.equals(codUsuarioOld)) {
                codUsuarioNew.getCorteUsuarioList().add(corteUsuario);
                codUsuarioNew = em.merge(codUsuarioNew);
            }
            if (codCorteOld != null && !codCorteOld.equals(codCorteNew)) {
                codCorteOld.getCorteUsuarioList().remove(corteUsuario);
                codCorteOld = em.merge(codCorteOld);
            }
            if (codCorteNew != null && !codCorteNew.equals(codCorteOld)) {
                codCorteNew.getCorteUsuarioList().add(corteUsuario);
                codCorteNew = em.merge(codCorteNew);
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
                Integer id = corteUsuario.getCodCorteUsuario();
                if (findCorteUsuario(id) == null) {
                    throw new NonexistentEntityException("The corteUsuario with id " + id + " no longer exists.");
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
            CorteUsuario corteUsuario;
            try {
                corteUsuario = em.getReference(CorteUsuario.class, id);
                corteUsuario.getCodCorteUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The corteUsuario with id " + id + " no longer exists.", enfe);
            }
            Usuario codUsuario = corteUsuario.getCodUsuario();
            if (codUsuario != null) {
                codUsuario.getCorteUsuarioList().remove(corteUsuario);
                codUsuario = em.merge(codUsuario);
            }
            Corte codCorte = corteUsuario.getCodCorte();
            if (codCorte != null) {
                codCorte.getCorteUsuarioList().remove(corteUsuario);
                codCorte = em.merge(codCorte);
            }
            em.remove(corteUsuario);
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

    public List<CorteUsuario> findCorteUsuarioEntities() {
        return findCorteUsuarioEntities(true, -1, -1);
    }

    public List<CorteUsuario> findCorteUsuarioEntities(int maxResults, int firstResult) {
        return findCorteUsuarioEntities(false, maxResults, firstResult);
    }

    private List<CorteUsuario> findCorteUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CorteUsuario.class));
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

    public CorteUsuario findCorteUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CorteUsuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getCorteUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CorteUsuario> rt = cq.from(CorteUsuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
