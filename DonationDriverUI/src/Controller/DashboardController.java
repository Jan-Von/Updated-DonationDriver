package Controller;

import View.*;

public class DashboardController {

    private DashboardView view;
    private BoxDonationView viewBox;

    public DashboardController(DashboardView view) {
        this.view = view;
        view.monetaryBtn.addActionListener(e -> openMonetaryDonation());
        view.goodsBtn.addActionListener(e -> openBoxDonation());
        view.notifBtn.addActionListener(e -> openNotification());
        view.donationBtn.addActionListener(e ->openDonations());
        view.DonateBtn.addActionListener(e -> openDonate());
    }

    private void openMonetaryDonation() {
        MonetaryDonationView moneyView = new MonetaryDonationView();
        new MonetaryDonationController(moneyView);
        moneyView.frame.setVisible(true); // open new frame
        view.frame.dispose(); // close current dashboard
    }

    private void openBoxDonation(){
        BoxDonationView boxView = new BoxDonationView();
        new BoxDonationController(boxView);
        boxView.frame.setVisible(true);
        view.frame.dispose();
    }

    private void openNotification (){
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

    private void openDonate (){
        DonateView donateView = new DonateView();
        new DonateController(donateView);
        donateView.frame.setVisible(true);
        view.frame.dispose();
    }

}
