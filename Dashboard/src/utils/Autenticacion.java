package utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import models.Usuario;

public class Autenticacion {

    private static final String ARCHIVO_USUARIOS = "usuarios.csv";

    public static String hashear(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Error al hashear password", ex);
        }
    }

    public static Usuario validarLogin(String user, String pass) {

        List<Usuario> listaUsuarios = cargarUsuarios();

        String passHash = convertirSHA256(pass);

        for (Usuario u : listaUsuarios) {

            if (u.getNombreUsuario().equalsIgnoreCase(user) && u.getPassword().equals(passHash)) {
                return u;
            }
        }

        return null;
    }

    private static List<Usuario> cargarUsuarios() {
        List<Usuario> lista = new ArrayList<>();

        try {
            InputStream is = Autenticacion.class.getResourceAsStream("/resources/usuarios.csv");

            if (is == null) {
                System.out.println("ERROR: No se encontró /resources/usuarios.txt");
                return lista;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String linea;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(",", 4);

                if (datos.length >= 4) {

                    Usuario u = new Usuario(datos[0].trim(), datos[1].trim(), datos[2].trim(), datos[3].trim());
                    lista.add(u);
                }
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    private static String convertirSHA256(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
