package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class LoginView {

    public JFrame frame;
    public JTextField emailField;
    public JPasswordField passField;
    public JButton loginBtn;
    public JButton signupBtn;

    public LoginView() {
        frame = new JFrame("DonationDriver");
        frame.setSize(1350, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon("logoicon.png");
        frame.setIconImage(icon.getImage());

        // Left Panel for Authentication
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(450, 800));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Logo
        ImageIcon originalIcon = new ImageIcon("logo.png");
        Image scaledImg = originalIcon.getImage().getScaledInstance(300, 225, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImg));

        gbc.gridy = 0;
        leftPanel.add(logoLabel, gbc);

        // Email Field
        emailField = new JTextField("Email");
        emailField.setPreferredSize(new Dimension(280, 40));
        emailField.setForeground(Color.GRAY);

        emailField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (emailField.getText().equals("Email")) {
                    emailField.setText("");
                    emailField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setText("Email");
                    emailField.setForeground(Color.GRAY);
                }
            }
        });

        gbc.gridy = 1;
        leftPanel.add(emailField, gbc);

        // Password Field
        passField = new JPasswordField("Password");
        passField.setPreferredSize(new Dimension(280, 40));
        passField.setForeground(Color.GRAY);
        passField.setEchoChar((char) 0); // show text initially

        passField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passField.getPassword()).equals("Password")) {
                    passField.setText("");
                    passField.setForeground(Color.BLACK);
                    passField.setEchoChar('•'); // hide password
                }
            }

            public void focusLost(FocusEvent e) {
                if (passField.getPassword().length == 0) {
                    passField.setText("Password");
                    passField.setForeground(Color.GRAY);
                    passField.setEchoChar((char) 0); // show placeholder
                }
            }
        });

        gbc.gridy = 2;
        leftPanel.add(passField, gbc);

        // Login Button
        loginBtn = new JButton("Login");
        loginBtn.setPreferredSize(new Dimension(280, 45));
        loginBtn.setBackground(new Color(20, 35, 100));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);

        gbc.gridy = 3;
        leftPanel.add(loginBtn, gbc);

        // Sign Up Button
        signupBtn = new JButton("Don't have an account yet? Sign up");
        signupBtn.setBorderPainted(false);
        signupBtn.setContentAreaFilled(false);
        signupBtn.setForeground(Color.GRAY);

        gbc.gridy = 4;
        leftPanel.add(signupBtn, gbc);

        //Right panel News
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(245, 247, 250));
        rightPanel.setLayout(new BorderLayout(20, 20));

        // Big Image
        ImageIcon rightImgIcon = new ImageIcon("LoginImage.png");
        Image rightImg = rightImgIcon.getImage().getScaledInstance(700, 450, Image.SCALE_SMOOTH);
        JLabel rightImageLabel = new JLabel(new ImageIcon(rightImg));
        rightImageLabel.setHorizontalAlignment(JLabel.CENTER);

        rightPanel.add(rightImageLabel, BorderLayout.NORTH);

        // Description Text
        JTextArea description = new JTextArea("A high-transparency logistics platform designed to take the \"guesswork\" out of giving. " +
                "It bridges the gap between generous donors and impactful causes by providing a real-time, end-to-end tracking system for " +
                "every contribution—whether it’s cash, physical goods, or professional services. \n\nThink of it as the \"delivery app for kindness.\" " +
                "Instead of wondering if your donation made a difference, you can watch its journey from the initial transaction to the final delivery, " +
                "ensuring that every bit of help reaches the right hands with total accountability. ");
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setOpaque(false);
        description.setFont(new Font("Arial", Font.BOLD, 16));
        description.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        rightPanel.add(description, BorderLayout.CENTER);
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}