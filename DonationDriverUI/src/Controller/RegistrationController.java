package Controller;

import Model.UserModel;
import View.LoginView;
import View.RegistrationView;

import javax.swing.*;

public class RegistrationController {

    private final RegistrationView view;

    public RegistrationController(RegistrationView view) {
        this.view = view;
        view.finishButton.addActionListener(e -> register());
    }

    private void register() {
        String firstName = view.firstNameField.getText().trim();
        String lastName = view.lastNameField.getText().trim();
        String middleName = view.middleNameField.getText().trim();
        String dateOfBirth = view.dateOfBirthField.getText().trim();
        String address = view.addressField.getText().trim();
        String phoneNumber = view.phoneNumberField.getText().trim();
        String email = view.emailField.getText().trim();
        String password = view.passwordField.getText();
        String confirmPassword = view.confirmPasswordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view.frame, "Email and password are required.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(view.frame, "Passwords do not match.");
            return;
        }
        if (!view.termsCheck.isSelected()) {
            JOptionPane.showMessageDialog(view.frame, "Please agree to the Terms and Agreements.");
            return;
        }

        UserModel user = new UserModel(
                firstName, lastName, middleName,
                dateOfBirth, address, phoneNumber,
                email, password
        );

        if (user.emailExists()) {
            JOptionPane.showMessageDialog(view.frame, "This email is already registered.");
            return;
        }

        if (user.register()) {
            JOptionPane.showMessageDialog(view.frame, "Registration successful! You can now log in.");
            view.frame.dispose();
            LoginView loginView = new LoginView();
            new LoginController(loginView);
        } else {
            JOptionPane.showMessageDialog(view.frame, "Registration failed. Please try again.");
        }
    }
}
