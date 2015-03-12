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
import entidades.Suministros;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.TipoSuministro;
import entidades.SuministrosProveedor;
import java.util.ArrayList;
import java.util.List;
import entidades.UsuarioSuministros;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class SuministrosJpaController implements Serializable {

    public SuministrosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Suministros suministros) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (suministros.getSuministrosProveedorList() == null) {
            suministros.setSuministrosProveedorList(new ArrayList<SuministrosProveedor>());
        }
        if (suministros.getUsuarioSuministrosList() == null) {
            suministros.setUsuarioSuministrosList(new ArrayList<UsuarioSuministros>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoSuministro codTipoSuministro = suministros.getCodTipoSuministro();
            if (codTipoSuministro != null) {
                codTipoSuministro = em.getReference(codTipoSuministro.getClass(), codTipoSuministro.getCodTipoSuministro());
                suministros.setCodTipoSuministro(codTipoSuministro);
            }
            List<SuministrosProveedor> attachedSuministrosProveedorList = new ArrayList<SuministrosProveedor>();
            for (SuministrosProveedor suministrosProveedorListSuministrosProveedorToAttach : suministros.getSuministrosProveedorList()) {
                suministrosProveedorListSuministrosProveedorToAttach = em.getReference(suministrosProveedorListSuministrosProveedorToAttach.getClass(), suministrosProveedorListSuministrosProveedorToAttach.getCodSuministrosProveedor());
                attachedSuministrosProveedorList.add(suministrosProveedorListSuministrosProveedorToAttach);
            }
            suministros.setSuministrosProveedorList(attachedSuministrosProveedorList);
            List<UsuarioSuministros> attachedUsuarioSuministrosList = new ArrayList<UsuarioSuministros>();
            for (UsuarioSuministros usuarioSuministrosListUsuarioSuministrosToAttach : suministros.getUsuarioSuministrosList()) {
                usuarioSuministrosListUsuarioSuministrosToAttach = em.getReference(usuarioSuministrosListUsuarioSuministrosToAttach.getClass(), usuarioSuministrosListUsuarioSuministrosToAttach.getCodUsuarioSuministro());
                attachedUsuarioSuministrosList.add(usuarioSuministrosListUsuarioSuministrosToAttach);
            }
            suministros.setUsuarioSuministrosList(attachedUsuarioSuministrosList);
            em.persist(suministros);
            if (codTipoSuministro != null) {
                codTipoSuministro.getSuministrosList().add(suministros);
                codTipoSuministro = em.merge(codTipoSuministro);
            }
            for (SuministrosProveedor suministrosProveedorListSuministrosProveedor : suministros.getSuministrosProveedorList()) {
                Suministros oldCodSuministrosOfSuministrosProveedorListSuministrosProveedor = suministrosProveedorListSuministrosProveedor.getCodSuministros();
                suministrosProveedorListSuministrosProveedor.setCodSuministros(suministros);
                suministrosProveedorListSuministrosProveedor = em.merge(suministrosProveedorListSuministrosProveedor);
                if (oldCodSuministrosOfSuministrosProveedorListSuministrosProveedor != null) {
                    oldCodSuministrosOfSuministrosProveedorListSuministrosProveedor.getSuministrosProveedorList().remove(suministrosProveedorListSuministrosProveedor);
                    oldCodSuministrosOfSuministrosProveedorListSuministrosProveedor = em.merge(oldCodSuministrosOfSuministrosProveedorListSuministrosProveedor);
                }
            }
            for (UsuarioSuministros usuarioSuministrosListUsuarioSuministros : suministros.getUsuarioSuministrosList()) {
                Suministros oldCodSuministrosOfUsuarioSuministrosListUsuarioSuministros = usuarioSuministrosListUsuarioSuministros.getCodSuministros();
                usuarioSuministrosListUsuarioSuministros.setCodSuministros(suministros);
                usuarioSuministrosListUsuarioSuministros = em.merge(usuarioSuministrosListUsuarioSuministros);
                if (oldCodSuministrosOfUsuarioSuministrosListUsuarioSuministros != null) {
                    oldCodSuministrosOfUsuarioSuministrosListUsuarioSuministros.getUsuarioSuministrosList().remove(usuarioSuministrosListUsuarioSuministros);
                    oldCodSuministrosOfUsuarioSuministrosListUsuarioSuministros = em.merge(oldCodSuministrosOfUsuarioSuministrosListUsuarioSuministros);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findSuministros(suministros.getCodSuministros()) != null) {
                throw new PreexistingEntityException("Suministros " + suministros + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Suministros suministros) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Suministros persistentSuministros = em.find(Suministros.class, suministros.getCodSuministros());
            TipoSuministro codTipoSuministroOld = persistentSuministros.getCodTipoSuministro();
            TipoSuministro codTipoSuministroNew = suministros.getCodTipoSuministro();
            List<SuministrosProveedor> suministrosProveedorListOld = persistentSuministros.getSuministrosProveedorList();
            List<SuministrosProveedor> suministrosProveedorListNew = suministros.getSuministrosProveedorList();
            List<UsuarioSuministros> usuarioSuministrosListOld = persistentSuministros.getUsuarioSuministrosList();
            List<UsuarioSuministros> usuarioSuministrosListNew = suministros.getUsuarioSuministrosList();
            List<String> illegalOrphanMessages = null;
            for (SuministrosProveedor suministrosProveedorListOldSuministrosProveedor : suministrosProveedorListOld) {
                if (!suministrosProveedorListNew.contains(suministrosProveedorListOldSuministrosProveedor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SuministrosProveedor " + suministrosProveedorListOldSuministrosProveedor + " since its codSuministros field is not nullable.");
                }
            }
            for (UsuarioSuministros usuarioSuministrosListOldUsuarioSuministros : usuarioSuministrosListOld) {
                if (!usuarioSuministrosListNew.contains(usuarioSuministrosListOldUsuarioSuministros)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioSuministros " + usuarioSuministrosListOldUsuarioSuministros + " since its codSuministros field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codTipoSuministroNew != null) {
                codTipoSuministroNew = em.getReference(codTipoSuministroNew.getClass(), codTipoSuministroNew.getCodTipoSuministro());
                suministros.setCodTipoSuministro(codTipoSuministroNew);
            }
            List<SuministrosProveedor> attachedSuministrosProveedorListNew = new ArrayList<SuministrosProveedor>();
            for (SuministrosProveedor suministrosProveedorListNewSuministrosProveedorToAttach : suministrosProveedorListNew) {
                suministrosProveedorListNewSuministrosProveedorToAttach = em.getReference(suministrosProveedorListNewSuministrosProveedorToAttach.getClass(), suministrosProveedorListNewSuministrosProveedorToAttach.getCodSuministrosProveedor());
                attachedSuministrosProveedorListNew.add(suministrosProveedorListNewSuministrosProveedorToAttach);
            }
            suministrosProveedorListNew = attachedSuministrosProveedorListNew;
            suministros.setSuministrosProveedorList(suministrosProveedorListNew);
            List<UsuarioSuministros> attachedUsuarioSuministrosListNew = new ArrayList<UsuarioSuministros>();
            for (UsuarioSuministros usuarioSuministrosListNewUsuarioSuministrosToAttach : usuarioSuministrosListNew) {
                usuarioSuministrosListNewUsuarioSuministrosToAttach = em.getReference(usuarioSuministrosListNewUsuarioSuministrosToAttach.getClass(), usuarioSuministrosListNewUsuarioSuministrosToAttach.getCodUsuarioSuministro());
                attachedUsuarioSuministrosListNew.add(usuarioSuministrosListNewUsuarioSuministrosToAttach);
            }
            usuarioSuministrosListNew = attachedUsuarioSuministrosListNew;
            suministros.setUsuarioSuministrosList(usuarioSuministrosListNew);
            suministros = em.merge(suministros);
            if (codTipoSuministroOld != null && !codTipoSuministroOld.equals(codTipoSuministroNew)) {
                codTipoSuministroOld.getSuministrosList().remove(suministros);
                codTipoSuministroOld = em.merge(codTipoSuministroOld);
            }
            if (codTipoSuministroNew != null && !codTipoSuministroNew.equals(codTipoSuministroOld)) {
                codTipoSuministroNew.getSuministrosList().add(suministros);
                codTipoSuministroNew = em.merge(codTipoSuministroNew);
            }
            for (SuministrosProveedor suministrosProveedorListNewSuministrosProveedor : suministrosProveedorListNew) {
                if (!suministrosProveedorListOld.contains(suministrosProveedorListNewSuministrosProveedor)) {
                    Suministros oldCodSuministrosOfSuministrosProveedorListNewSuministrosProveedor = suministrosProveedorListNewSuministrosProveedor.getCodSuministros();
                    suministrosProveedorListNewSuministrosProveedor.setCodSuministros(suministros);
                    suministrosProveedorListNewSuministrosProveedor = em.merge(suministrosProveedorListNewSuministrosProveedor);
                    if (oldCodSuministrosOfSuministrosProveedorListNewSuministrosProveedor != null && !oldCodSuministrosOfSuministrosProveedorListNewSuministrosProveedor.equals(suministros)) {
                        oldCodSuministrosOfSuministrosProveedorListNewSuministrosProveedor.getSuministrosProveedorList().remove(suministrosProveedorListNewSuministrosProveedor);
                        oldCodSuministrosOfSuministrosProveedorListNewSuministrosProveedor = em.merge(oldCodSuministrosOfSuministrosProveedorListNewSuministrosProveedor);
                    }
                }
            }
            for (UsuarioSuministros usuarioSuministrosListNewUsuarioSuministros : usuarioSuministrosListNew) {
                if (!usuarioSuministrosListOld.contains(usuarioSuministrosListNewUsuarioSuministros)) {
                    Suministros oldCodSuministrosOfUsuarioSuministrosListNewUsuarioSuministros = usuarioSuministrosListNewUsuarioSuministros.getCodSuministros();
                    usuarioSuministrosListNewUsuarioSuministros.setCodSuministros(suministros);
                    usuarioSuministrosListNewUsuarioSuministros = em.merge(usuarioSuministrosListNewUsuarioSuministros);
                    if (oldCodSuministrosOfUsuarioSuministrosListNewUsuarioSuministros != null && !oldCodSuministrosOfUsuarioSuministrosListNewUsuarioSuministros.equals(suministros)) {
                        oldCodSuministrosOfUsuarioSuministrosListNewUsuarioSuministros.getUsuarioSuministrosList().remove(usuarioSuministrosListNewUsuarioSuministros);
                        oldCodSuministrosOfUsuarioSuministrosListNewUsuarioSuministros = em.merge(oldCodSuministrosOfUsuarioSuministrosListNewUsuarioSuministros);
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
                Integer id = suministros.getCodSuministros();
                if (findSuministros(id) == null) {
                    throw new NonexistentEntityException("The suministros with id " + id + " no longer exists.");
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
            Suministros suministros;
            try {
                suministros = em.getReference(Suministros.class, id);
                suministros.getCodSuministros();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The suministros with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<SuministrosProveedor> suministrosProveedorListOrphanCheck = suministros.getSuministrosProveedorList();
            for (SuministrosProveedor suministrosProveedorListOrphanCheckSuministrosProveedor : suministrosProveedorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Suministros (" + suministros + ") cannot be destroyed since the SuministrosProveedor " + suministrosProveedorListOrphanCheckSuministrosProveedor + " in its suministrosProveedorList field has a non-nullable codSuministros field.");
            }
            List<UsuarioSuministros> usuarioSuministrosListOrphanCheck = suministros.getUsuarioSuministrosList();
            for (UsuarioSuministros usuarioSuministrosListOrphanCheckUsuarioSuministros : usuarioSuministrosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Suministros (" + suministros + ") cannot be destroyed since the UsuarioSuministros " + usuarioSuministrosListOrphanCheckUsuarioSuministros + " in its usuarioSuministrosList field has a non-nullable codSuministros field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoSuministro codTipoSuministro = suministros.getCodTipoSuministro();
            if (codTipoSuministro != null) {
                codTipoSuministro.getSuministrosList().remove(suministros);
                codTipoSuministro = em.merge(codTipoSuministro);
            }
            em.remove(suministros);
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

    public List<Suministros> findSuministrosEntities() {
        return findSuministrosEntities(true, -1, -1);
    }

    public List<Suministros> findSuministrosEntities(int maxResults, int firstResult) {
        return findSuministrosEntities(false, maxResults, firstResult);
    }

    private List<Suministros> findSuministrosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Suministros.class));
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

    public Suministros findSuministros(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Suministros.class, id);
        } finally {
            em.close();
        }
    }

    public int getSuministrosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Suministros> rt = cq.from(Suministros.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
