/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "rol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rol.findAll", query = "SELECT r FROM Rol r"),
    @NamedQuery(name = "Rol.findByCodRol", query = "SELECT r FROM Rol r WHERE r.codRol = :codRol"),
    @NamedQuery(name = "Rol.findByDescripcionRol", query = "SELECT r FROM Rol r WHERE r.descripcionRol = :descripcionRol")})
public class Rol implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_ROL")
    private Integer codRol;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "DESCRIPCION_ROL")
    private String descripcionRol;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codRol")
    private List<PermisoRol> permisoRolList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codRol")
    private List<UsuarioRol> usuarioRolList;

    public Rol() {
    }

    public Rol(Integer codRol) {
        this.codRol = codRol;
    }

    public Rol(Integer codRol, String descripcionRol) {
        this.codRol = codRol;
        this.descripcionRol = descripcionRol;
    }

    public Integer getCodRol() {
        return codRol;
    }

    public void setCodRol(Integer codRol) {
        this.codRol = codRol;
    }

    public String getDescripcionRol() {
        return descripcionRol;
    }

    public void setDescripcionRol(String descripcionRol) {
        this.descripcionRol = descripcionRol;
    }

    @XmlTransient
    public List<PermisoRol> getPermisoRolList() {
        return permisoRolList;
    }

    public void setPermisoRolList(List<PermisoRol> permisoRolList) {
        this.permisoRolList = permisoRolList;
    }

    @XmlTransient
    public List<UsuarioRol> getUsuarioRolList() {
        return usuarioRolList;
    }

    public void setUsuarioRolList(List<UsuarioRol> usuarioRolList) {
        this.usuarioRolList = usuarioRolList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codRol != null ? codRol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rol)) {
            return false;
        }
        Rol other = (Rol) object;
        if ((this.codRol == null && other.codRol != null) || (this.codRol != null && !this.codRol.equals(other.codRol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+getCodRol();
    }
    
}
