/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Franco
 */
public class Usuario {

    private String nombreUsuario;
    private String password;
    private String rol;
    private String codigoVendedor;

    public Usuario(String nombreUsuario, String password, String rol, String codigoVendedor) {
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.rol = rol;
        this.codigoVendedor = codigoVendedor;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public String getRol() {
        return rol;
    }

    public String getCodigoVendedor() {
        return codigoVendedor;
    }

    public boolean esAdmin() {
        return "ADMIN".equalsIgnoreCase(this.rol);
    }

    @Override
    public String toString() {
        return nombreUsuario + " (" + rol + ")";
    }
}
