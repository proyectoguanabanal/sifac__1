/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.RollbackFailureException;
import entidades.ClienteContrato;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Usuario;
import entidades.Contrato;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class ClienteContratoJpaController implements Serializable {

    public ClienteContratoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ClienteContrato clienteContrato) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario codUsuario = clienteContrato.getCodUsuario();
            if (codUsuario != null) {
                codUsuario = em.getReference(codUsuario.getClass(), codUsuario.getCodUsuario());
                clienteContrato.setCodUsuario(codUsuario);
            }
            Contrato codContrato = clienteContrato.getCodContrato();
            if (codContrato != null) {
                codContrato = em.getReference(codContrato.getClass(), codContrato.getCodContrato());
                clienteContrato.setCodContrato(codContrato);
            }
            em.persist(clienteContrato);
            if (codUsuario != null) {
                codUsuario.getClienteContratoList().add(clienteContrato);
                codUsuario = em.merge(codUsuario);
            }
            if (codContrato != null) {
                codContrato.getClienteContratoList().add(clienteContrato);
                codContrato = em.merge(codContrato);
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

    public void edit(ClienteContrato clienteContrato) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ClienteContrato persistentClienteContrato = em.find(ClienteContrato.class, clienteContrato.getCodClienteContrato());
            Usuario codUsuarioOld = persistentClienteContrato.getCodUsuario();
            Usuario codUsuarioNew = clienteContrato.getCodUsuario();
            Contrato codContratoOld = persistentClienteContrato.getCodContrato();
            Contrato codContratoNew = clienteContrato.getCodContrato();
            if (codUsuarioNew != null) {
                codUsuarioNew = em.getReference(codUsuarioNew.getClass(), codUsuarioNew.getCodUsuario());
                clienteContrato.setCodUsuario(codUsuarioNew);
            }
            if (codContratoNew != null) {
                codContratoNew = em.getReference(codContratoNew.getClass(), codContratoNew.getCodContrato());
                clienteContrato.setCodContrato(codContratoNew);
            }
            clienteContrato = em.merge(clienteContrato);
            if (codUsuarioOld != null && !codUsuarioOld.equals(codUsuarioNew)) {
                codUsuarioOld.getClienteContratoList().remove(clienteContrato);
                codUsuarioOld = em.merge(codUsuarioOld);
            }
            if (codUsuarioNew != null && !codUsuarioNew.equals(codUsuarioOld)) {
                codUsuarioNew.getClienteContratoList().add(clienteContrato);
                codUsuarioNew = em.merge(codUsuarioNew);
            }
            if (codContratoOld != null && !codContratoOld.equals(codContratoNew)) {
                codContratoOld.getClienteContratoList().remove(clienteContrato);
                codContratoOld = em.merge(codContratoOld);
            }
            if (codContratoNew != null && !codContratoNew.equals(codContratoOld)) {
                codContratoNew.getClienteContratoList().add(clienteContrato);
                codContratoNew = em.merge(codContratoNew);
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
                Integer id = clienteContrato.getCodClienteContrato();
                if (findClienteContrato(id) == null) {
                    throw new NonexistentEntityException("The clienteContrato with id " + id + " no longer exists.");
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
            ClienteContrato clienteContrato;
            try {
                clienteContrato = em.getReference(ClienteContrato.class, id);
                clienteContrato.getCodClienteContrato();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clienteContrato with id " + id + " no longer exists.", enfe);
            }
            Usuario codUsuario = clienteContrato.getCodUsuario();
            if (codUsuario != null) {
                codUsuario.getClienteContratoList().remove(clienteContrato);
                codUsuario = em.merge(codUsuario);
            }
            Contrato codContrato = clienteContrato.getCodContrato();
            if (codContrato != null) {
                codContrato.getClienteContratoList().remove(clienteContrato);
                codContrato = em.merge(codContrato);
            }
            em.remove(clienteContrato);
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

    public List<ClienteContrato> findClienteContratoEntities() {
        return findClienteContratoEntities(true, -1, -1);
    }

    public List<ClienteContrato> findClienteContratoEntities(int maxResults, int firstResult) {
        return findClienteContratoEntities(false, maxResults, firstResult);
    }

    private List<ClienteContrato> findClienteContratoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ClienteContrato.class));
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

    public ClienteContrato findClienteContrato(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClienteContrato.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteContratoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ClienteContrato> rt = cq.from(ClienteContrato.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
