/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.RollbackFailureException;
import entidades.ClienteFactura;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Usuario;
import entidades.Factura;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class ClienteFacturaJpaController implements Serializable {

    public ClienteFacturaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ClienteFactura clienteFactura) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario codUsuario = clienteFactura.getCodUsuario();
            if (codUsuario != null) {
                codUsuario = em.getReference(codUsuario.getClass(), codUsuario.getCodUsuario());
                clienteFactura.setCodUsuario(codUsuario);
            }
            Factura codFactura = clienteFactura.getCodFactura();
            if (codFactura != null) {
                codFactura = em.getReference(codFactura.getClass(), codFactura.getCodFactura());
                clienteFactura.setCodFactura(codFactura);
            }
            em.persist(clienteFactura);
            if (codUsuario != null) {
                codUsuario.getClienteFacturaList().add(clienteFactura);
                codUsuario = em.merge(codUsuario);
            }
            if (codFactura != null) {
                codFactura.getClienteFacturaList().add(clienteFactura);
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

    public void edit(ClienteFactura clienteFactura) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ClienteFactura persistentClienteFactura = em.find(ClienteFactura.class, clienteFactura.getCodClienteFactura());
            Usuario codUsuarioOld = persistentClienteFactura.getCodUsuario();
            Usuario codUsuarioNew = clienteFactura.getCodUsuario();
            Factura codFacturaOld = persistentClienteFactura.getCodFactura();
            Factura codFacturaNew = clienteFactura.getCodFactura();
            if (codUsuarioNew != null) {
                codUsuarioNew = em.getReference(codUsuarioNew.getClass(), codUsuarioNew.getCodUsuario());
                clienteFactura.setCodUsuario(codUsuarioNew);
            }
            if (codFacturaNew != null) {
                codFacturaNew = em.getReference(codFacturaNew.getClass(), codFacturaNew.getCodFactura());
                clienteFactura.setCodFactura(codFacturaNew);
            }
            clienteFactura = em.merge(clienteFactura);
            if (codUsuarioOld != null && !codUsuarioOld.equals(codUsuarioNew)) {
                codUsuarioOld.getClienteFacturaList().remove(clienteFactura);
                codUsuarioOld = em.merge(codUsuarioOld);
            }
            if (codUsuarioNew != null && !codUsuarioNew.equals(codUsuarioOld)) {
                codUsuarioNew.getClienteFacturaList().add(clienteFactura);
                codUsuarioNew = em.merge(codUsuarioNew);
            }
            if (codFacturaOld != null && !codFacturaOld.equals(codFacturaNew)) {
                codFacturaOld.getClienteFacturaList().remove(clienteFactura);
                codFacturaOld = em.merge(codFacturaOld);
            }
            if (codFacturaNew != null && !codFacturaNew.equals(codFacturaOld)) {
                codFacturaNew.getClienteFacturaList().add(clienteFactura);
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
                Integer id = clienteFactura.getCodClienteFactura();
                if (findClienteFactura(id) == null) {
                    throw new NonexistentEntityException("The clienteFactura with id " + id + " no longer exists.");
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
            ClienteFactura clienteFactura;
            try {
                clienteFactura = em.getReference(ClienteFactura.class, id);
                clienteFactura.getCodClienteFactura();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clienteFactura with id " + id + " no longer exists.", enfe);
            }
            Usuario codUsuario = clienteFactura.getCodUsuario();
            if (codUsuario != null) {
                codUsuario.getClienteFacturaList().remove(clienteFactura);
                codUsuario = em.merge(codUsuario);
            }
            Factura codFactura = clienteFactura.getCodFactura();
            if (codFactura != null) {
                codFactura.getClienteFacturaList().remove(clienteFactura);
                codFactura = em.merge(codFactura);
            }
            em.remove(clienteFactura);
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

    public List<ClienteFactura> findClienteFacturaEntities() {
        return findClienteFacturaEntities(true, -1, -1);
    }

    public List<ClienteFactura> findClienteFacturaEntities(int maxResults, int firstResult) {
        return findClienteFacturaEntities(false, maxResults, firstResult);
    }

    private List<ClienteFactura> findClienteFacturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ClienteFactura.class));
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

    public ClienteFactura findClienteFactura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClienteFactura.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteFacturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ClienteFactura> rt = cq.from(ClienteFactura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
