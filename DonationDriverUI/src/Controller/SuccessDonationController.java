package Controller;
import View.*;

public class SuccessDonationController {

    private SuccessDonationView view;
    private MonetaryDonationView view1;

    public SuccessDonationController(SuccessDonationView view) {
        this.view = view;

        view.homeBtn.addActionListener(e -> openDashBoard());
        view.monetaryBtn.addActionListener(e -> openMonetaryDonation());
        view.goodsBtn.addActionListener(e -> openBoxDonation());

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

    private void openBoxDonation(){
        BoxDonationView boxDonationView = new BoxDonationView();
        new BoxDonationController(boxDonationView);
        boxDonationView.frame.setVisible(true);
        view.frame.dispose();
    }

    private void openNotification(){
        NotificationView notificationView = new NotificationView();
        new NotificationController(notificationView);
        notificationView.frame.setVisible(true);
        view.frame.dispose();
    }
}
