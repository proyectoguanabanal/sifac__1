/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.PreexistingEntityException;
import controladores.exceptions.RollbackFailureException;
import entidades.ClienteEstado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Usuario;
import entidades.Estado;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class ClienteEstadoJpaController implements Serializable {

    public ClienteEstadoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ClienteEstado clienteEstado) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario codUsuario = clienteEstado.getCodUsuario();
            if (codUsuario != null) {
                codUsuario = em.getReference(codUsuario.getClass(), codUsuario.getCodUsuario());
                clienteEstado.setCodUsuario(codUsuario);
            }
            Estado codEstado = clienteEstado.getCodEstado();
            if (codEstado != null) {
                codEstado = em.getReference(codEstado.getClass(), codEstado.getCodEstado());
                clienteEstado.setCodEstado(codEstado);
            }
            em.persist(clienteEstado);
            if (codUsuario != null) {
                codUsuario.getClienteEstadoList().add(clienteEstado);
                codUsuario = em.merge(codUsuario);
            }
            if (codEstado != null) {
                codEstado.getClienteEstadoList().add(clienteEstado);
                codEstado = em.merge(codEstado);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findClienteEstado(clienteEstado.getCodClienteEstado()) != null) {
                throw new PreexistingEntityException("ClienteEstado " + clienteEstado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ClienteEstado clienteEstado) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ClienteEstado persistentClienteEstado = em.find(ClienteEstado.class, clienteEstado.getCodClienteEstado());
            Usuario codUsuarioOld = persistentClienteEstado.getCodUsuario();
            Usuario codUsuarioNew = clienteEstado.getCodUsuario();
            Estado codEstadoOld = persistentClienteEstado.getCodEstado();
            Estado codEstadoNew = clienteEstado.getCodEstado();
            if (codUsuarioNew != null) {
                codUsuarioNew = em.getReference(codUsuarioNew.getClass(), codUsuarioNew.getCodUsuario());
                clienteEstado.setCodUsuario(codUsuarioNew);
            }
            if (codEstadoNew != null) {
                codEstadoNew = em.getReference(codEstadoNew.getClass(), codEstadoNew.getCodEstado());
                clienteEstado.setCodEstado(codEstadoNew);
            }
            clienteEstado = em.merge(clienteEstado);
            if (codUsuarioOld != null && !codUsuarioOld.equals(codUsuarioNew)) {
                codUsuarioOld.getClienteEstadoList().remove(clienteEstado);
                codUsuarioOld = em.merge(codUsuarioOld);
            }
            if (codUsuarioNew != null && !codUsuarioNew.equals(codUsuarioOld)) {
                codUsuarioNew.getClienteEstadoList().add(clienteEstado);
                codUsuarioNew = em.merge(codUsuarioNew);
            }
            if (codEstadoOld != null && !codEstadoOld.equals(codEstadoNew)) {
                codEstadoOld.getClienteEstadoList().remove(clienteEstado);
                codEstadoOld = em.merge(codEstadoOld);
            }
            if (codEstadoNew != null && !codEstadoNew.equals(codEstadoOld)) {
                codEstadoNew.getClienteEstadoList().add(clienteEstado);
                codEstadoNew = em.merge(codEstadoNew);
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
                Integer id = clienteEstado.getCodClienteEstado();
                if (findClienteEstado(id) == null) {
                    throw new NonexistentEntityException("The clienteEstado with id " + id + " no longer exists.");
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
            ClienteEstado clienteEstado;
            try {
                clienteEstado = em.getReference(ClienteEstado.class, id);
                clienteEstado.getCodClienteEstado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clienteEstado with id " + id + " no longer exists.", enfe);
            }
            Usuario codUsuario = clienteEstado.getCodUsuario();
            if (codUsuario != null) {
                codUsuario.getClienteEstadoList().remove(clienteEstado);
                codUsuario = em.merge(codUsuario);
            }
            Estado codEstado = clienteEstado.getCodEstado();
            if (codEstado != null) {
                codEstado.getClienteEstadoList().remove(clienteEstado);
                codEstado = em.merge(codEstado);
            }
            em.remove(clienteEstado);
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

    public List<ClienteEstado> findClienteEstadoEntities() {
        return findClienteEstadoEntities(true, -1, -1);
    }

    public List<ClienteEstado> findClienteEstadoEntities(int maxResults, int firstResult) {
        return findClienteEstadoEntities(false, maxResults, firstResult);
    }

    private List<ClienteEstado> findClienteEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ClienteEstado.class));
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

    public ClienteEstado findClienteEstado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClienteEstado.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ClienteEstado> rt = cq.from(ClienteEstado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
