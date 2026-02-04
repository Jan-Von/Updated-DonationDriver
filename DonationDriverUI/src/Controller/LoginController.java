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
            new RegistrationController(regView);
        });
    }

    private void login() {
        String email = view.emailField.getText().trim();
        String password = new String(view.passField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view.frame, "Please fill in all fields.");
            return;
        }

        UserModel user = new UserModel(email, password);

        if (user.authenticate()) {
            JOptionPane.showMessageDialog(view.frame, "Login Successful!");
            view.frame.dispose(); // Close login
            DashboardView dashView = new DashboardView();
            new DashboardController(dashView); //
        } else {
            JOptionPane.showMessageDialog(view.frame, "Invalid email or password!");
        }
    }

}
