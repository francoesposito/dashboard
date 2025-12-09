/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author Franco
 */
public class Venta {
    
    private LocalDate fecha;
    private String anioMes;
    private Empresa empresa;
    private Cliente cliente;
    private Articulo articulo;
    private Vendedor vendedor;
    private String numeroFactura;
    private String tipoDeComprobante;
    private double bultos;
    private int cantidad;
    private double neto;

    public Venta(LocalDate fecha, String anioMes, Empresa empresa, Cliente cliente, Articulo articulo, Vendedor vendedor, String numeroFactura, String tipoDeComprobante, double bultos, int cantidad, double neto) {
        this.fecha = fecha;
        this.anioMes = anioMes;
        this.empresa = empresa;
        this.cliente = cliente;
        this.articulo = articulo;
        this.vendedor = vendedor;
        this.numeroFactura = numeroFactura;
        this.tipoDeComprobante = tipoDeComprobante;
        this.bultos = bultos;
        this.cantidad = cantidad;
        this.neto = neto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getAnioMes() {
        return anioMes;
    }

    public void setAnioMes(String anioMes) {
        this.anioMes = anioMes;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getTipoDeComprobante() {
        return tipoDeComprobante;
    }

    public void setTipoDeComprobante(String tipoDeComprobante) {
        this.tipoDeComprobante = tipoDeComprobante;
    }

    public double getBultos() {
        return bultos;
    }

    public void setBultos(double bultos) {
        this.bultos = bultos;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getNeto() {
        return neto;
    }

    public void setNeto(double neto) {
        this.neto = neto;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Venta other = (Venta) obj;
        if (Double.doubleToLongBits(this.bultos) != Double.doubleToLongBits(other.bultos)) {
            return false;
        }
        if (this.cantidad != other.cantidad) {
            return false;
        }
        if (Double.doubleToLongBits(this.neto) != Double.doubleToLongBits(other.neto)) {
            return false;
        }
        if (!Objects.equals(this.anioMes, other.anioMes)) {
            return false;
        }
        if (!Objects.equals(this.numeroFactura, other.numeroFactura)) {
            return false;
        }
        if (!Objects.equals(this.tipoDeComprobante, other.tipoDeComprobante)) {
            return false;
        }
        if (!Objects.equals(this.fecha, other.fecha)) {
            return false;
        }
        if (!Objects.equals(this.empresa, other.empresa)) {
            return false;
        }
        if (!Objects.equals(this.cliente, other.cliente)) {
            return false;
        }
        if (!Objects.equals(this.articulo, other.articulo)) {
            return false;
        }
        return Objects.equals(this.vendedor, other.vendedor);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Venta{");
        sb.append("fecha=").append(fecha);
        sb.append(", anioMes=").append(anioMes);
        sb.append(", empresa=").append(empresa);
        sb.append(", cliente=").append(cliente);
        sb.append(", articulo=").append(articulo);
        sb.append(", vendedor=").append(vendedor);
        sb.append(", numeroFactura=").append(numeroFactura);
        sb.append(", tipoDeComprobante=").append(tipoDeComprobante);
        sb.append(", bultos=").append(bultos);
        sb.append(", cantidad=").append(cantidad);
        sb.append(", neto=").append(neto);
        sb.append('}');
        return sb.toString();
    }
    
    
    
    
}
