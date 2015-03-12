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
import entidades.TipoRecargo;
import entidades.FacturaRecargo;
import entidades.Recargo;
import java.util.ArrayList;
import java.util.List;
import entidades.Reconexion;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class RecargoJpaController implements Serializable {

    public RecargoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Recargo recargo) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (recargo.getFacturaRecargoList() == null) {
            recargo.setFacturaRecargoList(new ArrayList<FacturaRecargo>());
        }
        if (recargo.getReconexionList() == null) {
            recargo.setReconexionList(new ArrayList<Reconexion>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoRecargo codTipoRecargo = recargo.getCodTipoRecargo();
            if (codTipoRecargo != null) {
                codTipoRecargo = em.getReference(codTipoRecargo.getClass(), codTipoRecargo.getCodTipoRecargo());
                recargo.setCodTipoRecargo(codTipoRecargo);
            }
            List<FacturaRecargo> attachedFacturaRecargoList = new ArrayList<FacturaRecargo>();
            for (FacturaRecargo facturaRecargoListFacturaRecargoToAttach : recargo.getFacturaRecargoList()) {
                facturaRecargoListFacturaRecargoToAttach = em.getReference(facturaRecargoListFacturaRecargoToAttach.getClass(), facturaRecargoListFacturaRecargoToAttach.getCodFacturaRecargo());
                attachedFacturaRecargoList.add(facturaRecargoListFacturaRecargoToAttach);
            }
            recargo.setFacturaRecargoList(attachedFacturaRecargoList);
            List<Reconexion> attachedReconexionList = new ArrayList<Reconexion>();
            for (Reconexion reconexionListReconexionToAttach : recargo.getReconexionList()) {
                reconexionListReconexionToAttach = em.getReference(reconexionListReconexionToAttach.getClass(), reconexionListReconexionToAttach.getCodReconexion());
                attachedReconexionList.add(reconexionListReconexionToAttach);
            }
            recargo.setReconexionList(attachedReconexionList);
            em.persist(recargo);
            if (codTipoRecargo != null) {
                codTipoRecargo.getRecargoList().add(recargo);
                codTipoRecargo = em.merge(codTipoRecargo);
            }
            for (FacturaRecargo facturaRecargoListFacturaRecargo : recargo.getFacturaRecargoList()) {
                Recargo oldCodRecargoOfFacturaRecargoListFacturaRecargo = facturaRecargoListFacturaRecargo.getCodRecargo();
                facturaRecargoListFacturaRecargo.setCodRecargo(recargo);
                facturaRecargoListFacturaRecargo = em.merge(facturaRecargoListFacturaRecargo);
                if (oldCodRecargoOfFacturaRecargoListFacturaRecargo != null) {
                    oldCodRecargoOfFacturaRecargoListFacturaRecargo.getFacturaRecargoList().remove(facturaRecargoListFacturaRecargo);
                    oldCodRecargoOfFacturaRecargoListFacturaRecargo = em.merge(oldCodRecargoOfFacturaRecargoListFacturaRecargo);
                }
            }
            for (Reconexion reconexionListReconexion : recargo.getReconexionList()) {
                Recargo oldCodRecargoOfReconexionListReconexion = reconexionListReconexion.getCodRecargo();
                reconexionListReconexion.setCodRecargo(recargo);
                reconexionListReconexion = em.merge(reconexionListReconexion);
                if (oldCodRecargoOfReconexionListReconexion != null) {
                    oldCodRecargoOfReconexionListReconexion.getReconexionList().remove(reconexionListReconexion);
                    oldCodRecargoOfReconexionListReconexion = em.merge(oldCodRecargoOfReconexionListReconexion);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRecargo(recargo.getCodRecargo()) != null) {
                throw new PreexistingEntityException("Recargo " + recargo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Recargo recargo) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Recargo persistentRecargo = em.find(Recargo.class, recargo.getCodRecargo());
            TipoRecargo codTipoRecargoOld = persistentRecargo.getCodTipoRecargo();
            TipoRecargo codTipoRecargoNew = recargo.getCodTipoRecargo();
            List<FacturaRecargo> facturaRecargoListOld = persistentRecargo.getFacturaRecargoList();
            List<FacturaRecargo> facturaRecargoListNew = recargo.getFacturaRecargoList();
            List<Reconexion> reconexionListOld = persistentRecargo.getReconexionList();
            List<Reconexion> reconexionListNew = recargo.getReconexionList();
            List<String> illegalOrphanMessages = null;
            for (FacturaRecargo facturaRecargoListOldFacturaRecargo : facturaRecargoListOld) {
                if (!facturaRecargoListNew.contains(facturaRecargoListOldFacturaRecargo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FacturaRecargo " + facturaRecargoListOldFacturaRecargo + " since its codRecargo field is not nullable.");
                }
            }
            for (Reconexion reconexionListOldReconexion : reconexionListOld) {
                if (!reconexionListNew.contains(reconexionListOldReconexion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reconexion " + reconexionListOldReconexion + " since its codRecargo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codTipoRecargoNew != null) {
                codTipoRecargoNew = em.getReference(codTipoRecargoNew.getClass(), codTipoRecargoNew.getCodTipoRecargo());
                recargo.setCodTipoRecargo(codTipoRecargoNew);
            }
            List<FacturaRecargo> attachedFacturaRecargoListNew = new ArrayList<FacturaRecargo>();
            for (FacturaRecargo facturaRecargoListNewFacturaRecargoToAttach : facturaRecargoListNew) {
                facturaRecargoListNewFacturaRecargoToAttach = em.getReference(facturaRecargoListNewFacturaRecargoToAttach.getClass(), facturaRecargoListNewFacturaRecargoToAttach.getCodFacturaRecargo());
                attachedFacturaRecargoListNew.add(facturaRecargoListNewFacturaRecargoToAttach);
            }
            facturaRecargoListNew = attachedFacturaRecargoListNew;
            recargo.setFacturaRecargoList(facturaRecargoListNew);
            List<Reconexion> attachedReconexionListNew = new ArrayList<Reconexion>();
            for (Reconexion reconexionListNewReconexionToAttach : reconexionListNew) {
                reconexionListNewReconexionToAttach = em.getReference(reconexionListNewReconexionToAttach.getClass(), reconexionListNewReconexionToAttach.getCodReconexion());
                attachedReconexionListNew.add(reconexionListNewReconexionToAttach);
            }
            reconexionListNew = attachedReconexionListNew;
            recargo.setReconexionList(reconexionListNew);
            recargo = em.merge(recargo);
            if (codTipoRecargoOld != null && !codTipoRecargoOld.equals(codTipoRecargoNew)) {
                codTipoRecargoOld.getRecargoList().remove(recargo);
                codTipoRecargoOld = em.merge(codTipoRecargoOld);
            }
            if (codTipoRecargoNew != null && !codTipoRecargoNew.equals(codTipoRecargoOld)) {
                codTipoRecargoNew.getRecargoList().add(recargo);
                codTipoRecargoNew = em.merge(codTipoRecargoNew);
            }
            for (FacturaRecargo facturaRecargoListNewFacturaRecargo : facturaRecargoListNew) {
                if (!facturaRecargoListOld.contains(facturaRecargoListNewFacturaRecargo)) {
                    Recargo oldCodRecargoOfFacturaRecargoListNewFacturaRecargo = facturaRecargoListNewFacturaRecargo.getCodRecargo();
                    facturaRecargoListNewFacturaRecargo.setCodRecargo(recargo);
                    facturaRecargoListNewFacturaRecargo = em.merge(facturaRecargoListNewFacturaRecargo);
                    if (oldCodRecargoOfFacturaRecargoListNewFacturaRecargo != null && !oldCodRecargoOfFacturaRecargoListNewFacturaRecargo.equals(recargo)) {
                        oldCodRecargoOfFacturaRecargoListNewFacturaRecargo.getFacturaRecargoList().remove(facturaRecargoListNewFacturaRecargo);
                        oldCodRecargoOfFacturaRecargoListNewFacturaRecargo = em.merge(oldCodRecargoOfFacturaRecargoListNewFacturaRecargo);
                    }
                }
            }
            for (Reconexion reconexionListNewReconexion : reconexionListNew) {
                if (!reconexionListOld.contains(reconexionListNewReconexion)) {
                    Recargo oldCodRecargoOfReconexionListNewReconexion = reconexionListNewReconexion.getCodRecargo();
                    reconexionListNewReconexion.setCodRecargo(recargo);
                    reconexionListNewReconexion = em.merge(reconexionListNewReconexion);
                    if (oldCodRecargoOfReconexionListNewReconexion != null && !oldCodRecargoOfReconexionListNewReconexion.equals(recargo)) {
                        oldCodRecargoOfReconexionListNewReconexion.getReconexionList().remove(reconexionListNewReconexion);
                        oldCodRecargoOfReconexionListNewReconexion = em.merge(oldCodRecargoOfReconexionListNewReconexion);
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
                Integer id = recargo.getCodRecargo();
                if (findRecargo(id) == null) {
                    throw new NonexistentEntityException("The recargo with id " + id + " no longer exists.");
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
            Recargo recargo;
            try {
                recargo = em.getReference(Recargo.class, id);
                recargo.getCodRecargo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recargo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<FacturaRecargo> facturaRecargoListOrphanCheck = recargo.getFacturaRecargoList();
            for (FacturaRecargo facturaRecargoListOrphanCheckFacturaRecargo : facturaRecargoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Recargo (" + recargo + ") cannot be destroyed since the FacturaRecargo " + facturaRecargoListOrphanCheckFacturaRecargo + " in its facturaRecargoList field has a non-nullable codRecargo field.");
            }
            List<Reconexion> reconexionListOrphanCheck = recargo.getReconexionList();
            for (Reconexion reconexionListOrphanCheckReconexion : reconexionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Recargo (" + recargo + ") cannot be destroyed since the Reconexion " + reconexionListOrphanCheckReconexion + " in its reconexionList field has a non-nullable codRecargo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoRecargo codTipoRecargo = recargo.getCodTipoRecargo();
            if (codTipoRecargo != null) {
                codTipoRecargo.getRecargoList().remove(recargo);
                codTipoRecargo = em.merge(codTipoRecargo);
            }
            em.remove(recargo);
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

    public List<Recargo> findRecargoEntities() {
        return findRecargoEntities(true, -1, -1);
    }

    public List<Recargo> findRecargoEntities(int maxResults, int firstResult) {
        return findRecargoEntities(false, maxResults, firstResult);
    }

    private List<Recargo> findRecargoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Recargo.class));
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

    public Recargo findRecargo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Recargo.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecargoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Recargo> rt = cq.from(Recargo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
