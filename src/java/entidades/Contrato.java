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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "contrato")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contrato.findAll", query = "SELECT c FROM Contrato c"),
    @NamedQuery(name = "Contrato.findByCodContrato", query = "SELECT c FROM Contrato c WHERE c.codContrato = :codContrato"),
    @NamedQuery(name = "Contrato.findByNombreContrato", query = "SELECT c FROM Contrato c WHERE c.nombreContrato = :nombreContrato")})
public class Contrato implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COD_CONTRATO")
    private Integer codContrato;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE_CONTRATO")
    private String nombreContrato;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codContrato")
    private List<ClienteContrato> clienteContratoList;

    public Contrato() {
    }

    public Contrato(Integer codContrato) {
        this.codContrato = codContrato;
    }

    public Contrato(Integer codContrato, String nombreContrato) {
        this.codContrato = codContrato;
        this.nombreContrato = nombreContrato;
    }

    public Integer getCodContrato() {
        return codContrato;
    }

    public void setCodContrato(Integer codContrato) {
        this.codContrato = codContrato;
    }

    public String getNombreContrato() {
        return nombreContrato;
    }

    public void setNombreContrato(String nombreContrato) {
        this.nombreContrato = nombreContrato;
    }

    @XmlTransient
    public List<ClienteContrato> getClienteContratoList() {
        return clienteContratoList;
    }

    public void setClienteContratoList(List<ClienteContrato> clienteContratoList) {
        this.clienteContratoList = clienteContratoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codContrato != null ? codContrato.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contrato)) {
            return false;
        }
        Contrato other = (Contrato) object;
        if ((this.codContrato == null && other.codContrato != null) || (this.codContrato != null && !this.codContrato.equals(other.codContrato))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+getCodContrato();
    }
    
}
