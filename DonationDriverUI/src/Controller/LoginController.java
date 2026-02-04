package Controller;

import Model.UserModel;
import View.LoginView;
import View.RegistrationView;
import View.DashboardView;

import javax.swing.*;

public class LoginController {

    private LoginView view;

    public LoginController(LoginView view) {
        this.view = view;

        // Login button Function
        view.loginBtn.addActionListener(e -> login());

        // Sign up button Function
        view.signupBtn.addActionListener(e -> {
            view.frame.dispose();
            RegistrationView regView = new RegistrationView();
        });
    }

    private void login() {
        String email = view.emailField.getText();
        String password = new String(view.passField.getPassword());

        UserModel user = new UserModel(email, password);

        if (user.authenticate()) {
            view.frame.dispose();
            JOptionPane.showMessageDialog(view.frame, "Login Success!");
            DashboardView dashboardView = new DashboardView();
            new DashboardController(dashboardView);
        } else {
            JOptionPane.showMessageDialog(view.frame, "Invalid email or password!");
        }
    }
}
