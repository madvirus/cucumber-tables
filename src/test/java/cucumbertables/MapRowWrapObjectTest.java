package cucumbertables;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class MapRowWrapObjectTest {
    @Test
    void convertTo() {
        HashMap<String, String> map = new HashMap<>();
        map.put("string", "cbk");
        map.put("int1", "10");
        map.put("int2", "10");
        map.put("long1", "1234");
        map.put("long2", "1234");
        map.put("double1", "10.3");
        map.put("double2", "10.3");
        map.put("float1", "10.3");
        map.put("float2", "10.3");
        map.put("localDate", "2021-06-11");
        map.put("localDateTime", "2021-06-11 12:31:59");
        map.put("localTime", "12:31:59");
        map.put("yearMonth", "2021-06");
        map.put("year", "2021");
        map.put("boolean1", "true");
        map.put("boolean2", "false");

        MapRowWrap row = new MapRowWrap(map, "<null>");
        Data data = row.convertTo(Data.class);
        assertThat(data.getString()).isEqualTo("cbk");
        assertThat(data.getInt1()).isEqualTo(10);
        assertThat(data.getInt2()).isEqualTo(10);
        assertThat(data.getLong1()).isEqualTo(1234L);
        assertThat(data.getLong2()).isEqualTo(1234L);
        assertThat(data.getDouble1()).isEqualTo(10.3);
        assertThat(data.getDouble2()).isEqualTo(10.3);
        assertThat(data.getFloat1()).isEqualTo(10.3f);
        assertThat(data.getFloat2()).isEqualTo(10.3f);
        assertThat(data.getLocalDate()).isEqualTo(LocalDate.of(2021, 6, 11));
        assertThat(data.getLocalDateTime())
                .isEqualTo(LocalDateTime.of(2021, 6, 11, 12, 31, 59));
        assertThat(data.getLocalTime()).isEqualTo(LocalTime.of(12, 31, 59));
        assertThat(data.getYearMonth()).isEqualTo(YearMonth.of(2021, 6));
        assertThat(data.getYear()).isEqualTo(Year.of(2021));
        assertThat(data.getBoolean1()).isTrue();
        assertThat(data.getBoolean2()).isFalse();
    }

    @Test
    void convertTo_UnderscoreToCamelcase() {
        HashMap<String, String> map = new HashMap<>();
        map.put("address_detail", "detail");
        MapRowWrap row = new MapRowWrap(map, "<null>");
        Data data = row.convertTo(Data.class, ConvertOptions.UNDERSCORE_TO_CAMELCASE);
        assertThat(data.getAddressDetail()).isEqualTo("detail");
    }

    @Test
    void convertTo_ErrorOnNoMatchingName() {
        Assertions.assertThatThrownBy(() ->
                {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("name1", "cbk");
                    MapRowWrap row = new MapRowWrap(map);
                    row.convertTo(Data.class, ConvertOptions.ERROR_ON_NO_MATCHING_NAME);
                })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name1");
    }

    @Test
    void copyTo() {
        HashMap<String, String> map = new HashMap<>();
        map.put("string", "cbk");
        map.put("double1", "10.3");
        map.put("double2", "10.3");
        MapRowWrap row = new MapRowWrap(map);

        Data data = new Data();
        data.setString("before");
        data.setDouble1(0.0);
        data.setDouble2(0.0);

        data.setInt1(1);
        data.setInt2(2);

        row.copyTo(data);
        assertThat(data.getString()).isEqualTo("cbk");
        assertThat(data.getDouble1()).isEqualTo(10.3);
        assertThat(data.getDouble2()).isEqualTo(10.3);

        assertThat(data.getInt1()).isEqualTo(1);
        assertThat(data.getInt2()).isEqualTo(2);
    }

    @Test
    void copyTo_UnderscoreToCamelCase() {
        HashMap<String, String> map = new HashMap<>();
        map.put("address_detail", "detail");
        MapRowWrap row = new MapRowWrap(map);

        Data mem = new Data();
        row.copyTo(mem, ConvertOptions.UNDERSCORE_TO_CAMELCASE, ConvertOptions.ERROR_ON_NO_MATCHING_NAME);
        assertThat(mem.getAddressDetail()).isEqualTo("detail");
    }

    @Test
    void copyTo_ErrorOnNoMatchingName() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "cbk");
        map.put("nomatching", "detail");
        MapRowWrap row = new MapRowWrap(map);

        Data mem = new Data();
        Assertions.assertThatThrownBy(() ->
                        row.copyTo(mem, ConvertOptions.ERROR_ON_NO_MATCHING_NAME)
                ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nomatching");
    }

    public static class Data {
        private String string;
        private int int1;
        private Integer int2;
        private long long1;
        private Long long2;
        private double double1;
        private Double double2;
        private float float1;
        private Float float2;
        private LocalDate localDate;
        private LocalDateTime localDateTime;
        private LocalTime localTime;
        private YearMonth yearMonth;
        private Year year;
        private String addressDetail;
        private boolean boolean1;
        private boolean boolean2;

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public int getInt1() {
            return int1;
        }

        public void setInt1(int int1) {
            this.int1 = int1;
        }

        public Integer getInt2() {
            return int2;
        }

        public void setInt2(Integer int2) {
            this.int2 = int2;
        }

        public long getLong1() {
            return long1;
        }

        public void setLong1(long long1) {
            this.long1 = long1;
        }

        public Long getLong2() {
            return long2;
        }

        public void setLong2(Long long2) {
            this.long2 = long2;
        }

        public double getDouble1() {
            return double1;
        }

        public void setDouble1(double double1) {
            this.double1 = double1;
        }

        public Double getDouble2() {
            return double2;
        }

        public void setDouble2(Double double2) {
            this.double2 = double2;
        }

        public float getFloat1() {
            return float1;
        }

        public void setFloat1(float float1) {
            this.float1 = float1;
        }

        public Float getFloat2() {
            return float2;
        }

        public void setFloat2(Float float2) {
            this.float2 = float2;
        }

        public LocalDate getLocalDate() {
            return localDate;
        }

        public void setLocalDate(LocalDate localDate) {
            this.localDate = localDate;
        }

        public LocalDateTime getLocalDateTime() {
            return localDateTime;
        }

        public void setLocalDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
        }

        public LocalTime getLocalTime() {
            return localTime;
        }

        public void setLocalTime(LocalTime localTime) {
            this.localTime = localTime;
        }

        public YearMonth getYearMonth() {
            return yearMonth;
        }

        public void setYearMonth(YearMonth yearMonth) {
            this.yearMonth = yearMonth;
        }

        public Year getYear() {
            return year;
        }

        public void setYear(Year year) {
            this.year = year;
        }

        public String getAddressDetail() {
            return addressDetail;
        }

        public void setAddressDetail(String addressDetail) {
            this.addressDetail = addressDetail;
        }

        public boolean getBoolean1() {
            return boolean1;
        }

        public void setBoolean1(boolean boolean1) {
            this.boolean1 = boolean1;
        }

        public boolean getBoolean2() {
            return boolean2;
        }

        public void setBoolean2(boolean boolean2) {
            this.boolean2 = boolean2;
        }
    }
}
