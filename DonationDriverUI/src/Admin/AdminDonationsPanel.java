package Admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminDonationsPanel extends JPanel {

    private static final Color TABLE_HEADER_BG = new Color(240, 240, 240);
    private DefaultTableModel donationsTableModel;

    public AdminDonationsPanel() {
        setLayout(new BorderLayout(16, 16));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Donations");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(new Color(20, 35, 100));
        add(title, BorderLayout.NORTH);

        donationsTableModel = new DefaultTableModel(
                new Object[]{"ID", "Type", "Donor", "Amount/Quantity", "Status", "Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(donationsTableModel);
        styleTable(table);
        add(new JScrollPane(table), BorderLayout.CENTER);
        refreshData();
    }

    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(TABLE_HEADER_BG);
        table.getTableHeader().setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
    }

    public void refreshData() {
        if (donationsTableModel == null) return;
        donationsTableModel.setRowCount(0);
        donationsTableModel.addRow(new Object[]{"D001", "Monetary", "James, LeBron", "10,000.00", "Delivered", "02/21/2025"});
        donationsTableModel.addRow(new Object[]{"D002", "Box", "Gina, Kai-In", "20 boxes", "In Transit", "02/20/2025"});
        donationsTableModel.addRow(new Object[]{"D003", "Monetary", "Paul, Chris", "13,000.00", "Pending", "02/19/2025"});
    }
}
