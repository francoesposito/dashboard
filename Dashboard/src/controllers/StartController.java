/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import models.Usuario;
import models.Venta;
import utils.dataload;

;

/**
 * FXML Controller class
 *
 * @author Franco
 */
public class StartController implements Initializable {

    @FXML
    private Button btnCargar;

    private Usuario usuarioActual;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleCargar(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Buscar Archivo CSV");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos CSV", "*.csv")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        File archivoSeleccionado = fileChooser.showOpenDialog(stage);

        if (archivoSeleccionado != null) {
            System.out.println("Archivo elegido: " + archivoSeleccionado.getAbsolutePath());
            dataload loaderVentas = new dataload();
            List<Venta> misVentas = loaderVentas.cargarVentasDesdeCSV(archivoSeleccionado);

            System.out.println("Se cargaron " + misVentas.size() + " ventas.");
            if (!misVentas.isEmpty()) {
                System.out.println("Ejemplo: " + misVentas.get(0).toString());
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
                Parent root = loader.load();

                DashboardController controller = loader.getController();

                controller.setVentas(misVentas);

                if (this.usuarioActual != null) {
                    controller.setUsuario(this.usuarioActual);
                } else {
                    System.out.println("¡Cuidado! El usuarioActual es null en StartController.");
                }

                Scene scene = new Scene(root);
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

                stage.setMaximized(true);
                stage.setX(screenBounds.getMinX());
                stage.setY(screenBounds.getMinY());
                stage.setWidth(screenBounds.getWidth());
                stage.setHeight(screenBounds.getHeight());

                stage.setScene(scene);
                stage.setTitle("Dashboard - " + usuarioActual.getNombreUsuario());
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error al cargar la pantalla del Dashboard: " + e.getMessage());
            }

        } else {
            System.out.println("El usuario canceló la selección.");
        }

    }

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        System.out.println("StartController: Usuario recibido -> " + usuario.getNombreUsuario());
    }

}
