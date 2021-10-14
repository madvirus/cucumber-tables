package cucumbertables;

public class TimeDelta {
    private long yearDelta;
    private long monthDelta;
    private long dayDelta;
    private long hourDelta;

    public TimeDelta(long yearDelta, long monthDelta, long dayDelta, long hourDelta) {
        this.yearDelta = yearDelta;
        this.monthDelta = monthDelta;
        this.dayDelta = dayDelta;
        this.hourDelta = hourDelta;
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

    public long getHourDelta() {
        return hourDelta;
    }

    public static TimeDelta zero() {
        return new TimeDelta(0L, 0L, 0L, 0L);
    }
}
