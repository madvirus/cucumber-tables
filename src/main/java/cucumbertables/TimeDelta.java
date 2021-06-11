package cucumbertables;

public class TimeDelta {
    private long yearDelta;
    private long monthDetal;
    private long dayDetal;

    public TimeDelta(long yearDelta, long monthDetal, long dayDetal) {
        this.yearDelta = yearDelta;
        this.monthDetal = monthDetal;
        this.dayDetal = dayDetal;
    }

    public long getYearDelta() {
        return yearDelta;
    }

    public long getMonthDelta() {
        return monthDetal;
    }

    public long getDayDelta() {
        return dayDetal;
    }

    public static TimeDelta zero() {
        return new TimeDelta(0L, 0L, 0L);
    }
}
