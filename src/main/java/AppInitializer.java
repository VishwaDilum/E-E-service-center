import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("view/mainDashboardForm.fxml"))));
        //primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("view/adminDashboard.fxml"))));
        //primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("view/userDashboardFrom.fxml"))));
        //primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("view/itemForm.fxml"))));
        //primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("view/OrderForm.fxml"))));
        //primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("view/zoneFrom.fxml"))));
        //primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/chartReports.fxml"))));
        //primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/pieChart.fxml"))));
        //primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Test.fxml"))));
        //primaryStage.setTitle("Main_Dashboard");
        primaryStage.show();
    }
}
