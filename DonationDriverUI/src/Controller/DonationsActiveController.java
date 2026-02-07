package Controller;
import View.*;

public class DonationsActiveController {

    private DonationsActiveView view;
    private MonetaryDonationView view1;

    public DonationsActiveController(DonationsActiveView view) {
        this.view = view;

        view.homeBtn.addActionListener(e -> openDashBoard());
        view.notifBtn.addActionListener(e -> openNotification());
        view.DeliveredButton.addActionListener(e -> openDonationsDelivered());
        view.RejectedButton.addActionListener(e -> openDonationsRejected());
        view.DonateBtn.addActionListener(e -> openDonate());

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

    private void openDonationsDelivered(){
        DonationsDeliveredView donationsDeliverView = new DonationsDeliveredView();
        new DonationsDeliveredController(donationsDeliverView);
        donationsDeliverView.frame.setVisible(true);
        view.frame.dispose();
    }

    private void openDonationsRejected(){
        DonationsRejectedView donationsRejectedView = new DonationsRejectedView();
        new DonationsRejectedController(donationsRejectedView);
        donationsRejectedView.frame.setVisible(true);
        view.frame.dispose();
    }

    private void openDonate (){
        DonateView donateView = new DonateView();
        new DonateController(donateView);
        donateView.frame.setVisible(true);
        view.frame.dispose();
    }
}
