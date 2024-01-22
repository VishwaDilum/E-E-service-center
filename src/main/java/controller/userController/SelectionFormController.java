package controller.userController;
import Bo.custom.Impl.OrderBoImpl;
import Bo.custom.OrderBo;
import entity.Orderz;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SelectionFormController {
    public AnchorPane pane;
    OrderBo orderBo = new OrderBoImpl();
    List<Orderz> orderzs = orderBo.allOrders();
    List<Orderz> electronic = new ArrayList<>();
    List<Orderz> electrical = new ArrayList<>();

    public void initialize() throws SQLException, ClassNotFoundException {

    }
    boolean clicked;
    public SelectionFormController() throws SQLException, ClassNotFoundException {
        clicked = true;
        for(int i = 0;orderzs.size()>i;i++){
            if(orderzs.get(i).getItem_Category().equals("Electronic")){
                electronic.add(orderzs.get(i));
            }else{
                electrical.add(orderzs.get(i));
            }
        }
        //System.out.println("electronic "+electronic.size());
        //System.out.println("electronic "+electrical.size());
    }



    public void electronicOnAction(ActionEvent actionEvent) {

        Stage stage = (Stage) pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/zoneFrom.fxml"))));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void electricalOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/zoneFrom.fxml"))));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
