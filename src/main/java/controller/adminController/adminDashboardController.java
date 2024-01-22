package controller.adminController;

import Bo.custom.CustomerBo;
import Bo.custom.Impl.CustomerBoImpl;
import Bo.custom.Impl.ItemBoImpl;
import Bo.custom.Impl.LoginsBoImpl;
import Bo.custom.Impl.OrderBoImpl;
import Bo.custom.ItemBo;
import Bo.custom.LoginsBo;
import Bo.custom.OrderBo;
import DBConnection.DBConnection;
import Dto.Logins;
import Dto.OrderDto;
import Dto.tm.customerTm;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import controller.userController.PieChartController;
import entity.Customer;
import entity.Item;
import entity.Orderz;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class adminDashboardController {

    public JFXTextField txtRegPaasowrd;

    public JFXTextField txtRegEmail;
    public JFXComboBox comItem;
    public TableView customerTable;
    public TableColumn colCustomer;
    public AnchorPane pane;
    public JFXTextField txtSearchOrderReport;
    @FXML
    private JFXComboBox comCategory;

    @FXML
    private JFXTextArea custDescription;

    @FXML
    private JFXTextField custNumber;

    @FXML
    private Label labelOrderID;
    String ID;
    @FXML
    private JFXTextField txtCustCod;
    Date todayWithZeroTime;
    @FXML
    private JFXTextField txtCustEmail;
    LoginsBo loginsBo = new LoginsBoImpl();
    CustomerBo customerBo  = new CustomerBoImpl();
    ItemBo itemBo = new ItemBoImpl();

    List<Item> itemElectrical = new ArrayList<>();
    List<Item> itemElectronic = new ArrayList<>();
    ArrayList<String> itemElectricalArray = new ArrayList<>();
    ArrayList<String>  itemElectronicArray= new ArrayList<>();
    OrderBo orderBo = new OrderBoImpl();
    PieChartController pieChartController = new PieChartController();
    public void initialize() throws ParseException, SQLException, ClassNotFoundException {
        System.out.println("Admin new location whotto");
        pieChartController.PieChartControllers("ss");
        ID = customerBo.generateOrderID();
        labelOrderID.setText(ID);
        loadItem();
        loadCustomer();
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
        customerTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            setData((customerTm) newValue);
        });
        comCategory.getItems().addAll("Electronic", "Electrical");
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        todayWithZeroTime = formatter.parse(formatter.format(today));

        comCategory.setOnAction(event -> {
            String selectedItem = (String) comCategory.getValue();
            if (selectedItem.equals("Electronic")) {
                comItem.getItems().clear(); // Clear existing items
                comItem.getItems().addAll(itemElectronicArray);
            } else if (selectedItem.equals("Electrical")) {
                comItem.getItems().clear(); // Clear existing items
                comItem.getItems().addAll(itemElectricalArray);
            }
        });

    }




    private void setData(customerTm newValue) {
        custNumber.setText(newValue.getCustomer_Number());
        txtCustCod.setText(newValue.getCustomer_Name());
        txtCustEmail.setText(newValue.getCustomer_Email());
    }

    private void loadCustomer() throws SQLException, ClassNotFoundException {
        ObservableList<customerTm> tmList = FXCollections.observableArrayList();

        List<Customer> dtoList = customerBo.allCustomers();

        for (Customer dto:dtoList) {

            customerTm c = new customerTm(
                    dto.getCustomer_Name(),
                    dto.getCustomer_Number(),
                    dto.getCustomer_Email()
            );
            tmList.add(c);
        }

        customerTable.setItems(tmList);
    }

    private void loadItem() throws SQLException, ClassNotFoundException {
        List<Item> itemList = itemBo.allCustomers();

        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getProduct_Category().equals("Electronic")) {
                itemElectronic.add(itemList.get(i));
            } else {
                itemElectrical.add(itemList.get(i));
            }
        }

        for (int i = 0; i < itemElectrical.size(); i++) {
            String productName = itemElectrical.get(i).getProduct_Name();
            itemElectricalArray.add(productName);
        }

        for (int i = 0; i < itemElectronic.size(); i++) {
            String productName = itemElectronic.get(i).getProduct_Name();
            itemElectronicArray.add(productName);
        }
    }
    public void registerUserOnAction(ActionEvent actionEvent) {
        String regEmail = txtRegEmail.getText();
        String regPassword = txtRegPaasowrd.getText();

        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        m.update(regPassword.getBytes());

        byte[] bytes = m.digest();

        StringBuilder s = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        String encryptedPassword = s.toString();
        System.out.println("Plain-text password: " + regPassword);
        System.out.println("Encrypted password using MD5: " + encryptedPassword);
        Logins logins = new Logins(regEmail,encryptedPassword);
        boolean userRegietered = loginsBo.registerUser(logins);

    }

    public void customerReportOnAction(ActionEvent actionEvent) {try {
        JasperDesign design = JRXmlLoader.load("src/main/resources/reports/user/admin/adminCustomer_Report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(design);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    } catch (JRException | ClassNotFoundException | SQLException e) {
        throw new RuntimeException(e);
    }
    }

    public void orderReportOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, JRException {
        String txtSearchOrderReportText = txtSearchOrderReport.getText();
        Connection connection = DBConnection.getInstance().getConnection();
        JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/reports/user/admin/adminCustomer_All_Orders.jrxml");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("P.Customer_Number", txtSearchOrderReportText); // Replace with the actual Order ID from user input

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer.viewReport(jasperPrint, false);
        connection.close();
    }

    public void salesReportOnAction(ActionEvent actionEvent) {

        Stage stage = (Stage) pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/adminView/adminpieChart.fxml"))));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void placeOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String generatedOrderId = customerBo.generateOrderID(); // Replace this with your logic

        String comCategorysValue = (String) comCategory.getValue();
        String comItemValue = (String) comItem.getValue();
        String txtContactNumText = custNumber.getText();
        String txtCustEmailText = txtCustEmail.getText();
        String txtCustNameText = txtCustCod.getText();
        String txtDescText = custDescription.getText();

        Customer customer = new Customer();
        customer.setCustomer_Name(txtCustNameText);
        customer.setCustomer_Email(txtCustEmailText);
        customer.setCustomer_Number(txtContactNumText);


        Orderz orderz = new Orderz();
        orderz.setOrder_ID(generatedOrderId);
        orderz.setOrder_Date(todayWithZeroTime);
        orderz.setOrder_Total(0.0);
        orderz.setOrder_Status("Pending");
        orderz.setItem_Name(comItemValue);
        orderz.setItem_Category(comCategorysValue);
        orderz.setItem_Description(txtDescText);
        orderz.setCustomer(customer);

        OrderDto orderDto = new OrderDto(
                ID,
                txtCustNameText,
                comCategorysValue,
                txtDescText,
                comItemValue,
                todayWithZeroTime,
                "Pending",
                0.0,
                customer
        );
        boolean isOrderSaved = orderBo.save(orderDto);
        if(isOrderSaved){
            new Alert(Alert.AlertType.INFORMATION,"Order Saved !!").show();
        }else {
            new Alert(Alert.AlertType.ERROR,"Somthing went wrong").show();
        }
    }

    public void viewOrdersOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/adminView/adminOrderForm.fxml"))));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewItemOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/adminView/adminitemForm.fxml"))));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
