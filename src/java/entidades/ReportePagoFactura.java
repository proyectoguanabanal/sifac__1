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
@Table(name = "reporte_pago_factura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReportePagoFactura.findAll", query = "SELECT r FROM ReportePagoFactura r"),
    @NamedQuery(name = "ReportePagoFactura.findByCodReportePagoFactura", query = "SELECT r FROM ReportePagoFactura r WHERE r.codReportePagoFactura = :codReportePagoFactura")})
public class ReportePagoFactura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COD_REPORTE_PAGO_FACTURA")
    private Integer codReportePagoFactura;
    @JoinColumn(name = "COD_REPORTE_PAGO", referencedColumnName = "COD_REPORTE_PAGO")
    @ManyToOne(optional = false)
    private ReportePago codReportePago;
    @JoinColumn(name = "COD_FACTURA", referencedColumnName = "COD_FACTURA")
    @ManyToOne(optional = false)
    private Factura codFactura;

    public ReportePagoFactura() {
    }

    public ReportePagoFactura(Integer codReportePagoFactura) {
        this.codReportePagoFactura = codReportePagoFactura;
    }

    public Integer getCodReportePagoFactura() {
        return codReportePagoFactura;
    }

    public void setCodReportePagoFactura(Integer codReportePagoFactura) {
        this.codReportePagoFactura = codReportePagoFactura;
    }

    public ReportePago getCodReportePago() {
        return codReportePago;
    }

    public void setCodReportePago(ReportePago codReportePago) {
        this.codReportePago = codReportePago;
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
        hash += (codReportePagoFactura != null ? codReportePagoFactura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReportePagoFactura)) {
            return false;
        }
        ReportePagoFactura other = (ReportePagoFactura) object;
        if ((this.codReportePagoFactura == null && other.codReportePagoFactura != null) || (this.codReportePagoFactura != null && !this.codReportePagoFactura.equals(other.codReportePagoFactura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.ReportePagoFactura[ codReportePagoFactura=" + codReportePagoFactura + " ]";
    }
    
}
