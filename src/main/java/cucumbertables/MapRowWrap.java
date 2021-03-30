package cucumbertables;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class MapRowWrap {
    private Map<String, String> row;
    private String nullString;

    public MapRowWrap(Map<String, String> row, String nullString) {
        this.row = row;
        if (nullString != null && !nullString.trim().isEmpty()) {
            this.nullString = nullString.trim();
        }
    }

    private String getValue(String colName) {
        String value = row.get(colName);
        if (nullString == null) return value;
        return nullString.equals(value) ? null : value;
    }

    public Integer getInteger(String colName) {
        String value = getValue(colName);
        return value == null || value.trim().isEmpty() ? null : Integer.valueOf(value);
    }

    public Double getDouble(String colName) {
        String value = getValue(colName);
        return value == null || value.trim().isEmpty() ? null : Double.valueOf(value);
    }

    public Long getLong(String colName) {
        String value = getValue(colName);
        return value == null || value.trim().isEmpty() ? null : Long.valueOf(value);
    }

    public BigDecimal getBigDecimal(String colName) {
        String value = getValue(colName);
        return value == null || value.trim().isEmpty() ? null : BigDecimal.valueOf(Double.valueOf(value));
    }

    public String getString(String colName) {
        String value = getValue(colName);
        return value;
    }

    public LocalDate getLocalDate(String colName, String pattern) {
        String value = getValue(colName);
        return value == null || value.trim().isEmpty() ? null : LocalDate.parse(value.trim(), DateTimeFormatter.ofPattern(pattern));
    }
}
