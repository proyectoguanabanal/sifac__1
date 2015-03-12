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
@Table(name = "tipo_recargo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoRecargo.findAll", query = "SELECT t FROM TipoRecargo t"),
    @NamedQuery(name = "TipoRecargo.findByCodTipoRecargo", query = "SELECT t FROM TipoRecargo t WHERE t.codTipoRecargo = :codTipoRecargo"),
    @NamedQuery(name = "TipoRecargo.findByNomTipoRecargo", query = "SELECT t FROM TipoRecargo t WHERE t.nomTipoRecargo = :nomTipoRecargo")})
public class TipoRecargo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_TIPO_RECARGO")
    private Integer codTipoRecargo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "NOM_TIPO_RECARGO")
    private String nomTipoRecargo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codTipoRecargo")
    private List<Recargo> recargoList;

    public TipoRecargo() {
    }

    public TipoRecargo(Integer codTipoRecargo) {
        this.codTipoRecargo = codTipoRecargo;
    }

    public TipoRecargo(Integer codTipoRecargo, String nomTipoRecargo) {
        this.codTipoRecargo = codTipoRecargo;
        this.nomTipoRecargo = nomTipoRecargo;
    }

    public Integer getCodTipoRecargo() {
        return codTipoRecargo;
    }

    public void setCodTipoRecargo(Integer codTipoRecargo) {
        this.codTipoRecargo = codTipoRecargo;
    }

    public String getNomTipoRecargo() {
        return nomTipoRecargo;
    }

    public void setNomTipoRecargo(String nomTipoRecargo) {
        this.nomTipoRecargo = nomTipoRecargo;
    }

    @XmlTransient
    public List<Recargo> getRecargoList() {
        return recargoList;
    }

    public void setRecargoList(List<Recargo> recargoList) {
        this.recargoList = recargoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codTipoRecargo != null ? codTipoRecargo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoRecargo)) {
            return false;
        }
        TipoRecargo other = (TipoRecargo) object;
        if ((this.codTipoRecargo == null && other.codTipoRecargo != null) || (this.codTipoRecargo != null && !this.codTipoRecargo.equals(other.codTipoRecargo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+getCodTipoRecargo();
    }
    
}
