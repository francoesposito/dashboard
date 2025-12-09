/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Franco
 */
public class Empresa extends Persona {
    
    public Empresa(int codigo, String nombre){
        super(codigo, nombre);
    }

    @Override
    public int getCodigo() {
        return codigo;
    }
    
    @Override
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Empresa{");
        sb.append(super.toString());
        sb.append('}');
        return sb.toString();
    }
    
    
    
    
    
}
