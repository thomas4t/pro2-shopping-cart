package uhk.fim.app;

import uhk.fim.gui.MainFrame;
import javax.swing.*;

public class ShoppingCart {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(800,600);
            mainFrame.setVisible(true);
        });
    }
}
