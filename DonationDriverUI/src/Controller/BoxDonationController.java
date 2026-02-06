package Controller;
import View.DashboardView;
import View.MonetaryDonationView;
import View.BoxDonationView;

public class BoxDonationController {

    private BoxDonationView view;
    private MonetaryDonationView view1;

    public BoxDonationController(BoxDonationView view) {
        this.view = view;

        view.homeBtn.addActionListener(e -> openDashBoard());
        view.monetaryBtn.addActionListener(e -> openMonetaryDonation());
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

}
