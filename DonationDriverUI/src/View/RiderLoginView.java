package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RiderLoginView extends JFrame {

    public RiderLoginView() {
        setTitle("DonationDriver Rider Login");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2)); // Left and Right panels

        // ================= LEFT PANEL (LOGIN) =================
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(60, 80, 60, 80));

        // Logo
        ImageIcon logoIcon = new ImageIcon("DonationDriverRiderLogo.png");
        JLabel logoLabel = new JLabel(new ImageIcon(
                logoIcon.getImage().getScaledInstance(250, 150, Image.SCALE_SMOOTH)));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(logoLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Email Field
        JTextField emailField = new JTextField();
        styleTextField(emailField, "Email");

        // Password Field
        JPasswordField passwordField = new JPasswordField();
        styleTextField(passwordField, "Password");

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setBackground(new Color(10, 35, 80));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Sign up label
        JLabel signupLabel = new JLabel("Don't have an account? Sign up");
        signupLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        signupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(emailField);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leftPanel.add(passwordField);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(loginButton);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leftPanel.add(signupLabel);

        // ================= RIGHT PANEL (INFO) =================
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(245, 247, 250));
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Rider Image
        ImageIcon riderPicIcon = new ImageIcon("RiderLoginPic.png");
        JLabel riderPicLabel = new JLabel(new ImageIcon(
                riderPicIcon.getImage().getScaledInstance(420, 230, Image.SCALE_SMOOTH)));
        riderPicLabel.setHorizontalAlignment(JLabel.CENTER);

        // Description Text
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setText(
                "As a DonationDriver Rider, you are the essential final link in the chain of "
                        + "\"Accelerated Giving,\" serving as the physical hands that turn a donor's intent "
                        + "into immediate relief.\n\n"
                        + "You are not just delivering a package; you are navigating the critical last mile "
                        + "to ensure transparency for both the giver and receiver through real-time tracking "
                        + "and accountability.\n\n"
                        + "You represent the heartbeat of the donation platform, delivering hope, speed, "
                        + "and reliability to communities in need."
        );
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descriptionArea.setBackground(rightPanel.getBackground());

        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setBorder(null);

        rightPanel.add(riderPicLabel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        add(leftPanel);
        add(rightPanel);
    }

    private void styleTextField(JTextField field, String title) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setBorder(BorderFactory.createTitledBorder(title));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RiderLoginView().setVisible(true));
    }
}

