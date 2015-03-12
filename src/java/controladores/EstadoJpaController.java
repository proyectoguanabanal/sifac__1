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
import entidades.ClienteEstado;
import entidades.Estado;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class EstadoJpaController implements Serializable {

    public EstadoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estado estado) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (estado.getClienteEstadoList() == null) {
            estado.setClienteEstadoList(new ArrayList<ClienteEstado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<ClienteEstado> attachedClienteEstadoList = new ArrayList<ClienteEstado>();
            for (ClienteEstado clienteEstadoListClienteEstadoToAttach : estado.getClienteEstadoList()) {
                clienteEstadoListClienteEstadoToAttach = em.getReference(clienteEstadoListClienteEstadoToAttach.getClass(), clienteEstadoListClienteEstadoToAttach.getCodClienteEstado());
                attachedClienteEstadoList.add(clienteEstadoListClienteEstadoToAttach);
            }
            estado.setClienteEstadoList(attachedClienteEstadoList);
            em.persist(estado);
            for (ClienteEstado clienteEstadoListClienteEstado : estado.getClienteEstadoList()) {
                Estado oldCodEstadoOfClienteEstadoListClienteEstado = clienteEstadoListClienteEstado.getCodEstado();
                clienteEstadoListClienteEstado.setCodEstado(estado);
                clienteEstadoListClienteEstado = em.merge(clienteEstadoListClienteEstado);
                if (oldCodEstadoOfClienteEstadoListClienteEstado != null) {
                    oldCodEstadoOfClienteEstadoListClienteEstado.getClienteEstadoList().remove(clienteEstadoListClienteEstado);
                    oldCodEstadoOfClienteEstadoListClienteEstado = em.merge(oldCodEstadoOfClienteEstadoListClienteEstado);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEstado(estado.getCodEstado()) != null) {
                throw new PreexistingEntityException("Estado " + estado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estado estado) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Estado persistentEstado = em.find(Estado.class, estado.getCodEstado());
            List<ClienteEstado> clienteEstadoListOld = persistentEstado.getClienteEstadoList();
            List<ClienteEstado> clienteEstadoListNew = estado.getClienteEstadoList();
            List<String> illegalOrphanMessages = null;
            for (ClienteEstado clienteEstadoListOldClienteEstado : clienteEstadoListOld) {
                if (!clienteEstadoListNew.contains(clienteEstadoListOldClienteEstado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ClienteEstado " + clienteEstadoListOldClienteEstado + " since its codEstado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ClienteEstado> attachedClienteEstadoListNew = new ArrayList<ClienteEstado>();
            for (ClienteEstado clienteEstadoListNewClienteEstadoToAttach : clienteEstadoListNew) {
                clienteEstadoListNewClienteEstadoToAttach = em.getReference(clienteEstadoListNewClienteEstadoToAttach.getClass(), clienteEstadoListNewClienteEstadoToAttach.getCodClienteEstado());
                attachedClienteEstadoListNew.add(clienteEstadoListNewClienteEstadoToAttach);
            }
            clienteEstadoListNew = attachedClienteEstadoListNew;
            estado.setClienteEstadoList(clienteEstadoListNew);
            estado = em.merge(estado);
            for (ClienteEstado clienteEstadoListNewClienteEstado : clienteEstadoListNew) {
                if (!clienteEstadoListOld.contains(clienteEstadoListNewClienteEstado)) {
                    Estado oldCodEstadoOfClienteEstadoListNewClienteEstado = clienteEstadoListNewClienteEstado.getCodEstado();
                    clienteEstadoListNewClienteEstado.setCodEstado(estado);
                    clienteEstadoListNewClienteEstado = em.merge(clienteEstadoListNewClienteEstado);
                    if (oldCodEstadoOfClienteEstadoListNewClienteEstado != null && !oldCodEstadoOfClienteEstadoListNewClienteEstado.equals(estado)) {
                        oldCodEstadoOfClienteEstadoListNewClienteEstado.getClienteEstadoList().remove(clienteEstadoListNewClienteEstado);
                        oldCodEstadoOfClienteEstadoListNewClienteEstado = em.merge(oldCodEstadoOfClienteEstadoListNewClienteEstado);
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
                Integer id = estado.getCodEstado();
                if (findEstado(id) == null) {
                    throw new NonexistentEntityException("The estado with id " + id + " no longer exists.");
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
            Estado estado;
            try {
                estado = em.getReference(Estado.class, id);
                estado.getCodEstado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ClienteEstado> clienteEstadoListOrphanCheck = estado.getClienteEstadoList();
            for (ClienteEstado clienteEstadoListOrphanCheckClienteEstado : clienteEstadoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estado (" + estado + ") cannot be destroyed since the ClienteEstado " + clienteEstadoListOrphanCheckClienteEstado + " in its clienteEstadoList field has a non-nullable codEstado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estado);
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

    public List<Estado> findEstadoEntities() {
        return findEstadoEntities(true, -1, -1);
    }

    public List<Estado> findEstadoEntities(int maxResults, int firstResult) {
        return findEstadoEntities(false, maxResults, firstResult);
    }

    private List<Estado> findEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estado.class));
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

    public Estado findEstado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estado> rt = cq.from(Estado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
