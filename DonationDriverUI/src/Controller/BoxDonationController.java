package Controller;
import View.*;

public class BoxDonationController {

    private BoxDonationView view;
    private MonetaryDonationView view1;

    public BoxDonationController(BoxDonationView view) {
        this.view = view;

        view.homeBtn.addActionListener(e -> openDashBoard());
        view.monetaryBtn.addActionListener(e -> openMonetaryDonation());
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
