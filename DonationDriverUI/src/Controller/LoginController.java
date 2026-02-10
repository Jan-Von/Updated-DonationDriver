package Controller;
import View.LoginView;
import View.RegistrationView;
import View.DashboardView;
import Network.Client;
import javax.swing.*;
import java.io.IOException;

public class LoginController {

    private LoginView view;
    public static String currentUserEmail;

    public LoginController(LoginView view) {
        this.view = view;

        // Login button Function
        view.loginBtn.addActionListener(e -> login());

        // Sign up button Function
        view.signupBtn.addActionListener(e -> {
            view.frame.dispose();
            RegistrationView regView = new RegistrationView();
            new RegistrationController(regView);
        });
    }

    private void login() {
        String email = view.emailField.getText();
        String password = new String(view.passField.getPassword());

            try {
                Client client = Client.getDefault();
                String responseXml = client.login(email, password);
                Client.Response response = Client.parseResponse(responseXml);

                if (response != null && response.isOk()) {
                    currentUserEmail = email;

                    JOptionPane.showMessageDialog(view.frame, "Login Success!");
                    DashboardView dashboardView = new DashboardView();
                    new DashboardController(dashboardView);
                    view.frame.dispose();
                } else {
                    String msg = (response != null && response.message != null && !response.message.isEmpty())
                            ? response.message
                            : "Invalid email or password!";
                    JOptionPane.showMessageDialog(view.frame, msg);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view.frame,
                        "Unable to contact server. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }