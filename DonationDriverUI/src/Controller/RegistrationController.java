package Controller;

import Model.UserModel;
import View.LoginView;
import View.RegistrationView;

import javax.swing.*;

public class RegistrationController {

    private RegistrationView view;

    public RegistrationController(RegistrationView view) {
        this.view = view;

        // Finish button Function
        view.finishButton.addActionListener(e -> registerUser());
    }

    private void registerUser() {

        if (!view.termsCheck.isSelected()) {
            JOptionPane.showMessageDialog(view.frame,"You must agree to the Terms and Agreements to register...");
            return;
        }
        String email = view.emailField.getText();
        String password = view.passwordField.getText();

        UserModel user = new UserModel(email, password);

        if (user.register()) {
            JOptionPane.showMessageDialog(view.frame, "Registration Successful!");
            view.frame.dispose(); // close registration window
            LoginView loginView = new LoginView();
            new LoginController(loginView);
        } else {
            JOptionPane.showMessageDialog(view.frame, "Registration Failed!");
        }
    }
}
