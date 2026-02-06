package Controller;

import View.MonetaryDonationView;
import View.DashboardView;
import View.BoxDonationView;

public class DashboardController {

    private DashboardView view;
    private BoxDonationView viewBox;

    public DashboardController(DashboardView view) {
        this.view = view;
        view.monetaryBtn.addActionListener(e -> openMonetaryDonation());
        view.goodsBtn.addActionListener(e -> openBoxDonation());
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

}
