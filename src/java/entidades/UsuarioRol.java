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
@Table(name = "usuario_rol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioRol.findAll", query = "SELECT u FROM UsuarioRol u"),
    @NamedQuery(name = "UsuarioRol.findByCodUsuarioRol", query = "SELECT u FROM UsuarioRol u WHERE u.codUsuarioRol = :codUsuarioRol")})
public class UsuarioRol implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COD_USUARIO_ROL")
    private Integer codUsuarioRol;
    @JoinColumn(name = "COD_USUARIO", referencedColumnName = "COD_USUARIO")
    @ManyToOne(optional = false)
    private Usuario codUsuario;
    @JoinColumn(name = "COD_ROL", referencedColumnName = "COD_ROL")
    @ManyToOne(optional = false)
    private Rol codRol;

    public UsuarioRol() {
    }

    public UsuarioRol(Integer codUsuarioRol) {
        this.codUsuarioRol = codUsuarioRol;
    }

    public Integer getCodUsuarioRol() {
        return codUsuarioRol;
    }

    public void setCodUsuarioRol(Integer codUsuarioRol) {
        this.codUsuarioRol = codUsuarioRol;
    }

    public Usuario getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(Usuario codUsuario) {
        this.codUsuario = codUsuario;
    }

    public Rol getCodRol() {
        return codRol;
    }

    public void setCodRol(Rol codRol) {
        this.codRol = codRol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codUsuarioRol != null ? codUsuarioRol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioRol)) {
            return false;
        }
        UsuarioRol other = (UsuarioRol) object;
        if ((this.codUsuarioRol == null && other.codUsuarioRol != null) || (this.codUsuarioRol != null && !this.codUsuarioRol.equals(other.codUsuarioRol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.UsuarioRol[ codUsuarioRol=" + codUsuarioRol + " ]";
    }
    
}
