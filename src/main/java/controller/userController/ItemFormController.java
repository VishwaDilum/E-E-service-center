package controller.userController;

import Bo.custom.Impl.ItemBoImpl;
import Bo.custom.ItemBo;
import Dto.tm.itemTm;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import entity.Item;
import javafx.beans.property.ReadOnlyObjectProperty;
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
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ItemFormController {

    public javafx.scene.image.ImageView ImageView;
    public AnchorPane img_pane;
    public TableView tblItem;
    public TableColumn colName;
    public TableColumn colQty;
    public TableColumn colBtn;
    public javafx.scene.image.ImageView img_view;
    public JFXTextField txtQty;
    public AnchorPane pane;

    @FXML
    private JFXComboBox comCategory;

    File absoluteFile;
    @FXML
    private Label labelCategory;

    @FXML
    private JFXTextField txtItemname;
    ItemBo itemBo = new ItemBoImpl();
    public void initialize() throws SQLException, ClassNotFoundException {
        comCategory.getItems().addAll("Electronic","Electrical");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colBtn.setCellValueFactory(new PropertyValueFactory<>("btn"));

        loadItemTable();

        tblItem.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            setData((itemTm)newValue);
        });

    }

    private void setData(itemTm newValue) {
        String productImg = "File:"+newValue.getImg();
        Image image = new Image(productImg, 120, 127, false, true);
        img_view.setImage(image);
        txtItemname.setText(newValue.getName());
        labelCategory.setText(newValue.getItem_category());
        txtQty.setText(String.valueOf(newValue.getQty()));
    }

    public void importOnAction(ActionEvent actionEvent) {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Img File","*png","*jpg"));
        File file = openFile.showOpenDialog(img_pane.getScene().getWindow());
        if(file!=null){
            absoluteFile = file.getAbsoluteFile();
            Image image = new Image(file.toURI().toString(), 120, 127, false, true);
            ImageView.setImage(image);
            System.out.println(absoluteFile);
        }
    }

    public void saveOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String text = txtItemname.getText();
        ReadOnlyObjectProperty readOnlyObjectProperty = comCategory.getSelectionModel().selectedItemProperty();
        Object values = readOnlyObjectProperty.getValue();
        String path = absoluteFile.getPath();
        String replace = path.replace("\\", "\\\\");
        String value = (String)values;
        Item item = new Item(text,value,3,replace);
        boolean isSavaed = itemBo.save(item);
        if(isSavaed){
            new Alert(Alert.AlertType.INFORMATION,"Customer Saved!").show();
            loadItemTable();
        }else{
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }
    private void loadItemTable() throws SQLException, ClassNotFoundException {
        txtQty.setText(null);
        txtItemname.setText(null);
        labelCategory.setText(null);
        img_view.setImage(null);
        ImageView.setImage(null);
        ObservableList<itemTm> tmList = FXCollections.observableArrayList();

        List<Item> dtoList = itemBo.allCustomers();

        for (Item dto:dtoList) {
            JFXButton btn = new JFXButton("Delete");

            itemTm c = new itemTm(
                    dto.getProduct_Name(),
                    dto.getProduct_Img(),
                    dto.getProduct_Category(),
                    dto.getQTY_ON_Hand(),
                    btn
            );

            btn.setOnAction(actionEvent -> {
                try {
                    itemBo.deleteCustomer(c.getName());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });

            tmList.add(c);
        }

        tblItem.setItems(tmList);
    }


    public void updateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String path = absoluteFile.getPath();
        String replace = path.replace("\\", "\\\\");
        String itemnameText = txtItemname.getText();
        Object value = comCategory.getSelectionModel().selectedItemProperty().getValue();
        String values = (String)value;
        System.out.println(values);
        String qtyText = txtQty.getText();
        int quantity = Integer.parseInt(qtyText);
        Item item = new Item(itemnameText,values,quantity,replace);

        boolean isUpdated = itemBo.update(item);
        if(isUpdated) {
            new Alert(Alert.AlertType.INFORMATION, "Item Updated!").show();
            loadItemTable();
        }else {
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }

    }

    public void relaodOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        txtQty.setText(null);
        txtItemname.setText(null);
        labelCategory.setText(null);
        img_view.setImage(null);
        ImageView.setImage(null);
        loadItemTable();
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
