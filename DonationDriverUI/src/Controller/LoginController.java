package Controller;

import Model.UserModel;
import View.DashboardView;
import View.LoginView;
import View.RegistrationView;

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
            new RegistrationController(regView);
        });
    }

    private void login() {
        String email = view.emailField.getText();
        String password = new String(view.passField.getPassword());

        if (email == null || email.isBlank() || "Email".equalsIgnoreCase(email.trim())) {
            JOptionPane.showMessageDialog(view.frame, "Please enter your email.");
            return;
        }

        if (password == null || password.isBlank() || "Password".equalsIgnoreCase(password.trim())) {
            JOptionPane.showMessageDialog(view.frame, "Please enter your password.");
            return;
        }

        UserModel user = new UserModel(email.trim(), password);

        if (user.authenticate()) {
            JOptionPane.showMessageDialog(view.frame, "Login Success!");
            view.frame.dispose();
            new DashboardView();
        } else {
            JOptionPane.showMessageDialog(view.frame, "Invalid email or password!");
        }
    }
}
