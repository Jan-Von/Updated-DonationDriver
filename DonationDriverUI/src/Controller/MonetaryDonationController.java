package Controller;
import View.DashboardView;
import View.MonetaryDonationView;
import View.BoxDonationView;
import View.SuccessDonationView;
import View.NotificationView;


public class MonetaryDonationController {

    private MonetaryDonationView view;
    private BoxDonationView viewBox;

    public MonetaryDonationController(MonetaryDonationView view) {
        this.view = view;

        view.homeBtn.addActionListener(e -> openDashBoard());
        view.goodsBtn.addActionListener(e -> openBoxDonation());
        view.donateNow.addActionListener(e -> openSuccessDonation());
        view.notifBtn.addActionListener(e -> openNotification());

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
}
