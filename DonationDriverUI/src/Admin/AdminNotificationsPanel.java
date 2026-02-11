package Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminNotificationsPanel extends JPanel {

    private DefaultTableModel monetaryTableModel;
    private DefaultTableModel donationBoxesTableModel;

    public AdminNotificationsPanel() {
        setLayout(new BorderLayout(16, 16));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTablesPanel(), BorderLayout.CENTER);

        refreshData();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Notifications");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(20, 35, 100));

        JLabel subtitle = new JLabel("Monetary donations and donation box activity");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(new Color(90, 90, 90));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(subtitle);

        header.add(textPanel, BorderLayout.WEST);

        return header;
    }

    private JPanel buildTablesPanel() {
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new GridLayout(2, 1, 0, 16));

        container.add(buildMonetaryDonationsSection());
        container.add(buildDonationBoxesSection());

        return container;
    }

    private JPanel buildMonetaryDonationsSection() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(new Color(250, 250, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("Monetary Donations");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(new Color(20, 35, 100));
        panel.add(title, BorderLayout.NORTH);

        monetaryTableModel = new DefaultTableModel(
                new Object[]{"Name", "Amount", "Reference no.", "Payment Method", "Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(monetaryTableModel);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildDonationBoxesSection() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(new Color(248, 249, 252));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("Donation Boxes");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(new Color(20, 35, 100));
        panel.add(title, BorderLayout.NORTH);

        donationBoxesTableModel = new DefaultTableModel(
                new Object[]{"Name", "Quantity", "Reference no.", "Municipality", "Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(donationBoxesTableModel);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private static final Color TABLE_HEADER_BG = new Color(240, 240, 240);

    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(TABLE_HEADER_BG);
        table.getTableHeader().setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
    }

    public void refreshData() {
        monetaryTableModel.setRowCount(0);
        monetaryTableModel.addRow(new Object[]{"James, LeBron", 10000, "76423964337482798", "Bank", "11/28/2013"});
        monetaryTableModel.addRow(new Object[]{"Curry, Stephen Wardell", 6000, "42341567678845657", "Bank", "11/28/2013"});
        monetaryTableModel.addRow(new Object[]{"Paul, Chris", 13000, "385675327360453878", "Bank", "11/28/2013"});

        donationBoxesTableModel.setRowCount(0);
        donationBoxesTableModel.addRow(new Object[]{"Abdul, Hahaha", 100, "76423964337482798", "La Trinidad", "11/28/2013"});
        donationBoxesTableModel.addRow(new Object[]{"Gina, Kai-In", 20, "42341567678845657", "Baguio", "11/28/2013"});
        donationBoxesTableModel.addRow(new Object[]{"Aquino, Tan La", 60, "385675327360453878", "Nueva Ecija", "11/28/2013"});
    }
}
