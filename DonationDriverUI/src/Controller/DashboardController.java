package Controller;

import View.MonetaryDonationView;
import View.DashboardView;

public class DashboardController {

    private DashboardView view;

    public DashboardController(DashboardView view) {
        this.view = view;

        // Attach listener to monetaryBtn
        view.monetaryBtn.addActionListener(e -> openMonetaryDonation());
    }

    private void openMonetaryDonation() {
        view.frame.dispose(); // close current dashboard
        MonetaryDonationView moneyView = new MonetaryDonationView();
        new MonetaryDonationController();
        moneyView.setVisible(true); // open new frame
    }
}
