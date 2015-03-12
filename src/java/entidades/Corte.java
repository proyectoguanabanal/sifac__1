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
@Table(name = "corte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Corte.findAll", query = "SELECT c FROM Corte c"),
    @NamedQuery(name = "Corte.findByCodCorte", query = "SELECT c FROM Corte c WHERE c.codCorte = :codCorte"),
    @NamedQuery(name = "Corte.findByFechaCorte", query = "SELECT c FROM Corte c WHERE c.fechaCorte = :fechaCorte")})
public class Corte implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_CORTE")
    private Integer codCorte;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_CORTE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCorte;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codCorte")
    private List<CorteUsuario> corteUsuarioList;
    @JoinColumn(name = "COD_FACTURA", referencedColumnName = "COD_FACTURA")
    @ManyToOne(optional = false)
    private Factura codFactura;

    public Corte() {
    }

    public Corte(Integer codCorte) {
        this.codCorte = codCorte;
    }

    public Corte(Integer codCorte, Date fechaCorte) {
        this.codCorte = codCorte;
        this.fechaCorte = fechaCorte;
    }

    public Integer getCodCorte() {
        return codCorte;
    }

    public void setCodCorte(Integer codCorte) {
        this.codCorte = codCorte;
    }

    public Date getFechaCorte() {
        return fechaCorte;
    }

    public void setFechaCorte(Date fechaCorte) {
        this.fechaCorte = fechaCorte;
    }

    @XmlTransient
    public List<CorteUsuario> getCorteUsuarioList() {
        return corteUsuarioList;
    }

    public void setCorteUsuarioList(List<CorteUsuario> corteUsuarioList) {
        this.corteUsuarioList = corteUsuarioList;
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
        hash += (codCorte != null ? codCorte.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Corte)) {
            return false;
        }
        Corte other = (Corte) object;
        if ((this.codCorte == null && other.codCorte != null) || (this.codCorte != null && !this.codCorte.equals(other.codCorte))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  ""+getCodCorte();
    }
    
}
