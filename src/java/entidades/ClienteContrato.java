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
@Table(name = "cliente_contrato")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClienteContrato.findAll", query = "SELECT c FROM ClienteContrato c"),
    @NamedQuery(name = "ClienteContrato.findByCodClienteContrato", query = "SELECT c FROM ClienteContrato c WHERE c.codClienteContrato = :codClienteContrato")})
public class ClienteContrato implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COD_CLIENTE_CONTRATO")
    private Integer codClienteContrato;
    @JoinColumn(name = "COD_USUARIO", referencedColumnName = "COD_USUARIO")
    @ManyToOne(optional = false)
    private Usuario codUsuario;
    @JoinColumn(name = "COD_CONTRATO", referencedColumnName = "COD_CONTRATO")
    @ManyToOne(optional = false)
    private Contrato codContrato;

    public ClienteContrato() {
    }

    public ClienteContrato(Integer codClienteContrato) {
        this.codClienteContrato = codClienteContrato;
    }

    public Integer getCodClienteContrato() {
        return codClienteContrato;
    }

    public void setCodClienteContrato(Integer codClienteContrato) {
        this.codClienteContrato = codClienteContrato;
    }

    public Usuario getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(Usuario codUsuario) {
        this.codUsuario = codUsuario;
    }

    public Contrato getCodContrato() {
        return codContrato;
    }

    public void setCodContrato(Contrato codContrato) {
        this.codContrato = codContrato;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codClienteContrato != null ? codClienteContrato.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClienteContrato)) {
            return false;
        }
        ClienteContrato other = (ClienteContrato) object;
        if ((this.codClienteContrato == null && other.codClienteContrato != null) || (this.codClienteContrato != null && !this.codClienteContrato.equals(other.codClienteContrato))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.ClienteContrato[ codClienteContrato=" + codClienteContrato + " ]";
    }
    
}
