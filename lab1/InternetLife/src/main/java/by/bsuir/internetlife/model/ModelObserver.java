package by.bsuir.internetlife.model;

/**
 * Наблюдатель активной модели MVC.
 * Модель сама уведомляет подписчиков после изменения состояния.
 */
public interface ModelObserver {
    void modelChanged();
}
