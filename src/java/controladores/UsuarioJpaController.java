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
import entidades.ClienteEstado;
import java.util.ArrayList;
import java.util.List;
import entidades.ClienteContrato;
import entidades.UsuarioReconexion;
import entidades.UsuarioRol;
import entidades.CorteUsuario;
import entidades.ClienteFactura;
import entidades.Usuario;
import entidades.UsuarioSuministros;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author yinneandyor
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws RollbackFailureException, Exception {
        if (usuario.getClienteEstadoList() == null) {
            usuario.setClienteEstadoList(new ArrayList<ClienteEstado>());
        }
        if (usuario.getClienteContratoList() == null) {
            usuario.setClienteContratoList(new ArrayList<ClienteContrato>());
        }
        if (usuario.getUsuarioReconexionList() == null) {
            usuario.setUsuarioReconexionList(new ArrayList<UsuarioReconexion>());
        }
        if (usuario.getUsuarioRolList() == null) {
            usuario.setUsuarioRolList(new ArrayList<UsuarioRol>());
        }
        if (usuario.getCorteUsuarioList() == null) {
            usuario.setCorteUsuarioList(new ArrayList<CorteUsuario>());
        }
        if (usuario.getClienteFacturaList() == null) {
            usuario.setClienteFacturaList(new ArrayList<ClienteFactura>());
        }
        if (usuario.getUsuarioSuministrosList() == null) {
            usuario.setUsuarioSuministrosList(new ArrayList<UsuarioSuministros>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<ClienteEstado> attachedClienteEstadoList = new ArrayList<ClienteEstado>();
            for (ClienteEstado clienteEstadoListClienteEstadoToAttach : usuario.getClienteEstadoList()) {
                clienteEstadoListClienteEstadoToAttach = em.getReference(clienteEstadoListClienteEstadoToAttach.getClass(), clienteEstadoListClienteEstadoToAttach.getCodClienteEstado());
                attachedClienteEstadoList.add(clienteEstadoListClienteEstadoToAttach);
            }
            usuario.setClienteEstadoList(attachedClienteEstadoList);
            List<ClienteContrato> attachedClienteContratoList = new ArrayList<ClienteContrato>();
            for (ClienteContrato clienteContratoListClienteContratoToAttach : usuario.getClienteContratoList()) {
                clienteContratoListClienteContratoToAttach = em.getReference(clienteContratoListClienteContratoToAttach.getClass(), clienteContratoListClienteContratoToAttach.getCodClienteContrato());
                attachedClienteContratoList.add(clienteContratoListClienteContratoToAttach);
            }
            usuario.setClienteContratoList(attachedClienteContratoList);
            List<UsuarioReconexion> attachedUsuarioReconexionList = new ArrayList<UsuarioReconexion>();
            for (UsuarioReconexion usuarioReconexionListUsuarioReconexionToAttach : usuario.getUsuarioReconexionList()) {
                usuarioReconexionListUsuarioReconexionToAttach = em.getReference(usuarioReconexionListUsuarioReconexionToAttach.getClass(), usuarioReconexionListUsuarioReconexionToAttach.getCodUsuarioReconexion());
                attachedUsuarioReconexionList.add(usuarioReconexionListUsuarioReconexionToAttach);
            }
            usuario.setUsuarioReconexionList(attachedUsuarioReconexionList);
            List<UsuarioRol> attachedUsuarioRolList = new ArrayList<UsuarioRol>();
            for (UsuarioRol usuarioRolListUsuarioRolToAttach : usuario.getUsuarioRolList()) {
                usuarioRolListUsuarioRolToAttach = em.getReference(usuarioRolListUsuarioRolToAttach.getClass(), usuarioRolListUsuarioRolToAttach.getCodUsuarioRol());
                attachedUsuarioRolList.add(usuarioRolListUsuarioRolToAttach);
            }
            usuario.setUsuarioRolList(attachedUsuarioRolList);
            List<CorteUsuario> attachedCorteUsuarioList = new ArrayList<CorteUsuario>();
            for (CorteUsuario corteUsuarioListCorteUsuarioToAttach : usuario.getCorteUsuarioList()) {
                corteUsuarioListCorteUsuarioToAttach = em.getReference(corteUsuarioListCorteUsuarioToAttach.getClass(), corteUsuarioListCorteUsuarioToAttach.getCodCorteUsuario());
                attachedCorteUsuarioList.add(corteUsuarioListCorteUsuarioToAttach);
            }
            usuario.setCorteUsuarioList(attachedCorteUsuarioList);
            List<ClienteFactura> attachedClienteFacturaList = new ArrayList<ClienteFactura>();
            for (ClienteFactura clienteFacturaListClienteFacturaToAttach : usuario.getClienteFacturaList()) {
                clienteFacturaListClienteFacturaToAttach = em.getReference(clienteFacturaListClienteFacturaToAttach.getClass(), clienteFacturaListClienteFacturaToAttach.getCodClienteFactura());
                attachedClienteFacturaList.add(clienteFacturaListClienteFacturaToAttach);
            }
            usuario.setClienteFacturaList(attachedClienteFacturaList);
            List<UsuarioSuministros> attachedUsuarioSuministrosList = new ArrayList<UsuarioSuministros>();
            for (UsuarioSuministros usuarioSuministrosListUsuarioSuministrosToAttach : usuario.getUsuarioSuministrosList()) {
                usuarioSuministrosListUsuarioSuministrosToAttach = em.getReference(usuarioSuministrosListUsuarioSuministrosToAttach.getClass(), usuarioSuministrosListUsuarioSuministrosToAttach.getCodUsuarioSuministro());
                attachedUsuarioSuministrosList.add(usuarioSuministrosListUsuarioSuministrosToAttach);
            }
            usuario.setUsuarioSuministrosList(attachedUsuarioSuministrosList);
            em.persist(usuario);
            for (ClienteEstado clienteEstadoListClienteEstado : usuario.getClienteEstadoList()) {
                Usuario oldCodUsuarioOfClienteEstadoListClienteEstado = clienteEstadoListClienteEstado.getCodUsuario();
                clienteEstadoListClienteEstado.setCodUsuario(usuario);
                clienteEstadoListClienteEstado = em.merge(clienteEstadoListClienteEstado);
                if (oldCodUsuarioOfClienteEstadoListClienteEstado != null) {
                    oldCodUsuarioOfClienteEstadoListClienteEstado.getClienteEstadoList().remove(clienteEstadoListClienteEstado);
                    oldCodUsuarioOfClienteEstadoListClienteEstado = em.merge(oldCodUsuarioOfClienteEstadoListClienteEstado);
                }
            }
            for (ClienteContrato clienteContratoListClienteContrato : usuario.getClienteContratoList()) {
                Usuario oldCodUsuarioOfClienteContratoListClienteContrato = clienteContratoListClienteContrato.getCodUsuario();
                clienteContratoListClienteContrato.setCodUsuario(usuario);
                clienteContratoListClienteContrato = em.merge(clienteContratoListClienteContrato);
                if (oldCodUsuarioOfClienteContratoListClienteContrato != null) {
                    oldCodUsuarioOfClienteContratoListClienteContrato.getClienteContratoList().remove(clienteContratoListClienteContrato);
                    oldCodUsuarioOfClienteContratoListClienteContrato = em.merge(oldCodUsuarioOfClienteContratoListClienteContrato);
                }
            }
            for (UsuarioReconexion usuarioReconexionListUsuarioReconexion : usuario.getUsuarioReconexionList()) {
                Usuario oldCodUsuarioOfUsuarioReconexionListUsuarioReconexion = usuarioReconexionListUsuarioReconexion.getCodUsuario();
                usuarioReconexionListUsuarioReconexion.setCodUsuario(usuario);
                usuarioReconexionListUsuarioReconexion = em.merge(usuarioReconexionListUsuarioReconexion);
                if (oldCodUsuarioOfUsuarioReconexionListUsuarioReconexion != null) {
                    oldCodUsuarioOfUsuarioReconexionListUsuarioReconexion.getUsuarioReconexionList().remove(usuarioReconexionListUsuarioReconexion);
                    oldCodUsuarioOfUsuarioReconexionListUsuarioReconexion = em.merge(oldCodUsuarioOfUsuarioReconexionListUsuarioReconexion);
                }
            }
            for (UsuarioRol usuarioRolListUsuarioRol : usuario.getUsuarioRolList()) {
                Usuario oldCodUsuarioOfUsuarioRolListUsuarioRol = usuarioRolListUsuarioRol.getCodUsuario();
                usuarioRolListUsuarioRol.setCodUsuario(usuario);
                usuarioRolListUsuarioRol = em.merge(usuarioRolListUsuarioRol);
                if (oldCodUsuarioOfUsuarioRolListUsuarioRol != null) {
                    oldCodUsuarioOfUsuarioRolListUsuarioRol.getUsuarioRolList().remove(usuarioRolListUsuarioRol);
                    oldCodUsuarioOfUsuarioRolListUsuarioRol = em.merge(oldCodUsuarioOfUsuarioRolListUsuarioRol);
                }
            }
            for (CorteUsuario corteUsuarioListCorteUsuario : usuario.getCorteUsuarioList()) {
                Usuario oldCodUsuarioOfCorteUsuarioListCorteUsuario = corteUsuarioListCorteUsuario.getCodUsuario();
                corteUsuarioListCorteUsuario.setCodUsuario(usuario);
                corteUsuarioListCorteUsuario = em.merge(corteUsuarioListCorteUsuario);
                if (oldCodUsuarioOfCorteUsuarioListCorteUsuario != null) {
                    oldCodUsuarioOfCorteUsuarioListCorteUsuario.getCorteUsuarioList().remove(corteUsuarioListCorteUsuario);
                    oldCodUsuarioOfCorteUsuarioListCorteUsuario = em.merge(oldCodUsuarioOfCorteUsuarioListCorteUsuario);
                }
            }
            for (ClienteFactura clienteFacturaListClienteFactura : usuario.getClienteFacturaList()) {
                Usuario oldCodUsuarioOfClienteFacturaListClienteFactura = clienteFacturaListClienteFactura.getCodUsuario();
                clienteFacturaListClienteFactura.setCodUsuario(usuario);
                clienteFacturaListClienteFactura = em.merge(clienteFacturaListClienteFactura);
                if (oldCodUsuarioOfClienteFacturaListClienteFactura != null) {
                    oldCodUsuarioOfClienteFacturaListClienteFactura.getClienteFacturaList().remove(clienteFacturaListClienteFactura);
                    oldCodUsuarioOfClienteFacturaListClienteFactura = em.merge(oldCodUsuarioOfClienteFacturaListClienteFactura);
                }
            }
            for (UsuarioSuministros usuarioSuministrosListUsuarioSuministros : usuario.getUsuarioSuministrosList()) {
                Usuario oldCodUsuarioOfUsuarioSuministrosListUsuarioSuministros = usuarioSuministrosListUsuarioSuministros.getCodUsuario();
                usuarioSuministrosListUsuarioSuministros.setCodUsuario(usuario);
                usuarioSuministrosListUsuarioSuministros = em.merge(usuarioSuministrosListUsuarioSuministros);
                if (oldCodUsuarioOfUsuarioSuministrosListUsuarioSuministros != null) {
                    oldCodUsuarioOfUsuarioSuministrosListUsuarioSuministros.getUsuarioSuministrosList().remove(usuarioSuministrosListUsuarioSuministros);
                    oldCodUsuarioOfUsuarioSuministrosListUsuarioSuministros = em.merge(oldCodUsuarioOfUsuarioSuministrosListUsuarioSuministros);
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

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getCodUsuario());
            List<ClienteEstado> clienteEstadoListOld = persistentUsuario.getClienteEstadoList();
            List<ClienteEstado> clienteEstadoListNew = usuario.getClienteEstadoList();
            List<ClienteContrato> clienteContratoListOld = persistentUsuario.getClienteContratoList();
            List<ClienteContrato> clienteContratoListNew = usuario.getClienteContratoList();
            List<UsuarioReconexion> usuarioReconexionListOld = persistentUsuario.getUsuarioReconexionList();
            List<UsuarioReconexion> usuarioReconexionListNew = usuario.getUsuarioReconexionList();
            List<UsuarioRol> usuarioRolListOld = persistentUsuario.getUsuarioRolList();
            List<UsuarioRol> usuarioRolListNew = usuario.getUsuarioRolList();
            List<CorteUsuario> corteUsuarioListOld = persistentUsuario.getCorteUsuarioList();
            List<CorteUsuario> corteUsuarioListNew = usuario.getCorteUsuarioList();
            List<ClienteFactura> clienteFacturaListOld = persistentUsuario.getClienteFacturaList();
            List<ClienteFactura> clienteFacturaListNew = usuario.getClienteFacturaList();
            List<UsuarioSuministros> usuarioSuministrosListOld = persistentUsuario.getUsuarioSuministrosList();
            List<UsuarioSuministros> usuarioSuministrosListNew = usuario.getUsuarioSuministrosList();
            List<String> illegalOrphanMessages = null;
            for (ClienteEstado clienteEstadoListOldClienteEstado : clienteEstadoListOld) {
                if (!clienteEstadoListNew.contains(clienteEstadoListOldClienteEstado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ClienteEstado " + clienteEstadoListOldClienteEstado + " since its codUsuario field is not nullable.");
                }
            }
            for (ClienteContrato clienteContratoListOldClienteContrato : clienteContratoListOld) {
                if (!clienteContratoListNew.contains(clienteContratoListOldClienteContrato)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ClienteContrato " + clienteContratoListOldClienteContrato + " since its codUsuario field is not nullable.");
                }
            }
            for (UsuarioReconexion usuarioReconexionListOldUsuarioReconexion : usuarioReconexionListOld) {
                if (!usuarioReconexionListNew.contains(usuarioReconexionListOldUsuarioReconexion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioReconexion " + usuarioReconexionListOldUsuarioReconexion + " since its codUsuario field is not nullable.");
                }
            }
            for (UsuarioRol usuarioRolListOldUsuarioRol : usuarioRolListOld) {
                if (!usuarioRolListNew.contains(usuarioRolListOldUsuarioRol)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioRol " + usuarioRolListOldUsuarioRol + " since its codUsuario field is not nullable.");
                }
            }
            for (CorteUsuario corteUsuarioListOldCorteUsuario : corteUsuarioListOld) {
                if (!corteUsuarioListNew.contains(corteUsuarioListOldCorteUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CorteUsuario " + corteUsuarioListOldCorteUsuario + " since its codUsuario field is not nullable.");
                }
            }
            for (ClienteFactura clienteFacturaListOldClienteFactura : clienteFacturaListOld) {
                if (!clienteFacturaListNew.contains(clienteFacturaListOldClienteFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ClienteFactura " + clienteFacturaListOldClienteFactura + " since its codUsuario field is not nullable.");
                }
            }
            for (UsuarioSuministros usuarioSuministrosListOldUsuarioSuministros : usuarioSuministrosListOld) {
                if (!usuarioSuministrosListNew.contains(usuarioSuministrosListOldUsuarioSuministros)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioSuministros " + usuarioSuministrosListOldUsuarioSuministros + " since its codUsuario field is not nullable.");
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
            usuario.setClienteEstadoList(clienteEstadoListNew);
            List<ClienteContrato> attachedClienteContratoListNew = new ArrayList<ClienteContrato>();
            for (ClienteContrato clienteContratoListNewClienteContratoToAttach : clienteContratoListNew) {
                clienteContratoListNewClienteContratoToAttach = em.getReference(clienteContratoListNewClienteContratoToAttach.getClass(), clienteContratoListNewClienteContratoToAttach.getCodClienteContrato());
                attachedClienteContratoListNew.add(clienteContratoListNewClienteContratoToAttach);
            }
            clienteContratoListNew = attachedClienteContratoListNew;
            usuario.setClienteContratoList(clienteContratoListNew);
            List<UsuarioReconexion> attachedUsuarioReconexionListNew = new ArrayList<UsuarioReconexion>();
            for (UsuarioReconexion usuarioReconexionListNewUsuarioReconexionToAttach : usuarioReconexionListNew) {
                usuarioReconexionListNewUsuarioReconexionToAttach = em.getReference(usuarioReconexionListNewUsuarioReconexionToAttach.getClass(), usuarioReconexionListNewUsuarioReconexionToAttach.getCodUsuarioReconexion());
                attachedUsuarioReconexionListNew.add(usuarioReconexionListNewUsuarioReconexionToAttach);
            }
            usuarioReconexionListNew = attachedUsuarioReconexionListNew;
            usuario.setUsuarioReconexionList(usuarioReconexionListNew);
            List<UsuarioRol> attachedUsuarioRolListNew = new ArrayList<UsuarioRol>();
            for (UsuarioRol usuarioRolListNewUsuarioRolToAttach : usuarioRolListNew) {
                usuarioRolListNewUsuarioRolToAttach = em.getReference(usuarioRolListNewUsuarioRolToAttach.getClass(), usuarioRolListNewUsuarioRolToAttach.getCodUsuarioRol());
                attachedUsuarioRolListNew.add(usuarioRolListNewUsuarioRolToAttach);
            }
            usuarioRolListNew = attachedUsuarioRolListNew;
            usuario.setUsuarioRolList(usuarioRolListNew);
            List<CorteUsuario> attachedCorteUsuarioListNew = new ArrayList<CorteUsuario>();
            for (CorteUsuario corteUsuarioListNewCorteUsuarioToAttach : corteUsuarioListNew) {
                corteUsuarioListNewCorteUsuarioToAttach = em.getReference(corteUsuarioListNewCorteUsuarioToAttach.getClass(), corteUsuarioListNewCorteUsuarioToAttach.getCodCorteUsuario());
                attachedCorteUsuarioListNew.add(corteUsuarioListNewCorteUsuarioToAttach);
            }
            corteUsuarioListNew = attachedCorteUsuarioListNew;
            usuario.setCorteUsuarioList(corteUsuarioListNew);
            List<ClienteFactura> attachedClienteFacturaListNew = new ArrayList<ClienteFactura>();
            for (ClienteFactura clienteFacturaListNewClienteFacturaToAttach : clienteFacturaListNew) {
                clienteFacturaListNewClienteFacturaToAttach = em.getReference(clienteFacturaListNewClienteFacturaToAttach.getClass(), clienteFacturaListNewClienteFacturaToAttach.getCodClienteFactura());
                attachedClienteFacturaListNew.add(clienteFacturaListNewClienteFacturaToAttach);
            }
            clienteFacturaListNew = attachedClienteFacturaListNew;
            usuario.setClienteFacturaList(clienteFacturaListNew);
            List<UsuarioSuministros> attachedUsuarioSuministrosListNew = new ArrayList<UsuarioSuministros>();
            for (UsuarioSuministros usuarioSuministrosListNewUsuarioSuministrosToAttach : usuarioSuministrosListNew) {
                usuarioSuministrosListNewUsuarioSuministrosToAttach = em.getReference(usuarioSuministrosListNewUsuarioSuministrosToAttach.getClass(), usuarioSuministrosListNewUsuarioSuministrosToAttach.getCodUsuarioSuministro());
                attachedUsuarioSuministrosListNew.add(usuarioSuministrosListNewUsuarioSuministrosToAttach);
            }
            usuarioSuministrosListNew = attachedUsuarioSuministrosListNew;
            usuario.setUsuarioSuministrosList(usuarioSuministrosListNew);
            usuario = em.merge(usuario);
            for (ClienteEstado clienteEstadoListNewClienteEstado : clienteEstadoListNew) {
                if (!clienteEstadoListOld.contains(clienteEstadoListNewClienteEstado)) {
                    Usuario oldCodUsuarioOfClienteEstadoListNewClienteEstado = clienteEstadoListNewClienteEstado.getCodUsuario();
                    clienteEstadoListNewClienteEstado.setCodUsuario(usuario);
                    clienteEstadoListNewClienteEstado = em.merge(clienteEstadoListNewClienteEstado);
                    if (oldCodUsuarioOfClienteEstadoListNewClienteEstado != null && !oldCodUsuarioOfClienteEstadoListNewClienteEstado.equals(usuario)) {
                        oldCodUsuarioOfClienteEstadoListNewClienteEstado.getClienteEstadoList().remove(clienteEstadoListNewClienteEstado);
                        oldCodUsuarioOfClienteEstadoListNewClienteEstado = em.merge(oldCodUsuarioOfClienteEstadoListNewClienteEstado);
                    }
                }
            }
            for (ClienteContrato clienteContratoListNewClienteContrato : clienteContratoListNew) {
                if (!clienteContratoListOld.contains(clienteContratoListNewClienteContrato)) {
                    Usuario oldCodUsuarioOfClienteContratoListNewClienteContrato = clienteContratoListNewClienteContrato.getCodUsuario();
                    clienteContratoListNewClienteContrato.setCodUsuario(usuario);
                    clienteContratoListNewClienteContrato = em.merge(clienteContratoListNewClienteContrato);
                    if (oldCodUsuarioOfClienteContratoListNewClienteContrato != null && !oldCodUsuarioOfClienteContratoListNewClienteContrato.equals(usuario)) {
                        oldCodUsuarioOfClienteContratoListNewClienteContrato.getClienteContratoList().remove(clienteContratoListNewClienteContrato);
                        oldCodUsuarioOfClienteContratoListNewClienteContrato = em.merge(oldCodUsuarioOfClienteContratoListNewClienteContrato);
                    }
                }
            }
            for (UsuarioReconexion usuarioReconexionListNewUsuarioReconexion : usuarioReconexionListNew) {
                if (!usuarioReconexionListOld.contains(usuarioReconexionListNewUsuarioReconexion)) {
                    Usuario oldCodUsuarioOfUsuarioReconexionListNewUsuarioReconexion = usuarioReconexionListNewUsuarioReconexion.getCodUsuario();
                    usuarioReconexionListNewUsuarioReconexion.setCodUsuario(usuario);
                    usuarioReconexionListNewUsuarioReconexion = em.merge(usuarioReconexionListNewUsuarioReconexion);
                    if (oldCodUsuarioOfUsuarioReconexionListNewUsuarioReconexion != null && !oldCodUsuarioOfUsuarioReconexionListNewUsuarioReconexion.equals(usuario)) {
                        oldCodUsuarioOfUsuarioReconexionListNewUsuarioReconexion.getUsuarioReconexionList().remove(usuarioReconexionListNewUsuarioReconexion);
                        oldCodUsuarioOfUsuarioReconexionListNewUsuarioReconexion = em.merge(oldCodUsuarioOfUsuarioReconexionListNewUsuarioReconexion);
                    }
                }
            }
            for (UsuarioRol usuarioRolListNewUsuarioRol : usuarioRolListNew) {
                if (!usuarioRolListOld.contains(usuarioRolListNewUsuarioRol)) {
                    Usuario oldCodUsuarioOfUsuarioRolListNewUsuarioRol = usuarioRolListNewUsuarioRol.getCodUsuario();
                    usuarioRolListNewUsuarioRol.setCodUsuario(usuario);
                    usuarioRolListNewUsuarioRol = em.merge(usuarioRolListNewUsuarioRol);
                    if (oldCodUsuarioOfUsuarioRolListNewUsuarioRol != null && !oldCodUsuarioOfUsuarioRolListNewUsuarioRol.equals(usuario)) {
                        oldCodUsuarioOfUsuarioRolListNewUsuarioRol.getUsuarioRolList().remove(usuarioRolListNewUsuarioRol);
                        oldCodUsuarioOfUsuarioRolListNewUsuarioRol = em.merge(oldCodUsuarioOfUsuarioRolListNewUsuarioRol);
                    }
                }
            }
            for (CorteUsuario corteUsuarioListNewCorteUsuario : corteUsuarioListNew) {
                if (!corteUsuarioListOld.contains(corteUsuarioListNewCorteUsuario)) {
                    Usuario oldCodUsuarioOfCorteUsuarioListNewCorteUsuario = corteUsuarioListNewCorteUsuario.getCodUsuario();
                    corteUsuarioListNewCorteUsuario.setCodUsuario(usuario);
                    corteUsuarioListNewCorteUsuario = em.merge(corteUsuarioListNewCorteUsuario);
                    if (oldCodUsuarioOfCorteUsuarioListNewCorteUsuario != null && !oldCodUsuarioOfCorteUsuarioListNewCorteUsuario.equals(usuario)) {
                        oldCodUsuarioOfCorteUsuarioListNewCorteUsuario.getCorteUsuarioList().remove(corteUsuarioListNewCorteUsuario);
                        oldCodUsuarioOfCorteUsuarioListNewCorteUsuario = em.merge(oldCodUsuarioOfCorteUsuarioListNewCorteUsuario);
                    }
                }
            }
            for (ClienteFactura clienteFacturaListNewClienteFactura : clienteFacturaListNew) {
                if (!clienteFacturaListOld.contains(clienteFacturaListNewClienteFactura)) {
                    Usuario oldCodUsuarioOfClienteFacturaListNewClienteFactura = clienteFacturaListNewClienteFactura.getCodUsuario();
                    clienteFacturaListNewClienteFactura.setCodUsuario(usuario);
                    clienteFacturaListNewClienteFactura = em.merge(clienteFacturaListNewClienteFactura);
                    if (oldCodUsuarioOfClienteFacturaListNewClienteFactura != null && !oldCodUsuarioOfClienteFacturaListNewClienteFactura.equals(usuario)) {
                        oldCodUsuarioOfClienteFacturaListNewClienteFactura.getClienteFacturaList().remove(clienteFacturaListNewClienteFactura);
                        oldCodUsuarioOfClienteFacturaListNewClienteFactura = em.merge(oldCodUsuarioOfClienteFacturaListNewClienteFactura);
                    }
                }
            }
            for (UsuarioSuministros usuarioSuministrosListNewUsuarioSuministros : usuarioSuministrosListNew) {
                if (!usuarioSuministrosListOld.contains(usuarioSuministrosListNewUsuarioSuministros)) {
                    Usuario oldCodUsuarioOfUsuarioSuministrosListNewUsuarioSuministros = usuarioSuministrosListNewUsuarioSuministros.getCodUsuario();
                    usuarioSuministrosListNewUsuarioSuministros.setCodUsuario(usuario);
                    usuarioSuministrosListNewUsuarioSuministros = em.merge(usuarioSuministrosListNewUsuarioSuministros);
                    if (oldCodUsuarioOfUsuarioSuministrosListNewUsuarioSuministros != null && !oldCodUsuarioOfUsuarioSuministrosListNewUsuarioSuministros.equals(usuario)) {
                        oldCodUsuarioOfUsuarioSuministrosListNewUsuarioSuministros.getUsuarioSuministrosList().remove(usuarioSuministrosListNewUsuarioSuministros);
                        oldCodUsuarioOfUsuarioSuministrosListNewUsuarioSuministros = em.merge(oldCodUsuarioOfUsuarioSuministrosListNewUsuarioSuministros);
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
                Integer id = usuario.getCodUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getCodUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ClienteEstado> clienteEstadoListOrphanCheck = usuario.getClienteEstadoList();
            for (ClienteEstado clienteEstadoListOrphanCheckClienteEstado : clienteEstadoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the ClienteEstado " + clienteEstadoListOrphanCheckClienteEstado + " in its clienteEstadoList field has a non-nullable codUsuario field.");
            }
            List<ClienteContrato> clienteContratoListOrphanCheck = usuario.getClienteContratoList();
            for (ClienteContrato clienteContratoListOrphanCheckClienteContrato : clienteContratoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the ClienteContrato " + clienteContratoListOrphanCheckClienteContrato + " in its clienteContratoList field has a non-nullable codUsuario field.");
            }
            List<UsuarioReconexion> usuarioReconexionListOrphanCheck = usuario.getUsuarioReconexionList();
            for (UsuarioReconexion usuarioReconexionListOrphanCheckUsuarioReconexion : usuarioReconexionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the UsuarioReconexion " + usuarioReconexionListOrphanCheckUsuarioReconexion + " in its usuarioReconexionList field has a non-nullable codUsuario field.");
            }
            List<UsuarioRol> usuarioRolListOrphanCheck = usuario.getUsuarioRolList();
            for (UsuarioRol usuarioRolListOrphanCheckUsuarioRol : usuarioRolListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the UsuarioRol " + usuarioRolListOrphanCheckUsuarioRol + " in its usuarioRolList field has a non-nullable codUsuario field.");
            }
            List<CorteUsuario> corteUsuarioListOrphanCheck = usuario.getCorteUsuarioList();
            for (CorteUsuario corteUsuarioListOrphanCheckCorteUsuario : corteUsuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the CorteUsuario " + corteUsuarioListOrphanCheckCorteUsuario + " in its corteUsuarioList field has a non-nullable codUsuario field.");
            }
            List<ClienteFactura> clienteFacturaListOrphanCheck = usuario.getClienteFacturaList();
            for (ClienteFactura clienteFacturaListOrphanCheckClienteFactura : clienteFacturaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the ClienteFactura " + clienteFacturaListOrphanCheckClienteFactura + " in its clienteFacturaList field has a non-nullable codUsuario field.");
            }
            List<UsuarioSuministros> usuarioSuministrosListOrphanCheck = usuario.getUsuarioSuministrosList();
            for (UsuarioSuministros usuarioSuministrosListOrphanCheckUsuarioSuministros : usuarioSuministrosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the UsuarioSuministros " + usuarioSuministrosListOrphanCheckUsuarioSuministros + " in its usuarioSuministrosList field has a non-nullable codUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
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

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
