/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import models.Venta;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

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

    @FXML
    private BarChart<String, Number> chartVendedores;

    @FXML
    private PieChart chartRubros;

    @FXML
    private LineChart<String, Number> chartTiempo;

    @FXML 
    private ToggleButton btnNeto;
    
    @FXML 
    private ToggleButton btnBultos;
    
    @FXML 
    private Label lblTituloTotal;
    
    private List<Venta> listaVentas;
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chartVendedores.setAnimated(false);
        chartRubros.setAnimated(false); // Opcional, pero recomendado
        chartTiempo.setAnimated(false);
        
        // Opcional: Si el PieChart tiene etiquetas que se solapan, esto ayuda:
        chartRubros.setLabelsVisible(true);
    }

    public void setVentas(List<Venta> ventas) {
        this.listaVentas = ventas;

        // Solo para verificar que llegaron bien:
        System.out.println("-------------------------------------");
        System.out.println("¡DASHBOARD LISTO!");
        System.out.println("Recibí " + listaVentas.size() + " ventas para procesar.");
        System.out.println("-------------------------------------");

        calcularKPIs();
        cargarGraficos();
    }

    private void calcularKPIs() {
        // 1. Verificamos qué botón está apretado
        boolean verPorPlata = btnNeto.isSelected();

        // 2. Variables para sumar
        int cantidad = listaVentas.size();
        double totalAcumulado = 0;

        // 3. Sumamos según el modo elegido
        for (Venta v : listaVentas) {
            if (verPorPlata) {
                totalAcumulado += v.getNeto();
                lblTituloTotal.setText("Total Facturado");
            } else {
                totalAcumulado += v.getBultos();
                lblTituloTotal.setText("Total Bultos");
            }
        }

        // 4. Calcular Promedio
        double promedio = (cantidad > 0) ? totalAcumulado / cantidad : 0;

        // --- EL FORMATO VISUAL ---
        // Aquí está el truco: cambiamos el "formateador" según el modo
        NumberFormat formato;
        
        if (verPorPlata) {
            // Modo Plata: Pone el signo $ y dos decimales ($ 1.500,00)
            formato = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        } else {
            // Modo Bultos: Solo números con separador de miles (1.500)
            formato = NumberFormat.getInstance(new Locale("es", "AR"));
            formato.setMaximumFractionDigits(1); // Opcional: mostrar 1 decimal si los bultos no son enteros
        }

        // 5. Mostrar en pantalla
        lblCantVentas.setText(String.valueOf(cantidad));
        
        // Usamos el formato dinámico que elegimos arriba
        lblTotalFacturado.setText(formato.format(totalAcumulado));
        lblTicketPromedio.setText(formato.format(promedio));
    }

    private void cargarGraficos() {
        // Limpieza inicial
        chartVendedores.getData().clear();
        chartRubros.getData().clear();
        chartTiempo.getData().clear();
        
        // 1. VERIFICAR MODO
        // ¿Está seleccionado el botón de Neto?
        boolean verPorPlata = btnNeto.isSelected();
        
        // Ajustamos los títulos de los gráficos para que el usuario sepa qué ve
        chartVendedores.setTitle(verPorPlata ? "Ranking Vendedores ($)" : "Ranking Vendedores (Bultos)");
        chartTiempo.setTitle(verPorPlata ? "Evolución Mensual ($)" : "Evolución Mensual (Bultos)");

        // 2. PREPARAR CUBETAS
        Map<String, Double> porVendedor = new HashMap<>();
        Map<String, Double> porRubro = new HashMap<>();
        Map<String, Double> porMes = new TreeMap<>(); 

        // 3. LLENAR CUBETAS (Con lógica dinámica)
        for (Venta v : listaVentas) {
            // --- DECISIÓN CLAVE ---
            // Si verPorPlata es true, usamos getNeto(). Si no, usamos getBultos().
            double valorAUsar = verPorPlata ? v.getNeto() : v.getBultos();
            // ----------------------

            // Ahora usamos 'valorAUsar' en lugar de v.getNeto()
            porVendedor.merge(v.getVendedor().getNombre(), valorAUsar, Double::sum);
            porRubro.merge(v.getArticulo().getRubro(), valorAUsar, Double::sum);
            porMes.merge(v.getAnioMes(), valorAUsar, Double::sum);
        }

        // 4. DIBUJAR (Esto queda casi igual, solo cambié los nombres de las series)
        
        // --- Vendedores ---
        XYChart.Series<String, Number> serieVend = new XYChart.Series<>();
        serieVend.setName(verPorPlata ? "Facturación" : "Bultos");
        for (Map.Entry<String, Double> entry : porVendedor.entrySet()) {
            serieVend.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        chartVendedores.getData().add(serieVend);

        // --- Rubros ---
        javafx.collections.ObservableList<PieChart.Data> datosTorta = javafx.collections.FXCollections.observableArrayList();
        for (Map.Entry<String, Double> entry : porRubro.entrySet()) {
            datosTorta.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        chartRubros.setData(datosTorta);

        // --- COPIA DESDE AQUÍ ---
        
        // Usamos runLater para esperar a que los gráficos se dibujen antes de poner las etiquetas
        Platform.runLater(() -> {
            
            // 1. Calculamos el total de la torta actual
            double totalTorta = 0;
            for (PieChart.Data d : chartRubros.getData()) {
                totalTorta += d.getPieValue();
            }

            // 2. Recorremos cada porción
            for (PieChart.Data data : chartRubros.getData()) {
                
                // Si la porción no existe visualmente, la saltamos
                if (data.getNode() == null) continue;

                // Calculamos porcentaje
                double porcentaje = (data.getPieValue() / totalTorta) * 100;
                
                // Texto del Tooltip
                String textoTooltip = String.format("%s\nValor: %s\nRepresenta: %.1f%%", 
                        data.getName(), 
                        (verPorPlata ? "$ " : "") + String.format("%.0f", data.getPieValue()), 
                        porcentaje);

                // 3. Instalamos el Tooltip instantáneo
                Tooltip tool = new Tooltip(textoTooltip);
                tool.setShowDelay(Duration.millis(0)); // <--- CLAVE: Retardo cero
                tool.setStyle("-fx-font-size: 14px; -fx-background-color: #333333; -fx-text-fill: white;");
                Tooltip.install(data.getNode(), tool);

                // 4. Efecto de Zoom seguro
                data.getNode().setOnMouseEntered(e -> {
                    data.getNode().setScaleX(1.1);
                    data.getNode().setScaleY(1.1);
                });

                data.getNode().setOnMouseExited(e -> {
                    data.getNode().setScaleX(1.0);
                    data.getNode().setScaleY(1.0);
                });
            }
        });
        
        // --- Tiempo ---
        XYChart.Series<String, Number> serieTiempo = new XYChart.Series<>();
        serieTiempo.setName(verPorPlata ? "Tendencia $" : "Tendencia Vol.");
        for (Map.Entry<String, Double> entry : porMes.entrySet()) {
            serieTiempo.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        chartTiempo.getData().add(serieTiempo);
    }
    
    @FXML
    private void cambiarVisualizacion(javafx.event.ActionEvent event) {
        // Simplemente recargamos los gráficos. 
        // El método cargarGraficos() se encargará de ver qué botón está activo.
        if (listaVentas != null) {
            cargarGraficos();
            calcularKPIs();
        }
    }

}
