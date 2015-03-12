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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author yinneandyor
 */
@Entity
@Table(name = "estado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estado.findAll", query = "SELECT e FROM Estado e"),
    @NamedQuery(name = "Estado.findByCodEstado", query = "SELECT e FROM Estado e WHERE e.codEstado = :codEstado"),
    @NamedQuery(name = "Estado.findByFacturasPendientes", query = "SELECT e FROM Estado e WHERE e.facturasPendientes = :facturasPendientes")})
public class Estado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_ESTADO")
    private Integer codEstado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FACTURAS_PENDIENTES")
    private int facturasPendientes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codEstado")
    private List<ClienteEstado> clienteEstadoList;

    public Estado() {
    }

    public Estado(Integer codEstado) {
        this.codEstado = codEstado;
    }

    public Estado(Integer codEstado, int facturasPendientes) {
        this.codEstado = codEstado;
        this.facturasPendientes = facturasPendientes;
    }

    public Integer getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(Integer codEstado) {
        this.codEstado = codEstado;
    }

    public int getFacturasPendientes() {
        return facturasPendientes;
    }

    public void setFacturasPendientes(int facturasPendientes) {
        this.facturasPendientes = facturasPendientes;
    }

    @XmlTransient
    public List<ClienteEstado> getClienteEstadoList() {
        return clienteEstadoList;
    }

    public void setClienteEstadoList(List<ClienteEstado> clienteEstadoList) {
        this.clienteEstadoList = clienteEstadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codEstado != null ? codEstado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estado)) {
            return false;
        }
        Estado other = (Estado) object;
        if ((this.codEstado == null && other.codEstado != null) || (this.codEstado != null && !this.codEstado.equals(other.codEstado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+getCodEstado();
    }
    
}
