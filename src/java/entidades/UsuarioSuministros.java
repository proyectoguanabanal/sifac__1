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
@Table(name = "usuario_suministros")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioSuministros.findAll", query = "SELECT u FROM UsuarioSuministros u"),
    @NamedQuery(name = "UsuarioSuministros.findByCodUsuarioSuministro", query = "SELECT u FROM UsuarioSuministros u WHERE u.codUsuarioSuministro = :codUsuarioSuministro")})
public class UsuarioSuministros implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_USUARIO_SUMINISTRO")
    private Integer codUsuarioSuministro;
    @JoinColumn(name = "COD_USUARIO", referencedColumnName = "COD_USUARIO")
    @ManyToOne(optional = false)
    private Usuario codUsuario;
    @JoinColumn(name = "COD_SUMINISTROS", referencedColumnName = "COD_SUMINISTROS")
    @ManyToOne(optional = false)
    private Suministros codSuministros;

    public UsuarioSuministros() {
    }

    public UsuarioSuministros(Integer codUsuarioSuministro) {
        this.codUsuarioSuministro = codUsuarioSuministro;
    }

    public Integer getCodUsuarioSuministro() {
        return codUsuarioSuministro;
    }

    public void setCodUsuarioSuministro(Integer codUsuarioSuministro) {
        this.codUsuarioSuministro = codUsuarioSuministro;
    }

    public Usuario getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(Usuario codUsuario) {
        this.codUsuario = codUsuario;
    }

    public Suministros getCodSuministros() {
        return codSuministros;
    }

    public void setCodSuministros(Suministros codSuministros) {
        this.codSuministros = codSuministros;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codUsuarioSuministro != null ? codUsuarioSuministro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioSuministros)) {
            return false;
        }
        UsuarioSuministros other = (UsuarioSuministros) object;
        if ((this.codUsuarioSuministro == null && other.codUsuarioSuministro != null) || (this.codUsuarioSuministro != null && !this.codUsuarioSuministro.equals(other.codUsuarioSuministro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.UsuarioSuministros[ codUsuarioSuministro=" + codUsuarioSuministro + " ]";
    }
    
}
