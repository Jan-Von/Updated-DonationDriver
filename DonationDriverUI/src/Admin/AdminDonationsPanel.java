package Admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import Network.Client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            public boolean isCellEditable(int row, int column) {
                return false;
            }
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

        List<Object[]> rows = loadDonationsFromServer();
        if (rows.isEmpty()) {
            donationsTableModel.addRow(new Object[]{"-", "-", "-", "-", "No data", "-"});
        } else {
            for (Object[] row : rows) {
                donationsTableModel.addRow(row);
            }
        }
    }

    private List<Object[]> loadDonationsFromServer() {
        List<Object[]> rows = new ArrayList<>();
        try {
            Client client = Client.getDefault();
            String responseXml = client.readTickets("", null);
            Client.Response response = Client.parseResponse(responseXml);
            if (response == null || !response.isOk()) {
                return rows;
            }
            String ticketsXml = response.message;
            if (ticketsXml == null || ticketsXml.isEmpty()) {
                return rows;
            }

            int idx = 0;
            while (true) {
                int start = ticketsXml.indexOf("<ticket>", idx);
                if (start < 0) break;
                int end = ticketsXml.indexOf("</ticket>", start);
                if (end < 0) break;

                String ticketXml = ticketsXml.substring(start, end + "</ticket>".length());

                String id = extractTagValue(ticketXml, "ticketId");
                String type = extractTagValue(ticketXml, "itemCategory"); // or drive type
                String donor = extractTagValue(ticketXml, "userId");
                String quantity = extractTagValue(ticketXml, "quantity");
                String status = extractTagValue(ticketXml, "status");
                String createdAt = extractTagValue(ticketXml, "createdAt");

                String amountOrQty = (quantity != null && !quantity.isEmpty())
                        ? quantity + " boxes"
                        : "-";

                rows.add(new Object[]{
                        id != null ? id : "-",
                        type != null ? type : "Goods",
                        donor != null ? donor : "-",
                        amountOrQty,
                        status != null ? status : "-",
                        createdAt != null ? createdAt : "-"
                });

                idx = end + "</ticket>".length();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
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
