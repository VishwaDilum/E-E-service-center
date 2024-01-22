package controller.adminController;

import Bo.custom.Impl.OrderBoImpl;
import Bo.custom.OrderBo;
import Dto.chartDto.chartDto;
import com.jfoenix.controls.JFXButton;
import entity.Orderz;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.time.format.DateTimeFormatter.*;

public class ChartReportsController {



    @FXML
    private AnchorPane pane;

    private HBox chartContainer; // HBox to hold line charts

    OrderBo orderBo = new OrderBoImpl();
    int[] monthArray = new int[12];
    int[] dayArray = new int[31]; // Assuming max 31 days in a month
    int[] yearArray = new int[10]; // Assuming max 10 years (adjust as needed)

    public void initialize() throws SQLException, ClassNotFoundException {
        chartContainer = new HBox(); // Initialize chartContainer here
        chartContainer.setSpacing(10); // Adjust spacing between charts if needed
        pane.getChildren().add(chartContainer);
        List<Orderz> orderzList = orderBo.allOrders();

        for (Orderz orderz : orderzList) {
            Date orderDate = orderz.getOrder_Date();
            LocalDateTime dateTime = orderDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime now = LocalDateTime.now();
            String yyyy =ofPattern("yyyy").format(now);
            int yearAsInteger = Integer.parseInt(yyyy);

            if(yearAsInteger ==dateTime.getYear()) {

                int monthValue = dateTime.getMonthValue();
                monthArray[monthValue - 1]++;

                int dayOfMonth = dateTime.getDayOfMonth();
                dayArray[dayOfMonth - 1]++;
            }
            int year = dateTime.getYear();
            if (year >= 2015 && year <= 2024) {
                yearArray[year - 2015]++;
            }
        }
        generateDailyReport();
    }


    private void generateDailyReport() throws SQLException, ClassNotFoundException {
        //createLineChart(dayArray, "Daily Report", "Day", 30);
        createLineChartDaily(dayArray, "Daily Report", "Day", 30);
    }

    private void generateMonthlyReport() throws SQLException, ClassNotFoundException {
        createLineChart(monthArray, "Monthly Report", "Month", 12); // Assuming 30 orders per month for simplicity
    }

    private void generateAnnualReport() throws SQLException, ClassNotFoundException {
        //createLineChart(yearArray, "Annual Report", "Year", 365); // Assuming 365 orders per year for simplicity
        //createLineChartAnnually(yearArray, "Annual Report", "Year", 365);
        createLineChartAnnually(yearArray, "Annual Report", "Year", 1);
    }

    private void createLineChart(int[] data, String title, String xAxisLabel, int interval) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xAxisLabel);

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle(title);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("My portfolio");
        int xValue = 1;

        // Iterate over the data array
        for (int i = 0;data.length>i;i++) {
            series.getData().add(new XYChart.Data<>(i, data[i]));
            //xValue += interval;
        }

        lineChart.getData().add(series);
        lineChart.setPrefSize(800, 600);
        chartContainer.getChildren().add(lineChart);
    }
    private void createLineChartAnnually(int[] data, String title, String xAxisLabel, int interval) {
        NumberAxis xAxis = new NumberAxis(2015, 2027, 1);
        xAxis.setLabel("Years");

        NumberAxis yAxis = new NumberAxis(0, 350, 50);
        yAxis.setLabel("No.of sales");

        LineChart linechart = new LineChart(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        series.setName("No of sales in an year");

        for(int i = 0;data.length>i;i++){
            series.getData().add(new XYChart.Data((i+2015),data[i]));
        }
        linechart.getData().add(series);
        linechart.setPrefSize(800, 600);
        chartContainer.getChildren().add(linechart);
    }

    private void createLineChartDaily(int[] data, String title, String xAxisLabel, int interval) {
        NumberAxis xAxis = new NumberAxis(1, 31, 1);
        xAxis.setLabel("Daily");

        NumberAxis yAxis = new NumberAxis(0, 20, 1);
        yAxis.setLabel("No.of sales");

        LineChart linechart = new LineChart(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        series.setName("No of sales in an daily");

        for(int i = 0;data.length>i;i++){
            series.getData().add(new XYChart.Data(i,data[i]));
        }
        linechart.getData().add(series);
        linechart.setPrefSize(800, 600);
        chartContainer.getChildren().add(linechart);
    }



    public void annualy(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clearCharts(); // Clear existing charts before adding new ones
        generateAnnualReport();
    }

    public void daily(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clearCharts();
        generateDailyReport();
    }

    public void monthly(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        clearCharts();
        generateMonthlyReport();
    }

    private void clearCharts() {
        chartContainer.getChildren().clear(); // Remove existing charts from HBox
    }

    public void backOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/adminView/adminpieChart.fxml"))));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
