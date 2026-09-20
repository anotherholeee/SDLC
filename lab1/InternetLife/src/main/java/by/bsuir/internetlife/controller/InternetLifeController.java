package by.bsuir.internetlife.controller;

import by.bsuir.internetlife.model.InternetLifeModel;
import by.bsuir.internetlife.view.InputDialog;
import by.bsuir.internetlife.view.MainFrame;

/**
 * Контроллер MVC: обрабатывает действия пользователя, проверяет ввод
 * и передаёт корректные данные активной модели.
 */
public class InternetLifeController {
    private final InternetLifeModel model;
    private final MainFrame view;

    public InternetLifeController(InternetLifeModel model, MainFrame view) {
        this.model = model;
        this.view = view;
    }

    public void onEnterData() {
        InputDialog dialog = new InputDialog(view, view.getLastEnteredHours());
        dialog.setVisible(true);

        if (!dialog.isConfirmed()) {
            return;
        }

        String rawHours = dialog.getHoursText();
        view.rememberEnteredHours(rawHours);

        try {
            double hours = parseHours(rawHours);
            model.applyHoursPerDay(hours);
        } catch (IllegalArgumentException exception) {
            view.showError(exception.getMessage());
        }
    }

    static double parseHours(String rawHours) {
        if (rawHours == null || rawHours.isBlank()) {
            throw new IllegalArgumentException("Введите количество часов онлайн в день.");
        }

        String normalized = rawHours.trim().replace(',', '.');
        double hours;
        try {
            hours = Double.parseDouble(normalized);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Количество часов должно быть числом, например 4 или 6.5.");
        }

        if (Double.isNaN(hours) || Double.isInfinite(hours)) {
            throw new IllegalArgumentException("Введено некорректное число часов.");
        }
        if (hours <= 0) {
            throw new IllegalArgumentException("Количество часов должно быть больше нуля.");
        }
        if (hours > 24) {
            throw new IllegalArgumentException("В сутках только 24 часа. Введите значение больше 0 и не больше 24.");
        }
        return hours;
    }
}
