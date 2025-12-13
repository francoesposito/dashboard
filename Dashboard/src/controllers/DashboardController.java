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
import java.util.ArrayList;
import javafx.scene.control.ComboBox;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import javafx.event.ActionEvent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Franco
 */
public class DashboardController implements Initializable {

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

    @FXML
    private ComboBox<String> cmbMeses;

    private Button btnExportar;

    private List<Venta> listaVentas;

    private List<Venta> listaVentasMaestra;

    private String mesSeleccionado = "Anual";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chartVendedores.setAnimated(false);
        chartRubros.setAnimated(false);
        chartTiempo.setAnimated(false);
        chartRubros.setLabelsVisible(true);

        javafx.scene.control.ToggleGroup grupo = btnNeto.getToggleGroup();

        grupo.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {

                oldVal.setSelected(true);
            }
        });

        CategoryAxis ejeX = (CategoryAxis) chartVendedores.getXAxis();
        ejeX.setTickLabelRotation(90);
        ejeX.setAnimated(false);
        chartVendedores.setLegendVisible(false);
        chartTiempo.setLegendVisible(false);
        chartRubros.setLegendVisible(false);
        chartRubros.setLabelsVisible(false);

    }

    public void setVentas(List<Venta> ventas) {
        this.listaVentasMaestra = ventas;
        this.listaVentas = ventas;

        cargarComboMeses();

        calcularKPIs();
        cargarGraficos();
    }

    private void calcularKPIs() {

        boolean verPorPlata = btnNeto.isSelected();

        int cantidad = listaVentas.size();
        double totalAcumulado = 0;

        for (Venta v : listaVentas) {
            if (verPorPlata) {
                totalAcumulado += v.getNeto();
                lblTituloTotal.setText("Total Facturado");
            } else {
                totalAcumulado += v.getBultos();
                lblTituloTotal.setText("Total Bultos");
            }
        }

        double promedio = (cantidad > 0) ? totalAcumulado / cantidad : 0;

        NumberFormat formato;

        if (verPorPlata) {

            formato = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        } else {

            formato = NumberFormat.getInstance(new Locale("es", "AR"));
            formato.setMaximumFractionDigits(1);
        }

        lblTotalFacturado.setText(formato.format(totalAcumulado));
        lblTicketPromedio.setText(formato.format(promedio));
    }

    private void cargarGraficos() {

        boolean verPorPlata = btnNeto.isSelected();

        chartVendedores.setTitle(verPorPlata ? "Ranking Vendedores ($)" : "Ranking Vendedores (Bultos)");
        chartTiempo.setTitle(verPorPlata ? "Evolución Mensual ($)" : "Evolución Mensual (Bultos)");

        Map<String, Double> porVendedor = new HashMap<>();
        Map<String, Double> porProveedor = new HashMap<>();
        Map<String, Double> porMes = new TreeMap<>();

        for (Venta v : listaVentas) {
            double valorAUsar = verPorPlata ? v.getNeto() : v.getBultos();

            porVendedor.merge(v.getVendedor().getNombre(), valorAUsar, Double::sum);

            String proveedor = v.getArticulo().getProveedor();

            if (proveedor == null || proveedor.isEmpty()) {
                proveedor = "SIN PROVEEDOR";
            }

            porProveedor.merge(proveedor, valorAUsar, Double::sum);

            porMes.merge(v.getAnioMes(), valorAUsar, Double::sum);
        }

        XYChart.Series<String, Number> serieVend = new XYChart.Series<>();
        serieVend.setName(verPorPlata ? "Facturación" : "Bultos");

        List<Map.Entry<String, Double>> listaOrdenada = new ArrayList<>(porVendedor.entrySet());

        listaOrdenada.sort((entrada1, entrada2) -> entrada2.getValue().compareTo(entrada1.getValue()));

        // if (listaOrdenada.size() > 10) {
        //    listaOrdenada = listaOrdenada.subList(0, 10);
        // }
        for (Map.Entry<String, Double> entry : listaOrdenada) {
            serieVend.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        chartVendedores.getData().clear();

        CategoryAxis ejeX = (CategoryAxis) chartVendedores.getXAxis();

        ObservableList<String> categoriasOrdenadas = FXCollections.observableArrayList();
        for (XYChart.Data<String, Number> dato : serieVend.getData()) {
            categoriasOrdenadas.add(dato.getXValue());
        }

        ejeX.setCategories(categoriasOrdenadas);

        chartVendedores.getData().add(serieVend);
        ordenarGrafico(serieVend);

        Platform.runLater(() -> {
            for (XYChart.Data<String, Number> d : serieVend.getData()) {
                if (d.getNode() == null) {
                    continue;
                }

                String textoValor;
                if (verPorPlata) {
                    textoValor = "$ " + String.format("%,.2f", d.getYValue().doubleValue());
                } else {
                    textoValor = String.format("%,.0f Bultos", d.getYValue().doubleValue());
                }

                String mensaje = String.format("Vendedor: %s\nTotal: %s", d.getXValue(), textoValor);

                Tooltip tool = new Tooltip(mensaje);
                tool.setShowDelay(Duration.millis(0));
                tool.setStyle("-fx-font-size: 14px; -fx-background-color: #333333; -fx-text-fill: white;");
                Tooltip.install(d.getNode(), tool);

                String estiloOriginal = d.getNode().getStyle();

                d.getNode().setOnMouseEntered(e -> {

                    d.getNode().setStyle(estiloOriginal + "; -fx-opacity: 0.7; -fx-cursor: hand;");
                });

                d.getNode().setOnMouseExited(e -> {

                    d.getNode().setStyle(estiloOriginal + "; -fx-opacity: 1.0; -fx-cursor: default;");
                });
            }

        });

        javafx.collections.ObservableList<PieChart.Data> datosTorta = javafx.collections.FXCollections.observableArrayList();

        for (Map.Entry<String, Double> entry : porProveedor.entrySet()) {
            datosTorta.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        chartRubros.getData().clear();
        chartRubros.setData(datosTorta);
        chartRubros.setTitle(verPorPlata ? "Facturación por Proveedor" : "Volumen por Proveedor");

        Platform.runLater(() -> {

            double totalTorta = 0;
            for (PieChart.Data d : chartRubros.getData()) {
                totalTorta += d.getPieValue();
            }

            for (PieChart.Data data : chartRubros.getData()) {
                if (data.getNode() == null) {
                    continue;
                }

                double porcentaje = (data.getPieValue() / totalTorta) * 100;

                String textoValor;
                if (verPorPlata) {
                    textoValor = NumberFormat.getCurrencyInstance(new Locale("es", "AR")).format(data.getPieValue());
                } else {

                    NumberFormat formatoBultos = NumberFormat.getInstance(new Locale("es", "AR"));
                    formatoBultos.setMaximumFractionDigits(0);
                    textoValor = formatoBultos.format(data.getPieValue());
                }

                String textoTooltip = String.format("%s\nValor: %s\nRepresenta: %.1f%%",
                        data.getName(),
                        textoValor,
                        porcentaje);

                Tooltip tool = new Tooltip(textoTooltip);
                tool.setShowDelay(Duration.millis(0));
                tool.setStyle("-fx-font-size: 14px; -fx-background-color: #333333; -fx-text-fill: white;");
                Tooltip.install(data.getNode(), tool);

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

        XYChart.Series<String, Number> serieTiempo = new XYChart.Series<>();
        serieTiempo.setName(verPorPlata ? "Tendencia $" : "Tendencia Vol.");
        for (Map.Entry<String, Double> entry : porMes.entrySet()) {
            serieTiempo.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        chartTiempo.getData().clear();
        chartTiempo.getData().add(serieTiempo);

        Platform.runLater(() -> {
            for (XYChart.Data<String, Number> d : serieTiempo.getData()) {

                if (d.getNode() == null) {
                    continue;
                }

                String textoValor;
                if (verPorPlata) {
                    textoValor = NumberFormat.getCurrencyInstance(new Locale("es", "AR")).format(d.getYValue());
                } else {
                    textoValor = String.format("%,.0f Unidades", d.getYValue().doubleValue());
                }

                String mensaje = String.format("Periodo: %s\nTotal: %s", d.getXValue(), textoValor);

                Tooltip tool = new Tooltip(mensaje);
                tool.setShowDelay(Duration.millis(0));
                tool.setStyle("-fx-font-size: 14px; -fx-background-color: #333333; -fx-text-fill: white;");
                Tooltip.install(d.getNode(), tool);

                d.getNode().setOnMouseEntered(e -> {
                    d.getNode().setScaleX(1.5);
                    d.getNode().setScaleY(1.5);
                    d.getNode().setStyle("-fx-cursor: hand;");
                });

                d.getNode().setOnMouseExited(e -> {
                    d.getNode().setScaleX(1.0);
                    d.getNode().setScaleY(1.0);
                    d.getNode().setStyle("-fx-cursor: default;");
                });
            }
        });
    }

    @FXML
    private void cambiarVisualizacion(javafx.event.ActionEvent event) {

        if (listaVentas != null) {
            cargarGraficos();
            calcularKPIs();
        }
    }

    private void cargarComboMeses() {

        Set<String> mesesUnicos = new HashSet<>();
        for (Venta v : listaVentasMaestra) {
            mesesUnicos.add(v.getAnioMes());
        }

        List<String> listaMeses = new ArrayList<>(mesesUnicos);
        Collections.sort(listaMeses);

        listaMeses.add(0, "Anual");

        cmbMeses.getItems().clear();
        cmbMeses.getItems().addAll(listaMeses);

        cmbMeses.getSelectionModel().select("Anual");
    }

    @FXML
    private void filtrarPorMes(ActionEvent event) {
        String seleccion = cmbMeses.getSelectionModel().getSelectedItem();

        if (seleccion == null) {
            return;
        }

        this.mesSeleccionado = seleccion;

        if (seleccion.equals("Anual")) {

            this.listaVentas = new ArrayList<>(listaVentasMaestra);
        } else {

            this.listaVentas = new ArrayList<>();
            for (Venta v : listaVentasMaestra) {
                if (v.getAnioMes().equals(seleccion)) {
                    this.listaVentas.add(v);
                }
            }
        }
        calcularKPIs();
        cargarGraficos();
    }

    @FXML
    private void exportarPantalla(ActionEvent event) {
        Node nodoRaiz = ((Node) event.getSource()).getScene().getRoot();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagen PNG", "*.png"));
        fileChooser.setInitialFileName("Reporte_Ventas_" + mesSeleccionado.replace("/", "-") + ".png");

        File archivoDestino = fileChooser.showSaveDialog(nodoRaiz.getScene().getWindow());

        if (archivoDestino != null) {
            try {

                chartRubros.setLabelsVisible(true);

                chartRubros.layout();

                WritableImage foto = nodoRaiz.snapshot(new SnapshotParameters(), null);

                chartRubros.setLabelsVisible(false);

                ImageIO.write(SwingFXUtils.fromFXImage(foto, null), "png", archivoDestino);

            } catch (IOException ex) {

                chartRubros.setLabelsVisible(false);
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    private void ordenarGrafico(XYChart.Series<String, Number> series) {

        Comparator<XYChart.Data<String, Number>> comparador = (a, b)
                -> Double.compare(b.getYValue().doubleValue(), a.getYValue().doubleValue());

        FXCollections.sort(series.getData(), comparador);

    }
}
