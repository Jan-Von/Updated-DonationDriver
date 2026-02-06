package Controller;
import View.DashboardView;
import View.MonetaryDonationView;
import View.BoxDonationView;

public class MonetaryDonationController {

    private MonetaryDonationView view;
    private BoxDonationView viewBox;

    public MonetaryDonationController(MonetaryDonationView view) {
        this.view = view;

        view.homeBtn.addActionListener(e -> openDashBoard());
        view.goodsBtn.addActionListener(e -> openBoxDonation());
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

}
