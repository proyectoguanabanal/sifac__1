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
import entidades.ReportePago;
import entidades.Factura;
import entidades.ReportePagoFactura;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class ReportePagoFacturaJpaController implements Serializable {

    public ReportePagoFacturaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ReportePagoFactura reportePagoFactura) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ReportePago codReportePago = reportePagoFactura.getCodReportePago();
            if (codReportePago != null) {
                codReportePago = em.getReference(codReportePago.getClass(), codReportePago.getCodReportePago());
                reportePagoFactura.setCodReportePago(codReportePago);
            }
            Factura codFactura = reportePagoFactura.getCodFactura();
            if (codFactura != null) {
                codFactura = em.getReference(codFactura.getClass(), codFactura.getCodFactura());
                reportePagoFactura.setCodFactura(codFactura);
            }
            em.persist(reportePagoFactura);
            if (codReportePago != null) {
                codReportePago.getReportePagoFacturaList().add(reportePagoFactura);
                codReportePago = em.merge(codReportePago);
            }
            if (codFactura != null) {
                codFactura.getReportePagoFacturaList().add(reportePagoFactura);
                codFactura = em.merge(codFactura);
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

    public void edit(ReportePagoFactura reportePagoFactura) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ReportePagoFactura persistentReportePagoFactura = em.find(ReportePagoFactura.class, reportePagoFactura.getCodReportePagoFactura());
            ReportePago codReportePagoOld = persistentReportePagoFactura.getCodReportePago();
            ReportePago codReportePagoNew = reportePagoFactura.getCodReportePago();
            Factura codFacturaOld = persistentReportePagoFactura.getCodFactura();
            Factura codFacturaNew = reportePagoFactura.getCodFactura();
            if (codReportePagoNew != null) {
                codReportePagoNew = em.getReference(codReportePagoNew.getClass(), codReportePagoNew.getCodReportePago());
                reportePagoFactura.setCodReportePago(codReportePagoNew);
            }
            if (codFacturaNew != null) {
                codFacturaNew = em.getReference(codFacturaNew.getClass(), codFacturaNew.getCodFactura());
                reportePagoFactura.setCodFactura(codFacturaNew);
            }
            reportePagoFactura = em.merge(reportePagoFactura);
            if (codReportePagoOld != null && !codReportePagoOld.equals(codReportePagoNew)) {
                codReportePagoOld.getReportePagoFacturaList().remove(reportePagoFactura);
                codReportePagoOld = em.merge(codReportePagoOld);
            }
            if (codReportePagoNew != null && !codReportePagoNew.equals(codReportePagoOld)) {
                codReportePagoNew.getReportePagoFacturaList().add(reportePagoFactura);
                codReportePagoNew = em.merge(codReportePagoNew);
            }
            if (codFacturaOld != null && !codFacturaOld.equals(codFacturaNew)) {
                codFacturaOld.getReportePagoFacturaList().remove(reportePagoFactura);
                codFacturaOld = em.merge(codFacturaOld);
            }
            if (codFacturaNew != null && !codFacturaNew.equals(codFacturaOld)) {
                codFacturaNew.getReportePagoFacturaList().add(reportePagoFactura);
                codFacturaNew = em.merge(codFacturaNew);
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
                Integer id = reportePagoFactura.getCodReportePagoFactura();
                if (findReportePagoFactura(id) == null) {
                    throw new NonexistentEntityException("The reportePagoFactura with id " + id + " no longer exists.");
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
            ReportePagoFactura reportePagoFactura;
            try {
                reportePagoFactura = em.getReference(ReportePagoFactura.class, id);
                reportePagoFactura.getCodReportePagoFactura();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reportePagoFactura with id " + id + " no longer exists.", enfe);
            }
            ReportePago codReportePago = reportePagoFactura.getCodReportePago();
            if (codReportePago != null) {
                codReportePago.getReportePagoFacturaList().remove(reportePagoFactura);
                codReportePago = em.merge(codReportePago);
            }
            Factura codFactura = reportePagoFactura.getCodFactura();
            if (codFactura != null) {
                codFactura.getReportePagoFacturaList().remove(reportePagoFactura);
                codFactura = em.merge(codFactura);
            }
            em.remove(reportePagoFactura);
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

    public List<ReportePagoFactura> findReportePagoFacturaEntities() {
        return findReportePagoFacturaEntities(true, -1, -1);
    }

    public List<ReportePagoFactura> findReportePagoFacturaEntities(int maxResults, int firstResult) {
        return findReportePagoFacturaEntities(false, maxResults, firstResult);
    }

    private List<ReportePagoFactura> findReportePagoFacturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ReportePagoFactura.class));
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

    public ReportePagoFactura findReportePagoFactura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ReportePagoFactura.class, id);
        } finally {
            em.close();
        }
    }

    public int getReportePagoFacturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ReportePagoFactura> rt = cq.from(ReportePagoFactura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
