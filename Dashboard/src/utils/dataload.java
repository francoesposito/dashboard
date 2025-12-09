/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import models.Articulo;
import models.Cliente;
import models.Empresa;
import models.Vendedor;
import models.Venta;

/**
 *
 * @author Franco
 */
public class dataload {

    public List<Venta> cargarVentasDesdeCSV(File archivo) {
        List<Venta> listaVentas = new ArrayList<>();
        String linea = "";

        // Ajustamos al formato que vimos en tu error: dd/MM/yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            br.readLine(); // Saltar cabecera

            while ((linea = br.readLine()) != null) {
                // USAREMOS NUESTRO PARSER PROPIO (ver método abajo)
                List<String> datos = parsearLineaCSV(linea);

                // Si la línea está vacía o incompleta, la saltamos
                if (datos.size() < 20) {
                    continue;
                }

                try {
                    // --- 1. EMPRESA (Indices 2 y 3) ---
                    int empCod = parseIntSeguro(datos.get(2));
                    String empNom = datos.get(3).trim();
                    Empresa empresa = new Empresa(empCod, empNom);

                    // --- 2. CLIENTE (Indices 4 al 12) ---
                    int cliCod = parseIntSeguro(datos.get(4));
                    String cliNom = datos.get(7).trim();
                    String cliDir = datos.get(8).trim();
                    String cliCP = datos.get(6).trim();
                    String cliLoc = datos.get(9).trim();
                    String cliProv = datos.get(10).trim();
                    String cliRub = datos.get(11).trim();
                    String cliGrup = datos.get(12).trim();

                    Cliente cliente = new Cliente(cliCod, cliNom, cliDir, cliCP, cliLoc, cliProv, cliRub, cliGrup);

                    // --- 3. ARTICULO (Indices 13 al 21) ---
                    int artCod = parseIntSeguro(datos.get(13));
                    String artDesc = datos.get(14).trim();
                    String artRub = datos.get(15).trim(); // Rubro Articulo
                    String artLin = datos.get(16).trim();
                    String artDiv = datos.get(17).trim();
                    String artGrup = datos.get(18).trim();
                    int artCont = 1; // Default
                    String artProv = datos.get(20).trim();
                    String artEAN = datos.get(21).trim();

                    Articulo articulo = new Articulo(artCod, artDesc, artRub, artLin, artDiv, artGrup, artCont, artProv, artEAN, 0);

                    // --- 4. VENDEDOR (Indices 22 y 23) ---
                    // Indice 22: Código puro (ej: 18) -> Es el más seguro
                    // Indice 23: Nombre completo (ej: "018-BIRD, SEBASTIAN") -> Gracias al parser, esto llega entero!
                    int vendCod = parseIntSeguro(datos.get(22));
                    String vendNom = datos.get(23).trim();

                    // Limpieza extra por si queremos sacar el "018-" del nombre
                    if (vendNom.contains("-")) {
                        String[] partes = vendNom.split("-");
                        if (partes.length > 1) {
                            vendNom = partes[1].trim();
                        }
                    }

                    Vendedor vendedor = new Vendedor(vendCod, vendNom);

                    // --- 5. VENTA (Indices 0, 1 y finales 24, 25, 26) ---
                    // IMPORTANTE: Al usar el parser, los índices NO se corren. Bultos siempre es el que sigue al vendedor.
                    LocalDate fecha = LocalDate.parse(datos.get(0).trim(), formatter);
                    String anioMes = datos.get(1).trim();

                    // INDICES CORREGIDOS SEGÚN TU EXCEL:
                    // Col W (22) = VendCod
                    // Col X (23) = VendNom
                    // Col Y (24) = Bultos
                    // Col Z (25) = Cantidad
                    // Col AA (26) = Neto
                    double bultos = parseDoubleSeguro(datos.get(24));
                    int cantidad = (int) parseDoubleSeguro(datos.get(25));
                    double neto = parseDoubleSeguro(datos.get(26));

                    Venta venta = new Venta(fecha, anioMes, empresa, cliente, articulo, vendedor, "0000", "FACTURA", bultos, cantidad, neto);
                    listaVentas.add(venta);

                } catch (Exception e) {
                    System.err.println("Error en linea: " + linea);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaVentas;
    }

    // --- MÉTODOS AYUDANTES (COPIAR TAMBIÉN) ---
    // Este es el secreto: un parser manual que respeta las comillas "..."
    private List<String> parsearLineaCSV(String linea) {
        List<String> valores = new ArrayList<>();
        StringBuilder valorActual = new StringBuilder();
        boolean dentroDeComillas = false;

        for (char c : linea.toCharArray()) {
            if (c == '\"') {
                dentroDeComillas = !dentroDeComillas; // Entramos o salimos de comillas
            } else if (c == ',' && !dentroDeComillas) {
                valores.add(valorActual.toString()); // Terminó la celda
                valorActual.setLength(0); // Limpiar buffer
            } else {
                valorActual.append(c); // Agregar letra
            }
        }
        valores.add(valorActual.toString()); // Agregar el último valor
        return valores;
    }

    // Ayudantes para evitar errores si viene vacío o texto raro
    private int parseIntSeguro(String str) {
        try {
            return Integer.parseInt(str.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private double parseDoubleSeguro(String str) {
        try {
            return Double.parseDouble(str.trim().replace(",", ".")); // Reemplaza coma decimal por punto
        } catch (Exception e) {
            return 0.0;
        }

    }

}
