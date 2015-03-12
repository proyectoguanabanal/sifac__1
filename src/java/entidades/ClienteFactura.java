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
@Table(name = "cliente_factura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClienteFactura.findAll", query = "SELECT c FROM ClienteFactura c"),
    @NamedQuery(name = "ClienteFactura.findByCodClienteFactura", query = "SELECT c FROM ClienteFactura c WHERE c.codClienteFactura = :codClienteFactura")})
public class ClienteFactura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COD_CLIENTE_FACTURA")
    private Integer codClienteFactura;
    @JoinColumn(name = "COD_USUARIO", referencedColumnName = "COD_USUARIO")
    @ManyToOne(optional = false)
    private Usuario codUsuario;
    @JoinColumn(name = "COD_FACTURA", referencedColumnName = "COD_FACTURA")
    @ManyToOne(optional = false)
    private Factura codFactura;

    public ClienteFactura() {
    }

    public ClienteFactura(Integer codClienteFactura) {
        this.codClienteFactura = codClienteFactura;
    }

    public Integer getCodClienteFactura() {
        return codClienteFactura;
    }

    public void setCodClienteFactura(Integer codClienteFactura) {
        this.codClienteFactura = codClienteFactura;
    }

    public Usuario getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(Usuario codUsuario) {
        this.codUsuario = codUsuario;
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
        hash += (codClienteFactura != null ? codClienteFactura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClienteFactura)) {
            return false;
        }
        ClienteFactura other = (ClienteFactura) object;
        if ((this.codClienteFactura == null && other.codClienteFactura != null) || (this.codClienteFactura != null && !this.codClienteFactura.equals(other.codClienteFactura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  ""+getCodFactura();
    }
    
}
