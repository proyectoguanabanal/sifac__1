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
@Table(name = "reconexion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reconexion.findAll", query = "SELECT r FROM Reconexion r"),
    @NamedQuery(name = "Reconexion.findByCodReconexion", query = "SELECT r FROM Reconexion r WHERE r.codReconexion = :codReconexion"),
    @NamedQuery(name = "Reconexion.findByFechaReconexion", query = "SELECT r FROM Reconexion r WHERE r.fechaReconexion = :fechaReconexion")})
public class Reconexion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_RECONEXION")
    private Integer codReconexion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_RECONEXION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReconexion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codReconexion")
    private List<UsuarioReconexion> usuarioReconexionList;
    @JoinColumn(name = "COD_RECARGO", referencedColumnName = "COD_RECARGO")
    @ManyToOne(optional = false)
    private Recargo codRecargo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codReconexion")
    private List<FacturaReconexion> facturaReconexionList;

    public Reconexion() {
    }

    public Reconexion(Integer codReconexion) {
        this.codReconexion = codReconexion;
    }

    public Reconexion(Integer codReconexion, Date fechaReconexion) {
        this.codReconexion = codReconexion;
        this.fechaReconexion = fechaReconexion;
    }

    public Integer getCodReconexion() {
        return codReconexion;
    }

    public void setCodReconexion(Integer codReconexion) {
        this.codReconexion = codReconexion;
    }

    public Date getFechaReconexion() {
        return fechaReconexion;
    }

    public void setFechaReconexion(Date fechaReconexion) {
        this.fechaReconexion = fechaReconexion;
    }

    @XmlTransient
    public List<UsuarioReconexion> getUsuarioReconexionList() {
        return usuarioReconexionList;
    }

    public void setUsuarioReconexionList(List<UsuarioReconexion> usuarioReconexionList) {
        this.usuarioReconexionList = usuarioReconexionList;
    }

    public Recargo getCodRecargo() {
        return codRecargo;
    }

    public void setCodRecargo(Recargo codRecargo) {
        this.codRecargo = codRecargo;
    }

    @XmlTransient
    public List<FacturaReconexion> getFacturaReconexionList() {
        return facturaReconexionList;
    }

    public void setFacturaReconexionList(List<FacturaReconexion> facturaReconexionList) {
        this.facturaReconexionList = facturaReconexionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codReconexion != null ? codReconexion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reconexion)) {
            return false;
        }
        Reconexion other = (Reconexion) object;
        if ((this.codReconexion == null && other.codReconexion != null) || (this.codReconexion != null && !this.codReconexion.equals(other.codReconexion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+getCodReconexion();
    }
    
}
