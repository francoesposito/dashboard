/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import models.Venta;

/**
 * FXML Controller class
 *
 * @author Franco
 */
public class DashboardController implements Initializable {

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
    }
}
