/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author yinneandyor
 */
@Entity
@Table(name = "usuario_reconexion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioReconexion.findAll", query = "SELECT u FROM UsuarioReconexion u"),
    @NamedQuery(name = "UsuarioReconexion.findByCodUsuarioReconexion", query = "SELECT u FROM UsuarioReconexion u WHERE u.codUsuarioReconexion = :codUsuarioReconexion")})
public class UsuarioReconexion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COD_USUARIO_RECONEXION")
    private Integer codUsuarioReconexion;
    @JoinColumn(name = "COD_USUARIO", referencedColumnName = "COD_USUARIO")
    @ManyToOne(optional = false)
    private Usuario codUsuario;
    @JoinColumn(name = "COD_RECONEXION", referencedColumnName = "COD_RECONEXION")
    @ManyToOne(optional = false)
    private Reconexion codReconexion;

    public UsuarioReconexion() {
    }

    public UsuarioReconexion(Integer codUsuarioReconexion) {
        this.codUsuarioReconexion = codUsuarioReconexion;
    }

    public Integer getCodUsuarioReconexion() {
        return codUsuarioReconexion;
    }

    public void setCodUsuarioReconexion(Integer codUsuarioReconexion) {
        this.codUsuarioReconexion = codUsuarioReconexion;
    }

    public Usuario getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(Usuario codUsuario) {
        this.codUsuario = codUsuario;
    }

    public Reconexion getCodReconexion() {
        return codReconexion;
    }

    public void setCodReconexion(Reconexion codReconexion) {
        this.codReconexion = codReconexion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codUsuarioReconexion != null ? codUsuarioReconexion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioReconexion)) {
            return false;
        }
        UsuarioReconexion other = (UsuarioReconexion) object;
        if ((this.codUsuarioReconexion == null && other.codUsuarioReconexion != null) || (this.codUsuarioReconexion != null && !this.codUsuarioReconexion.equals(other.codUsuarioReconexion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.UsuarioReconexion[ codUsuarioReconexion=" + codUsuarioReconexion + " ]";
    }
    
}
