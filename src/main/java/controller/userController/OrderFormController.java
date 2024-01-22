package controller.userController;

import Bo.custom.Impl.OrderBoImpl;
import Bo.custom.OrderBo;
import Dao.Util.HibernateUtil;
import Dao.custom.Impl.OrderDaoImpl;
import Dao.custom.OrderDao;
import Dto.addPartDto;
import Dto.tm.customerTm;
import Dto.tm.orderTm;
import Dto.updateStatusDto;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import entity.Additional_Item;
import entity.Customer;
import entity.Orderz;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class OrderFormController {
    public JFXButton loadElectronic;
    public JFXButton loadElectrical;
    public TableView orderTable;
    public Label labelOrder;
    public AnchorPane pane;
    OrderDao orderDao = new OrderDaoImpl();
    public JFXTextField txtPartofname;
    public JFXTextField txtPartofPrice;
    public TableColumn colOrder_ID;
    public TableColumn colCustName;
    public TableColumn colContactNum;
    public TableColumn colEmail;
    public TableColumn colOrderDesc;
    public TableColumn colStatus;
    public TableColumn colBtn;
    public JFXComboBox comStatus;
    OrderBo orderBo = new OrderBoImpl();
    List<orderTm> allTableItem;
    ObservableList<orderTm> tmListElectronic = FXCollections.observableArrayList();
    String x;
    String selectedStatus;
    public OrderFormController() throws SQLException, ClassNotFoundException {

    }

    public void initialize() throws SQLException, ClassNotFoundException, NullPointerException {
        comStatus.getItems().addAll("Pending", "Complete", "Processing", "Closed");
        comStatus.setValue("Pending"); // Set a default value
        selectedStatus = (String) comStatus.getValue();

        // Add a listener to update selectedStatus when the ComboBox value changes
        comStatus.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedStatus = (String) newValue;
        });

        allTableItem = orderDao.getAllTableItem();
        loadOrders();
        colOrder_ID.setCellValueFactory(new PropertyValueFactory<>("Order_ID"));
        colCustName.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Customer_Email"));
        colContactNum.setCellValueFactory(new PropertyValueFactory<>("Customer_Number"));
        colBtn.setCellValueFactory(new PropertyValueFactory<>("Delete"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("Order_Status"));
        colOrderDesc.setCellValueFactory(new PropertyValueFactory<>("Order_Description"));
        orderTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            setData((orderTm) newValue);
        });
    }


    private void setData(orderTm newValue) {
        labelOrder.setText(newValue.getOrder_ID());
        x = newValue.getOrder_ID();
    }

    public void refreshOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

    }

    public void addpartOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Orderz d002 = orderBo.getOrder(x);
        String partofnameText = txtPartofname.getText();
        double partofPriceText = Double.parseDouble(txtPartofPrice.getText());
        Additional_Item additionalItem = new Additional_Item(partofnameText,d002,partofPriceText);

        System.out.println(additionalItem.getOrderID()+"\t"+additionalItem.getCost_of_Part()+"\t"+additionalItem.getAdditional_item().getOrder_ID());

        boolean added = orderDao.saveAddItem(additionalItem);
        if(added){
            new Alert(Alert.AlertType.INFORMATION,"Item add success !!").show();
        }else {
            new Alert(Alert.AlertType.ERROR,"Somthing went wrong").show();
        }
    }


    public void updateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        System.out.println("heuichqwucvhqwc :-"+selectedStatus);
        updateStatusDto updateStatusDto = new updateStatusDto();
        updateStatusDto.setOrder_ID(x);
        updateStatusDto.setStatus(selectedStatus);
        boolean statusUpdated = orderBo.updateStatus(updateStatusDto);
        if(statusUpdated){
            new Alert(Alert.AlertType.INFORMATION,"Item add success !!").show();
        }else {
            new Alert(Alert.AlertType.ERROR,"Somthing went wrong").show();
        }
    }

    private void loadOrders() {
        for (orderTm dto:allTableItem) {
            JFXButton btn = new JFXButton("Delete");
            orderTm c = new orderTm(
                    dto.getOrder_ID(),
                    dto.getCustomer_Name() ,
                    dto.getCustomer_Number(),
                    dto.getCustomer_Email(),
                    dto.getOrder_Description(),
                    dto.getOrder_Status(),
                    btn
            );
            tmListElectronic.add(c);
            btn.setOnAction(actionEvent -> {
                deleteCustomer(c.getOrder_ID());
            });
        }
        orderTable.setItems(tmListElectronic);
        //orderTable.setItems(tmListElectrical);
    }

    private void deleteCustomer(String orderId) {
        try {
            boolean isDeleted = orderBo.deleteCustomer(orderId);
            if (isDeleted){
                new Alert(Alert.AlertType.INFORMATION,"Customer Deleted!").show();
                //loadOrders();
            }else{
                new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void backOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/userDashboardFrom.fxml"))));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
