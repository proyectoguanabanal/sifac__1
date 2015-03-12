/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.RollbackFailureException;
import entidades.ReportePago;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.ReportePagoFactura;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class ReportePagoJpaController implements Serializable {

    public ReportePagoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ReportePago reportePago) throws RollbackFailureException, Exception {
        if (reportePago.getReportePagoFacturaList() == null) {
            reportePago.setReportePagoFacturaList(new ArrayList<ReportePagoFactura>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<ReportePagoFactura> attachedReportePagoFacturaList = new ArrayList<ReportePagoFactura>();
            for (ReportePagoFactura reportePagoFacturaListReportePagoFacturaToAttach : reportePago.getReportePagoFacturaList()) {
                reportePagoFacturaListReportePagoFacturaToAttach = em.getReference(reportePagoFacturaListReportePagoFacturaToAttach.getClass(), reportePagoFacturaListReportePagoFacturaToAttach.getCodReportePagoFactura());
                attachedReportePagoFacturaList.add(reportePagoFacturaListReportePagoFacturaToAttach);
            }
            reportePago.setReportePagoFacturaList(attachedReportePagoFacturaList);
            em.persist(reportePago);
            for (ReportePagoFactura reportePagoFacturaListReportePagoFactura : reportePago.getReportePagoFacturaList()) {
                ReportePago oldCodReportePagoOfReportePagoFacturaListReportePagoFactura = reportePagoFacturaListReportePagoFactura.getCodReportePago();
                reportePagoFacturaListReportePagoFactura.setCodReportePago(reportePago);
                reportePagoFacturaListReportePagoFactura = em.merge(reportePagoFacturaListReportePagoFactura);
                if (oldCodReportePagoOfReportePagoFacturaListReportePagoFactura != null) {
                    oldCodReportePagoOfReportePagoFacturaListReportePagoFactura.getReportePagoFacturaList().remove(reportePagoFacturaListReportePagoFactura);
                    oldCodReportePagoOfReportePagoFacturaListReportePagoFactura = em.merge(oldCodReportePagoOfReportePagoFacturaListReportePagoFactura);
                }
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

    public void edit(ReportePago reportePago) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ReportePago persistentReportePago = em.find(ReportePago.class, reportePago.getCodReportePago());
            List<ReportePagoFactura> reportePagoFacturaListOld = persistentReportePago.getReportePagoFacturaList();
            List<ReportePagoFactura> reportePagoFacturaListNew = reportePago.getReportePagoFacturaList();
            List<String> illegalOrphanMessages = null;
            for (ReportePagoFactura reportePagoFacturaListOldReportePagoFactura : reportePagoFacturaListOld) {
                if (!reportePagoFacturaListNew.contains(reportePagoFacturaListOldReportePagoFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ReportePagoFactura " + reportePagoFacturaListOldReportePagoFactura + " since its codReportePago field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ReportePagoFactura> attachedReportePagoFacturaListNew = new ArrayList<ReportePagoFactura>();
            for (ReportePagoFactura reportePagoFacturaListNewReportePagoFacturaToAttach : reportePagoFacturaListNew) {
                reportePagoFacturaListNewReportePagoFacturaToAttach = em.getReference(reportePagoFacturaListNewReportePagoFacturaToAttach.getClass(), reportePagoFacturaListNewReportePagoFacturaToAttach.getCodReportePagoFactura());
                attachedReportePagoFacturaListNew.add(reportePagoFacturaListNewReportePagoFacturaToAttach);
            }
            reportePagoFacturaListNew = attachedReportePagoFacturaListNew;
            reportePago.setReportePagoFacturaList(reportePagoFacturaListNew);
            reportePago = em.merge(reportePago);
            for (ReportePagoFactura reportePagoFacturaListNewReportePagoFactura : reportePagoFacturaListNew) {
                if (!reportePagoFacturaListOld.contains(reportePagoFacturaListNewReportePagoFactura)) {
                    ReportePago oldCodReportePagoOfReportePagoFacturaListNewReportePagoFactura = reportePagoFacturaListNewReportePagoFactura.getCodReportePago();
                    reportePagoFacturaListNewReportePagoFactura.setCodReportePago(reportePago);
                    reportePagoFacturaListNewReportePagoFactura = em.merge(reportePagoFacturaListNewReportePagoFactura);
                    if (oldCodReportePagoOfReportePagoFacturaListNewReportePagoFactura != null && !oldCodReportePagoOfReportePagoFacturaListNewReportePagoFactura.equals(reportePago)) {
                        oldCodReportePagoOfReportePagoFacturaListNewReportePagoFactura.getReportePagoFacturaList().remove(reportePagoFacturaListNewReportePagoFactura);
                        oldCodReportePagoOfReportePagoFacturaListNewReportePagoFactura = em.merge(oldCodReportePagoOfReportePagoFacturaListNewReportePagoFactura);
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
                Integer id = reportePago.getCodReportePago();
                if (findReportePago(id) == null) {
                    throw new NonexistentEntityException("The reportePago with id " + id + " no longer exists.");
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
            ReportePago reportePago;
            try {
                reportePago = em.getReference(ReportePago.class, id);
                reportePago.getCodReportePago();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reportePago with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ReportePagoFactura> reportePagoFacturaListOrphanCheck = reportePago.getReportePagoFacturaList();
            for (ReportePagoFactura reportePagoFacturaListOrphanCheckReportePagoFactura : reportePagoFacturaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ReportePago (" + reportePago + ") cannot be destroyed since the ReportePagoFactura " + reportePagoFacturaListOrphanCheckReportePagoFactura + " in its reportePagoFacturaList field has a non-nullable codReportePago field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(reportePago);
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

    public List<ReportePago> findReportePagoEntities() {
        return findReportePagoEntities(true, -1, -1);
    }

    public List<ReportePago> findReportePagoEntities(int maxResults, int firstResult) {
        return findReportePagoEntities(false, maxResults, firstResult);
    }

    private List<ReportePago> findReportePagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ReportePago.class));
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

    public ReportePago findReportePago(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ReportePago.class, id);
        } finally {
            em.close();
        }
    }

    public int getReportePagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ReportePago> rt = cq.from(ReportePago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
