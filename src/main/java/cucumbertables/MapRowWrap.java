package cucumbertables;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public Integer getInteger(String colName) {
        String value = getValue(colName);
        return isNullOrEmpty(value) ? null : Integer.valueOf(removeComma(value));
    }

    private String removeComma(String value) {
        return value.replace(",", "");
    }

    public Double getDouble(String colName) {
        String value = getValue(colName);
        return isNullOrEmpty(value) ? null : Double.valueOf(removeComma(value));
    }

    public Long getLong(String colName) {
        String value = getValue(colName);
        return isNullOrEmpty(value) ? null : Long.valueOf(removeComma(value));
    }

    public BigDecimal getBigDecimal(String colName) {
        String value = getValue(colName);
        return isNullOrEmpty(value) ? null : BigDecimal.valueOf(Double.valueOf(removeComma(value)));
    }

    public String getString(String colName) {
        return getValue(colName);
    }

    public LocalDate getLocalDate(String colName, String pattern) {
        String value = getValue(colName);
        if (isNullOrEmpty(value)) return null;
        if (value.startsWith("D") || value.startsWith("d")) {
            if (value.length() == 1) return LocalDate.now();
            else return LocalDate.now().plusDays(Integer.parseInt(value.substring(1)));
        }
        return LocalDate.parse(value.trim(), DateTimeFormatter.ofPattern(pattern));
    }

    public LocalDate getLocalDate(String colName) {
        return getLocalDate(colName, "yyyy-MM-dd");
    }

    public LocalDateTime getLocalDateTime(String colName, String pattern) {
        String value = getValue(colName);
        return isNullOrEmpty(value) ? null : LocalDateTime.parse(value.trim(), DateTimeFormatter.ofPattern(pattern));
    }

    public LocalDateTime getLocalDateTime(String colName) {
        return getLocalDateTime(colName, "yyyy-MM-dd HH:mm:ss");
    }

    public LocalTime getLocalTime(String colName, String pattern) {
        String value = getValue(colName);
        return isNullOrEmpty(value) ? null : LocalTime.parse(value.trim(), DateTimeFormatter.ofPattern(pattern));
    }

    public LocalTime getLocalTime(String colName) {
        return getLocalTime(colName, "HH:mm:ss");
    }
}
