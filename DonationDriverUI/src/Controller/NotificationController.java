package Controller;

import View.MonetaryDonationView;
import View.DashboardView;
import View.BoxDonationView;
import View.NotificationView;

public class NotificationController {

    private NotificationView view;
    private BoxDonationView viewBox;

    public NotificationController(NotificationView view) {
        this.view = view;
        view.homeBtn.addActionListener(e -> {openDashboard();});
        view.monetaryBtn.addActionListener(e -> openMonetaryDonation());
        view.goodsBtn.addActionListener(e -> openBoxDonation());
        view.notifBtn.addActionListener(e -> openNotificaton());
    }

    private void openDashboard(){
        DashboardView dashboardview = new DashboardView();
        new DashboardController(dashboardview);
        dashboardview.setVisible(true);
        view.frame.dispose();
    }

    private void openMonetaryDonation() {
        MonetaryDonationView moneyView = new MonetaryDonationView();
        new MonetaryDonationController(moneyView);
        moneyView.setVisible(true); // open new frame
        view.frame.dispose(); // close current dashboard
    }

    private void openBoxDonation(){
        BoxDonationView boxView = new BoxDonationView();
        new BoxDonationController(boxView);
        boxView.setVisible(true);
        view.frame.dispose();
    }

    private void openNotificaton (){
        NotificationView notifView = new NotificationView();
        new NotificationController(notifView);
        notifView.setVisible(true);
        view.frame.dispose();
    }

}
