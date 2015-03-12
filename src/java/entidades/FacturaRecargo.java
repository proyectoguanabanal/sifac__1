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
@Table(name = "factura_recargo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacturaRecargo.findAll", query = "SELECT f FROM FacturaRecargo f"),
    @NamedQuery(name = "FacturaRecargo.findByCodFacturaRecargo", query = "SELECT f FROM FacturaRecargo f WHERE f.codFacturaRecargo = :codFacturaRecargo")})
public class FacturaRecargo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_FACTURA_RECARGO")
    private Integer codFacturaRecargo;
    @JoinColumn(name = "COD_RECARGO", referencedColumnName = "COD_RECARGO")
    @ManyToOne(optional = false)
    private Recargo codRecargo;
    @JoinColumn(name = "COD_FACTURA", referencedColumnName = "COD_FACTURA")
    @ManyToOne(optional = false)
    private Factura codFactura;

    public FacturaRecargo() {
    }

    public FacturaRecargo(Integer codFacturaRecargo) {
        this.codFacturaRecargo = codFacturaRecargo;
    }

    public Integer getCodFacturaRecargo() {
        return codFacturaRecargo;
    }

    public void setCodFacturaRecargo(Integer codFacturaRecargo) {
        this.codFacturaRecargo = codFacturaRecargo;
    }

    public Recargo getCodRecargo() {
        return codRecargo;
    }

    public void setCodRecargo(Recargo codRecargo) {
        this.codRecargo = codRecargo;
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
        hash += (codFacturaRecargo != null ? codFacturaRecargo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacturaRecargo)) {
            return false;
        }
        FacturaRecargo other = (FacturaRecargo) object;
        if ((this.codFacturaRecargo == null && other.codFacturaRecargo != null) || (this.codFacturaRecargo != null && !this.codFacturaRecargo.equals(other.codFacturaRecargo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.FacturaRecargo[ codFacturaRecargo=" + codFacturaRecargo + " ]";
    }
    
}
