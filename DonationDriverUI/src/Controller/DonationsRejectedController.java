package Controller;
import View.*;

import java.awt.event.ActionListener;

public class DonationsRejectedController {

    private DonationsRejectedView view;
    private MonetaryDonationView view1;

    public DonationsRejectedController(DonationsRejectedView view) {
        this.view = view;

        view.homeBtn.addActionListener(e -> openDashBoard());
        view.notifBtn.addActionListener(e -> openNotification());
        view.ActiveDeliveryButton.addActionListener(e -> openDonationsActive());
        view.DeliveredButton.addActionListener(e -> openDonationsDelivered());
    }

    private void openDashBoard(){
        DashboardView dashboardView = new DashboardView();
        new DashboardController(dashboardView);
        dashboardView.frame.setVisible(true);
        view.frame.dispose();
    }

    private void openNotification(){
        NotificationView notificationView = new NotificationView();
        new NotificationController(notificationView);
        notificationView.frame.setVisible(true);
        view.frame.dispose();
    }

    private void openDonationsActive(){
        DonationsActiveView donationsactiveView = new DonationsActiveView();
        new DonationsActiveController(donationsactiveView);
        donationsactiveView.frame.setVisible(true);
        view.frame.dispose();
    }

    private void openDonationsDelivered(){
        DonationsDeliveredView donationsdeliveredView = new DonationsDeliveredView();
        new DonationsDeliveredController(donationsdeliveredView);
        donationsdeliveredView.frame.setVisible(true);
        view.frame.dispose();
    }
}
