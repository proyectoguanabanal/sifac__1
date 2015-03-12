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
@Table(name = "permiso_rol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PermisoRol.findAll", query = "SELECT p FROM PermisoRol p"),
    @NamedQuery(name = "PermisoRol.findByCodPermisoRol", query = "SELECT p FROM PermisoRol p WHERE p.codPermisoRol = :codPermisoRol")})
public class PermisoRol implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COD_PERMISO_ROL")
    private Integer codPermisoRol;
    @JoinColumn(name = "COD_ROL", referencedColumnName = "COD_ROL")
    @ManyToOne(optional = false)
    private Rol codRol;
    @JoinColumn(name = "COD_PERMISO", referencedColumnName = "COD_PERMISO")
    @ManyToOne(optional = false)
    private Permiso codPermiso;

    public PermisoRol() {
    }

    public PermisoRol(Integer codPermisoRol) {
        this.codPermisoRol = codPermisoRol;
    }

    public Integer getCodPermisoRol() {
        return codPermisoRol;
    }

    public void setCodPermisoRol(Integer codPermisoRol) {
        this.codPermisoRol = codPermisoRol;
    }

    public Rol getCodRol() {
        return codRol;
    }

    public void setCodRol(Rol codRol) {
        this.codRol = codRol;
    }

    public Permiso getCodPermiso() {
        return codPermiso;
    }

    public void setCodPermiso(Permiso codPermiso) {
        this.codPermiso = codPermiso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codPermisoRol != null ? codPermisoRol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PermisoRol)) {
            return false;
        }
        PermisoRol other = (PermisoRol) object;
        if ((this.codPermisoRol == null && other.codPermisoRol != null) || (this.codPermisoRol != null && !this.codPermisoRol.equals(other.codPermisoRol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.PermisoRol[ codPermisoRol=" + codPermisoRol + " ]";
    }
    
}
