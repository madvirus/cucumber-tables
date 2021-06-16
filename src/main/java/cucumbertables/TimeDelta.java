package cucumbertables;

public class TimeDelta {
    private long yearDelta;
    private long monthDelta;
    private long dayDelta;

    public TimeDelta(long yearDelta, long monthDelta, long dayDelta) {
        this.yearDelta = yearDelta;
        this.monthDelta = monthDelta;
        this.dayDelta = dayDelta;
    }

    public long getYearDelta() {
        return yearDelta;
    }

    public long getMonthDelta() {
        return monthDelta;
    }

    public long getDayDelta() {
        return dayDelta;
    }

    public static TimeDelta zero() {
        return new TimeDelta(0L, 0L, 0L);
    }
}
