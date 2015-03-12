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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "suministros")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Suministros.findAll", query = "SELECT s FROM Suministros s"),
    @NamedQuery(name = "Suministros.findByCodSuministros", query = "SELECT s FROM Suministros s WHERE s.codSuministros = :codSuministros"),
    @NamedQuery(name = "Suministros.findByDescripcion", query = "SELECT s FROM Suministros s WHERE s.descripcion = :descripcion")})
public class Suministros implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_SUMINISTROS")
    private Integer codSuministros;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codSuministros")
    private List<SuministrosProveedor> suministrosProveedorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codSuministros")
    private List<UsuarioSuministros> usuarioSuministrosList;
    @JoinColumn(name = "COD_TIPO_SUMINISTRO", referencedColumnName = "COD_TIPO_SUMINISTRO")
    @ManyToOne(optional = false)
    private TipoSuministro codTipoSuministro;

    public Suministros() {
    }

    public Suministros(Integer codSuministros) {
        this.codSuministros = codSuministros;
    }

    public Suministros(Integer codSuministros, String descripcion) {
        this.codSuministros = codSuministros;
        this.descripcion = descripcion;
    }

    public Integer getCodSuministros() {
        return codSuministros;
    }

    public void setCodSuministros(Integer codSuministros) {
        this.codSuministros = codSuministros;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<SuministrosProveedor> getSuministrosProveedorList() {
        return suministrosProveedorList;
    }

    public void setSuministrosProveedorList(List<SuministrosProveedor> suministrosProveedorList) {
        this.suministrosProveedorList = suministrosProveedorList;
    }

    @XmlTransient
    public List<UsuarioSuministros> getUsuarioSuministrosList() {
        return usuarioSuministrosList;
    }

    public void setUsuarioSuministrosList(List<UsuarioSuministros> usuarioSuministrosList) {
        this.usuarioSuministrosList = usuarioSuministrosList;
    }

    public TipoSuministro getCodTipoSuministro() {
        return codTipoSuministro;
    }

    public void setCodTipoSuministro(TipoSuministro codTipoSuministro) {
        this.codTipoSuministro = codTipoSuministro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codSuministros != null ? codSuministros.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Suministros)) {
            return false;
        }
        Suministros other = (Suministros) object;
        if ((this.codSuministros == null && other.codSuministros != null) || (this.codSuministros != null && !this.codSuministros.equals(other.codSuministros))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+getCodSuministros();
    }
    
}
