package cucumbertables;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class MapRowWrapTest {
    @Test
    void stringValue() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "cbk");

        MapRowWrap row = new MapRowWrap(map);
        assertThat(row.getString("name")).isEqualTo("cbk");
    }

    @Test
    void numberValue() {
        HashMap<String, String> map = new HashMap<>();
        map.put("integer", "1");
        map.put("double", "0.123");
        map.put("long", "12345");
        map.put("comma", "12,345");

        MapRowWrap row = new MapRowWrap(map);
        assertThat(row.getInteger("integer")).isEqualTo(Integer.valueOf(1));
        assertThat(row.getDouble("double")).isEqualTo(Double.valueOf(0.123));
        assertThat(row.getLong("long")).isEqualTo(Long.valueOf(12345));
        assertThat(row.getBigDecimal("double")).isEqualTo(BigDecimal.valueOf(0.123));

        assertThat(row.getInteger("comma")).isEqualTo(Integer.valueOf(12345));
        assertThat(row.getLong("comma")).isEqualTo(Long.valueOf(12345));
        assertThat(row.getDouble("comma")).isEqualTo(Double.valueOf(12345));
    }

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
        HashMap<String, String> map = new HashMap<>();
        map.put("date1", "D");
        map.put("date2", "D-5");
        map.put("date3", "D+5");
        map.put("date4", "d");
        map.put("date5", "d-3");
        map.put("date6", "d+3");
        MapRowWrap row = new MapRowWrap(map);
        assertThat(row.getLocalDate("date1")).isEqualTo(LocalDate.now());
        assertThat(row.getLocalDate("date2")).isEqualTo(LocalDate.now().minusDays(5));
        assertThat(row.getLocalDate("date3")).isEqualTo(LocalDate.now().plusDays(5));
        assertThat(row.getLocalDate("date4")).isEqualTo(LocalDate.now());
        assertThat(row.getLocalDate("date5")).isEqualTo(LocalDate.now().minusDays(3));
        assertThat(row.getLocalDate("date6")).isEqualTo(LocalDate.now().plusDays(3));
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
    void localTime() {
        HashMap<String, String> map = new HashMap<>();
        map.put("time1", "14:50:15");
        map.put("time2", "145015");
        MapRowWrap row = new MapRowWrap(map);
        assertThat(row.getLocalTime("time1")).isEqualTo(LocalTime.of(14, 50, 15));
        assertThat(row.getLocalTime("time1", "HH:mm:ss")).isEqualTo(LocalTime.of(14, 50, 15));
        assertThat(row.getLocalTime("time2", "HHmmss")).isEqualTo(LocalTime.of(14, 50, 15));
    }

}
