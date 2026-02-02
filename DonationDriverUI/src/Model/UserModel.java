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
    public boolean authenticate() {
        try {
            Scanner sc = new Scanner(new File("users.csv"));
            while (sc.hasNextLine()) {
                String[] u = sc.nextLine().split(",");
                if (u[0].trim().equals(emailField) &&
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
