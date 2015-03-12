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
import entidades.UsuarioReconexion;
import java.util.ArrayList;
import java.util.List;
import entidades.FacturaReconexion;
import entidades.Reconexion;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class ReconexionJpaController implements Serializable {

    public ReconexionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reconexion reconexion) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (reconexion.getUsuarioReconexionList() == null) {
            reconexion.setUsuarioReconexionList(new ArrayList<UsuarioReconexion>());
        }
        if (reconexion.getFacturaReconexionList() == null) {
            reconexion.setFacturaReconexionList(new ArrayList<FacturaReconexion>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Recargo codRecargo = reconexion.getCodRecargo();
            if (codRecargo != null) {
                codRecargo = em.getReference(codRecargo.getClass(), codRecargo.getCodRecargo());
                reconexion.setCodRecargo(codRecargo);
            }
            List<UsuarioReconexion> attachedUsuarioReconexionList = new ArrayList<UsuarioReconexion>();
            for (UsuarioReconexion usuarioReconexionListUsuarioReconexionToAttach : reconexion.getUsuarioReconexionList()) {
                usuarioReconexionListUsuarioReconexionToAttach = em.getReference(usuarioReconexionListUsuarioReconexionToAttach.getClass(), usuarioReconexionListUsuarioReconexionToAttach.getCodUsuarioReconexion());
                attachedUsuarioReconexionList.add(usuarioReconexionListUsuarioReconexionToAttach);
            }
            reconexion.setUsuarioReconexionList(attachedUsuarioReconexionList);
            List<FacturaReconexion> attachedFacturaReconexionList = new ArrayList<FacturaReconexion>();
            for (FacturaReconexion facturaReconexionListFacturaReconexionToAttach : reconexion.getFacturaReconexionList()) {
                facturaReconexionListFacturaReconexionToAttach = em.getReference(facturaReconexionListFacturaReconexionToAttach.getClass(), facturaReconexionListFacturaReconexionToAttach.getCodFacturaReconexion());
                attachedFacturaReconexionList.add(facturaReconexionListFacturaReconexionToAttach);
            }
            reconexion.setFacturaReconexionList(attachedFacturaReconexionList);
            em.persist(reconexion);
            if (codRecargo != null) {
                codRecargo.getReconexionList().add(reconexion);
                codRecargo = em.merge(codRecargo);
            }
            for (UsuarioReconexion usuarioReconexionListUsuarioReconexion : reconexion.getUsuarioReconexionList()) {
                Reconexion oldCodReconexionOfUsuarioReconexionListUsuarioReconexion = usuarioReconexionListUsuarioReconexion.getCodReconexion();
                usuarioReconexionListUsuarioReconexion.setCodReconexion(reconexion);
                usuarioReconexionListUsuarioReconexion = em.merge(usuarioReconexionListUsuarioReconexion);
                if (oldCodReconexionOfUsuarioReconexionListUsuarioReconexion != null) {
                    oldCodReconexionOfUsuarioReconexionListUsuarioReconexion.getUsuarioReconexionList().remove(usuarioReconexionListUsuarioReconexion);
                    oldCodReconexionOfUsuarioReconexionListUsuarioReconexion = em.merge(oldCodReconexionOfUsuarioReconexionListUsuarioReconexion);
                }
            }
            for (FacturaReconexion facturaReconexionListFacturaReconexion : reconexion.getFacturaReconexionList()) {
                Reconexion oldCodReconexionOfFacturaReconexionListFacturaReconexion = facturaReconexionListFacturaReconexion.getCodReconexion();
                facturaReconexionListFacturaReconexion.setCodReconexion(reconexion);
                facturaReconexionListFacturaReconexion = em.merge(facturaReconexionListFacturaReconexion);
                if (oldCodReconexionOfFacturaReconexionListFacturaReconexion != null) {
                    oldCodReconexionOfFacturaReconexionListFacturaReconexion.getFacturaReconexionList().remove(facturaReconexionListFacturaReconexion);
                    oldCodReconexionOfFacturaReconexionListFacturaReconexion = em.merge(oldCodReconexionOfFacturaReconexionListFacturaReconexion);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findReconexion(reconexion.getCodReconexion()) != null) {
                throw new PreexistingEntityException("Reconexion " + reconexion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reconexion reconexion) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Reconexion persistentReconexion = em.find(Reconexion.class, reconexion.getCodReconexion());
            Recargo codRecargoOld = persistentReconexion.getCodRecargo();
            Recargo codRecargoNew = reconexion.getCodRecargo();
            List<UsuarioReconexion> usuarioReconexionListOld = persistentReconexion.getUsuarioReconexionList();
            List<UsuarioReconexion> usuarioReconexionListNew = reconexion.getUsuarioReconexionList();
            List<FacturaReconexion> facturaReconexionListOld = persistentReconexion.getFacturaReconexionList();
            List<FacturaReconexion> facturaReconexionListNew = reconexion.getFacturaReconexionList();
            List<String> illegalOrphanMessages = null;
            for (UsuarioReconexion usuarioReconexionListOldUsuarioReconexion : usuarioReconexionListOld) {
                if (!usuarioReconexionListNew.contains(usuarioReconexionListOldUsuarioReconexion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioReconexion " + usuarioReconexionListOldUsuarioReconexion + " since its codReconexion field is not nullable.");
                }
            }
            for (FacturaReconexion facturaReconexionListOldFacturaReconexion : facturaReconexionListOld) {
                if (!facturaReconexionListNew.contains(facturaReconexionListOldFacturaReconexion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FacturaReconexion " + facturaReconexionListOldFacturaReconexion + " since its codReconexion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codRecargoNew != null) {
                codRecargoNew = em.getReference(codRecargoNew.getClass(), codRecargoNew.getCodRecargo());
                reconexion.setCodRecargo(codRecargoNew);
            }
            List<UsuarioReconexion> attachedUsuarioReconexionListNew = new ArrayList<UsuarioReconexion>();
            for (UsuarioReconexion usuarioReconexionListNewUsuarioReconexionToAttach : usuarioReconexionListNew) {
                usuarioReconexionListNewUsuarioReconexionToAttach = em.getReference(usuarioReconexionListNewUsuarioReconexionToAttach.getClass(), usuarioReconexionListNewUsuarioReconexionToAttach.getCodUsuarioReconexion());
                attachedUsuarioReconexionListNew.add(usuarioReconexionListNewUsuarioReconexionToAttach);
            }
            usuarioReconexionListNew = attachedUsuarioReconexionListNew;
            reconexion.setUsuarioReconexionList(usuarioReconexionListNew);
            List<FacturaReconexion> attachedFacturaReconexionListNew = new ArrayList<FacturaReconexion>();
            for (FacturaReconexion facturaReconexionListNewFacturaReconexionToAttach : facturaReconexionListNew) {
                facturaReconexionListNewFacturaReconexionToAttach = em.getReference(facturaReconexionListNewFacturaReconexionToAttach.getClass(), facturaReconexionListNewFacturaReconexionToAttach.getCodFacturaReconexion());
                attachedFacturaReconexionListNew.add(facturaReconexionListNewFacturaReconexionToAttach);
            }
            facturaReconexionListNew = attachedFacturaReconexionListNew;
            reconexion.setFacturaReconexionList(facturaReconexionListNew);
            reconexion = em.merge(reconexion);
            if (codRecargoOld != null && !codRecargoOld.equals(codRecargoNew)) {
                codRecargoOld.getReconexionList().remove(reconexion);
                codRecargoOld = em.merge(codRecargoOld);
            }
            if (codRecargoNew != null && !codRecargoNew.equals(codRecargoOld)) {
                codRecargoNew.getReconexionList().add(reconexion);
                codRecargoNew = em.merge(codRecargoNew);
            }
            for (UsuarioReconexion usuarioReconexionListNewUsuarioReconexion : usuarioReconexionListNew) {
                if (!usuarioReconexionListOld.contains(usuarioReconexionListNewUsuarioReconexion)) {
                    Reconexion oldCodReconexionOfUsuarioReconexionListNewUsuarioReconexion = usuarioReconexionListNewUsuarioReconexion.getCodReconexion();
                    usuarioReconexionListNewUsuarioReconexion.setCodReconexion(reconexion);
                    usuarioReconexionListNewUsuarioReconexion = em.merge(usuarioReconexionListNewUsuarioReconexion);
                    if (oldCodReconexionOfUsuarioReconexionListNewUsuarioReconexion != null && !oldCodReconexionOfUsuarioReconexionListNewUsuarioReconexion.equals(reconexion)) {
                        oldCodReconexionOfUsuarioReconexionListNewUsuarioReconexion.getUsuarioReconexionList().remove(usuarioReconexionListNewUsuarioReconexion);
                        oldCodReconexionOfUsuarioReconexionListNewUsuarioReconexion = em.merge(oldCodReconexionOfUsuarioReconexionListNewUsuarioReconexion);
                    }
                }
            }
            for (FacturaReconexion facturaReconexionListNewFacturaReconexion : facturaReconexionListNew) {
                if (!facturaReconexionListOld.contains(facturaReconexionListNewFacturaReconexion)) {
                    Reconexion oldCodReconexionOfFacturaReconexionListNewFacturaReconexion = facturaReconexionListNewFacturaReconexion.getCodReconexion();
                    facturaReconexionListNewFacturaReconexion.setCodReconexion(reconexion);
                    facturaReconexionListNewFacturaReconexion = em.merge(facturaReconexionListNewFacturaReconexion);
                    if (oldCodReconexionOfFacturaReconexionListNewFacturaReconexion != null && !oldCodReconexionOfFacturaReconexionListNewFacturaReconexion.equals(reconexion)) {
                        oldCodReconexionOfFacturaReconexionListNewFacturaReconexion.getFacturaReconexionList().remove(facturaReconexionListNewFacturaReconexion);
                        oldCodReconexionOfFacturaReconexionListNewFacturaReconexion = em.merge(oldCodReconexionOfFacturaReconexionListNewFacturaReconexion);
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
                Integer id = reconexion.getCodReconexion();
                if (findReconexion(id) == null) {
                    throw new NonexistentEntityException("The reconexion with id " + id + " no longer exists.");
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
            Reconexion reconexion;
            try {
                reconexion = em.getReference(Reconexion.class, id);
                reconexion.getCodReconexion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reconexion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<UsuarioReconexion> usuarioReconexionListOrphanCheck = reconexion.getUsuarioReconexionList();
            for (UsuarioReconexion usuarioReconexionListOrphanCheckUsuarioReconexion : usuarioReconexionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Reconexion (" + reconexion + ") cannot be destroyed since the UsuarioReconexion " + usuarioReconexionListOrphanCheckUsuarioReconexion + " in its usuarioReconexionList field has a non-nullable codReconexion field.");
            }
            List<FacturaReconexion> facturaReconexionListOrphanCheck = reconexion.getFacturaReconexionList();
            for (FacturaReconexion facturaReconexionListOrphanCheckFacturaReconexion : facturaReconexionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Reconexion (" + reconexion + ") cannot be destroyed since the FacturaReconexion " + facturaReconexionListOrphanCheckFacturaReconexion + " in its facturaReconexionList field has a non-nullable codReconexion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Recargo codRecargo = reconexion.getCodRecargo();
            if (codRecargo != null) {
                codRecargo.getReconexionList().remove(reconexion);
                codRecargo = em.merge(codRecargo);
            }
            em.remove(reconexion);
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

    public List<Reconexion> findReconexionEntities() {
        return findReconexionEntities(true, -1, -1);
    }

    public List<Reconexion> findReconexionEntities(int maxResults, int firstResult) {
        return findReconexionEntities(false, maxResults, firstResult);
    }

    private List<Reconexion> findReconexionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reconexion.class));
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

    public Reconexion findReconexion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reconexion.class, id);
        } finally {
            em.close();
        }
    }

    public int getReconexionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reconexion> rt = cq.from(Reconexion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
