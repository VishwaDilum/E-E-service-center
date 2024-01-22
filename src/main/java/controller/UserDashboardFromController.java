
package controller;

import Bo.custom.CustomerBo;
import Bo.custom.Impl.CustomerBoImpl;
import Bo.custom.Impl.ItemBoImpl;
import Bo.custom.Impl.OrderBoImpl;
import Bo.custom.ItemBo;
import Bo.custom.OrderBo;
import Dao.custom.CustomerDao;
import Dao.custom.Impl.CustomerDaoImpl;
import Dao.custom.Impl.OrderDaoImpl;
import Dao.custom.OrderDao;
import Dto.CustomerDto;
import Dto.OrderDto;
import Dto.tm.customerTm;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import DBConnection.DBConnection;
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserDashboardFromController {

    public AnchorPane pane;
    public JFXComboBox comCategorys;
    public Label LabelOrderID;
    public JFXTextField txtnewCustomer;
    public JFXTextField txtNewEmail;
    public TableView customerTable;
    public TableColumn colCustomer;
    public JFXTextField txtSearchCustomer;
    public JFXTextField txtContact;
    public JFXTextField txtSearchOrderReport;
    @FXML
    private JFXButton btnCustomerReport;

    @FXML
    private JFXButton btnImport;

    @FXML
    private JFXButton btnOrderReport;

    @FXML
    private JFXButton btnPlaceOrder;

    @FXML
    private JFXButton btnViewItem;

    @FXML
    private JFXButton btnViewOrder;

    @FXML
    private JFXComboBox comItem;

    @FXML
    private AnchorPane imgPane;

    @FXML
    private JFXTextField txtContactNum;

    @FXML
    private JFXTextField txtCustEmail;

    @FXML
    private JFXTextField txtCustName;

    @FXML
    private JFXTextArea txtDesc;

    @FXML
    private ImageView viewImg;
    Date todayWithZeroTime;
    String ID;
    String hh;
    CustomerDao customerDao = new CustomerDaoImpl();
    OrderDao orderDao =new OrderDaoImpl();

    List<Item> itemElectrical = new ArrayList<>();
    List<Item> itemElectronic = new ArrayList<>();
    ArrayList<String> itemElectricalArray = new ArrayList<>();
    ArrayList<String>  itemElectronicArray= new ArrayList<>();


    ItemBo itemBo = new ItemBoImpl();
    CustomerBo customerBo  = new CustomerBoImpl();
    OrderBo orderBo = new OrderBoImpl();


    public void initialize() throws SQLException, ClassNotFoundException, ParseException {

        loadCustomers();
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
        customerTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            setData((customerTm) newValue);
        });
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();

        todayWithZeroTime = formatter.parse(formatter.format(today));

        loadItem();
        ID = customerBo.generateOrderID();
        LabelOrderID.setText(ID);
        comCategorys.getItems().addAll("Electronic", "Electrical");

        comCategorys.setOnAction(event -> {
            String selectedItem = (String) comCategorys.getValue();
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
        txtCustName.setText(newValue.getCustomer_Name());
        txtCustEmail.setText(newValue.getCustomer_Email());
        txtContactNum.setText(newValue.getCustomer_Number());
    }

    private void loadCustomers() throws SQLException, ClassNotFoundException {
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

    public void viewItemOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/itemForm.fxml"))));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewOrderOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/selectionForm.fxml"))));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void orderReportOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, JRException {
        String txtSearchOrderReportText = txtSearchOrderReport.getText();
            Connection connection = DBConnection.getInstance().getConnection();
            JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/reports/Customer_All_Orders.jrxml");
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("P.Customer_Number", txtSearchOrderReportText); // Replace with the actual Order ID from user input

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            JasperViewer.viewReport(jasperPrint, false);
            connection.close();
        }

    public void customerReportOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load("src/main/resources/reports/Customer_Report.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException | ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void placeOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        String generatedOrderId = customerBo.generateOrderID(); // Replace this with your logic

        String comCategorysValue = (String) comCategorys.getValue();
        String comItemValue = (String) comItem.getValue();
        String txtContactNumText = txtContactNum.getText();
        String txtCustEmailText = txtCustEmail.getText();
        String txtCustNameText = txtCustName.getText();
        String txtDescText = txtDesc.getText();

        Customer customer = new Customer();
        customer.setCustomer_Name(txtCustNameText);
        customer.setCustomer_Email(txtCustEmailText);
        customer.setCustomer_Number(txtContactNumText);


        System.out.println(txtDescText);
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

    public void registerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String txtnewCustomerText = txtnewCustomer.getText();
        String txtNewEmailText = txtNewEmail.getText();
        String txtContactText = txtContact.getText();

        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomer_Email(txtNewEmailText);
        customerDto.setCustomer_Number(txtContactText);
        customerDto.setCustomer_Name(txtnewCustomerText);
        boolean isSaved = customerBo.save(customerDto);
        if(isSaved){
            new Alert(Alert.AlertType.INFORMATION,"Customer Saved !!").show();
        }else {
            new Alert(Alert.AlertType.ERROR,"Somthing went wrong").show();
        }
    }
}
