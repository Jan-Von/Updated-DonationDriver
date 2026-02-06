package Controller;
import View.DashboardView;
import View.MonetaryDonationView;

public class MonetaryDonationController {

    private MonetaryDonationView view;

    public MonetaryDonationController(MonetaryDonationView view) {
        this.view = view;

        view.homeBtn.addActionListener(e -> openDashBoard());
    }

    private void openDashBoard(){
        DashboardView dashboardView = new DashboardView();
        new DashboardController(dashboardView);
        dashboardView.frame.setVisible(true);
        view.frame.dispose();

    }

}
