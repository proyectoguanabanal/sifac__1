/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author yinneandyor
 */
@Entity
@Table(name = "usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByCodUsuario", query = "SELECT u FROM Usuario u WHERE u.codUsuario = :codUsuario"),
    @NamedQuery(name = "Usuario.findByCedula", query = "SELECT u FROM Usuario u WHERE u.cedula = :cedula"),
    @NamedQuery(name = "Usuario.findByPrimerNombre", query = "SELECT u FROM Usuario u WHERE u.primerNombre = :primerNombre"),
    @NamedQuery(name = "Usuario.findBySegundoNombre", query = "SELECT u FROM Usuario u WHERE u.segundoNombre = :segundoNombre"),
    @NamedQuery(name = "Usuario.findByPrimerApellido", query = "SELECT u FROM Usuario u WHERE u.primerApellido = :primerApellido"),
    @NamedQuery(name = "Usuario.findBySegundoApellido", query = "SELECT u FROM Usuario u WHERE u.segundoApellido = :segundoApellido"),
    @NamedQuery(name = "Usuario.findByDireccion", query = "SELECT u FROM Usuario u WHERE u.direccion = :direccion"),
    @NamedQuery(name = "Usuario.findByNumeroDePersonasQueQueHabitanEnLaCas", query = "SELECT u FROM Usuario u WHERE u.numeroDePersonasQueQueHabitanEnLaCas = :numeroDePersonasQueQueHabitanEnLaCas"),
    @NamedQuery(name = "Usuario.findByTelefonoFijo", query = "SELECT u FROM Usuario u WHERE u.telefonoFijo = :telefonoFijo"),
    @NamedQuery(name = "Usuario.findByTelefonoMovil", query = "SELECT u FROM Usuario u WHERE u.telefonoMovil = :telefonoMovil"),
    @NamedQuery(name = "Usuario.findByEmail", query = "SELECT u FROM Usuario u WHERE u.email = :email")})
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COD_USUARIO")
    private Integer codUsuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CEDULA")
    private double cedula;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "PRIMER_NOMBRE")
    private String primerNombre;
    @Size(max = 25)
    @Column(name = "SEGUNDO_NOMBRE")
    private String segundoNombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 55)
    @Column(name = "PRIMER_APELLIDO")
    private String primerApellido;
    @Size(max = 25)
    @Column(name = "SEGUNDO_APELLIDO")
    private String segundoApellido;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DIRECCION")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMERO_DE_PERSONAS_QUE_QUE_HABITAN_EN_LA_CAS")
    private int numeroDePersonasQueQueHabitanEnLaCas;
    @Column(name = "TELEFONO_FIJO")
    private BigInteger telefonoFijo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TELEFONO_MOVIL")
    private long telefonoMovil;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "EMAIL")
    private String email;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codUsuario")
    private List<ClienteEstado> clienteEstadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codUsuario")
    private List<ClienteContrato> clienteContratoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codUsuario")
    private List<UsuarioReconexion> usuarioReconexionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codUsuario")
    private List<UsuarioRol> usuarioRolList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codUsuario")
    private List<CorteUsuario> corteUsuarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codUsuario")
    private List<ClienteFactura> clienteFacturaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codUsuario")
    private List<UsuarioSuministros> usuarioSuministrosList;

    public Usuario() {
    }

    public Usuario(Integer codUsuario) {
        this.codUsuario = codUsuario;
    }

    public Usuario(Integer codUsuario, double cedula, String primerNombre, String primerApellido, String direccion, int numeroDePersonasQueQueHabitanEnLaCas, long telefonoMovil, String email) {
        this.codUsuario = codUsuario;
        this.cedula = cedula;
        this.primerNombre = primerNombre;
        this.primerApellido = primerApellido;
        this.direccion = direccion;
        this.numeroDePersonasQueQueHabitanEnLaCas = numeroDePersonasQueQueHabitanEnLaCas;
        this.telefonoMovil = telefonoMovil;
        this.email = email;
    }

    public Integer getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(Integer codUsuario) {
        this.codUsuario = codUsuario;
    }

    public double getCedula() {
        return cedula;
    }

    public void setCedula(double cedula) {
        this.cedula = cedula;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getNumeroDePersonasQueQueHabitanEnLaCas() {
        return numeroDePersonasQueQueHabitanEnLaCas;
    }

    public void setNumeroDePersonasQueQueHabitanEnLaCas(int numeroDePersonasQueQueHabitanEnLaCas) {
        this.numeroDePersonasQueQueHabitanEnLaCas = numeroDePersonasQueQueHabitanEnLaCas;
    }

    public BigInteger getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(BigInteger telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public long getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(long telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlTransient
    public List<ClienteEstado> getClienteEstadoList() {
        return clienteEstadoList;
    }

    public void setClienteEstadoList(List<ClienteEstado> clienteEstadoList) {
        this.clienteEstadoList = clienteEstadoList;
    }

    @XmlTransient
    public List<ClienteContrato> getClienteContratoList() {
        return clienteContratoList;
    }

    public void setClienteContratoList(List<ClienteContrato> clienteContratoList) {
        this.clienteContratoList = clienteContratoList;
    }

    @XmlTransient
    public List<UsuarioReconexion> getUsuarioReconexionList() {
        return usuarioReconexionList;
    }

    public void setUsuarioReconexionList(List<UsuarioReconexion> usuarioReconexionList) {
        this.usuarioReconexionList = usuarioReconexionList;
    }

    @XmlTransient
    public List<UsuarioRol> getUsuarioRolList() {
        return usuarioRolList;
    }

    public void setUsuarioRolList(List<UsuarioRol> usuarioRolList) {
        this.usuarioRolList = usuarioRolList;
    }

    @XmlTransient
    public List<CorteUsuario> getCorteUsuarioList() {
        return corteUsuarioList;
    }

    public void setCorteUsuarioList(List<CorteUsuario> corteUsuarioList) {
        this.corteUsuarioList = corteUsuarioList;
    }

    @XmlTransient
    public List<ClienteFactura> getClienteFacturaList() {
        return clienteFacturaList;
    }

    public void setClienteFacturaList(List<ClienteFactura> clienteFacturaList) {
        this.clienteFacturaList = clienteFacturaList;
    }

    @XmlTransient
    public List<UsuarioSuministros> getUsuarioSuministrosList() {
        return usuarioSuministrosList;
    }

    public void setUsuarioSuministrosList(List<UsuarioSuministros> usuarioSuministrosList) {
        this.usuarioSuministrosList = usuarioSuministrosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codUsuario != null ? codUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.codUsuario == null && other.codUsuario != null) || (this.codUsuario != null && !this.codUsuario.equals(other.codUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+getCodUsuario();
    }
    
}
