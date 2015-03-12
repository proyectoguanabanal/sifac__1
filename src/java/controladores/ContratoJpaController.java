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
import entidades.ClienteContrato;
import entidades.Contrato;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class ContratoJpaController implements Serializable {

    public ContratoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Contrato contrato) throws RollbackFailureException, Exception {
        if (contrato.getClienteContratoList() == null) {
            contrato.setClienteContratoList(new ArrayList<ClienteContrato>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<ClienteContrato> attachedClienteContratoList = new ArrayList<ClienteContrato>();
            for (ClienteContrato clienteContratoListClienteContratoToAttach : contrato.getClienteContratoList()) {
                clienteContratoListClienteContratoToAttach = em.getReference(clienteContratoListClienteContratoToAttach.getClass(), clienteContratoListClienteContratoToAttach.getCodClienteContrato());
                attachedClienteContratoList.add(clienteContratoListClienteContratoToAttach);
            }
            contrato.setClienteContratoList(attachedClienteContratoList);
            em.persist(contrato);
            for (ClienteContrato clienteContratoListClienteContrato : contrato.getClienteContratoList()) {
                Contrato oldCodContratoOfClienteContratoListClienteContrato = clienteContratoListClienteContrato.getCodContrato();
                clienteContratoListClienteContrato.setCodContrato(contrato);
                clienteContratoListClienteContrato = em.merge(clienteContratoListClienteContrato);
                if (oldCodContratoOfClienteContratoListClienteContrato != null) {
                    oldCodContratoOfClienteContratoListClienteContrato.getClienteContratoList().remove(clienteContratoListClienteContrato);
                    oldCodContratoOfClienteContratoListClienteContrato = em.merge(oldCodContratoOfClienteContratoListClienteContrato);
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

    public void edit(Contrato contrato) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Contrato persistentContrato = em.find(Contrato.class, contrato.getCodContrato());
            List<ClienteContrato> clienteContratoListOld = persistentContrato.getClienteContratoList();
            List<ClienteContrato> clienteContratoListNew = contrato.getClienteContratoList();
            List<String> illegalOrphanMessages = null;
            for (ClienteContrato clienteContratoListOldClienteContrato : clienteContratoListOld) {
                if (!clienteContratoListNew.contains(clienteContratoListOldClienteContrato)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ClienteContrato " + clienteContratoListOldClienteContrato + " since its codContrato field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ClienteContrato> attachedClienteContratoListNew = new ArrayList<ClienteContrato>();
            for (ClienteContrato clienteContratoListNewClienteContratoToAttach : clienteContratoListNew) {
                clienteContratoListNewClienteContratoToAttach = em.getReference(clienteContratoListNewClienteContratoToAttach.getClass(), clienteContratoListNewClienteContratoToAttach.getCodClienteContrato());
                attachedClienteContratoListNew.add(clienteContratoListNewClienteContratoToAttach);
            }
            clienteContratoListNew = attachedClienteContratoListNew;
            contrato.setClienteContratoList(clienteContratoListNew);
            contrato = em.merge(contrato);
            for (ClienteContrato clienteContratoListNewClienteContrato : clienteContratoListNew) {
                if (!clienteContratoListOld.contains(clienteContratoListNewClienteContrato)) {
                    Contrato oldCodContratoOfClienteContratoListNewClienteContrato = clienteContratoListNewClienteContrato.getCodContrato();
                    clienteContratoListNewClienteContrato.setCodContrato(contrato);
                    clienteContratoListNewClienteContrato = em.merge(clienteContratoListNewClienteContrato);
                    if (oldCodContratoOfClienteContratoListNewClienteContrato != null && !oldCodContratoOfClienteContratoListNewClienteContrato.equals(contrato)) {
                        oldCodContratoOfClienteContratoListNewClienteContrato.getClienteContratoList().remove(clienteContratoListNewClienteContrato);
                        oldCodContratoOfClienteContratoListNewClienteContrato = em.merge(oldCodContratoOfClienteContratoListNewClienteContrato);
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
                Integer id = contrato.getCodContrato();
                if (findContrato(id) == null) {
                    throw new NonexistentEntityException("The contrato with id " + id + " no longer exists.");
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
            Contrato contrato;
            try {
                contrato = em.getReference(Contrato.class, id);
                contrato.getCodContrato();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contrato with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ClienteContrato> clienteContratoListOrphanCheck = contrato.getClienteContratoList();
            for (ClienteContrato clienteContratoListOrphanCheckClienteContrato : clienteContratoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Contrato (" + contrato + ") cannot be destroyed since the ClienteContrato " + clienteContratoListOrphanCheckClienteContrato + " in its clienteContratoList field has a non-nullable codContrato field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(contrato);
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

    public List<Contrato> findContratoEntities() {
        return findContratoEntities(true, -1, -1);
    }

    public List<Contrato> findContratoEntities(int maxResults, int firstResult) {
        return findContratoEntities(false, maxResults, firstResult);
    }

    private List<Contrato> findContratoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contrato.class));
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

    public Contrato findContrato(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contrato.class, id);
        } finally {
            em.close();
        }
    }

    public int getContratoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contrato> rt = cq.from(Contrato.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
