package controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.application.Platform;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class EmailSender {

    @FXML
    private TextField emailField;

    @FXML
    private Button sendOtpButton;

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
                    String otp = generateOTP();

                    // Send email
                    sendEmail(email, otp);

                    // Update UI on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        showAlert("Email sent successfully with OTP: " + otp);
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
        properties.put("mail.smtp.host", "smtp.example.com"); // Use your SMTP server
        properties.put("mail.smtp.port", "587"); // Use the appropriate port

        // Create a session with the email server
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("your_email@example.com", "your_email_password");
            }
        });

        // Create a MimeMessage object
        Message message = new MimeMessage(session);

        // Set the sender and recipient addresses
        message.setFrom(new InternetAddress("your_email@example.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

        // Set the email subject
        message.setSubject("Password Reset OTP");

        // Set the email content with the OTP
        String emailContent = "Your OTP for password reset is: " + otp;
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
}
