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
@Table(name = "reporte_pago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReportePago.findAll", query = "SELECT r FROM ReportePago r"),
    @NamedQuery(name = "ReportePago.findByCodReportePago", query = "SELECT r FROM ReportePago r WHERE r.codReportePago = :codReportePago"),
    @NamedQuery(name = "ReportePago.findByMesPago", query = "SELECT r FROM ReportePago r WHERE r.mesPago = :mesPago")})
public class ReportePago implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COD_REPORTE_PAGO")
    private Integer codReportePago;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MES_PAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mesPago;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codReportePago")
    private List<ReportePagoFactura> reportePagoFacturaList;

    public ReportePago() {
    }

    public ReportePago(Integer codReportePago) {
        this.codReportePago = codReportePago;
    }

    public ReportePago(Integer codReportePago, Date mesPago) {
        this.codReportePago = codReportePago;
        this.mesPago = mesPago;
    }

    public Integer getCodReportePago() {
        return codReportePago;
    }

    public void setCodReportePago(Integer codReportePago) {
        this.codReportePago = codReportePago;
    }

    public Date getMesPago() {
        return mesPago;
    }

    public void setMesPago(Date mesPago) {
        this.mesPago = mesPago;
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
        hash += (codReportePago != null ? codReportePago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReportePago)) {
            return false;
        }
        ReportePago other = (ReportePago) object;
        if ((this.codReportePago == null && other.codReportePago != null) || (this.codReportePago != null && !this.codReportePago.equals(other.codReportePago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+getCodReportePago();
    }
    
}
