/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Usuario;
import utils.Autenticacion;

public class LoginController implements Initializable {

    @FXML
    private Label lblMensaje;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnIngresar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblMensaje.setText("");

        Platform.runLater(() -> {

            lblMensaje.getParent().requestFocus();
        });
    }

    @FXML
    private void handleIngresar(ActionEvent event) {
        String user = txtUsuario.getText().trim();
        String pass = txtPassword.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            lblMensaje.setText("Por favor complete los campos.");
            return;
        }

        Usuario usuarioValidado = Autenticacion.validarLogin(user, pass);

        if (usuarioValidado != null) {
            abrirPantallaCarga(usuarioValidado);

            Stage stageLogin = (Stage) txtUsuario.getScene().getWindow();
            stageLogin.close();

        } else {
            lblMensaje.setText("Usuario o contraseña incorrectos.");
        }

    }

    private void abrirPantallaCarga(Usuario usuario) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/start.fxml"));

            Parent root = loader.load();

            StartController controller = loader.getController();

            controller.setUsuario(usuario);

            Stage stage = new Stage();
            stage.setTitle("Cargar Datos - Usuario: " + usuario.getNombreUsuario());

            try {
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.ico")));
            } catch (Exception e) {
            }

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
            lblMensaje.setText("Error al abrir la pantalla de carga.");
        }
    }

}
