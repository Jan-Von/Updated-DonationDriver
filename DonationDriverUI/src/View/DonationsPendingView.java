package View;

import javax.swing.*;
import java.awt.*;

public class DonationsPendingView{
    public JFrame frame;
    public JButton notifBtn;
    public JButton donationBtn;
    public JButton DonateBtn;
    public JButton helpBtn;
    public JButton settingsBtn;
    public JButton homeBtn;
    public JButton ActiveDeliveryButton;
    public JButton DeliveredButton;
    public JButton PendingButton;
    public JButton RejectedButton;

    public DonationsPendingView() {
        frame = new JFrame("DonationDriver - Dashboard");
        frame.setSize(1400, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.WHITE);
        ImageIcon frameIcon = new ImageIcon("Resources/Images/logoicon.png");
        frame.setIconImage(frameIcon.getImage());

        JPanel header = new JPanel();
        header.setLayout(null);
        header.setBackground(new Color( 245, 245, 245));
        header.setBounds(0, 0, 1400, 80);

        ImageIcon logo = new ImageIcon("Resources/Images/logoicon.png");
        Image logoImg = logo.getImage().getScaledInstance(50, 40, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImg));
        logoLabel.setBounds(20, 18, 50, 40);
        header.add(logoLabel);

        JLabel title = new JLabel("DonationDriver");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBounds(80, 18, 200, 20);
        header.add(title);

        JLabel subtitle = new JLabel("Accelerated Giving");
        subtitle.setFont(new Font("Arial", Font.BOLD, 12));
        subtitle.setForeground(new Color(20, 35, 100));
        subtitle.setBounds(80, 38, 200, 20);
        header.add(subtitle);

        frame.add(header);

        /* ================= SIDEBAR ================= */
        JPanel sidebar = new JPanel();
        sidebar.setLayout(null);
        sidebar.setBackground(new Color(245, 245, 245));
        sidebar.setBounds(0, 80, 200, 720);

        homeBtn = new JButton("Home");
        homeBtn.setBounds(65, 40, 80, 40);
        homeBtn.setContentAreaFilled(false);
        homeBtn.setBorderPainted(false);
        homeBtn.setFocusPainted(false);
        sidebar.add(homeBtn);

        ImageIcon Home = new ImageIcon("Resources/Images/home.png");
        Image scaledImg = Home.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel sidebarHome = new JLabel(new ImageIcon(scaledImg));
        sidebarHome.setBounds(30, 45, 25, 25);
        sidebar.add(sidebarHome);

        notifBtn = new JButton("Notifications");
        notifBtn.setBounds(55, 90, 110, 40);
        notifBtn.setBorderPainted(false);
        notifBtn.setFocusPainted(false);
        notifBtn.setContentAreaFilled(false);
        sidebar.add(notifBtn);

        ImageIcon Notif = new ImageIcon("Resources/Images/notification.png");
        scaledImg = Notif.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel sidebarNotif = new JLabel(new ImageIcon(scaledImg));
        sidebarNotif.setBounds(30, 95, 25, 25);
        sidebar.add(sidebarNotif);

        donationBtn = new JButton("Donations");
        donationBtn.setBounds(55, 140, 110, 40);
        donationBtn.setBorderPainted(false);
        donationBtn.setFocusPainted(false);
        donationBtn.setBackground(Color.lightGray);
        sidebar.add(donationBtn);

        ImageIcon donation = new ImageIcon("Resources/Images/charity.png");
        scaledImg = donation.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel sidebarDonation = new JLabel(new ImageIcon(scaledImg));
        sidebarDonation.setBounds(30, 145, 25, 25);
        sidebar.add(sidebarDonation);

        DonateBtn = new JButton("Donate");
        DonateBtn.setBounds(45, 190, 120, 40);
        DonateBtn.setBorderPainted(false);
        DonateBtn.setFocusPainted(false);
        DonateBtn.setContentAreaFilled(false);
        sidebar.add(DonateBtn);

        ImageIcon donate = new ImageIcon("Resources/Images/heart.png");
        scaledImg = donate.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel sidebarDonate = new JLabel(new ImageIcon(scaledImg));
        sidebarDonate.setBounds(30, 195, 25, 25);
        sidebar.add(sidebarDonate);

