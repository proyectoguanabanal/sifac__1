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
import entidades.Proveedor;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.SuministrosProveedor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class ProveedorJpaController implements Serializable {

    public ProveedorJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proveedor proveedor) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (proveedor.getSuministrosProveedorList() == null) {
            proveedor.setSuministrosProveedorList(new ArrayList<SuministrosProveedor>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<SuministrosProveedor> attachedSuministrosProveedorList = new ArrayList<SuministrosProveedor>();
            for (SuministrosProveedor suministrosProveedorListSuministrosProveedorToAttach : proveedor.getSuministrosProveedorList()) {
                suministrosProveedorListSuministrosProveedorToAttach = em.getReference(suministrosProveedorListSuministrosProveedorToAttach.getClass(), suministrosProveedorListSuministrosProveedorToAttach.getCodSuministrosProveedor());
                attachedSuministrosProveedorList.add(suministrosProveedorListSuministrosProveedorToAttach);
            }
            proveedor.setSuministrosProveedorList(attachedSuministrosProveedorList);
            em.persist(proveedor);
            for (SuministrosProveedor suministrosProveedorListSuministrosProveedor : proveedor.getSuministrosProveedorList()) {
                Proveedor oldCodProveedorOfSuministrosProveedorListSuministrosProveedor = suministrosProveedorListSuministrosProveedor.getCodProveedor();
                suministrosProveedorListSuministrosProveedor.setCodProveedor(proveedor);
                suministrosProveedorListSuministrosProveedor = em.merge(suministrosProveedorListSuministrosProveedor);
                if (oldCodProveedorOfSuministrosProveedorListSuministrosProveedor != null) {
                    oldCodProveedorOfSuministrosProveedorListSuministrosProveedor.getSuministrosProveedorList().remove(suministrosProveedorListSuministrosProveedor);
                    oldCodProveedorOfSuministrosProveedorListSuministrosProveedor = em.merge(oldCodProveedorOfSuministrosProveedorListSuministrosProveedor);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProveedor(proveedor.getCodProveedor()) != null) {
                throw new PreexistingEntityException("Proveedor " + proveedor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proveedor proveedor) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Proveedor persistentProveedor = em.find(Proveedor.class, proveedor.getCodProveedor());
            List<SuministrosProveedor> suministrosProveedorListOld = persistentProveedor.getSuministrosProveedorList();
            List<SuministrosProveedor> suministrosProveedorListNew = proveedor.getSuministrosProveedorList();
            List<String> illegalOrphanMessages = null;
            for (SuministrosProveedor suministrosProveedorListOldSuministrosProveedor : suministrosProveedorListOld) {
                if (!suministrosProveedorListNew.contains(suministrosProveedorListOldSuministrosProveedor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SuministrosProveedor " + suministrosProveedorListOldSuministrosProveedor + " since its codProveedor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<SuministrosProveedor> attachedSuministrosProveedorListNew = new ArrayList<SuministrosProveedor>();
            for (SuministrosProveedor suministrosProveedorListNewSuministrosProveedorToAttach : suministrosProveedorListNew) {
                suministrosProveedorListNewSuministrosProveedorToAttach = em.getReference(suministrosProveedorListNewSuministrosProveedorToAttach.getClass(), suministrosProveedorListNewSuministrosProveedorToAttach.getCodSuministrosProveedor());
                attachedSuministrosProveedorListNew.add(suministrosProveedorListNewSuministrosProveedorToAttach);
            }
            suministrosProveedorListNew = attachedSuministrosProveedorListNew;
            proveedor.setSuministrosProveedorList(suministrosProveedorListNew);
            proveedor = em.merge(proveedor);
            for (SuministrosProveedor suministrosProveedorListNewSuministrosProveedor : suministrosProveedorListNew) {
                if (!suministrosProveedorListOld.contains(suministrosProveedorListNewSuministrosProveedor)) {
                    Proveedor oldCodProveedorOfSuministrosProveedorListNewSuministrosProveedor = suministrosProveedorListNewSuministrosProveedor.getCodProveedor();
                    suministrosProveedorListNewSuministrosProveedor.setCodProveedor(proveedor);
                    suministrosProveedorListNewSuministrosProveedor = em.merge(suministrosProveedorListNewSuministrosProveedor);
                    if (oldCodProveedorOfSuministrosProveedorListNewSuministrosProveedor != null && !oldCodProveedorOfSuministrosProveedorListNewSuministrosProveedor.equals(proveedor)) {
                        oldCodProveedorOfSuministrosProveedorListNewSuministrosProveedor.getSuministrosProveedorList().remove(suministrosProveedorListNewSuministrosProveedor);
                        oldCodProveedorOfSuministrosProveedorListNewSuministrosProveedor = em.merge(oldCodProveedorOfSuministrosProveedorListNewSuministrosProveedor);
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
                Integer id = proveedor.getCodProveedor();
                if (findProveedor(id) == null) {
                    throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.");
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
            Proveedor proveedor;
            try {
                proveedor = em.getReference(Proveedor.class, id);
                proveedor.getCodProveedor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<SuministrosProveedor> suministrosProveedorListOrphanCheck = proveedor.getSuministrosProveedorList();
            for (SuministrosProveedor suministrosProveedorListOrphanCheckSuministrosProveedor : suministrosProveedorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proveedor (" + proveedor + ") cannot be destroyed since the SuministrosProveedor " + suministrosProveedorListOrphanCheckSuministrosProveedor + " in its suministrosProveedorList field has a non-nullable codProveedor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(proveedor);
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

    public List<Proveedor> findProveedorEntities() {
        return findProveedorEntities(true, -1, -1);
    }

    public List<Proveedor> findProveedorEntities(int maxResults, int firstResult) {
        return findProveedorEntities(false, maxResults, firstResult);
    }

    private List<Proveedor> findProveedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proveedor.class));
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

    public Proveedor findProveedor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proveedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProveedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proveedor> rt = cq.from(Proveedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
