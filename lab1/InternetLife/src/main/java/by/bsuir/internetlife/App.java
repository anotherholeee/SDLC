package by.bsuir.internetlife;

import by.bsuir.internetlife.controller.InternetLifeController;
import by.bsuir.internetlife.model.InternetLifeModel;
import by.bsuir.internetlife.view.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new NimbusLookAndFeel());
            } catch (Exception ignored) {
                // Если Nimbus недоступен, остаётся системный Look and Feel.
            }

            InternetLifeModel model = new InternetLifeModel();
            MainFrame view = new MainFrame(model);
            InternetLifeController controller = new InternetLifeController(model, view);
            view.setController(controller);
            model.addObserver(view);
            view.setVisible(true);
        });
    }
}
