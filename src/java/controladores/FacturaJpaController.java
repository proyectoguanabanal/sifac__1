/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.FacturaRecargo;
import java.util.ArrayList;
import java.util.List;
import entidades.FacturaReconexion;
import entidades.Corte;
import entidades.ClienteFactura;
import entidades.Factura;
import entidades.ReportePagoFactura;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class FacturaJpaController implements Serializable {

    public FacturaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Factura factura) throws RollbackFailureException, Exception {
        if (factura.getFacturaRecargoList() == null) {
            factura.setFacturaRecargoList(new ArrayList<FacturaRecargo>());
        }
        if (factura.getFacturaReconexionList() == null) {
            factura.setFacturaReconexionList(new ArrayList<FacturaReconexion>());
        }
        if (factura.getCorteList() == null) {
            factura.setCorteList(new ArrayList<Corte>());
        }
        if (factura.getClienteFacturaList() == null) {
            factura.setClienteFacturaList(new ArrayList<ClienteFactura>());
        }
        if (factura.getReportePagoFacturaList() == null) {
            factura.setReportePagoFacturaList(new ArrayList<ReportePagoFactura>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<FacturaRecargo> attachedFacturaRecargoList = new ArrayList<FacturaRecargo>();
            for (FacturaRecargo facturaRecargoListFacturaRecargoToAttach : factura.getFacturaRecargoList()) {
                facturaRecargoListFacturaRecargoToAttach = em.getReference(facturaRecargoListFacturaRecargoToAttach.getClass(), facturaRecargoListFacturaRecargoToAttach.getCodFacturaRecargo());
                attachedFacturaRecargoList.add(facturaRecargoListFacturaRecargoToAttach);
            }
            factura.setFacturaRecargoList(attachedFacturaRecargoList);
            List<FacturaReconexion> attachedFacturaReconexionList = new ArrayList<FacturaReconexion>();
            for (FacturaReconexion facturaReconexionListFacturaReconexionToAttach : factura.getFacturaReconexionList()) {
                facturaReconexionListFacturaReconexionToAttach = em.getReference(facturaReconexionListFacturaReconexionToAttach.getClass(), facturaReconexionListFacturaReconexionToAttach.getCodFacturaReconexion());
                attachedFacturaReconexionList.add(facturaReconexionListFacturaReconexionToAttach);
            }
            factura.setFacturaReconexionList(attachedFacturaReconexionList);
            List<Corte> attachedCorteList = new ArrayList<Corte>();
            for (Corte corteListCorteToAttach : factura.getCorteList()) {
                corteListCorteToAttach = em.getReference(corteListCorteToAttach.getClass(), corteListCorteToAttach.getCodCorte());
                attachedCorteList.add(corteListCorteToAttach);
            }
            factura.setCorteList(attachedCorteList);
            List<ClienteFactura> attachedClienteFacturaList = new ArrayList<ClienteFactura>();
            for (ClienteFactura clienteFacturaListClienteFacturaToAttach : factura.getClienteFacturaList()) {
                clienteFacturaListClienteFacturaToAttach = em.getReference(clienteFacturaListClienteFacturaToAttach.getClass(), clienteFacturaListClienteFacturaToAttach.getCodClienteFactura());
                attachedClienteFacturaList.add(clienteFacturaListClienteFacturaToAttach);
            }
            factura.setClienteFacturaList(attachedClienteFacturaList);
            List<ReportePagoFactura> attachedReportePagoFacturaList = new ArrayList<ReportePagoFactura>();
            for (ReportePagoFactura reportePagoFacturaListReportePagoFacturaToAttach : factura.getReportePagoFacturaList()) {
                reportePagoFacturaListReportePagoFacturaToAttach = em.getReference(reportePagoFacturaListReportePagoFacturaToAttach.getClass(), reportePagoFacturaListReportePagoFacturaToAttach.getCodReportePagoFactura());
                attachedReportePagoFacturaList.add(reportePagoFacturaListReportePagoFacturaToAttach);
            }
            factura.setReportePagoFacturaList(attachedReportePagoFacturaList);
            em.persist(factura);
            for (FacturaRecargo facturaRecargoListFacturaRecargo : factura.getFacturaRecargoList()) {
                Factura oldCodFacturaOfFacturaRecargoListFacturaRecargo = facturaRecargoListFacturaRecargo.getCodFactura();
                facturaRecargoListFacturaRecargo.setCodFactura(factura);
                facturaRecargoListFacturaRecargo = em.merge(facturaRecargoListFacturaRecargo);
                if (oldCodFacturaOfFacturaRecargoListFacturaRecargo != null) {
                    oldCodFacturaOfFacturaRecargoListFacturaRecargo.getFacturaRecargoList().remove(facturaRecargoListFacturaRecargo);
                    oldCodFacturaOfFacturaRecargoListFacturaRecargo = em.merge(oldCodFacturaOfFacturaRecargoListFacturaRecargo);
                }
            }
            for (FacturaReconexion facturaReconexionListFacturaReconexion : factura.getFacturaReconexionList()) {
                Factura oldCodFacturaOfFacturaReconexionListFacturaReconexion = facturaReconexionListFacturaReconexion.getCodFactura();
                facturaReconexionListFacturaReconexion.setCodFactura(factura);
                facturaReconexionListFacturaReconexion = em.merge(facturaReconexionListFacturaReconexion);
                if (oldCodFacturaOfFacturaReconexionListFacturaReconexion != null) {
                    oldCodFacturaOfFacturaReconexionListFacturaReconexion.getFacturaReconexionList().remove(facturaReconexionListFacturaReconexion);
                    oldCodFacturaOfFacturaReconexionListFacturaReconexion = em.merge(oldCodFacturaOfFacturaReconexionListFacturaReconexion);
                }
            }
            for (Corte corteListCorte : factura.getCorteList()) {
                Factura oldCodFacturaOfCorteListCorte = corteListCorte.getCodFactura();
                corteListCorte.setCodFactura(factura);
                corteListCorte = em.merge(corteListCorte);
                if (oldCodFacturaOfCorteListCorte != null) {
                    oldCodFacturaOfCorteListCorte.getCorteList().remove(corteListCorte);
                    oldCodFacturaOfCorteListCorte = em.merge(oldCodFacturaOfCorteListCorte);
                }
            }
            for (ClienteFactura clienteFacturaListClienteFactura : factura.getClienteFacturaList()) {
                Factura oldCodFacturaOfClienteFacturaListClienteFactura = clienteFacturaListClienteFactura.getCodFactura();
                clienteFacturaListClienteFactura.setCodFactura(factura);
                clienteFacturaListClienteFactura = em.merge(clienteFacturaListClienteFactura);
                if (oldCodFacturaOfClienteFacturaListClienteFactura != null) {
                    oldCodFacturaOfClienteFacturaListClienteFactura.getClienteFacturaList().remove(clienteFacturaListClienteFactura);
                    oldCodFacturaOfClienteFacturaListClienteFactura = em.merge(oldCodFacturaOfClienteFacturaListClienteFactura);
                }
            }
            for (ReportePagoFactura reportePagoFacturaListReportePagoFactura : factura.getReportePagoFacturaList()) {
                Factura oldCodFacturaOfReportePagoFacturaListReportePagoFactura = reportePagoFacturaListReportePagoFactura.getCodFactura();
                reportePagoFacturaListReportePagoFactura.setCodFactura(factura);
                reportePagoFacturaListReportePagoFactura = em.merge(reportePagoFacturaListReportePagoFactura);
                if (oldCodFacturaOfReportePagoFacturaListReportePagoFactura != null) {
                    oldCodFacturaOfReportePagoFacturaListReportePagoFactura.getReportePagoFacturaList().remove(reportePagoFacturaListReportePagoFactura);
                    oldCodFacturaOfReportePagoFacturaListReportePagoFactura = em.merge(oldCodFacturaOfReportePagoFacturaListReportePagoFactura);
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

    public void edit(Factura factura) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Factura persistentFactura = em.find(Factura.class, factura.getCodFactura());
            List<FacturaRecargo> facturaRecargoListOld = persistentFactura.getFacturaRecargoList();
            List<FacturaRecargo> facturaRecargoListNew = factura.getFacturaRecargoList();
            List<FacturaReconexion> facturaReconexionListOld = persistentFactura.getFacturaReconexionList();
            List<FacturaReconexion> facturaReconexionListNew = factura.getFacturaReconexionList();
            List<Corte> corteListOld = persistentFactura.getCorteList();
            List<Corte> corteListNew = factura.getCorteList();
            List<ClienteFactura> clienteFacturaListOld = persistentFactura.getClienteFacturaList();
            List<ClienteFactura> clienteFacturaListNew = factura.getClienteFacturaList();
            List<ReportePagoFactura> reportePagoFacturaListOld = persistentFactura.getReportePagoFacturaList();
            List<ReportePagoFactura> reportePagoFacturaListNew = factura.getReportePagoFacturaList();
            List<String> illegalOrphanMessages = null;
            for (FacturaRecargo facturaRecargoListOldFacturaRecargo : facturaRecargoListOld) {
                if (!facturaRecargoListNew.contains(facturaRecargoListOldFacturaRecargo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FacturaRecargo " + facturaRecargoListOldFacturaRecargo + " since its codFactura field is not nullable.");
                }
            }
            for (FacturaReconexion facturaReconexionListOldFacturaReconexion : facturaReconexionListOld) {
                if (!facturaReconexionListNew.contains(facturaReconexionListOldFacturaReconexion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FacturaReconexion " + facturaReconexionListOldFacturaReconexion + " since its codFactura field is not nullable.");
                }
            }
            for (Corte corteListOldCorte : corteListOld) {
                if (!corteListNew.contains(corteListOldCorte)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Corte " + corteListOldCorte + " since its codFactura field is not nullable.");
                }
            }
            for (ClienteFactura clienteFacturaListOldClienteFactura : clienteFacturaListOld) {
                if (!clienteFacturaListNew.contains(clienteFacturaListOldClienteFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ClienteFactura " + clienteFacturaListOldClienteFactura + " since its codFactura field is not nullable.");
                }
            }
            for (ReportePagoFactura reportePagoFacturaListOldReportePagoFactura : reportePagoFacturaListOld) {
                if (!reportePagoFacturaListNew.contains(reportePagoFacturaListOldReportePagoFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ReportePagoFactura " + reportePagoFacturaListOldReportePagoFactura + " since its codFactura field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<FacturaRecargo> attachedFacturaRecargoListNew = new ArrayList<FacturaRecargo>();
            for (FacturaRecargo facturaRecargoListNewFacturaRecargoToAttach : facturaRecargoListNew) {
                facturaRecargoListNewFacturaRecargoToAttach = em.getReference(facturaRecargoListNewFacturaRecargoToAttach.getClass(), facturaRecargoListNewFacturaRecargoToAttach.getCodFacturaRecargo());
                attachedFacturaRecargoListNew.add(facturaRecargoListNewFacturaRecargoToAttach);
            }
            facturaRecargoListNew = attachedFacturaRecargoListNew;
            factura.setFacturaRecargoList(facturaRecargoListNew);
            List<FacturaReconexion> attachedFacturaReconexionListNew = new ArrayList<FacturaReconexion>();
            for (FacturaReconexion facturaReconexionListNewFacturaReconexionToAttach : facturaReconexionListNew) {
                facturaReconexionListNewFacturaReconexionToAttach = em.getReference(facturaReconexionListNewFacturaReconexionToAttach.getClass(), facturaReconexionListNewFacturaReconexionToAttach.getCodFacturaReconexion());
                attachedFacturaReconexionListNew.add(facturaReconexionListNewFacturaReconexionToAttach);
            }
            facturaReconexionListNew = attachedFacturaReconexionListNew;
            factura.setFacturaReconexionList(facturaReconexionListNew);
            List<Corte> attachedCorteListNew = new ArrayList<Corte>();
            for (Corte corteListNewCorteToAttach : corteListNew) {
                corteListNewCorteToAttach = em.getReference(corteListNewCorteToAttach.getClass(), corteListNewCorteToAttach.getCodCorte());
                attachedCorteListNew.add(corteListNewCorteToAttach);
            }
            corteListNew = attachedCorteListNew;
            factura.setCorteList(corteListNew);
            List<ClienteFactura> attachedClienteFacturaListNew = new ArrayList<ClienteFactura>();
            for (ClienteFactura clienteFacturaListNewClienteFacturaToAttach : clienteFacturaListNew) {
                clienteFacturaListNewClienteFacturaToAttach = em.getReference(clienteFacturaListNewClienteFacturaToAttach.getClass(), clienteFacturaListNewClienteFacturaToAttach.getCodClienteFactura());
                attachedClienteFacturaListNew.add(clienteFacturaListNewClienteFacturaToAttach);
            }
            clienteFacturaListNew = attachedClienteFacturaListNew;
            factura.setClienteFacturaList(clienteFacturaListNew);
            List<ReportePagoFactura> attachedReportePagoFacturaListNew = new ArrayList<ReportePagoFactura>();
            for (ReportePagoFactura reportePagoFacturaListNewReportePagoFacturaToAttach : reportePagoFacturaListNew) {
                reportePagoFacturaListNewReportePagoFacturaToAttach = em.getReference(reportePagoFacturaListNewReportePagoFacturaToAttach.getClass(), reportePagoFacturaListNewReportePagoFacturaToAttach.getCodReportePagoFactura());
                attachedReportePagoFacturaListNew.add(reportePagoFacturaListNewReportePagoFacturaToAttach);
            }
            reportePagoFacturaListNew = attachedReportePagoFacturaListNew;
            factura.setReportePagoFacturaList(reportePagoFacturaListNew);
            factura = em.merge(factura);
            for (FacturaRecargo facturaRecargoListNewFacturaRecargo : facturaRecargoListNew) {
                if (!facturaRecargoListOld.contains(facturaRecargoListNewFacturaRecargo)) {
                    Factura oldCodFacturaOfFacturaRecargoListNewFacturaRecargo = facturaRecargoListNewFacturaRecargo.getCodFactura();
                    facturaRecargoListNewFacturaRecargo.setCodFactura(factura);
                    facturaRecargoListNewFacturaRecargo = em.merge(facturaRecargoListNewFacturaRecargo);
                    if (oldCodFacturaOfFacturaRecargoListNewFacturaRecargo != null && !oldCodFacturaOfFacturaRecargoListNewFacturaRecargo.equals(factura)) {
                        oldCodFacturaOfFacturaRecargoListNewFacturaRecargo.getFacturaRecargoList().remove(facturaRecargoListNewFacturaRecargo);
                        oldCodFacturaOfFacturaRecargoListNewFacturaRecargo = em.merge(oldCodFacturaOfFacturaRecargoListNewFacturaRecargo);
                    }
                }
            }
            for (FacturaReconexion facturaReconexionListNewFacturaReconexion : facturaReconexionListNew) {
                if (!facturaReconexionListOld.contains(facturaReconexionListNewFacturaReconexion)) {
                    Factura oldCodFacturaOfFacturaReconexionListNewFacturaReconexion = facturaReconexionListNewFacturaReconexion.getCodFactura();
                    facturaReconexionListNewFacturaReconexion.setCodFactura(factura);
                    facturaReconexionListNewFacturaReconexion = em.merge(facturaReconexionListNewFacturaReconexion);
                    if (oldCodFacturaOfFacturaReconexionListNewFacturaReconexion != null && !oldCodFacturaOfFacturaReconexionListNewFacturaReconexion.equals(factura)) {
                        oldCodFacturaOfFacturaReconexionListNewFacturaReconexion.getFacturaReconexionList().remove(facturaReconexionListNewFacturaReconexion);
                        oldCodFacturaOfFacturaReconexionListNewFacturaReconexion = em.merge(oldCodFacturaOfFacturaReconexionListNewFacturaReconexion);
                    }
                }
            }
            for (Corte corteListNewCorte : corteListNew) {
                if (!corteListOld.contains(corteListNewCorte)) {
                    Factura oldCodFacturaOfCorteListNewCorte = corteListNewCorte.getCodFactura();
                    corteListNewCorte.setCodFactura(factura);
                    corteListNewCorte = em.merge(corteListNewCorte);
                    if (oldCodFacturaOfCorteListNewCorte != null && !oldCodFacturaOfCorteListNewCorte.equals(factura)) {
                        oldCodFacturaOfCorteListNewCorte.getCorteList().remove(corteListNewCorte);
                        oldCodFacturaOfCorteListNewCorte = em.merge(oldCodFacturaOfCorteListNewCorte);
                    }
                }
            }
            for (ClienteFactura clienteFacturaListNewClienteFactura : clienteFacturaListNew) {
                if (!clienteFacturaListOld.contains(clienteFacturaListNewClienteFactura)) {
                    Factura oldCodFacturaOfClienteFacturaListNewClienteFactura = clienteFacturaListNewClienteFactura.getCodFactura();
                    clienteFacturaListNewClienteFactura.setCodFactura(factura);
                    clienteFacturaListNewClienteFactura = em.merge(clienteFacturaListNewClienteFactura);
                    if (oldCodFacturaOfClienteFacturaListNewClienteFactura != null && !oldCodFacturaOfClienteFacturaListNewClienteFactura.equals(factura)) {
                        oldCodFacturaOfClienteFacturaListNewClienteFactura.getClienteFacturaList().remove(clienteFacturaListNewClienteFactura);
                        oldCodFacturaOfClienteFacturaListNewClienteFactura = em.merge(oldCodFacturaOfClienteFacturaListNewClienteFactura);
                    }
                }
            }
            for (ReportePagoFactura reportePagoFacturaListNewReportePagoFactura : reportePagoFacturaListNew) {
                if (!reportePagoFacturaListOld.contains(reportePagoFacturaListNewReportePagoFactura)) {
                    Factura oldCodFacturaOfReportePagoFacturaListNewReportePagoFactura = reportePagoFacturaListNewReportePagoFactura.getCodFactura();
                    reportePagoFacturaListNewReportePagoFactura.setCodFactura(factura);
                    reportePagoFacturaListNewReportePagoFactura = em.merge(reportePagoFacturaListNewReportePagoFactura);
                    if (oldCodFacturaOfReportePagoFacturaListNewReportePagoFactura != null && !oldCodFacturaOfReportePagoFacturaListNewReportePagoFactura.equals(factura)) {
                        oldCodFacturaOfReportePagoFacturaListNewReportePagoFactura.getReportePagoFacturaList().remove(reportePagoFacturaListNewReportePagoFactura);
                        oldCodFacturaOfReportePagoFacturaListNewReportePagoFactura = em.merge(oldCodFacturaOfReportePagoFacturaListNewReportePagoFactura);
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
                Integer id = factura.getCodFactura();
                if (findFactura(id) == null) {
                    throw new NonexistentEntityException("The factura with id " + id + " no longer exists.");
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
            Factura factura;
            try {
                factura = em.getReference(Factura.class, id);
                factura.getCodFactura();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The factura with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<FacturaRecargo> facturaRecargoListOrphanCheck = factura.getFacturaRecargoList();
            for (FacturaRecargo facturaRecargoListOrphanCheckFacturaRecargo : facturaRecargoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Factura (" + factura + ") cannot be destroyed since the FacturaRecargo " + facturaRecargoListOrphanCheckFacturaRecargo + " in its facturaRecargoList field has a non-nullable codFactura field.");
            }
            List<FacturaReconexion> facturaReconexionListOrphanCheck = factura.getFacturaReconexionList();
            for (FacturaReconexion facturaReconexionListOrphanCheckFacturaReconexion : facturaReconexionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Factura (" + factura + ") cannot be destroyed since the FacturaReconexion " + facturaReconexionListOrphanCheckFacturaReconexion + " in its facturaReconexionList field has a non-nullable codFactura field.");
            }
            List<Corte> corteListOrphanCheck = factura.getCorteList();
            for (Corte corteListOrphanCheckCorte : corteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Factura (" + factura + ") cannot be destroyed since the Corte " + corteListOrphanCheckCorte + " in its corteList field has a non-nullable codFactura field.");
            }
            List<ClienteFactura> clienteFacturaListOrphanCheck = factura.getClienteFacturaList();
            for (ClienteFactura clienteFacturaListOrphanCheckClienteFactura : clienteFacturaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Factura (" + factura + ") cannot be destroyed since the ClienteFactura " + clienteFacturaListOrphanCheckClienteFactura + " in its clienteFacturaList field has a non-nullable codFactura field.");
            }
            List<ReportePagoFactura> reportePagoFacturaListOrphanCheck = factura.getReportePagoFacturaList();
            for (ReportePagoFactura reportePagoFacturaListOrphanCheckReportePagoFactura : reportePagoFacturaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Factura (" + factura + ") cannot be destroyed since the ReportePagoFactura " + reportePagoFacturaListOrphanCheckReportePagoFactura + " in its reportePagoFacturaList field has a non-nullable codFactura field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(factura);
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

    public List<Factura> findFacturaEntities() {
        return findFacturaEntities(true, -1, -1);
    }

    public List<Factura> findFacturaEntities(int maxResults, int firstResult) {
        return findFacturaEntities(false, maxResults, firstResult);
    }

    private List<Factura> findFacturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Factura.class));
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

    public Factura findFactura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Factura.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Factura> rt = cq.from(Factura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
