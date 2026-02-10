package Admin;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Network.Client;

import java.io.IOException;

public class AdminHomePanel extends JPanel {

    private JLabel shippedValueLabel;
    private JLabel ridersOnlineValueLabel;
    private JLabel ongoingShipmentValueLabel;


    private Runnable onShowNotifications;

    private static final int PAD = 20;
    private static final int GAP = 16;

    public AdminHomePanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(PAD, PAD, PAD, PAD));

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);
        main.add(buildMetricsBar());
        main.add(Box.createVerticalStrut(GAP));
        main.add(buildUrgentDonations());
        main.add(Box.createVerticalStrut(GAP));
        main.add(buildBottomRow());
        main.add(Box.createVerticalGlue());

        add(new JScrollPane(main), BorderLayout.CENTER);
        refreshData();
    }

    private JPanel buildBottomRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, GAP, 0));
        row.setOpaque(false);
        row.add(buildActiveDeliveryPanel());
        row.add(buildNotificationsPanel());
        return row;
    }

    private JPanel buildMetricsBar() {
        JPanel bar = new JPanel(new GridLayout(1, 3, GAP, 0));
        bar.setOpaque(false);

        shippedValueLabel = new JLabel("204,118");
        ridersOnlineValueLabel = new JLabel("512");
        ongoingShipmentValueLabel = new JLabel("1,084");

        JPanel c1 = buildMetricCard("Shipped Donations", shippedValueLabel, new Color(234, 248, 255));
        JPanel c2 = buildMetricCard("Riders Online", ridersOnlineValueLabel, new Color(238, 247, 239));
        JPanel c3 = buildMetricCard("Ongoing Shipment", ongoingShipmentValueLabel, new Color(253, 244, 230));
        c1.setMinimumSize(new Dimension(180, 80));
        c2.setMinimumSize(new Dimension(180, 80));
        c3.setMinimumSize(new Dimension(180, 80));
        bar.add(c1);
        bar.add(c2);
        bar.add(c3);
        return bar;
    }

    private JPanel buildMetricCard(String title, JLabel valueLabel, Color bg) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(bg);
        card.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(90, 90, 90));
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(new Color(20, 35, 100));
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildUrgentDonations() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);

        JLabel header = new JLabel("Urgent Donations");
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setForeground(new Color(20, 35, 100));
        panel.add(header, BorderLayout.NORTH);

        JPanel cards = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        cards.setOpaque(false);

        cards.add(buildUrgentCard("Super Typhoon Halyan", "443,721.00", "1,432", "287", 65,
                "Donations are being collected and routed to affected areas. Relief packs are being assembled."));
        cards.add(buildUrgentCard("6.9-Magnitude in Cebu Ear...", "320,500.00", "890", "156", 58,
                "Monetary and in-kind donations are flowing in. Distribution to evacuation centers is in progress."));
        cards.add(buildUrgentCard("Fire Hits Supermarket in Qu...", "180,200.00", "420", "98", 48,
                "Emergency supplies and cash aid are being coordinated with local responders."));
        JButton addNew = new JButton("+");
        addNew.setPreferredSize(new Dimension(120, 140));
        addNew.setBackground(new Color(20, 35, 100));
        addNew.setForeground(Color.WHITE);
        addNew.setFont(new Font("Arial", Font.PLAIN, 36));
        addNew.setFocusPainted(false);
        addNew.setToolTipText("Add new urgent donation campaign");
        cards.add(addNew);

        panel.add(cards, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildUrgentCard(String title, String monetary, String current, String incoming,
                                   int progressPercent, String whatsHappening) {
        Color cardBg = new Color(246, 249, 255);
        Color borderColor = new Color(200, 210, 230);
        Color hoverBg = new Color(235, 242, 252);
        Color hoverBorder = new Color(20, 35, 100);

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(220, 140));
        card.setBackground(cardBg);
        card.setBorder(new LineBorder(borderColor, 1));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Arial", Font.BOLD, 12));
        t.setForeground(new Color(20, 35, 100));
        card.add(t);
        card.add(Box.createVerticalStrut(4));
        card.add(new JLabel("Monetary Donations: " + monetary));
        card.add(new JLabel("Current Donations: " + current));
        card.add(new JLabel("Incoming Donations: " + incoming));
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(progressPercent);
        bar.setPreferredSize(new Dimension(200, 6));
        card.add(Box.createVerticalStrut(4));
        card.add(bar);

        final String what = whatsHappening;
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(hoverBg);
                card.setBorder(new LineBorder(hoverBorder, 1));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(cardBg);
                card.setBorder(new LineBorder(borderColor, 1));
                card.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                showUrgentDetailDialog(title, monetary, current, incoming, progressPercent, what);
            }
        });

        return card;
    }

    private void showUrgentDetailDialog(String title, String monetary, String current, String incoming,
                                        int progressPercent, String whatsHappening) {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(20, 35, 100));
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(12));

        content.add(new JLabel("Monetary Donations: " + monetary));
        content.add(new JLabel("Current Donations: " + current));
        content.add(new JLabel("Incoming Donations: " + incoming));
        content.add(Box.createVerticalStrut(8));

        JLabel progressLabel = new JLabel("Progress: " + progressPercent + "% of goal");
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        content.add(progressLabel);
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(progressPercent);
        bar.setPreferredSize(new Dimension(280, 8));
        content.add(bar);
        content.add(Box.createVerticalStrut(12));

        JLabel whatLabel = new JLabel("<html>What's happening: " + whatsHappening + "</html>");
        whatLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        whatLabel.setForeground(new Color(70, 70, 70));
        whatLabel.setMaximumSize(new Dimension(320, 80));
        content.add(whatLabel);

        JButton viewNotifBtn = new JButton("View in Notifications");
        viewNotifBtn.setFocusPainted(false);
        JButton closeBtn = new JButton("Close");
        closeBtn.setFocusPainted(false);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.add(viewNotifBtn);
        buttons.add(closeBtn);
        content.add(Box.createVerticalStrut(16));
        content.add(buttons);

        JDialog dialog = new JDialog((Frame) null, "Urgent Donation Details", true);
        dialog.getContentPane().add(content);
        dialog.setSize(380, 320);
        dialog.setLocationRelativeTo(this);

        closeBtn.addActionListener(e -> dialog.dispose());
        viewNotifBtn.addActionListener(e -> {
            dialog.dispose();
            if (onShowNotifications != null) {
                onShowNotifications.run();
            }
        });

        dialog.setVisible(true);
    }

    public void setOnShowNotifications(Runnable onShowNotifications) {
        this.onShowNotifications = onShowNotifications;
    }

    private JPanel buildActiveDeliveryPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(new Color(250, 250, 252));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 235), 1),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Active Delivery");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(new Color(20, 35, 100));
        header.add(title, BorderLayout.WEST);
        JButton viewAll = new JButton("View All");
        viewAll.setFocusPainted(false);
        viewAll.setFont(new Font("Arial", Font.PLAIN, 12));
        header.add(viewAll, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout(8, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        card.add(new JLabel("J&T Express"), BorderLayout.NORTH);
        card.add(new JLabel("Baguio City - Tacloban"), BorderLayout.CENTER);
        card.add(new JLabel("Today 9:22 AM"), BorderLayout.CENTER);
        card.add(new JLabel("Donation box left at the sorting facility"), BorderLayout.CENTER);
        JLabel status = new JLabel("In Transit");
        status.setForeground(new Color(200, 120, 0));
        status.setFont(new Font("Arial", Font.BOLD, 12));
        card.add(status, BorderLayout.CENTER);
        JButton viewDetails = new JButton("View Details");
        viewDetails.setFocusPainted(false);
        viewDetails.setFont(new Font("Arial", Font.PLAIN, 12));
        card.add(viewDetails, BorderLayout.SOUTH);
        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildNotificationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(new Color(250, 250, 252));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 235), 1),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Notifications");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(new Color(20, 35, 100));
        header.add(title, BorderLayout.WEST);
        JButton viewAll = new JButton("View All");
        viewAll.setFocusPainted(false);
        viewAll.setFont(new Font("Arial", Font.PLAIN, 12));
        header.add(viewAll, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        list.add(buildNotificationItem("Donation Delivered", "Baguio City - Tacloban", "3:00 PM Feb. 21 Delivered"));
        list.add(Box.createVerticalStrut(8));
        list.add(buildNotificationItem("In Transit", "Baguio City - Tacloban", "Today 9:22 AM"));
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildNotificationItem(String title, String msg, String meta) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        JLabel t = new JLabel(title);
        t.setFont(new Font("Arial", Font.BOLD, 12));
        row.add(t, BorderLayout.NORTH);
        JLabel msgLabel = new JLabel(msg);
        msgLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        row.add(msgLabel, BorderLayout.CENTER);
        JLabel m = new JLabel(meta);
        m.setFont(new Font("Arial", Font.PLAIN, 11));
        m.setForeground(new Color(120, 120, 120));
        row.add(m, BorderLayout.SOUTH);
        return row;
    }

    public void refreshData() {
        Metrics m = loadMetricsFromServer();

        if (shippedValueLabel != null) {
            shippedValueLabel.setText(String.valueOf(m.deliveredCount));
        }
        if (ridersOnlineValueLabel != null) {
            ridersOnlineValueLabel.setText(String.valueOf(m.totalCount));
        }
        if (ongoingShipmentValueLabel != null) {
            ongoingShipmentValueLabel.setText(String.valueOf(m.activeCount));
        }
    }

    private static class Metrics {
        int totalCount;
        int deliveredCount;
        int activeCount;
        int rejectedCount;
    }

    private Metrics loadMetricsFromServer() {
        Metrics m = new Metrics();
        try {
            Client client = Client.getDefault();
            String responseXml = client.readTickets("", null);
            Client.Response response = Client.parseResponse(responseXml);
            if (response == null || !response.isOk()) {
                return m;
            }

            String ticketsXml = response.message;
            if (ticketsXml == null || ticketsXml.isEmpty()) {
                return m;
            }

            int idx = 0;
            while (true) {
                int start = ticketsXml.indexOf("<ticket>", idx);
                if (start < 0) break;
                int end = ticketsXml.indexOf("</ticket>", start);
                if (end < 0) break;

                String ticketXml = ticketsXml.substring(start, end + "</ticket>".length());
                m.totalCount++;

                String status = extractTagValue(ticketXml, "status");
                String isDeleted = extractTagValue(ticketXml, "isDeleted");
                String st = status != null ? status.toUpperCase() : "";
                boolean deleted = "true".equalsIgnoreCase(isDeleted);

                if ("DELIVERED".equals(st)) {
                    m.deliveredCount++;
                } else if ("REJECTED".equals(st) || "CANCELLED".equals(st) || deleted) {
                    m.rejectedCount++;
                } else {
                    m.activeCount++;
                }

                idx = end + "</ticket>".length();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return m;
    }

    private String extractTagValue(String xml, String tag) {
        String open = "<" + tag + ">";
        String close = "</" + tag + ">";
        int i = xml.indexOf(open);
        int j = xml.indexOf(close);
        if (i == -1 || j == -1 || j <= i) {
            return null;
        }
        return xml.substring(i + open.length(), j).trim();
    }
}
