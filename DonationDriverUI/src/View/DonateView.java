package View;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DonateView {
    public JFrame frame;
    public JButton notifBtn;
    public JButton donationBtn;
    public JButton DonateBtn;
    public JButton helpBtn;
    public JButton settingsBtn;
    public JButton homeBtn;

    public DonateView() {
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
        donationBtn.setContentAreaFilled(false);
        sidebar.add(donationBtn);

        ImageIcon donation = new ImageIcon("Resources/Images/charity.png");
        scaledImg = donation.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel sidebarDonation = new JLabel(new ImageIcon(scaledImg));
        sidebarDonation.setBounds(30, 145, 25, 25);
        sidebar.add(sidebarDonation);

        DonateBtn = new JButton("Donate");
        DonateBtn.setBounds(57, 190, 110, 40);
        DonateBtn.setBorderPainted(false);
        DonateBtn.setFocusPainted(false);
        DonateBtn.setBackground(Color.lightGray);
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

        JPanel card1 = new JPanel();
        card1.setLayout(null);
        card1.setBounds(300, 100, 400, 250);
        card1.setBackground(new Color(204, 255, 255));
        card1.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        frame.add(card1);

        JLabel card1Title = new JLabel("The Sunflower Center");
        card1Title.setForeground(new Color(32,61,102));
        card1Title.setBounds(20, 20, 250, 20);
        card1Title.setFont(new Font("Arial", Font.BOLD, 24));
        card1.add(card1Title);

        JLabel card1Location = new JLabel("Baguio City, Benguet");
        card1Location.setForeground(new Color(75,104,139));
        card1Location.setBounds(25,45,250,20);
        card1Location.setFont(new Font("Arial", Font.BOLD, 14));
        card1.add(card1Location);

        JLabel card1Text = new JLabel("<html>The Sunflower Center <br> is a professional <br> ministry based in <br> Baguio City focused <br> " +
                "on therapuetiic <br> healing and <br> psychosocial support <br> for childing who have ...</html>");
        card1Text.setForeground(new Color(75,104,139));
        card1Text.setFont(new Font("Arial", Font.PLAIN, 14));
        card1Text.setBounds(255,75,255,150);
        card1.add(card1Text);

        ImageIcon card1photo = new ImageIcon("Resources/Images/card1Photo.png");
        scaledImg = card1photo.getImage().getScaledInstance(225, 150, Image.SCALE_SMOOTH);
        JLabel card1Photo = new JLabel(new ImageIcon(scaledImg));
        card1Photo.setBounds(5, 75, 255, 150);
        card1.add(card1Photo);

        JPanel card2 = new JPanel();
        card2.setLayout(null);
        card2.setBounds(900, 100, 400, 250);
        card2.setBackground(new Color(204, 255, 255));
        card2.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        frame.add(card2);

        JLabel card2Title = new JLabel("The Children's Home of Eucharistic Lo...");
        card2Title.setForeground(new Color(32,61,102));
        card2Title.setBounds(5, 20, 400, 20);
        card2Title.setFont(new Font("Arial", Font.BOLD, 24));
        card2.add(card2Title);

        JLabel card2Location = new JLabel("San Fernando, Pampanga");
        card2Location.setForeground(new Color(75,104,139));
        card2Location.setBounds(25,45,250,20);
        card2Location.setFont(new Font("Arial", Font.BOLD, 14));
        card2.add(card2Location);

        JLabel card2Text = new JLabel("<html>A non profit <br> organization is <br>" +
                " administered by <br> the Missionaries of <br> Eucharistic Love <br> caring children <br> between the age <br> of 5 to 9 years...</html>");
        card2Text.setForeground(new Color(75,104,139));
        card2Text.setFont(new Font("Arial", Font.PLAIN, 14));
        card2Text.setBounds(255,75,255,150);
        card2.add(card2Text);

        ImageIcon card2photo = new ImageIcon("Resources/Images/card2Photo.png");
        scaledImg = card2photo.getImage().getScaledInstance(225, 150, Image.SCALE_SMOOTH);
        JLabel card2Photo = new JLabel(new ImageIcon(scaledImg));
        card2Photo.setBounds(5, 75, 255, 150);
        card2.add(card2Photo);

        JPanel card3 = new JPanel();
        card3.setLayout(null);
        card3.setBounds(300, 400, 400, 250);
        card3.setBackground(new Color(204, 255, 255));
        card3.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        frame.add(card3);

        JLabel card3Title = new JLabel("Maharlika Charity Foundation Inc.");
        card3Title.setForeground(new Color(32,61,102));
        card3Title.setBounds(5, 20, 400, 20);
        card3Title.setFont(new Font("Arial", Font.BOLD, 24));
        card3.add(card3Title);

        JLabel card3Location = new JLabel("Davao City, Davao del Sur");
        card3Location.setForeground(new Color(75,104,139));
        card3Location.setBounds(25,45,250,20);
        card3Location.setFont(new Font("Arial", Font.BOLD, 14));
        card3.add(card3Location);

        JLabel card3Text = new JLabel("<html>As a pioneer among <br> a dedicated group of <br> medical <br> professionals, <br>" +
                " inspiring the younger <br> generation to <br> embrace charity and <br> collaboration in the...</html>");
        card3Text.setForeground(new Color(75,104,139));
        card3Text.setFont(new Font("Arial", Font.PLAIN, 14));
        card3Text.setBounds(255,75,255,150);
        card3.add(card3Text);

        ImageIcon card3photo = new ImageIcon("Resources/Images/card3Photo.png");
        scaledImg = card3photo.getImage().getScaledInstance(225, 150, Image.SCALE_SMOOTH);
        JLabel card3Photo = new JLabel(new ImageIcon(scaledImg));
        card3Photo.setBounds(5, 75, 255, 150);
        card3.add(card3Photo);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new DonateView();
    }
}
