/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author yinneandyor
 */
@Entity
@Table(name = "factura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Factura.findAll", query = "SELECT f FROM Factura f"),
    @NamedQuery(name = "Factura.findByCodFactura", query = "SELECT f FROM Factura f WHERE f.codFactura = :codFactura"),
    @NamedQuery(name = "Factura.findByValorAPagar", query = "SELECT f FROM Factura f WHERE f.valorAPagar = :valorAPagar"),
    @NamedQuery(name = "Factura.findByFechaPago", query = "SELECT f FROM Factura f WHERE f.fechaPago = :fechaPago")})
public class Factura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COD_FACTURA")
    private Integer codFactura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALOR_A_PAGAR")
    private double valorAPagar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_PAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codFactura")
    private List<FacturaRecargo> facturaRecargoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codFactura")
    private List<FacturaReconexion> facturaReconexionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codFactura")
    private List<Corte> corteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codFactura")
    private List<ClienteFactura> clienteFacturaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codFactura")
    private List<ReportePagoFactura> reportePagoFacturaList;

    public Factura() {
    }

    public Factura(Integer codFactura) {
        this.codFactura = codFactura;
    }

    public Factura(Integer codFactura, double valorAPagar, Date fechaPago) {
        this.codFactura = codFactura;
        this.valorAPagar = valorAPagar;
        this.fechaPago = fechaPago;
    }

    public Integer getCodFactura() {
        return codFactura;
    }

    public void setCodFactura(Integer codFactura) {
        this.codFactura = codFactura;
    }

    public double getValorAPagar() {
        return valorAPagar;
    }

    public void setValorAPagar(double valorAPagar) {
        this.valorAPagar = valorAPagar;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    @XmlTransient
    public List<FacturaRecargo> getFacturaRecargoList() {
        return facturaRecargoList;
    }

    public void setFacturaRecargoList(List<FacturaRecargo> facturaRecargoList) {
        this.facturaRecargoList = facturaRecargoList;
    }

    @XmlTransient
    public List<FacturaReconexion> getFacturaReconexionList() {
        return facturaReconexionList;
    }

    public void setFacturaReconexionList(List<FacturaReconexion> facturaReconexionList) {
        this.facturaReconexionList = facturaReconexionList;
    }

    @XmlTransient
    public List<Corte> getCorteList() {
        return corteList;
    }

    public void setCorteList(List<Corte> corteList) {
        this.corteList = corteList;
    }

    @XmlTransient
    public List<ClienteFactura> getClienteFacturaList() {
        return clienteFacturaList;
    }

    public void setClienteFacturaList(List<ClienteFactura> clienteFacturaList) {
        this.clienteFacturaList = clienteFacturaList;
    }

    @XmlTransient
    public List<ReportePagoFactura> getReportePagoFacturaList() {
        return reportePagoFacturaList;
    }

    public void setReportePagoFacturaList(List<ReportePagoFactura> reportePagoFacturaList) {
        this.reportePagoFacturaList = reportePagoFacturaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codFactura != null ? codFactura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Factura)) {
            return false;
        }
        Factura other = (Factura) object;
        if ((this.codFactura == null && other.codFactura != null) || (this.codFactura != null && !this.codFactura.equals(other.codFactura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Factura[ codFactura=" + codFactura + " ]";
    }
    
}
