package by.bsuir.internetlife.model;

/**
 * Результат расчёта «жизни в интернете» при заданном числе часов онлайн в день.
 */
public final class CalculationResult {
    private final double hoursPerDay;
    private final double yearsOfLife;
    private final long memesSeen;
    private final long adsScrolled;

    public CalculationResult(double hoursPerDay, double yearsOfLife, long memesSeen, long adsScrolled) {
        this.hoursPerDay = hoursPerDay;
        this.yearsOfLife = yearsOfLife;
        this.memesSeen = memesSeen;
        this.adsScrolled = adsScrolled;
    }

    public double getHoursPerDay() {
        return hoursPerDay;
    }

    public double getYearsOfLife() {
        return yearsOfLife;
    }

    public long getMemesSeen() {
        return memesSeen;
    }

    public long getAdsScrolled() {
        return adsScrolled;
    }
}
