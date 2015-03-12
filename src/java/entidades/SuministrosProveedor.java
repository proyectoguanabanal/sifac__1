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
@Table(name = "suministros_proveedor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SuministrosProveedor.findAll", query = "SELECT s FROM SuministrosProveedor s"),
    @NamedQuery(name = "SuministrosProveedor.findByCodSuministrosProveedor", query = "SELECT s FROM SuministrosProveedor s WHERE s.codSuministrosProveedor = :codSuministrosProveedor")})
public class SuministrosProveedor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COD_SUMINISTROS_PROVEEDOR")
    private Integer codSuministrosProveedor;
    @JoinColumn(name = "COD_SUMINISTROS", referencedColumnName = "COD_SUMINISTROS")
    @ManyToOne(optional = false)
    private Suministros codSuministros;
    @JoinColumn(name = "COD_PROVEEDOR", referencedColumnName = "COD_PROVEEDOR")
    @ManyToOne(optional = false)
    private Proveedor codProveedor;

    public SuministrosProveedor() {
    }

    public SuministrosProveedor(Integer codSuministrosProveedor) {
        this.codSuministrosProveedor = codSuministrosProveedor;
    }

    public Integer getCodSuministrosProveedor() {
        return codSuministrosProveedor;
    }

    public void setCodSuministrosProveedor(Integer codSuministrosProveedor) {
        this.codSuministrosProveedor = codSuministrosProveedor;
    }

    public Suministros getCodSuministros() {
        return codSuministros;
    }

    public void setCodSuministros(Suministros codSuministros) {
        this.codSuministros = codSuministros;
    }

    public Proveedor getCodProveedor() {
        return codProveedor;
    }

    public void setCodProveedor(Proveedor codProveedor) {
        this.codProveedor = codProveedor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codSuministrosProveedor != null ? codSuministrosProveedor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SuministrosProveedor)) {
            return false;
        }
        SuministrosProveedor other = (SuministrosProveedor) object;
        if ((this.codSuministrosProveedor == null && other.codSuministrosProveedor != null) || (this.codSuministrosProveedor != null && !this.codSuministrosProveedor.equals(other.codSuministrosProveedor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.SuministrosProveedor[ codSuministrosProveedor=" + codSuministrosProveedor + " ]";
    }
    
}
