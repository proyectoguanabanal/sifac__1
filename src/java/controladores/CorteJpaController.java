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
import entidades.Corte;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Factura;
import entidades.CorteUsuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class CorteJpaController implements Serializable {

    public CorteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Corte corte) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (corte.getCorteUsuarioList() == null) {
            corte.setCorteUsuarioList(new ArrayList<CorteUsuario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Factura codFactura = corte.getCodFactura();
            if (codFactura != null) {
                codFactura = em.getReference(codFactura.getClass(), codFactura.getCodFactura());
                corte.setCodFactura(codFactura);
            }
            List<CorteUsuario> attachedCorteUsuarioList = new ArrayList<CorteUsuario>();
            for (CorteUsuario corteUsuarioListCorteUsuarioToAttach : corte.getCorteUsuarioList()) {
                corteUsuarioListCorteUsuarioToAttach = em.getReference(corteUsuarioListCorteUsuarioToAttach.getClass(), corteUsuarioListCorteUsuarioToAttach.getCodCorteUsuario());
                attachedCorteUsuarioList.add(corteUsuarioListCorteUsuarioToAttach);
            }
            corte.setCorteUsuarioList(attachedCorteUsuarioList);
            em.persist(corte);
            if (codFactura != null) {
                codFactura.getCorteList().add(corte);
                codFactura = em.merge(codFactura);
            }
            for (CorteUsuario corteUsuarioListCorteUsuario : corte.getCorteUsuarioList()) {
                Corte oldCodCorteOfCorteUsuarioListCorteUsuario = corteUsuarioListCorteUsuario.getCodCorte();
                corteUsuarioListCorteUsuario.setCodCorte(corte);
                corteUsuarioListCorteUsuario = em.merge(corteUsuarioListCorteUsuario);
                if (oldCodCorteOfCorteUsuarioListCorteUsuario != null) {
                    oldCodCorteOfCorteUsuarioListCorteUsuario.getCorteUsuarioList().remove(corteUsuarioListCorteUsuario);
                    oldCodCorteOfCorteUsuarioListCorteUsuario = em.merge(oldCodCorteOfCorteUsuarioListCorteUsuario);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCorte(corte.getCodCorte()) != null) {
                throw new PreexistingEntityException("Corte " + corte + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Corte corte) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Corte persistentCorte = em.find(Corte.class, corte.getCodCorte());
            Factura codFacturaOld = persistentCorte.getCodFactura();
            Factura codFacturaNew = corte.getCodFactura();
            List<CorteUsuario> corteUsuarioListOld = persistentCorte.getCorteUsuarioList();
            List<CorteUsuario> corteUsuarioListNew = corte.getCorteUsuarioList();
            List<String> illegalOrphanMessages = null;
            for (CorteUsuario corteUsuarioListOldCorteUsuario : corteUsuarioListOld) {
                if (!corteUsuarioListNew.contains(corteUsuarioListOldCorteUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CorteUsuario " + corteUsuarioListOldCorteUsuario + " since its codCorte field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codFacturaNew != null) {
                codFacturaNew = em.getReference(codFacturaNew.getClass(), codFacturaNew.getCodFactura());
                corte.setCodFactura(codFacturaNew);
            }
            List<CorteUsuario> attachedCorteUsuarioListNew = new ArrayList<CorteUsuario>();
            for (CorteUsuario corteUsuarioListNewCorteUsuarioToAttach : corteUsuarioListNew) {
                corteUsuarioListNewCorteUsuarioToAttach = em.getReference(corteUsuarioListNewCorteUsuarioToAttach.getClass(), corteUsuarioListNewCorteUsuarioToAttach.getCodCorteUsuario());
                attachedCorteUsuarioListNew.add(corteUsuarioListNewCorteUsuarioToAttach);
            }
            corteUsuarioListNew = attachedCorteUsuarioListNew;
            corte.setCorteUsuarioList(corteUsuarioListNew);
            corte = em.merge(corte);
            if (codFacturaOld != null && !codFacturaOld.equals(codFacturaNew)) {
                codFacturaOld.getCorteList().remove(corte);
                codFacturaOld = em.merge(codFacturaOld);
            }
            if (codFacturaNew != null && !codFacturaNew.equals(codFacturaOld)) {
                codFacturaNew.getCorteList().add(corte);
                codFacturaNew = em.merge(codFacturaNew);
            }
            for (CorteUsuario corteUsuarioListNewCorteUsuario : corteUsuarioListNew) {
                if (!corteUsuarioListOld.contains(corteUsuarioListNewCorteUsuario)) {
                    Corte oldCodCorteOfCorteUsuarioListNewCorteUsuario = corteUsuarioListNewCorteUsuario.getCodCorte();
                    corteUsuarioListNewCorteUsuario.setCodCorte(corte);
                    corteUsuarioListNewCorteUsuario = em.merge(corteUsuarioListNewCorteUsuario);
                    if (oldCodCorteOfCorteUsuarioListNewCorteUsuario != null && !oldCodCorteOfCorteUsuarioListNewCorteUsuario.equals(corte)) {
                        oldCodCorteOfCorteUsuarioListNewCorteUsuario.getCorteUsuarioList().remove(corteUsuarioListNewCorteUsuario);
                        oldCodCorteOfCorteUsuarioListNewCorteUsuario = em.merge(oldCodCorteOfCorteUsuarioListNewCorteUsuario);
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
                Integer id = corte.getCodCorte();
                if (findCorte(id) == null) {
                    throw new NonexistentEntityException("The corte with id " + id + " no longer exists.");
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
            Corte corte;
            try {
                corte = em.getReference(Corte.class, id);
                corte.getCodCorte();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The corte with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<CorteUsuario> corteUsuarioListOrphanCheck = corte.getCorteUsuarioList();
            for (CorteUsuario corteUsuarioListOrphanCheckCorteUsuario : corteUsuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Corte (" + corte + ") cannot be destroyed since the CorteUsuario " + corteUsuarioListOrphanCheckCorteUsuario + " in its corteUsuarioList field has a non-nullable codCorte field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Factura codFactura = corte.getCodFactura();
            if (codFactura != null) {
                codFactura.getCorteList().remove(corte);
                codFactura = em.merge(codFactura);
            }
            em.remove(corte);
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

    public List<Corte> findCorteEntities() {
        return findCorteEntities(true, -1, -1);
    }

    public List<Corte> findCorteEntities(int maxResults, int firstResult) {
        return findCorteEntities(false, maxResults, firstResult);
    }

    private List<Corte> findCorteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Corte.class));
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

    public Corte findCorte(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Corte.class, id);
        } finally {
            em.close();
        }
    }

    public int getCorteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Corte> rt = cq.from(Corte.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
