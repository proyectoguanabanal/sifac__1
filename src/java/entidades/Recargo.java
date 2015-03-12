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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "recargo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Recargo.findAll", query = "SELECT r FROM Recargo r"),
    @NamedQuery(name = "Recargo.findByCodRecargo", query = "SELECT r FROM Recargo r WHERE r.codRecargo = :codRecargo"),
    @NamedQuery(name = "Recargo.findByValorRecargo", query = "SELECT r FROM Recargo r WHERE r.valorRecargo = :valorRecargo"),
    @NamedQuery(name = "Recargo.findByValorMulta", query = "SELECT r FROM Recargo r WHERE r.valorMulta = :valorMulta"),
    @NamedQuery(name = "Recargo.findByFechaCorte", query = "SELECT r FROM Recargo r WHERE r.fechaCorte = :fechaCorte")})
public class Recargo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_RECARGO")
    private Integer codRecargo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALOR_RECARGO")
    private double valorRecargo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALOR_MULTA")
    private double valorMulta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CORTE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCorte;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codRecargo")
    private List<FacturaRecargo> facturaRecargoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codRecargo")
    private List<Reconexion> reconexionList;
    @JoinColumn(name = "COD_TIPO_RECARGO", referencedColumnName = "COD_TIPO_RECARGO")
    @ManyToOne(optional = false)
    private TipoRecargo codTipoRecargo;

    public Recargo() {
    }

    public Recargo(Integer codRecargo) {
        this.codRecargo = codRecargo;
    }

    public Recargo(Integer codRecargo, double valorRecargo, double valorMulta, Date fechaCorte) {
        this.codRecargo = codRecargo;
        this.valorRecargo = valorRecargo;
        this.valorMulta = valorMulta;
        this.fechaCorte = fechaCorte;
    }

    public Integer getCodRecargo() {
        return codRecargo;
    }

    public void setCodRecargo(Integer codRecargo) {
        this.codRecargo = codRecargo;
    }

    public double getValorRecargo() {
        return valorRecargo;
    }

    public void setValorRecargo(double valorRecargo) {
        this.valorRecargo = valorRecargo;
    }

    public double getValorMulta() {
        return valorMulta;
    }

    public void setValorMulta(double valorMulta) {
        this.valorMulta = valorMulta;
    }

    public Date getFechaCorte() {
        return fechaCorte;
    }

    public void setFechaCorte(Date fechaCorte) {
        this.fechaCorte = fechaCorte;
    }

    @XmlTransient
    public List<FacturaRecargo> getFacturaRecargoList() {
        return facturaRecargoList;
    }

    public void setFacturaRecargoList(List<FacturaRecargo> facturaRecargoList) {
        this.facturaRecargoList = facturaRecargoList;
    }

    @XmlTransient
    public List<Reconexion> getReconexionList() {
        return reconexionList;
    }

    public void setReconexionList(List<Reconexion> reconexionList) {
        this.reconexionList = reconexionList;
    }

    public TipoRecargo getCodTipoRecargo() {
        return codTipoRecargo;
    }

    public void setCodTipoRecargo(TipoRecargo codTipoRecargo) {
        this.codTipoRecargo = codTipoRecargo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codRecargo != null ? codRecargo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recargo)) {
            return false;
        }
        Recargo other = (Recargo) object;
        if ((this.codRecargo == null && other.codRecargo != null) || (this.codRecargo != null && !this.codRecargo.equals(other.codRecargo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+getCodRecargo();
    }
    
}
