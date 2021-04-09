package cucumbertables;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class MapRowWrap {
    private Map<String, String> row;
    private String nullString;
    private boolean nullToEmpty;

    public MapRowWrap(Map<String, String> row, String nullString, boolean nullToEmpty) {
        this.row = row;
        if (nullString != null && !nullString.trim().isEmpty()) {
            this.nullString = nullString.trim();
        }
        this.nullToEmpty = nullToEmpty;
    }

    public MapRowWrap(Map<String, String> row, String nullString) {
        this(row, nullString, false);
    }

    public MapRowWrap(Map<String, String> row) {
        this(row, null, false);
    }

    private String getValue(String colName) {
        String value = row.get(colName);
        if (value == null && nullToEmpty) return "";
        return nullString != null && nullString.equals(value) ? null : value;
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public Integer getInteger(String colName) {
        String value = getValue(colName);
        return isNullOrEmpty(value) ? null : Integer.valueOf(removeCommaOrUnderscore(value));
    }

    private String removeCommaOrUnderscore(String value) {
        return value.replace(",", "").replace("_", "");
    }

    public Double getDouble(String colName) {
        String value = getValue(colName);
        return isNullOrEmpty(value) ? null : Double.valueOf(removeCommaOrUnderscore(value));
    }

    public Long getLong(String colName) {
        String value = getValue(colName);
        return isNullOrEmpty(value) ? null : Long.valueOf(removeCommaOrUnderscore(value));
    }

    public BigDecimal getBigDecimal(String colName) {
        String value = getValue(colName);
        return isNullOrEmpty(value) ? null : BigDecimal.valueOf(Double.valueOf(removeCommaOrUnderscore(value)));
    }

    public String getString(String colName) {
        return getValue(colName);
    }

    public LocalDate getLocalDate(String colName, String pattern) {
        String value = getValue(colName);
        if (isNullOrEmpty(value)) return null;
        if (ddayFormat(value)) {
            if (value.length() == 1) {
                return LocalDate.now();
            } else if (value.endsWith("M")) {
                return LocalDate.now().plusMonths(Integer.parseInt(value.substring(1, value.length() - 1)));
            } else {
                return LocalDate.now().plusDays(Integer.parseInt(value.substring(1)));
            }
        }
        return LocalDate.parse(value.trim(), DateTimeFormatter.ofPattern(pattern));
    }

    private boolean ddayFormat(String value) {
        return value.startsWith("D") || value.startsWith("d");
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

    public YearMonth getYearMonth(String colName) {
        return getYearMonth(colName, "yyyy-MM");
    }

    public YearMonth getYearMonth(String colName, String pattern) {
        String value = getValue(colName);
        if (isNullOrEmpty(value)) return null;
        if (mmonthFormat(value)) {
            if (value.length() == 1) return YearMonth.now();
            else return YearMonth.now().plusMonths(Integer.parseInt(value.substring(1)));
        }
        return YearMonth.parse(value.trim(), DateTimeFormatter.ofPattern(pattern));
    }

    private boolean mmonthFormat(String value) {
        return value.startsWith("M") || value.startsWith("m");
    }

    public <T> T convertTo(Class<T> type) {
        return convertTo(type, false);
    }

    public <T> T convertTo(Class<T> type, boolean errorWhenNotMatchName) {
        T obj = null;
        try {
            obj = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        copyTo(type, obj, errorWhenNotMatchName);
        return obj;
    }

    public <T> void copyTo(T obj) {
        this.copyTo(obj.getClass(), obj, false);
    }

    public <T> void copyTo(T obj, boolean errorWhenNotMatchName) {
        this.copyTo(obj.getClass(), obj, errorWhenNotMatchName);
    }

    private <T> void copyTo(Class<? extends T> type, T obj, boolean errorWhenNotMatchName) {
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
                    } else if (fieldType.isAssignableFrom(YearMonth.class)) {
                        field.set(obj, getYearMonth(name));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } catch (NoSuchFieldException e) {
                if (errorWhenNotMatchName) {
                    throw new IllegalArgumentException(String.format("not found '%s' field", name));
                }
            }
        }
    }
}
