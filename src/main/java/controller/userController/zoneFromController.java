package controller.userController;

import Bo.custom.Impl.OrderBoImpl;
import Bo.custom.OrderBo;
import Dto.tm.orderTm;
import Dto.tm.orderZoneTm;
import com.jfoenix.controls.JFXComboBox;
import entity.Orderz;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class zoneFromController {

    public TableColumn colOrderID;
    public TableColumn colName;
    public TableColumn colItem;

    public TableView orderTable;
    OrderBo orderBo = new OrderBoImpl();
    List<Orderz> orderzs = orderBo.allOrders();

    static LocalDate startLocalDate;

    List<Orderz> orange = new ArrayList<>();
    List<Orderz> red = new ArrayList<>();
    List<Orderz> yellow = new ArrayList<>();
    List<Orderz> green = new ArrayList<>();
    long daysDifference;
    LocalDate currentDate;

    ObservableList<orderZoneTm> tmListorange = FXCollections.observableArrayList();
    ObservableList<orderZoneTm> tmListred = FXCollections.observableArrayList();
    ObservableList<orderZoneTm> tmListyellow = FXCollections.observableArrayList();
    ObservableList<orderZoneTm> tmListgreen = FXCollections.observableArrayList();

    public zoneFromController() throws SQLException, ClassNotFoundException {

        currentDate = LocalDate.now();
        for (Orderz order : orderzs) {

            daysDifference = calculateDaysDifference(order.getOrder_Date(), currentDate);
            System.out.println(daysDifference);
            if ("Pending".equalsIgnoreCase(order.getOrder_Status())) {
                if (daysDifference > 10) {
                    red.add(order);
                } else if (daysDifference > 5) {
                    System.out.println(order.getOrder_ID());
                    orange.add(order);
                }
            } else if ("Processing".equalsIgnoreCase(order.getOrder_Status())) {
                yellow.add(order);
            } else if ("Complete".equalsIgnoreCase(order.getOrder_Status())) {
                green.add(order);
            }
        }
        System.out.println("Red :-\t"+orange.size()+"\t");
    }

    private static long calculateDaysDifference(Date startDate, LocalDate endDate) {
        startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return endDate.toEpochDay() - startLocalDate.toEpochDay();
    }


    public void initialize() {
        loadOrders();
        colName.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
        colItem.setCellValueFactory(new PropertyValueFactory<>("Item_Name"));
        colOrderID.setCellValueFactory(new PropertyValueFactory<>("Order_ID"));

        orderTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            setData((Orderz)newValue);
        });
    }

    private void loadOrders() {
        for (Orderz orderz :orange) {
            orderZoneTm orderZoneTm = new orderZoneTm(orderz.getOrder_ID(),orderz.getCustomer().getCustomer_Name(),orderz.getItem_Name());
            tmListorange.add(orderZoneTm);
        }
        for (Orderz orderz :red) {
            orderZoneTm orderZoneTm = new orderZoneTm(orderz.getOrder_ID(),orderz.getCustomer().getCustomer_Name(),orderz.getItem_Name());
            tmListred.add(orderZoneTm);
        }
        for (Orderz orderz :yellow) {
            orderZoneTm orderZoneTm = new orderZoneTm(orderz.getOrder_ID(),orderz.getCustomer().getCustomer_Name(),orderz.getItem_Name());
            tmListyellow.add(orderZoneTm);
        }
        for (Orderz orderz :green) {
            orderZoneTm orderZoneTm = new orderZoneTm(orderz.getOrder_ID(),orderz.getCustomer().getCustomer_Name(),orderz.getItem_Name());
            tmListgreen.add(orderZoneTm);
        }

    }


    private void setData(Orderz newValue) {

    }


    public void greenZoneOnAction(ActionEvent actionEvent) {
        orderTable.setItems(tmListgreen);
    }

    public void yellowZoneOnActio0n(ActionEvent actionEvent) {
        orderTable.setItems(tmListyellow);
    }

    public void redZoneOnAction(ActionEvent actionEvent) {
        orderTable.setItems(tmListred);
    }

    public void orangeZoneOnAction(ActionEvent actionEvent) {
        orderTable.setItems(tmListorange);
    }
}