        helpBtn = new JButton("Help");
        helpBtn.setBounds(45,550,120,40);
        helpBtn.setBorderPainted(false);
        helpBtn.setFocusPainted(false);
        helpBtn.setContentAreaFilled(false);
        sidebar.add(helpBtn);

        settingsBtn = new JButton("Settings");
        settingsBtn.setBounds(45, 600, 120, 40);
        settingsBtn.setBorderPainted(false);
        settingsBtn.setFocusPainted(false);
        settingsBtn.setContentAreaFilled(false);
        sidebar.add(settingsBtn);

        ImageIcon help = new ImageIcon("Resources/Images/information.png");
        scaledImg = help.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel sidebarHelp = new JLabel(new ImageIcon(scaledImg));
        sidebarHelp.setBounds(27, 555, 30, 30);
        sidebar.add(sidebarHelp);

        ImageIcon setting = new ImageIcon("Resources/Images/settings.png");
        scaledImg = setting.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel sidebarSetting = new JLabel(new ImageIcon(scaledImg));
        sidebarSetting.setBounds(30, 605, 25, 25);
        sidebar.add(sidebarSetting);

        frame.add(sidebar);

        ActiveDeliveryButton = new JButton("Active Delivery");
        ActiveDeliveryButton.setBounds(225, 100, 120, 40);
        ActiveDeliveryButton.setBorderPainted(false);
        ActiveDeliveryButton.setFocusPainted(false);
        ActiveDeliveryButton.setBackground(new Color(245,245,245));
        frame.add(ActiveDeliveryButton);

        PendingButton = new JButton("Pending");
        PendingButton.setBounds(375, 100, 120, 40);
        PendingButton.setBorderPainted(false);
        PendingButton.setFocusPainted(false);
        PendingButton.setBackground(Color.lightGray);
        frame.add(PendingButton);

        DeliveredButton = new JButton("Delivered");
        DeliveredButton.setBounds(525, 100, 120, 40);
        DeliveredButton.setBorderPainted(false);
        DeliveredButton.setFocusPainted(false);
        DeliveredButton.setBackground(new Color(245,245,245));
        frame.add(DeliveredButton);

        RejectedButton = new JButton("Rejected");
        RejectedButton.setBounds(657, 100, 120, 40);
        RejectedButton.setBorderPainted(false);
        RejectedButton.setFocusPainted(false);
        RejectedButton.setBackground(new Color(245,245,245));
        frame.add(RejectedButton);


        JPanel Notif1Card = new JPanel();
        Notif1Card.setLayout(null);
        Notif1Card.setBounds(500, 175, 600, 150);
        Notif1Card.setBackground(new Color(245, 245, 245));
        frame.add(Notif1Card);

        ImageIcon activeDeliveryphoto = new ImageIcon("Resources/Images/ActiveDeliveryPhoto.png");
        scaledImg = activeDeliveryphoto.getImage().getScaledInstance(50, 35, Image.SCALE_SMOOTH);
        JLabel activeDeliveryPhoto = new JLabel(new ImageIcon(scaledImg));
        activeDeliveryPhoto.setBounds(10, 20, 50, 35);
        Notif1Card.add(activeDeliveryPhoto);

        JLabel jntexpress = new JLabel("J&T Express");
        jntexpress.setForeground(Color.BLACK);
        jntexpress.setFont(new Font("Arial", Font.BOLD, 12));
        jntexpress.setBounds(75, 20, 100, 20);
        Notif1Card.add(jntexpress);

        JLabel deliveryLocation = new JLabel("Baguio City - Tacloban");
        deliveryLocation.setForeground(Color.BLACK);
        deliveryLocation.setFont(new Font("Arial", Font.PLAIN, 12));
        deliveryLocation.setBounds(75, 40, 165, 20);
        Notif1Card.add(deliveryLocation);

        JLabel DateToday1 = new JLabel("<html>Today <br> 5:33 AM</html>");
        DateToday1.setForeground(Color.BLACK);
        DateToday1.setFont(new Font("Arial", Font.PLAIN, 14));
        DateToday1.setBounds(150, 70, 165, 35);
        Notif1Card.add(DateToday1);

