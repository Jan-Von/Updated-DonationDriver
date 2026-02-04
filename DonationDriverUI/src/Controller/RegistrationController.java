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
        String firstName = view.firstNameField.getText();
        String lastName = view.lastNameField.getText();
        String middleName = view.middleNameField.getText();
        String dateOfBirth = view.dateOfBirthField.getText();
        String address = view.addressField.getText();
        String phoneNumber = view.phoneNumberField.getText();
        String email = view.emailField.getText();
        String password = view.passwordField.getText();
        String confirmPassword = view.confirmPasswordField.getText();

        // Basic validations – controller handles user input checks,
        // model handles persistence (MVC separation)
        if (firstName == null || firstName.isBlank()
                || lastName == null || lastName.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()
                || confirmPassword == null || confirmPassword.isBlank()) {
            JOptionPane.showMessageDialog(view.frame, "Please fill in all required fields (First Name, Last Name, Email, Password, Confirm Password).");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(view.frame, "Passwords do not match.");
            return;
        }

        UserModel user = new UserModel(
                firstName.trim(),
                lastName.trim(),
                middleName != null ? middleName.trim() : "",
                dateOfBirth != null ? dateOfBirth.trim() : "",
                address != null ? address.trim() : "",
                phoneNumber != null ? phoneNumber.trim() : "",
                email.trim(),
                password
        );

        if (user.emailExists()) {
            JOptionPane.showMessageDialog(view.frame, "An account with this email already exists.");
            return;
        }

        if (user.register()) {
            JOptionPane.showMessageDialog(view.frame, "Registration Successful!");
            view.frame.dispose(); // close registration window
            LoginView loginView = new LoginView();
            new LoginController(loginView);
        } else {
            JOptionPane.showMessageDialog(view.frame, "Registration Failed! Please try again.");
        }
    }
}
