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
import entidades.Recargo;
import entidades.TipoRecargo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class TipoRecargoJpaController implements Serializable {

    public TipoRecargoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoRecargo tipoRecargo) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tipoRecargo.getRecargoList() == null) {
            tipoRecargo.setRecargoList(new ArrayList<Recargo>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Recargo> attachedRecargoList = new ArrayList<Recargo>();
            for (Recargo recargoListRecargoToAttach : tipoRecargo.getRecargoList()) {
                recargoListRecargoToAttach = em.getReference(recargoListRecargoToAttach.getClass(), recargoListRecargoToAttach.getCodRecargo());
                attachedRecargoList.add(recargoListRecargoToAttach);
            }
            tipoRecargo.setRecargoList(attachedRecargoList);
            em.persist(tipoRecargo);
            for (Recargo recargoListRecargo : tipoRecargo.getRecargoList()) {
                TipoRecargo oldCodTipoRecargoOfRecargoListRecargo = recargoListRecargo.getCodTipoRecargo();
                recargoListRecargo.setCodTipoRecargo(tipoRecargo);
                recargoListRecargo = em.merge(recargoListRecargo);
                if (oldCodTipoRecargoOfRecargoListRecargo != null) {
                    oldCodTipoRecargoOfRecargoListRecargo.getRecargoList().remove(recargoListRecargo);
                    oldCodTipoRecargoOfRecargoListRecargo = em.merge(oldCodTipoRecargoOfRecargoListRecargo);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTipoRecargo(tipoRecargo.getCodTipoRecargo()) != null) {
                throw new PreexistingEntityException("TipoRecargo " + tipoRecargo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoRecargo tipoRecargo) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoRecargo persistentTipoRecargo = em.find(TipoRecargo.class, tipoRecargo.getCodTipoRecargo());
            List<Recargo> recargoListOld = persistentTipoRecargo.getRecargoList();
            List<Recargo> recargoListNew = tipoRecargo.getRecargoList();
            List<String> illegalOrphanMessages = null;
            for (Recargo recargoListOldRecargo : recargoListOld) {
                if (!recargoListNew.contains(recargoListOldRecargo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Recargo " + recargoListOldRecargo + " since its codTipoRecargo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Recargo> attachedRecargoListNew = new ArrayList<Recargo>();
            for (Recargo recargoListNewRecargoToAttach : recargoListNew) {
                recargoListNewRecargoToAttach = em.getReference(recargoListNewRecargoToAttach.getClass(), recargoListNewRecargoToAttach.getCodRecargo());
                attachedRecargoListNew.add(recargoListNewRecargoToAttach);
            }
            recargoListNew = attachedRecargoListNew;
            tipoRecargo.setRecargoList(recargoListNew);
            tipoRecargo = em.merge(tipoRecargo);
            for (Recargo recargoListNewRecargo : recargoListNew) {
                if (!recargoListOld.contains(recargoListNewRecargo)) {
                    TipoRecargo oldCodTipoRecargoOfRecargoListNewRecargo = recargoListNewRecargo.getCodTipoRecargo();
                    recargoListNewRecargo.setCodTipoRecargo(tipoRecargo);
                    recargoListNewRecargo = em.merge(recargoListNewRecargo);
                    if (oldCodTipoRecargoOfRecargoListNewRecargo != null && !oldCodTipoRecargoOfRecargoListNewRecargo.equals(tipoRecargo)) {
                        oldCodTipoRecargoOfRecargoListNewRecargo.getRecargoList().remove(recargoListNewRecargo);
                        oldCodTipoRecargoOfRecargoListNewRecargo = em.merge(oldCodTipoRecargoOfRecargoListNewRecargo);
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
                Integer id = tipoRecargo.getCodTipoRecargo();
                if (findTipoRecargo(id) == null) {
                    throw new NonexistentEntityException("The tipoRecargo with id " + id + " no longer exists.");
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
            TipoRecargo tipoRecargo;
            try {
                tipoRecargo = em.getReference(TipoRecargo.class, id);
                tipoRecargo.getCodTipoRecargo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoRecargo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Recargo> recargoListOrphanCheck = tipoRecargo.getRecargoList();
            for (Recargo recargoListOrphanCheckRecargo : recargoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoRecargo (" + tipoRecargo + ") cannot be destroyed since the Recargo " + recargoListOrphanCheckRecargo + " in its recargoList field has a non-nullable codTipoRecargo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoRecargo);
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

    public List<TipoRecargo> findTipoRecargoEntities() {
        return findTipoRecargoEntities(true, -1, -1);
    }

    public List<TipoRecargo> findTipoRecargoEntities(int maxResults, int firstResult) {
        return findTipoRecargoEntities(false, maxResults, firstResult);
    }

    private List<TipoRecargo> findTipoRecargoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoRecargo.class));
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

    public TipoRecargo findTipoRecargo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoRecargo.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoRecargoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoRecargo> rt = cq.from(TipoRecargo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
