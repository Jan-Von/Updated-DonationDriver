package Model;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class UserModel {

    private String emailField;
    private String passField;

    public UserModel(String email, String password) {
        this.emailField = email;
        this.passField = password;
    }

    // Authenticate user from CSV
    // Enhanced Authenticate
    public boolean authenticate() {
        File file = new File("users.csv");

        // Check if file exists first to avoid errors
        if (!file.exists()) {
            System.out.println("Error: users.csv not found at " + file.getAbsolutePath());
            return false;
        }

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] u = line.split(",");
                if (u.length >= 2) { // Ensure there are at least 2 columns
                    String storedEmail = u[0].trim();
                    String storedPass = u[1].trim();

                    if (storedEmail.equals(emailField.trim()) &&
                            storedPass.equals(passField.trim())) {
                        return true;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Register user (append to CSV) // Dagdagan pa lahat ng information ng registration
    public boolean register() {
        try (FileWriter fw = new FileWriter("users.csv", true)) {
            fw.write(emailField + "," + passField + "\n");
            fw.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
