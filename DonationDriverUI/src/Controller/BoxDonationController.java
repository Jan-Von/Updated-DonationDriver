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

}
