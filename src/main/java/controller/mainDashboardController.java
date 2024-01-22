package controller;

import Bo.custom.Impl.LoginsBoImpl;
import Bo.custom.Impl.OrderBoImpl;
import Bo.custom.LoginsBo;
import Bo.custom.OrderBo;
import Dao.custom.Impl.LoginsDaoImpl;
import Dao.custom.Impl.OrderDaoImpl;
import Dao.custom.LoginsDao;
import Dao.custom.OrderDao;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class mainDashboardController {


    public AnchorPane pane;
    @FXML
    private JFXTextField txtPassword;

    @FXML
    private JFXTextField txtUserName;
    LoginsBo loginsBo =new LoginsBoImpl();

    String AdminEmail = "admin@gmail.com";
    List<Dto.Logins> list = new ArrayList<>();
    public void initialize(){
        List<Dto.Logins> logins = loginsBo.getAll();

        for (Dto.Logins loginss :logins) {
            list.add(new Dto.Logins(
                    loginss.getEmail(),
                    loginss.getPassword())
            );
        }
    }

    @FXML
    void loginOnAction(ActionEvent event) throws IOException {
        String password = txtPassword.getText();
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        m.update(password.getBytes());

        byte[] bytes = m.digest();

        StringBuilder s = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        String encryptedPassword = s.toString();
        System.out.println("Plain-text password: " + password);
        System.out.println("Encrypted password using MD5: " + encryptedPassword);
        if(txtUserName.getText().equals(AdminEmail)){
            for(int i = 0;list.size()>i;i++){
                if(txtUserName.getText().equals(list.get(i).getEmail()) && list.get(i).getPassword().equals(encryptedPassword)){
                    Stage stage = (Stage) pane.getScene().getWindow();
                    try {
                        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/adminDashboard.fxml"))));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
        for(int i = 0;list.size()>i;i++){
            if(txtUserName.getText().equals(list.get(i).getEmail()) && list.get(i).getPassword().equals(encryptedPassword)){
                Stage stage = (Stage) pane.getScene().getWindow();
                try {
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/userDashboardFrom.fxml"))));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    }

    public void ForgotPassdOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Test.fxml"))));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



