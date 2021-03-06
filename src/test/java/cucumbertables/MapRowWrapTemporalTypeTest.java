package cucumbertables;

import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class MapRowWrapTemporalTypeTest {

    @Test
    void localDate() {
        HashMap<String, String> map = new HashMap<>();
        map.put("date1", "2021-03-31");
        map.put("date2", "20210331");
        MapRowWrap row = new MapRowWrap(map);
        assertThat(row.getLocalDate("date1")).isEqualTo(LocalDate.of(2021, 3, 31));
        assertThat(row.getLocalDate("date1", "yyyy-MM-dd")).isEqualTo(LocalDate.of(2021, 3, 31));
        assertThat(row.getLocalDate("date2", "yyyyMMdd")).isEqualTo(LocalDate.of(2021, 3, 31));
    }

    @Test
    void localDate_DdayFormat() {
        assertLocalDate("D", LocalDate.now());
        assertLocalDate("D-5", LocalDate.now().minusDays(5));
        assertLocalDate("D+5", LocalDate.now().plusDays(5));
        assertLocalDate("d", LocalDate.now());
        assertLocalDate("d-3", LocalDate.now().minusDays(3));
        assertLocalDate("d+3", LocalDate.now().plusDays(3));
        assertLocalDate("D-5M", LocalDate.now().minusMonths(5));
        assertLocalDate("D+5M", LocalDate.now().plusMonths(5));
        assertLocalDate("d-3M", LocalDate.now().minusMonths(3));
        assertLocalDate("d+3M", LocalDate.now().plusMonths(3));
        assertLocalDate("d+3M+5D", LocalDate.now().plusMonths(3).plusDays(5));
        assertLocalDate("d+1Y", LocalDate.now().plusYears(1));
    }

    @Test
    void localDate_MdayFormat() {
        assertLocalDate("M/5", YearMonth.now().atDay(5));
        assertLocalDate("M/5+5", YearMonth.now().atDay(5).plusDays(5));
        assertLocalDate("M/5+1M", YearMonth.now().atDay(5).plusMonths(1));
        assertLocalDate("M/5-1Y", YearMonth.now().atDay(5).minusYears(1));
    }

    private void assertLocalDate(String str, LocalDate expected) {
        HashMap<String, String> map = new HashMap<>();
        map.put("date1", str);
        MapRowWrap row = new MapRowWrap(map);
        assertThat(row.getLocalDate("date1")).isEqualTo(expected);
    }

    @Test
    void localDateTime() {
        HashMap<String, String> map = new HashMap<>();
        map.put("datetime1", "2021-03-31 14:50:15");
        map.put("datetime2", "20210331145015");
        MapRowWrap row = new MapRowWrap(map);
        assertThat(row.getLocalDateTime("datetime1")).isEqualTo(LocalDateTime.of(2021, 3, 31, 14, 50, 15));
        assertThat(row.getLocalDateTime("datetime1", "yyyy-MM-dd HH:mm:ss")).isEqualTo(LocalDateTime.of(2021, 3, 31, 14, 50, 15));
        assertThat(row.getLocalDateTime("datetime2", "yyyyMMddHHmmss")).isEqualTo(LocalDateTime.of(2021, 3, 31, 14, 50, 15));
    }

    @Test
    void localDateTime_now() {
        assertLocalDateTimeIsCloseTo("now", LocalDateTime.now());
        assertLocalDateTimeIsCloseTo("now+1H", LocalDateTime.now().plusHours(1));
    }

    private void assertLocalDateTimeIsCloseTo(String str, LocalDateTime expected) {
        HashMap<String, String> map = new HashMap<>();
        map.put("datetime", str);
        MapRowWrap row = new MapRowWrap(map);
        assertThat(row.getLocalDateTime("datetime")).isCloseTo(
                expected,
                new TemporalUnitWithinOffset(1, ChronoUnit.SECONDS)
        );
    }

    @Test
    void localDateTime_dday() {
        assertLocalDateTime("D 11:22:33", LocalDate.now().atTime(11, 22, 33));
        assertLocalDateTime("D+5 11:22:33", LocalDate.now().plusDays(5).atTime(11, 22, 33));
    }

    @Test
    void localDateTime_mday() {
        assertLocalDateTime("M/5 11:22:33", YearMonth.now().atDay(5).atTime(11, 22, 33));
        assertLocalDateTime("M/5+15 11:22:33", YearMonth.now().atDay(5).plusDays(15).atTime(11, 22, 33));
    }

    private void assertLocalDateTime(String str, LocalDateTime expected) {
        HashMap<String, String> map = new HashMap<>();
        map.put("datetime", str);
        MapRowWrap row = new MapRowWrap(map);
        assertThat(row.getLocalDateTime("datetime")).isEqualTo(expected);
    }

    @Test
    void localTime() {
        HashMap<String, String> map = new HashMap<>();
        map.put("time1", "14:50:15");
        map.put("time2", "145015");
        MapRowWrap row = new MapRowWrap(map);
        assertThat(row.getLocalTime("time1")).isEqualTo(LocalTime.of(14, 50, 15));
        assertThat(row.getLocalTime("time1", "HH:mm:ss")).isEqualTo(LocalTime.of(14, 50, 15));
        assertThat(row.getLocalTime("time2", "HHmmss")).isEqualTo(LocalTime.of(14, 50, 15));
    }

    @Test
    void localTime_nowFormat() {
        HashMap<String, String> map = new HashMap<>();
        map.put("time1", "now");
        map.put("time2", "now+2H");
        MapRowWrap row = new MapRowWrap(map);
        assertThat(row.getLocalTime("time1")).isCloseTo(
                LocalTime.now(), new TemporalUnitWithinOffset(1, ChronoUnit.SECONDS)
        );
        assertThat(row.getLocalTime("time2")).isCloseTo(
                LocalTime.now().plusHours(2), new TemporalUnitWithinOffset(1, ChronoUnit.SECONDS)
        );
    }

    @Test
    void yearMonth() {
        HashMap<String, String> map = new HashMap<>();
        map.put("month1", "2021-04");
        map.put("month2", "202104");
        MapRowWrap row = new MapRowWrap(map);
        assertThat(row.getYearMonth("month1")).isEqualTo(YearMonth.of(2021, 4));
        assertThat(row.getYearMonth("month2", "yyyyMM")).isEqualTo(YearMonth.of(2021, 4));
    }

    @Test
    void yearMonth_Mformat() {
        assertYearMonth("M", YearMonth.now());
        assertYearMonth("M+1", YearMonth.now().plusMonths(1));
        assertYearMonth("M-3M", YearMonth.now().plusMonths(-3));
        assertYearMonth("M+1Y+1M", YearMonth.now().plusYears(1).plusMonths(1));
    }

    private void assertYearMonth(String str, YearMonth expected) {
        HashMap<String, String> map = new HashMap<>();
        map.put("month1", str);
        MapRowWrap row = new MapRowWrap(map);
        assertThat(row.getYearMonth("month1")).isEqualTo(expected);
    }

    @Test
    void year() {
        assertYear(Year.now().format(DateTimeFormatter.ofPattern("yyyy")), Year.now());
        assertYear("Y", Year.now());
        assertYear("Y+10", Year.now().plusYears(10));
        assertYear("Y-5Y", Year.now().plusYears(-5));
    }

    private void assertYear(String str, Year expected) {
        HashMap<String, String> map = new HashMap<>();
        map.put("year1", str);
        MapRowWrap row = new MapRowWrap(map);
        assertThat(row.getYear("year1")).isEqualTo(expected);
    }
}
