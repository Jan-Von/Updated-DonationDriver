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
        MonetaryDonationView moneyView = new MonetaryDonationView();
        new MonetaryDonationController(moneyView);
        moneyView.setVisible(true); // open new frame
        view.frame.dispose(); // close current dashboard
    }
}
