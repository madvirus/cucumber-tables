package cucumbertables;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
    void stringEmptyValue() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "");

        MapRowWrap row = new MapRowWrap(map);
        assertThat(row.getString("name")).isEqualTo("");
    }

    @Test
    void nullToEmptyString() {
        HashMap<String, String> map = new HashMap<>();
        MapRowWrap row = new MapRowWrap(map, "<null>", true);
        assertThat(row.getString("name")).isEqualTo("");
    }

    @Test
    void numberValue() {
        HashMap<String, String> map = new HashMap<>();
        map.put("integer", "1");
        map.put("double", "0.123");
        map.put("long", "12345");
        map.put("comma", "12,345");
        map.put("underscore", "12_345");

        MapRowWrap row = new MapRowWrap(map);
        assertThat(row.getInteger("integer")).isEqualTo(Integer.valueOf(1));
        assertThat(row.getDouble("double")).isEqualTo(Double.valueOf(0.123));
        assertThat(row.getFloat("double")).isEqualTo(Float.valueOf(0.123f));
        assertThat(row.getLong("long")).isEqualTo(Long.valueOf(12345));
        assertThat(row.getBigDecimal("double")).isEqualTo(BigDecimal.valueOf(0.123));

        assertThat(row.getInteger("comma")).isEqualTo(Integer.valueOf(12345));
        assertThat(row.getLong("comma")).isEqualTo(Long.valueOf(12345));
        assertThat(row.getDouble("comma")).isEqualTo(Double.valueOf(12345));
        assertThat(row.getFloat("comma")).isEqualTo(Float.valueOf(12345));

        assertThat(row.getInteger("underscore")).isEqualTo(Integer.valueOf(12345));
    }
}
