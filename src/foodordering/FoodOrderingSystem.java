package foodordering;

import foodordering.util.Theme;
import foodordering.view.LoginView;
import javax.swing.SwingUtilities;

public class FoodOrderingSystem {
    public static void main(String[] args) {
        Theme.apply();
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}