        JLabel deliveryPlace = new JLabel("<html>Waiting for Pick up</html>");
        deliveryPlace.setForeground(Color.BLACK);
        deliveryPlace.setFont(new Font("Arial", Font.PLAIN, 14));
        deliveryPlace.setBounds(350, 70, 165, 35);
        Notif1Card.add(deliveryPlace);

        JButton seeMore = new JButton("See More");
        seeMore.setForeground(Color.BLACK);
        seeMore.setBackground(Color.LIGHT_GRAY);
        seeMore.setFont(new Font("Arial", Font.PLAIN, 12));
        seeMore.setBounds(200, 115, 150, 25);
        Notif1Card.add(seeMore);

        JLabel transitBox = new JLabel("Pending", SwingConstants.CENTER);
        transitBox.setBounds(480, 20, 100, 35);
        transitBox.setBackground(new Color(255, 255, 138));
        transitBox.setForeground(new Color(194, 194, 0));
        transitBox.setFont(new Font("Arial", Font.BOLD, 14));
        transitBox.setOpaque(true);
        Notif1Card.add(transitBox);


        JPanel Notif2Card = new JPanel();
        Notif2Card.setLayout(null);
        Notif2Card.setBounds(500, 375, 600, 150);
        Notif2Card.setBackground(new Color(245, 245, 245));
        frame.add(Notif2Card);

        ImageIcon activeDeliveryphoto2 = new ImageIcon("Resources/Images/ActiveDeliveryPhoto.png");
        scaledImg = activeDeliveryphoto2.getImage().getScaledInstance(50, 35, Image.SCALE_SMOOTH);
        JLabel activeDeliveryPhoto2 = new JLabel(new ImageIcon(scaledImg));
        activeDeliveryPhoto2.setBounds(10, 20, 50, 35);
        Notif2Card.add(activeDeliveryPhoto2);

        JLabel jntexpress2 = new JLabel("J&T Express");
        jntexpress2.setForeground(Color.BLACK);
        jntexpress2.setFont(new Font("Arial", Font.BOLD, 12));
        jntexpress2.setBounds(75, 20, 100, 20);
        Notif2Card.add(jntexpress2);

        JLabel deliveryLocation2 = new JLabel("Baguio City - Quezon City");
        deliveryLocation2.setForeground(Color.BLACK);
        deliveryLocation2.setFont(new Font("Arial", Font.PLAIN, 12));
        deliveryLocation2.setBounds(75, 40, 165, 20);
        Notif2Card.add(deliveryLocation2);

        JLabel DateToday2 = new JLabel("<html>Today <br> 2:37 PM</html>");
        DateToday2.setForeground(Color.BLACK);
        DateToday2.setFont(new Font("Arial", Font.PLAIN, 14));
        DateToday2.setBounds(150, 70, 165, 35);
        Notif2Card.add(DateToday2);


        JLabel deliveryPlace2 = new JLabel("<html>Donation box received at sorting facility</html>");
        deliveryPlace2.setForeground(Color.BLACK);
        deliveryPlace2.setFont(new Font("Arial", Font.PLAIN, 14));
        deliveryPlace2.setBounds(350, 70, 165, 35);
        Notif2Card.add(deliveryPlace2);

        JButton seeMore2 = new JButton("See More");
        seeMore2.setForeground(Color.BLACK);
        seeMore2.setBackground(Color.LIGHT_GRAY);
        seeMore2.setFont(new Font("Arial", Font.PLAIN, 12));
        seeMore2.setBounds(200, 115, 150, 25);
        Notif2Card.add(seeMore2);

        JLabel transitBox2 = new JLabel("In Transit", SwingConstants.CENTER);
        transitBox2.setBounds(480, 20, 100, 35);
        transitBox2.setBackground(new Color(255, 255, 138));
        transitBox2.setForeground(new Color(194, 194, 0));
        transitBox2.setFont(new Font("Arial", Font.BOLD, 14));
        transitBox2.setOpaque(true);
        Notif2Card.add(transitBox2);


        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new DonationsPendingView();
    }
}
