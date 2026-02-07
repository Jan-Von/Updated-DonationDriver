package Controller;

import View.*;

public class DonateController {

    private DonateView view;
    private BoxDonationView viewBox;

    public DonateController(DonateView view) {
        this.view = view;
        view.homeBtn.addActionListener(e -> {openDashboard();});
        view.notifBtn.addActionListener(e -> openNotificaton());
        view.donationBtn.addActionListener(e ->openDonations());
    }

    private void openDashboard(){
        DashboardView dashboardview = new DashboardView();
        new DashboardController(dashboardview);
        dashboardview.frame.setVisible(true);
        view.frame.dispose();
    }

    private void openNotificaton (){
        NotificationView notifView = new NotificationView();
        new NotificationController(notifView);
        notifView.frame.setVisible(true);
        view.frame.dispose();
    }

    private void openDonations(){
        DonationsActiveView donationsView = new DonationsActiveView();
        new DonationsActiveController(donationsView);
        donationsView.frame.setVisible(true);
        view.frame.dispose();
    }


}
