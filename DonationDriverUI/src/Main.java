import Controller.LoginController;
import View.DashboardView;
import View.LoginView;

public class Main {
    public static void main(String[] args) {
        LoginView loginView = new LoginView();
        new LoginController(loginView);
    }
}
