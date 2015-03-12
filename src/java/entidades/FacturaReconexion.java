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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author yinneandyor
 */
@Entity
@Table(name = "factura_reconexion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacturaReconexion.findAll", query = "SELECT f FROM FacturaReconexion f"),
    @NamedQuery(name = "FacturaReconexion.findByCodFacturaReconexion", query = "SELECT f FROM FacturaReconexion f WHERE f.codFacturaReconexion = :codFacturaReconexion")})
public class FacturaReconexion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_FACTURA_RECONEXION")
    private Integer codFacturaReconexion;
    @JoinColumn(name = "COD_RECONEXION", referencedColumnName = "COD_RECONEXION")
    @ManyToOne(optional = false)
    private Reconexion codReconexion;
    @JoinColumn(name = "COD_FACTURA", referencedColumnName = "COD_FACTURA")
    @ManyToOne(optional = false)
    private Factura codFactura;

    public FacturaReconexion() {
    }

    public FacturaReconexion(Integer codFacturaReconexion) {
        this.codFacturaReconexion = codFacturaReconexion;
    }

    public Integer getCodFacturaReconexion() {
        return codFacturaReconexion;
    }

    public void setCodFacturaReconexion(Integer codFacturaReconexion) {
        this.codFacturaReconexion = codFacturaReconexion;
    }

    public Reconexion getCodReconexion() {
        return codReconexion;
    }

    public void setCodReconexion(Reconexion codReconexion) {
        this.codReconexion = codReconexion;
    }

    public Factura getCodFactura() {
        return codFactura;
    }

    public void setCodFactura(Factura codFactura) {
        this.codFactura = codFactura;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codFacturaReconexion != null ? codFacturaReconexion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacturaReconexion)) {
            return false;
        }
        FacturaReconexion other = (FacturaReconexion) object;
        if ((this.codFacturaReconexion == null && other.codFacturaReconexion != null) || (this.codFacturaReconexion != null && !this.codFacturaReconexion.equals(other.codFacturaReconexion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacturaReconexion[ codFacturaReconexion=" + codFacturaReconexion + " ]";
    }
    
}
