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
@Table(name = "tipo_suministro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoSuministro.findAll", query = "SELECT t FROM TipoSuministro t"),
    @NamedQuery(name = "TipoSuministro.findByCodTipoSuministro", query = "SELECT t FROM TipoSuministro t WHERE t.codTipoSuministro = :codTipoSuministro"),
    @NamedQuery(name = "TipoSuministro.findByDescripcionSuministro", query = "SELECT t FROM TipoSuministro t WHERE t.descripcionSuministro = :descripcionSuministro")})
public class TipoSuministro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_TIPO_SUMINISTRO")
    private Integer codTipoSuministro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DESCRIPCION_SUMINISTRO")
    private String descripcionSuministro;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codTipoSuministro")
    private List<Suministros> suministrosList;

    public TipoSuministro() {
    }

    public TipoSuministro(Integer codTipoSuministro) {
        this.codTipoSuministro = codTipoSuministro;
    }

    public TipoSuministro(Integer codTipoSuministro, String descripcionSuministro) {
        this.codTipoSuministro = codTipoSuministro;
        this.descripcionSuministro = descripcionSuministro;
    }

    public Integer getCodTipoSuministro() {
        return codTipoSuministro;
    }

    public void setCodTipoSuministro(Integer codTipoSuministro) {
        this.codTipoSuministro = codTipoSuministro;
    }

    public String getDescripcionSuministro() {
        return descripcionSuministro;
    }

    public void setDescripcionSuministro(String descripcionSuministro) {
        this.descripcionSuministro = descripcionSuministro;
    }

    @XmlTransient
    public List<Suministros> getSuministrosList() {
        return suministrosList;
    }

    public void setSuministrosList(List<Suministros> suministrosList) {
        this.suministrosList = suministrosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codTipoSuministro != null ? codTipoSuministro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoSuministro)) {
            return false;
        }
        TipoSuministro other = (TipoSuministro) object;
        if ((this.codTipoSuministro == null && other.codTipoSuministro != null) || (this.codTipoSuministro != null && !this.codTipoSuministro.equals(other.codTipoSuministro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+getCodTipoSuministro();
    }
    
}
