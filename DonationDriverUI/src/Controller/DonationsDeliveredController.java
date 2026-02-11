package Controller;
import View.*;

public class DonationsDeliveredController {

    private DonationsDeliveredView view;
    private MonetaryDonationView view1;

    public DonationsDeliveredController(DonationsDeliveredView view) {
        this.view = view;

        view.homeBtn.addActionListener(e -> openDashBoard());
        view.notifBtn.addActionListener(e -> openNotification());
        view.ActiveDeliveryButton.addActionListener(e -> openDonationsActive());
        view.RejectedButton.addActionListener(e -> openDonationsRejected());
        view.DonateBtn.addActionListener(e -> openDonate());
        view.PendingButton.addActionListener(e -> openDonationPending());

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

    private void openDonationsRejected(){
        DonationsRejectedView donationsRejectedView = new DonationsRejectedView();
        new DonationsRejectedController(donationsRejectedView);
        donationsRejectedView.frame.setVisible(true);
        view.frame.dispose();
    }

    private void openDonationPending(){
        DonationsPendingView donationsPendingView = new DonationsPendingView();
        new DonationsPendingController(donationsPendingView);
        donationsPendingView.frame.setVisible(true);
        view.frame.dispose();
    }

    private void openDonate (){
        DonateView donateView = new DonateView();
        new DonateController(donateView);
        donateView.frame.setVisible(true);
        view.frame.dispose();
    }
}
