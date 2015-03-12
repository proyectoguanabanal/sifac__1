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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author yinneandyor
 */
@Entity
@Table(name = "cliente_estado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClienteEstado.findAll", query = "SELECT c FROM ClienteEstado c"),
    @NamedQuery(name = "ClienteEstado.findByCodClienteEstado", query = "SELECT c FROM ClienteEstado c WHERE c.codClienteEstado = :codClienteEstado")})
public class ClienteEstado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_CLIENTE_ESTADO")
    private Integer codClienteEstado;
    @JoinColumn(name = "COD_USUARIO", referencedColumnName = "COD_USUARIO")
    @ManyToOne(optional = false)
    private Usuario codUsuario;
    @JoinColumn(name = "COD_ESTADO", referencedColumnName = "COD_ESTADO")
    @ManyToOne(optional = false)
    private Estado codEstado;

    public ClienteEstado() {
    }

    public ClienteEstado(Integer codClienteEstado) {
        this.codClienteEstado = codClienteEstado;
    }

    public Integer getCodClienteEstado() {
        return codClienteEstado;
    }

    public void setCodClienteEstado(Integer codClienteEstado) {
        this.codClienteEstado = codClienteEstado;
    }

    public Usuario getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(Usuario codUsuario) {
        this.codUsuario = codUsuario;
    }

    public Estado getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(Estado codEstado) {
        this.codEstado = codEstado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codClienteEstado != null ? codClienteEstado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClienteEstado)) {
            return false;
        }
        ClienteEstado other = (ClienteEstado) object;
        if ((this.codClienteEstado == null && other.codClienteEstado != null) || (this.codClienteEstado != null && !this.codClienteEstado.equals(other.codClienteEstado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.ClienteEstado[ codClienteEstado=" + codClienteEstado + " ]";
    }
    
}
