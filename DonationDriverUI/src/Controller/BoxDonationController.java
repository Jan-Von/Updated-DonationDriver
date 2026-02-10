package Controller;
import View.*;
import View.*;
import Network.Client;
import javax.swing.*;
import java.io.IOException;

public class BoxDonationController {

    private BoxDonationView view;
    private MonetaryDonationView view1;

    public BoxDonationController(BoxDonationView view) {
        this.view = view;

        view.homeBtn.addActionListener(e -> openDashBoard());
        view.monetaryBtn.addActionListener(e -> openMonetaryDonation());
        view.donateNow.addActionListener(e -> handleDonateNow());
        view.notifBtn.addActionListener(e -> openNotification());
        view.donationBtn.addActionListener(e ->openDonations());
        view.DonateBtn.addActionListener(e -> openDonate());

    }

    private void openDashBoard(){
        DashboardView dashboardView = new DashboardView();
        new DashboardController(dashboardView);
        dashboardView.frame.setVisible(true);
        view.frame.dispose();
    }
    private void openMonetaryDonation(){
        MonetaryDonationView monetaryDonationView = new MonetaryDonationView();
        new MonetaryDonationController(monetaryDonationView);
        monetaryDonationView.frame.setVisible(true);
        view.frame.dispose();
    }

    private void openSuccessDonation(){
        SuccessDonationView successDonationView = new SuccessDonationView();
        new SuccessDonationController(successDonationView);
        successDonationView.frame.setVisible(true);
        view.frame.dispose();
    }

    private void handleDonateNow() {

        String goods = view.typeOfGoodsField.getText().trim();
        String boxesText = view.numberOfBoxesField.getText().trim();
        String location = view.locationField.getText().trim();

        if (goods.isEmpty() || boxesText.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(view.frame,
                    "Please fill in Type of Goods, Number of Boxes, and Your Location.",
                    "Create Donation Ticket",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(boxesText);
            if (quantity <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view.frame,
                    "Number of Boxes must be a positive whole number.",
                    "Create Donation Ticket",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userId = (LoginController.currentUserEmail != null && !LoginController.currentUserEmail.isEmpty())
                ? LoginController.currentUserEmail
                : "guest@donationdriver";

        try {
            Client client = Client.getDefault();

            String responseXml = client.createTicket(
                    userId,
                    goods,                  // itemCategory
                    quantity,               // quantity
                    "good condition",       // condition
                    "",                     // expirationDate
                    "",                     // pickupDateTime
                    location,               // pickupLocation
                    "",                     // photoPath
                    "Box goods donation"
            );

            Client.Response response = Client.parseResponse(responseXml);
            if (response != null && response.isOk()) {
                JOptionPane.showMessageDialog(view.frame,
                        "Donation ticket created!\n" + response.message,
                        "Create Donation Ticket",
                        JOptionPane.INFORMATION_MESSAGE);
                openSuccessDonation();
            } else {
                String msg = (response != null && response.message != null && !response.message.isEmpty())
                        ? response.message
                        : "Failed to create donation ticket.";
                JOptionPane.showMessageDialog(view.frame,
                        msg,
                        "Create Donation Ticket",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view.frame,
                    "Unable to contact server. Please try again.",
                    "Create Donation Ticket",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openNotification(){
        NotificationView notificationView = new NotificationView();
        new NotificationController(notificationView);
        notificationView.frame.setVisible(true);
        view.frame.dispose();
    }

    private void openDonations(){
        DonationsActiveView donationsView = new DonationsActiveView();
        new DonationsActiveController(donationsView);
        donationsView.frame.setVisible(true);
        view.frame.dispose();
    }

    private void openDonate (){
        DonateView donateView = new DonateView();
        new DonateController(donateView);
        donateView.frame.setVisible(true);
        view.frame.dispose();
    }

}
