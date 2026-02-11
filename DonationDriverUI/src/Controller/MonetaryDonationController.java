package Controller;
import View.*;


public class MonetaryDonationController {

    private MonetaryDonationView view;
    private BoxDonationView viewBox;

    public MonetaryDonationController(MonetaryDonationView view) {
        this.view = view;

        view.homeBtn.addActionListener(e -> openDashBoard());
        view.donateNow.addActionListener(e -> openSuccessDonation());
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

    private void openBoxDonation(){
        BoxDonationView boxDonationView = new BoxDonationView();
        new BoxDonationController(boxDonationView);
        boxDonationView.frame.setVisible(true);
        view.frame.dispose();
    }

    private void openSuccessDonation(){
        SuccessDonationView successDonationView = new SuccessDonationView();
        new SuccessDonationController(successDonationView);
        successDonationView.frame.setVisible(true);
        view.frame.dispose();
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
