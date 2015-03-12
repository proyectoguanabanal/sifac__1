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
@Table(name = "permiso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permiso.findAll", query = "SELECT p FROM Permiso p"),
    @NamedQuery(name = "Permiso.findByCodPermiso", query = "SELECT p FROM Permiso p WHERE p.codPermiso = :codPermiso"),
    @NamedQuery(name = "Permiso.findByNombrePermiso", query = "SELECT p FROM Permiso p WHERE p.nombrePermiso = :nombrePermiso")})
public class Permiso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_PERMISO")
    private Integer codPermiso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "NOMBRE_PERMISO")
    private String nombrePermiso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codPermiso")
    private List<PermisoRol> permisoRolList;

    public Permiso() {
    }

    public Permiso(Integer codPermiso) {
        this.codPermiso = codPermiso;
    }

    public Permiso(Integer codPermiso, String nombrePermiso) {
        this.codPermiso = codPermiso;
        this.nombrePermiso = nombrePermiso;
    }

    public Integer getCodPermiso() {
        return codPermiso;
    }

    public void setCodPermiso(Integer codPermiso) {
        this.codPermiso = codPermiso;
    }

    public String getNombrePermiso() {
        return nombrePermiso;
    }

    public void setNombrePermiso(String nombrePermiso) {
        this.nombrePermiso = nombrePermiso;
    }

    @XmlTransient
    public List<PermisoRol> getPermisoRolList() {
        return permisoRolList;
    }

    public void setPermisoRolList(List<PermisoRol> permisoRolList) {
        this.permisoRolList = permisoRolList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codPermiso != null ? codPermiso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permiso)) {
            return false;
        }
        Permiso other = (Permiso) object;
        if ((this.codPermiso == null && other.codPermiso != null) || (this.codPermiso != null && !this.codPermiso.equals(other.codPermiso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+getCodPermiso();
    }
    
}
