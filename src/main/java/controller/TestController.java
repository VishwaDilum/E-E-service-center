package controller;

import Dao.custom.Impl.RestPasswordDaoImpl;
import Dao.custom.RestPasswordDao;
import Dto.Logins;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import entity.logins;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

public class TestController {
    public JFXTextField txtEnterOtp;
    public AnchorPane pane;
    public JFXTextField emailField;
    public JFXTextField txtNewPassword;
    public JFXButton btnReset;


    @FXML
    private  Button sendOtpButton;
    String otp;
    @FXML
    public void initialize() {
        sendOtpButton.setDisable(true);
        btnReset.setDisable(false);
    }
    RestPasswordDao restPasswordDao = new RestPasswordDaoImpl();
    @FXML
    private void handleSendOtpButtonAction() {
        // Validate email field
        String email = emailField.getText();
        if (email.isEmpty() || !isValidEmail(email)) {
            showAlert("Please enter a valid email address.");
            return;
        }

        // Disable the button to prevent multiple clicks during processing
        sendOtpButton.setDisable(true);

        // Generate and send OTP in the background
        Task<Void> sendOtpTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    // Generate a random 6-digit OTP
                    otp = generateOTP();

                    // Send email
                    sendEmail(email, otp);

                    // Update UI on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        showAlert("Email sent successfully");
                        // Enable the button after email is sent
                        sendOtpButton.setDisable(false);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle any exception during email sending
                    showAlert("Error sending email. Please try again.");
                    // Enable the button after encountering an error
                    sendOtpButton.setDisable(false);
                }

                return null;
            }
        };

        // Start the task on a background thread
        new Thread(sendOtpTask).start();
    }

    private boolean isValidEmail(String email) {
        // Simple email validation (you may use a more sophisticated approach)
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    private String generateOTP() {
        // Generate a random 6-digit OTP
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }

    private void sendEmail(String email, String otp) throws MessagingException {
        // Email properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com"); // Use Gmail SMTP server
        properties.put("mail.smtp.port", "587"); // Use the appropriate port for Gmail
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.ssl.ciphers", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256");



        // Create a session with the email server
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("test.info.200511@gmail.com", "zrsr htkb lgwf ywwy");
            }
        });

        // Create a MimeMessage object
        Message message = new MimeMessage(session);

        // Set the sender and recipient addresses
        message.setFrom(new InternetAddress("test.info.200511@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

        // Set the email subject
        message.setSubject("Password Reset OTP");

        // Set the email content with the OTP
        String emailContent = "Your OTP for password reset is: "+otp;
        message.setText(emailContent);

        // Send the email
        Transport.send(message);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Controller class where you want to transfer data

        // ... other code ...

        public void confirmOnAction(ActionEvent actionEvent) {
            String text = txtEnterOtp.getText();

            if (text.equals(otp)) {
                new Alert(Alert.AlertType.INFORMATION, "Verification Successful").show();
                btnReset.setDisable(false);
                txtEnterOtp.clear();
            } else {
                new Alert(Alert.AlertType.ERROR, "Wrong Otp").show();
            }
        }


    public void resetOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String emailField1Text = emailField.getText();
        String txtNewPasswordText = txtNewPassword.getText();
        String encryptPassword = encryptPassword(txtNewPasswordText);
        logins logins = new logins(emailField1Text,encryptPassword);

        boolean update = restPasswordDao.update(logins);
        if(update){
            emailField.clear();
            txtNewPassword.clear();
            new Alert(Alert.AlertType.INFORMATION, "Update Successful").show();
        }else{
            new Alert(Alert.AlertType.ERROR, "Wrong Otp").show();
        }
    }
    private String encryptPassword(String password){
        String regPassword = password;

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

        return encryptedPassword;
    }
}
