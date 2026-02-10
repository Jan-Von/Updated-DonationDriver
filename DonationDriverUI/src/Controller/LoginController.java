package Controller;

import Admin.AdminCredentialsStore;
import Admin.AdminDashboardView;
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
            new RegistrationController(regView);
        });
    }

    private void login() {
        String email = view.emailField.getText();
        String password = new String(view.passField.getPassword());

        // Treat placeholder text as empty
        if (email == null || email.trim().isEmpty() || "Email".equals(email)
                || password == null || password.trim().isEmpty() || "Password".equals(password)) {
            JOptionPane.showMessageDialog(view.frame,
                    "Please enter both email and password.",
                    "Login",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // First, check if this is an admin login (from admin_credentials.xml)
        AdminCredentialsStore adminStore = new AdminCredentialsStore();
        if (adminStore.isValidAdmin(email, password)) {
            JOptionPane.showMessageDialog(view.frame, "Admin login successful!");
            new AdminDashboardView();
            view.frame.dispose();
            return;
        }

        // Otherwise, treat as a normal user login
        UserModel user = new UserModel(email, password);

        if (user.authenticate()) {
            JOptionPane.showMessageDialog(view.frame, "Login Success!");
            DashboardView dashboardView = new DashboardView();
            new DashboardController(dashboardView);
            view.frame.dispose();
        } else {
            JOptionPane.showMessageDialog(view.frame, "Invalid email or password!");
        }
    }
}
