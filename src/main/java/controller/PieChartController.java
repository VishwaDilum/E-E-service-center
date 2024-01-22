package controller;

import Bo.custom.Impl.OrderBoImpl;
import Bo.custom.OrderBo;
import entity.Orderz;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static java.time.format.DateTimeFormatter.ofPattern;

public class PieChartController {

    @FXML
    private AnchorPane pane;

    @FXML
    private PieChart pieChart;
    int[] monthArray = new int[12];
    int[] dayArray = new int[31]; // Assuming max 31 days in a month
    int[] yearArray = new int[10]; // Assuming max 10 years (adjust as needed)
    OrderBo orderBo = new OrderBoImpl();
    String ss;

    public void PieChartControllers(String ss) {
        this.ss = ss;
    }


    public void initialize() throws SQLException, ClassNotFoundException {
        List<Orderz> orderzList = orderBo.allOrders();

        for (Orderz orderz : orderzList) {
            Date orderDate = orderz.getOrder_Date();
            LocalDateTime dateTime = orderDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime now = LocalDateTime.now();
            String yyyy = ofPattern("yyyy").format(now);
            int yearAsInteger = Integer.parseInt(yyyy);

            if (yearAsInteger == dateTime.getYear()) {
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

        // Set up initial PieChart based on dayArray
        createDaily(dayArray, "Daily Chart");
    }

    private void createDaily(int[] data, String title) {
        int totalDays = 31; // Assuming max 31 days in a month

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (int i = 0; i < data.length; i++) {
            int value = data[i];
            if (value > 0) { // Skip days with zero values
                int percentage = calculatePercentage(value, totalDays);
                pieChartData.add(new PieChart.Data("Day " + (i + 1), percentage));
            }
        }

        pieChart.setData(pieChartData);
    }
    private void createAnnually(int[] data, String monthlyChart) {
        int totalDays = 31; // Assuming max 31 days in a month
        String[] years = new String[]{"2015","2016","2017","2018","2019","2020","2021","2022","2023","2024","2025"};
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (int i = 0; i < data.length; i++) {
            int value = data[i];
            if (value > 0) { // Skip days with zero values
                int percentage = calculatePercentage(value, totalDays);
                pieChartData.add(new PieChart.Data(years[i], percentage));
            }
        }

        pieChart.setData(pieChartData);
    }

    private void createMonthly(int[] data, String monthlyChart) {
        int totalDays = 12; // Assuming max 31 days in a month
        String[] months = new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (int i = 0; i < data.length; i++) {
            int value = data[i];
            if (value > 0) { // Skip days with zero values
                int percentage = calculatePercentage(value, totalDays);
                pieChartData.add(new PieChart.Data(months[i], percentage));
            }
        }

        pieChart.setData(pieChartData);
    }


    public int calculatePercentage(int value, int total) {
        return value * 100 / total;
    }

    public void dailyOnAction(ActionEvent actionEvent) {
        createDaily(dayArray, "Daily Chart");
    }

    public void monthlyOnAction(ActionEvent actionEvent) {
        createMonthly(monthArray, "Monthly Chart");
    }
    public void annualyOnAction(ActionEvent actionEvent) {
        createAnnually(yearArray, "Monthly Chart");
    }


    public void lineChartOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/chartReports.fxml"))));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backOnAction(ActionEvent actionEvent) {
        System.out.println(ss + " Clicked");
        if ("Admin".equals(ss)) {
            System.out.println(ss + " Condition done");
            Stage stage = (Stage) pane.getScene().getWindow();
            try {
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/adminDashboard.fxml"))));
                stage.centerOnScreen();
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
