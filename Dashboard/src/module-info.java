/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/module-info.java to edit this template
 */

module Dashboard {
    requires javafx.swt;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;
    requires java.base;
    
    
    // 2. ABRIMOS los paquetes a JavaFX. 
    // Esto es CRÍTICO: permite que el FXML "inyecte" datos en tu código.
    opens dashboard to javafx.fxml;
    opens controllers to javafx.fxml;
    
    // (Opcional por ahora) Si usas tablas más adelante, necesitarás abrir 'models'
    opens models to javafx.base;

    // 3. Exportamos el paquete principal para que la App pueda arrancar
    exports dashboard;
}
