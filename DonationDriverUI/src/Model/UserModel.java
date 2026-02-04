package Model;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class UserModel {

    private String emailField;
    private String passField;

    // Additional registration details
    private String firstName;
    private String lastName;
    private String middleName;
    private String dateOfBirth;
    private String address;
    private String phoneNumber;

    public UserModel(String email, String password) {
        this.emailField = email;
        this.passField = password;
    }

    // Full constructor for registration with personal details
    public UserModel(String firstName,
                     String lastName,
                     String middleName,
                     String dateOfBirth,
                     String address,
                     String phoneNumber,
                     String email,
                     String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.emailField = email;
        this.passField = password;
    }

    // Authenticate user from CSV
    public boolean authenticate() {
        try {
            // Always read from the users.csv inside DonationDriverUI
            Scanner sc = new Scanner(new File("DonationDriverUI/users.csv"));
            while (sc.hasNextLine()) {
                String[] u = sc.nextLine().split(",");
                // Skip header row if present
                if (u.length > 0 && "Email".equalsIgnoreCase(u[0].trim())) {
                    continue;
                }
                if (u.length >= 2 &&
                        u[0].trim().equals(emailField) &&
                        u[1].trim().equals(passField)) {
                    sc.close();
                    return true; // found match
                }
            }
            sc.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false; // not found or error
    }

    // Register user (append to CSV) // Dagdagan pa lahat ng information ng registration
    public boolean register() {
        try (FileWriter fw = new FileWriter("DonationDriverUI/users.csv", true)) {
            // CSV format:
            // Email,Password,FirstName,LastName,MiddleName,DateOfBirth,Address,PhoneNumber
            fw.write(String.join(",",
                    emailField,
                    passField,
                    firstName != null ? firstName : "",
                    lastName != null ? lastName : "",
                    middleName != null ? middleName : "",
                    dateOfBirth != null ? dateOfBirth : "",
                    address != null ? address : "",
                    phoneNumber != null ? phoneNumber : ""));
            fw.write("\n");
            fw.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if an email is already registered
    public boolean emailExists() {
        try {
            Scanner sc = new Scanner(new File("DonationDriverUI/users.csv"));
            while (sc.hasNextLine()) {
                String[] u = sc.nextLine().split(",");
                // Skip header row if present
                if (u.length > 0 && "Email".equalsIgnoreCase(u[0].trim())) {
                    continue;
                }
                if (u.length >= 1 && u[0].trim().equalsIgnoreCase(emailField)) {
                    sc.close();
                    return true;
                }
            }
            sc.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
