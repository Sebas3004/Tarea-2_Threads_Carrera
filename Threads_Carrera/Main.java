import controller.CarreraController;
import model.CarreraModel;
import view.MainView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            CarreraModel modelo = new CarreraModel();
            MainView     vista  = new MainView(modelo.getAnimales());
            new CarreraController(modelo, vista);
            vista.setVisible(true);
        });
    }
}