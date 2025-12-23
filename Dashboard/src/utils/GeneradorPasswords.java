/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;

public class GeneradorPasswords {

    // ACTUALIZAR ESTO SI SE AGREGA UN NUEVO VENDEDOR
    private static final String[] VENDEDORES = {
        "DEVOTO, JUAN",
        "BIRD, SEBASTIAN",
        "CAIRO, LAURA",
        "ZONA 12",
        "VILLALBA, CLAUDIA",
        "RAMIREZ, MILAGROS",
        "ARGAÑARAZ, BRENDA",
        "GONZALEZ, FABIANA",
        "WEIMER, NEHUEN",
        "ALASIA, VALERIA",
        "REIF, PABLO",
        "Juan Madof",
        "MAYORISTAS",
        "CAROLINA, TORRES",
        "maria eugenia",
        "JIMENEZ, DANTE",
        "CASPER, NICOLAS",
        "JARA, MIRTA",
        "MARTIN",
        "GRISELDA, LAVIN",
        "MOLINA, NOELIA",
        "CENTRO",
        "WEB",
        "Norberto",
        "VACANTE DANTE",
        "ROXANA ACEVEDO",
        "DANIEL IBARRA",
        "ZONA QUO 3",
        "PROVEEDURIA",
        "YAEL SILVESTRO"
    };

    public static void main(String[] args) {
        System.out.println("--- COPIAR DESDE AQUÍ AL ARCHIVO usuarios.csv ---");

        System.out.println("admin," + hashear("admin101") + ",ADMIN,null");

        Random random = new Random();

        for (String nombreReal : VENDEDORES) {
            String usuario = generarUsuario(nombreReal);

            int pin = 1000 + random.nextInt(9000);

            // 3. Hashear el PIN
            String hash = hashear(String.valueOf(pin));

            System.out.printf("%s,%s,VENDEDOR,%s%n", usuario, hash, nombreReal);

            System.err.println("GUARDAR ESTO APARTE -> " + nombreReal + " | Usuario: " + usuario + " | PIN: " + pin);
        }
    }

    private static String generarUsuario(String nombreCompleto) {

        return nombreCompleto.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }

    public static String hashear(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            return "ERROR";
        }
    }
}
