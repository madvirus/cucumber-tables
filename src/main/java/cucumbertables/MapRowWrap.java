package cucumbertables;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

public class MapRowWrap {
    private Map<String, String> row;
    private String nullString;
    private boolean emptyToNull;

    public MapRowWrap(Map<String, String> row, String nullString, boolean emptyToNull) {
        this.row = row;
        if (nullString != null && !nullString.trim().isEmpty()) {
            this.nullString = nullString.trim();
        }
        this.emptyToNull = emptyToNull;
    }

    public MapRowWrap(Map<String, String> row, String nullString) {
        this(row, nullString, false);
    }

    public MapRowWrap(Map<String, String> row) {
        this(row, null, false);
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
        String value = getValue(colName);
        return emptyToNull && isEmpty(value) ? null : value;
    }

    private boolean isEmpty(String value) {
        return value != null && value.isEmpty();
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

    public <T> T convertTo(Class<T> type) {
        T obj = null;
        try {
            obj = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        copyTo(type, obj);
        return obj;
    }

    public <T> void copyTo(T obj) {
        this.copyTo(obj.getClass(), obj);
    }

    private <T> void copyTo(Class<? extends T> type, T obj) {
        for (String name : row.keySet()) {
            try {
                Field field = type.getDeclaredField(name);
                Class<?> fieldType = field.getType();
                field.setAccessible(true);
                try {
                    if (fieldType.isAssignableFrom(String.class)) {
                        field.set(obj, getString(name));
                    } else if (fieldType == long.class) {
                        field.set(obj, getLong(name));
                    } else if (fieldType.isAssignableFrom(Long.class)) {
                        field.set(obj, getLong(name));
                    } else if (fieldType.isAssignableFrom(Integer.class)) {
                        field.set(obj, getInteger(name));
                    } else if (fieldType == int.class) {
                        field.set(obj, getInteger(name));
                    } else if (fieldType.isAssignableFrom(Double.class)) {
                        field.set(obj, getDouble(name));
                    } else if (fieldType == double.class) {
                        field.set(obj, getDouble(name));
                    } else if (fieldType.isAssignableFrom(BigDecimal.class)) {
                        field.set(obj, getBigDecimal(name));
                    } else if (fieldType.isAssignableFrom(LocalDate.class)) {
                        field.set(obj, getLocalDate(name));
                    } else if (fieldType.isAssignableFrom(LocalDateTime.class)) {
                        field.set(obj, getLocalDateTime(name));
                    } else if (fieldType.isAssignableFrom(LocalTime.class)) {
                        field.set(obj, getLocalTime(name));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } catch (NoSuchFieldException e) {
                // ignore
            }
        }
    }
}
