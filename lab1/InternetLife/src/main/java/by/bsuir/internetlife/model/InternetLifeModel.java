package by.bsuir.internetlife.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Активная модель: хранит данные, считает результат и сама оповещает View.
 */
public class InternetLifeModel {
    /**
     * Горизонт привычки: если сохранять текущий темп 50 лет
     * (типичный оставшийся период активной жизни в сети).
     */
    public static final int HORIZON_YEARS = 50;

    /** Среднее число мемов в час ленты соцсетей (около двух в минуту). */
    public static final int MEMES_PER_HOUR = 120;

    /** Среднее число рекламных объявлений в час онлайн (баннеры, pre-roll, натив). */
    public static final int ADS_PER_HOUR = 72;

    public static final double DAYS_PER_YEAR = 365.25;

    private final List<ModelObserver> observers = new ArrayList<>();
    private CalculationResult result;

    public void addObserver(ModelObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(ModelObserver observer) {
        observers.remove(observer);
    }

    public CalculationResult getResult() {
        return result;
    }

    public boolean hasResult() {
        return result != null;
    }

    /**
     * Пересчитывает показатели и уведомляет наблюдателей.
     */
    public void applyHoursPerDay(double hoursPerDay) {
        double yearsOfLife = hoursPerDay / 24.0 * HORIZON_YEARS;
        double totalHours = hoursPerDay * DAYS_PER_YEAR * HORIZON_YEARS;
        long memesSeen = Math.round(totalHours * MEMES_PER_HOUR);
        long adsScrolled = Math.round(totalHours * ADS_PER_HOUR);

        this.result = new CalculationResult(hoursPerDay, yearsOfLife, memesSeen, adsScrolled);
        notifyObservers();
    }

    private void notifyObservers() {
        for (ModelObserver observer : List.copyOf(observers)) {
            observer.modelChanged();
        }
    }
}
