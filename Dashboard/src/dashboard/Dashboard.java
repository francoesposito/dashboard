/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package dashboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Dashboard extends Application {

    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/start.fxml"));

        Parent root = loader.load();

        stage.setScene(new Scene(root));

        stage.setTitle("Dashboard");

        stage.show();

    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
