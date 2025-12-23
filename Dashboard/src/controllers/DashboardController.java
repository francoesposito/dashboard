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
import models.Usuario;

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

    private Usuario usuarioActual;

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

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;

        if (listaVentasMaestra != null && !listaVentasMaestra.isEmpty()) {
            aplicarFiltrosGlobales();
        }
    }

    public void setVentas(List<Venta> ventas) {
        this.listaVentasMaestra = ventas;

        cargarComboMeses();

        aplicarFiltrosGlobales();
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
        aplicarColoresConsistentes();
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

        // Llamamos al método que sabe combinar Mes + Usuario
        aplicarFiltrosGlobales();
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

    private void aplicarFiltrosGlobales() {

        if (listaVentasMaestra == null || usuarioActual == null) {
            listaVentas = new ArrayList<>(); // Lista vacía
            cargarGraficos();
            calcularKPIs();
            return;
        }

        this.listaVentas = new ArrayList<>();

        for (Venta v : listaVentasMaestra) {

            boolean pasaFiltroMes = false;
            if (mesSeleccionado.equals("Anual")) {
                pasaFiltroMes = true;
            } else if (v.getAnioMes().equals(mesSeleccionado)) {
                pasaFiltroMes = true;
            }

            boolean pasaFiltroUsuario = false;

            if (usuarioActual.esAdmin()) {

                pasaFiltroUsuario = true;
            } else {

                String vendedorVenta = v.getVendedor().getNombre();
                String miCodigo = usuarioActual.getCodigoVendedor();

                if (vendedorVenta != null && miCodigo != null
                        && vendedorVenta.trim().equalsIgnoreCase(miCodigo.trim())) {
                    pasaFiltroUsuario = true;
                }
            }

            if (pasaFiltroMes && pasaFiltroUsuario) {
                this.listaVentas.add(v);
            }
        }

        calcularKPIs();
        cargarGraficos();
    }
    
    
    // ACTUALIZAR ESTO SI SE AGREGA UN PROVEEDOR

    private void aplicarColoresConsistentes() {

        for (PieChart.Data data : chartRubros.getData()) {

            String categoria = data.getName().toUpperCase();
            String colorHex;

            switch (categoria) {
                case "00039 - SIDEREUS S.R.L - ECOCHEP -":
                    colorHex = "#E74C3C"; 
                    break;
                case "00046 - CEPAS ARGENTINAS S A":
                    colorHex = "#3498DB"; 
                    break;
                case "00016 - MOLINOS TRES ARROYOS S.A":
                    colorHex = "#2ECC71";
                    break;
                case "NULL":
                    colorHex = "#9B59B6";
                    break;
                case "00044 - DULZURAS EL SUR S.R.L.":
                    colorHex = "#F1C40F";
                    break;
                case "00008 - PENAFLOR":
                    colorHex = "#E67E22";
                    break;
                case "00051 - CEMIBA MEDIC.LAB.":
                    colorHex = "#1ABC9C";
                    break;
                case "00035 - MOLINO CANUELAS S.A.":
                    colorHex = "#34495E";
                    break;
                case "00614 - LLAVANERES SRL":
                    colorHex = "#D35400";
                    break;
                case "00285 - YOUNG S.R.L.":
                    colorHex = "#7F8C8D";
                    break;
                case "00656 - CASSINI Y CESARATTO":
                    colorHex = "#C0392B";
                    break;
                case "00566 - DOS HERMANOS S.A.":
                    colorHex = "#8E44AD";
                    break;
                case "00673 - DIA ARGENTINA S.A.":
                    colorHex = "#2980B9";
                    break;
                case "00001 - UNILEVER - FOODS":
                    colorHex = "#27AE60";
                    break;
                case "00602 - LEONCIO ARIZU SA":
                    colorHex = "#16A085";
                    break;
                case "00002 - UNILEVER BPC - BEAUTY & PERSONAL CARE":
                    colorHex = "#F39C12";
                    break;
                case "00635 - CONSULTORA UMAMI SRL":
                    colorHex = "#FF00FF";
                    break;
                case "00617 - AGGGROUP SOLUCIONES TACTICAS SRL -PROFUGO-":
                    colorHex = "#00FFFF";
                    break;
                case "00015 - UNILEVER HC - HOME CARE":
                    colorHex = "#000080";
                    break;
                case "00591 - COSMOPOLITAN S.A.":
                    colorHex = "#808000";
                    break;
                case "00695 - NECHO S A":
                    colorHex = "#FFC0CB";
                    break;
                case "00593 - BODEGAS SALENTEIN SA":
                    colorHex = "#FFD700";
                    break;
                case "00004 - ESTABLEC. LAS MARIAS S.A.":
                    colorHex = "#4B0082";
                    break;
                case "00043 - AEROSOLES HECSPI SACIFI Y DE M Y S":
                    colorHex = "#FF4500";
                    break;
                case "00045 - BODEGAS CUVILLIER S.A":
                    colorHex = "#DA70D6";
                    break;
                case "00647 - BONAFIDE S.A.I Y C":
                    colorHex = "#00CED1";
                    break;
                case "00691 - BUYANOR S.A.":
                    colorHex = "#F08080";
                    break;
                case "00689 - DELLEPIANE SAN LUIS SA":
                    colorHex = "#5F9EA0";
                    break;
                case "00693 - PILARES COMPA?IA ALIMENTICIA SA":
                    colorHex = "#BDB76B";
                    break;
                case "00686 - SOFTYS ARGENTINA SA":
                    colorHex = "#4682B4";
                    break;
                case "00661 - DASBURG S.A. - (SECCO) -":
                    colorHex = "#CD5C5C";
                    break;
                case "00003 - QUIMICA ESTRELLA":
                    colorHex = "#40E0D0"; 
                    break;
                case "00610 - GEORGALOS HERMANOS SAICA":
                    colorHex = "#FF1493"; 
                    break;
                case "00005 - AGROCOM S.A.C.I.I. Y F.":
                    colorHex = "#228B22"; 
                    break;
                case "00660 - PIER DESCUENTOS":
                    colorHex = "#8B4513"; 
                    break;
                case "00598 - PARALLEL S.A.":
                    colorHex = "#708090"; 
                    break;

                default:
                    colorHex = "#95A5A6"; 
                    break;
            }

            if (data.getNode() != null) {
                data.getNode().setStyle("-fx-pie-color: " + colorHex + ";");
            }
        }

    }

}
