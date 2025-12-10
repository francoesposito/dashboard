/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import models.Venta;

/**
 * FXML Controller class
 *
 * @author Franco
 */
public class DashboardController implements Initializable {

    @FXML 
    private Label lblCantVentas;
    
    @FXML
    private Label lblTotalFacturado;
    
    @FXML 
    private Label lblTicketPromedio;
    
    
    private List<Venta> listaVentas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setVentas(List<Venta> ventas) {
        this.listaVentas = ventas;

        // Solo para verificar que llegaron bien:
        System.out.println("-------------------------------------");
        System.out.println("¡DASHBOARD LISTO!");
        System.out.println("Recibí " + listaVentas.size() + " ventas para procesar.");
        System.out.println("-------------------------------------");
        
        calcularKPIs();
    }
    
    private void calcularKPIs() {
        // A. Calcular Cantidad
        int cantidad = listaVentas.size();

        // B. Calcular Total Facturado (Sumar todos los netos)
        double totalPlata = 0;
        for (Venta v : listaVentas) {
            totalPlata += v.getNeto();
        }

        // C. Calcular Promedio (Evitando división por cero)
        double promedio = (cantidad > 0) ? totalPlata / cantidad : 0;

        // --- FORMATO DE DINERO ---
        // Usamos Locale para Argentina (es-AR) o USA (en-US) según prefieras el punto/coma
        NumberFormat formatoDinero = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        // D. Mostrar en pantalla
        lblCantVentas.setText(String.valueOf(cantidad));
        lblTotalFacturado.setText(formatoDinero.format(totalPlata));
        lblTicketPromedio.setText(formatoDinero.format(promedio));
    }
}
