/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.Objects;

/**
 *
 * @author Franco
 */
public class Articulo {
    
    private int codigo;
    private String descripcion;
    private String rubro;
    private String linea;
    private String division;
    private String grupo;
    private int contenidoUnidad;
    private String proveedor;
    private String codigoEan;
    private int codigoCompra;    

    public Articulo(int codigo, String descripcion, String rubro, String linea, String division, String grupo, int contenidoUnidad, String proveedor, String codigoEan, int codigoCompra) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.rubro = rubro;
        this.linea = linea;
        this.division = division;
        this.grupo = grupo;
        this.contenidoUnidad = contenidoUnidad;
        this.proveedor = proveedor;
        this.codigoEan = codigoEan;
        this.codigoCompra = codigoCompra;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int getContenidoUnidad() {
        return contenidoUnidad;
    }

    public void setContenidoUnidad(int contenidoUnidad) {
        this.contenidoUnidad = contenidoUnidad;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getCodigoEan() {
        return codigoEan;
    }

    public void setCodigoEan(String codigoEan) {
        this.codigoEan = codigoEan;
    }

    public int getCodigoCompra() {
        return codigoCompra;
    }

    public void setCodigoCompra(int codigoCompra) {
        this.codigoCompra = codigoCompra;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.codigo;
        hash = 37 * hash + Objects.hashCode(this.descripcion);
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
        final Articulo other = (Articulo) obj;
        if (this.codigo != other.codigo) {
            return false;
        }
        return Objects.equals(this.descripcion, other.descripcion);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Articulo{");
        sb.append("codigo=").append(codigo);
        sb.append(", descripcion=").append(descripcion);
        sb.append(", rubro=").append(rubro);
        sb.append(", linea=").append(linea);
        sb.append(", division=").append(division);
        sb.append(", grupo=").append(grupo);
        sb.append(", contenidoUnidad=").append(contenidoUnidad);
        sb.append(", proveedor=").append(proveedor);
        sb.append(", codigoEan=").append(codigoEan);
        sb.append(", codigoCompra=").append(codigoCompra);
        sb.append('}');
        return sb.toString();
    }
    
    
    
    
}
