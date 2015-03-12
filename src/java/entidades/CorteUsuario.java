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
@Table(name = "corte_usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CorteUsuario.findAll", query = "SELECT c FROM CorteUsuario c"),
    @NamedQuery(name = "CorteUsuario.findByCodCorteUsuario", query = "SELECT c FROM CorteUsuario c WHERE c.codCorteUsuario = :codCorteUsuario")})
public class CorteUsuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COD_CORTE_USUARIO")
    private Integer codCorteUsuario;
    @JoinColumn(name = "COD_USUARIO", referencedColumnName = "COD_USUARIO")
    @ManyToOne(optional = false)
    private Usuario codUsuario;
    @JoinColumn(name = "COD_CORTE", referencedColumnName = "COD_CORTE")
    @ManyToOne(optional = false)
    private Corte codCorte;

    public CorteUsuario() {
    }

    public CorteUsuario(Integer codCorteUsuario) {
        this.codCorteUsuario = codCorteUsuario;
    }

    public Integer getCodCorteUsuario() {
        return codCorteUsuario;
    }

    public void setCodCorteUsuario(Integer codCorteUsuario) {
        this.codCorteUsuario = codCorteUsuario;
    }

    public Usuario getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(Usuario codUsuario) {
        this.codUsuario = codUsuario;
    }

    public Corte getCodCorte() {
        return codCorte;
    }

    public void setCodCorte(Corte codCorte) {
        this.codCorte = codCorte;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codCorteUsuario != null ? codCorteUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CorteUsuario)) {
            return false;
        }
        CorteUsuario other = (CorteUsuario) object;
        if ((this.codCorteUsuario == null && other.codCorteUsuario != null) || (this.codCorteUsuario != null && !this.codCorteUsuario.equals(other.codCorteUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.CorteUsuario[ codCorteUsuario=" + codCorteUsuario + " ]";
    }
    
}
