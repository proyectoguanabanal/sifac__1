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
import entidades.Suministros;
import entidades.TipoSuministro;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class TipoSuministroJpaController implements Serializable {

    public TipoSuministroJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoSuministro tipoSuministro) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tipoSuministro.getSuministrosList() == null) {
            tipoSuministro.setSuministrosList(new ArrayList<Suministros>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Suministros> attachedSuministrosList = new ArrayList<Suministros>();
            for (Suministros suministrosListSuministrosToAttach : tipoSuministro.getSuministrosList()) {
                suministrosListSuministrosToAttach = em.getReference(suministrosListSuministrosToAttach.getClass(), suministrosListSuministrosToAttach.getCodSuministros());
                attachedSuministrosList.add(suministrosListSuministrosToAttach);
            }
            tipoSuministro.setSuministrosList(attachedSuministrosList);
            em.persist(tipoSuministro);
            for (Suministros suministrosListSuministros : tipoSuministro.getSuministrosList()) {
                TipoSuministro oldCodTipoSuministroOfSuministrosListSuministros = suministrosListSuministros.getCodTipoSuministro();
                suministrosListSuministros.setCodTipoSuministro(tipoSuministro);
                suministrosListSuministros = em.merge(suministrosListSuministros);
                if (oldCodTipoSuministroOfSuministrosListSuministros != null) {
                    oldCodTipoSuministroOfSuministrosListSuministros.getSuministrosList().remove(suministrosListSuministros);
                    oldCodTipoSuministroOfSuministrosListSuministros = em.merge(oldCodTipoSuministroOfSuministrosListSuministros);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTipoSuministro(tipoSuministro.getCodTipoSuministro()) != null) {
                throw new PreexistingEntityException("TipoSuministro " + tipoSuministro + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoSuministro tipoSuministro) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoSuministro persistentTipoSuministro = em.find(TipoSuministro.class, tipoSuministro.getCodTipoSuministro());
            List<Suministros> suministrosListOld = persistentTipoSuministro.getSuministrosList();
            List<Suministros> suministrosListNew = tipoSuministro.getSuministrosList();
            List<String> illegalOrphanMessages = null;
            for (Suministros suministrosListOldSuministros : suministrosListOld) {
                if (!suministrosListNew.contains(suministrosListOldSuministros)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Suministros " + suministrosListOldSuministros + " since its codTipoSuministro field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Suministros> attachedSuministrosListNew = new ArrayList<Suministros>();
            for (Suministros suministrosListNewSuministrosToAttach : suministrosListNew) {
                suministrosListNewSuministrosToAttach = em.getReference(suministrosListNewSuministrosToAttach.getClass(), suministrosListNewSuministrosToAttach.getCodSuministros());
                attachedSuministrosListNew.add(suministrosListNewSuministrosToAttach);
            }
            suministrosListNew = attachedSuministrosListNew;
            tipoSuministro.setSuministrosList(suministrosListNew);
            tipoSuministro = em.merge(tipoSuministro);
            for (Suministros suministrosListNewSuministros : suministrosListNew) {
                if (!suministrosListOld.contains(suministrosListNewSuministros)) {
                    TipoSuministro oldCodTipoSuministroOfSuministrosListNewSuministros = suministrosListNewSuministros.getCodTipoSuministro();
                    suministrosListNewSuministros.setCodTipoSuministro(tipoSuministro);
                    suministrosListNewSuministros = em.merge(suministrosListNewSuministros);
                    if (oldCodTipoSuministroOfSuministrosListNewSuministros != null && !oldCodTipoSuministroOfSuministrosListNewSuministros.equals(tipoSuministro)) {
                        oldCodTipoSuministroOfSuministrosListNewSuministros.getSuministrosList().remove(suministrosListNewSuministros);
                        oldCodTipoSuministroOfSuministrosListNewSuministros = em.merge(oldCodTipoSuministroOfSuministrosListNewSuministros);
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
                Integer id = tipoSuministro.getCodTipoSuministro();
                if (findTipoSuministro(id) == null) {
                    throw new NonexistentEntityException("The tipoSuministro with id " + id + " no longer exists.");
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
            TipoSuministro tipoSuministro;
            try {
                tipoSuministro = em.getReference(TipoSuministro.class, id);
                tipoSuministro.getCodTipoSuministro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoSuministro with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Suministros> suministrosListOrphanCheck = tipoSuministro.getSuministrosList();
            for (Suministros suministrosListOrphanCheckSuministros : suministrosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoSuministro (" + tipoSuministro + ") cannot be destroyed since the Suministros " + suministrosListOrphanCheckSuministros + " in its suministrosList field has a non-nullable codTipoSuministro field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoSuministro);
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

    public List<TipoSuministro> findTipoSuministroEntities() {
        return findTipoSuministroEntities(true, -1, -1);
    }

    public List<TipoSuministro> findTipoSuministroEntities(int maxResults, int firstResult) {
        return findTipoSuministroEntities(false, maxResults, firstResult);
    }

    private List<TipoSuministro> findTipoSuministroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoSuministro.class));
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

    public TipoSuministro findTipoSuministro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoSuministro.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoSuministroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoSuministro> rt = cq.from(TipoSuministro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
