package cucumbertables;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public Float getFloat(String colName) {
        String value = getValue(colName);
        return isNullOrEmpty(value) ? null : Float.valueOf(removeCommaOrUnderscore(value));
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
            return getLocalDateFromDdayValue(value);
        } else if (mdayFormat(value)) {
            return getLocalDateFromMdayValue(value);
        }
        return LocalDate.parse(value.trim(), DateTimeFormatter.ofPattern(pattern));
    }

    private boolean ddayFormat(String value) {
        return value.startsWith("D") || value.startsWith("d");
    }

    private boolean mdayFormat(String value) {
        return value.startsWith("M/") || value.startsWith("m/");
    }

    private LocalDate getLocalDateFromDdayValue(String value) {
        TimeDelta delta = parseDeltaFormat(value);
        return LocalDate.now().plusYears(delta.getYearDelta()).plusMonths(delta.getMonthDelta()).plusDays(delta.getDayDelta());
    }

    private LocalDate getLocalDateFromMdayValue(String value) {
        TimeDelta delta = parseDeltaFormat(value);
        Pattern pattern = Pattern.compile("^[0-9]?[0-9]");
        String rest = value.substring(2);
        Matcher matcher = pattern.matcher(rest);
        if (matcher.find()) {
            int day = Integer.parseInt(rest.substring(matcher.start(), matcher.end()));
            return YearMonth.now().atDay(day).plusYears(delta.getYearDelta()).plusMonths(delta.getMonthDelta()).plusDays(delta.getDayDelta());
        } else {
            throw new IllegalArgumentException("bad mday format: " + value);
        }
    }

    private TimeDelta parseDeltaFormat(String value) {
        return DeltaFormatParser.parse(value);
    }

    public LocalDate getLocalDate(String colName) {
        return getLocalDate(colName, "yyyy-MM-dd");
    }

    public LocalDateTime getLocalDateTime(String colName, String pattern) {
        String value = getValue(colName);
        if (isNullOrEmpty(value)) return null;
        if (nowFormat(value)) {
            if (value.length() == 3) {
                return LocalDateTime.now();
            } else {
                TimeDelta delta = parseDeltaFormat(value.substring(3));
                long hourInc = delta.getHourDelta();
                return LocalDateTime.now().plusHours(hourInc);
            }
        }
        if (ddayFormat(value)) {
            int spIdx = value.indexOf(" ");
            return getLocalDateFromDdayValue(value.substring(0, spIdx))
                    .atTime(LocalTime.parse(value.substring(spIdx + 1), DateTimeFormatter.ofPattern("HH:mm:ss")));
        }
        if (mdayFormat(value)) {
            int spIdx = value.indexOf(" ");
            return getLocalDateFromMdayValue(value.substring(0, spIdx))
                    .atTime(LocalTime.parse(value.substring(spIdx + 1), DateTimeFormatter.ofPattern("HH:mm:ss")));
        }
        return LocalDateTime.parse(value.trim(), DateTimeFormatter.ofPattern(pattern));
    }

    private boolean nowFormat(String value) {
        return value != null && value.startsWith("now");
    }

    public LocalDateTime getLocalDateTime(String colName) {
        return getLocalDateTime(colName, "yyyy-MM-dd HH:mm:ss");
    }

    public LocalTime getLocalTime(String colName, String pattern) {
        String value = getValue(colName);
        if (isNullOrEmpty(value)) return null;
        if (nowFormat(value)) {
            if (value.length() == 3) {
                return LocalTime.now();
            } else {
                TimeDelta delta = parseDeltaFormat(value.substring(3));
                long hourInc = delta.getHourDelta();
                return LocalTime.now().plusHours(hourInc);
            }
        }
        return LocalTime.parse(value.trim(), DateTimeFormatter.ofPattern(pattern));
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
            TimeDelta delta = parseDeltaFormat(value);
            return YearMonth.now().plusYears(delta.getYearDelta()).plusMonths(delta.getMonthDelta());
        }
        return YearMonth.parse(value.trim(), DateTimeFormatter.ofPattern(pattern));
    }

    private boolean mmonthFormat(String value) {
        return value.startsWith("M") || value.startsWith("m");
    }

    public Year getYear(String colName) {
        String value = getValue(colName);
        if (isNullOrEmpty(value)) return null;
        if (yyearFormat(value)) {
            TimeDelta delta = parseDeltaFormat(value);
            return Year.now().plusYears(delta.getYearDelta());
        }
        return Year.of(Integer.parseInt(value));
    }

    private boolean yyearFormat(String value) {
        return value.startsWith("Y") || value.startsWith("y");
    }

    public <T> T convertTo(Class<T> type, ConvertOptions ... options) {
        T obj = null;
        try {
            obj = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        copyTo(type, obj, options);
        return obj;
    }

    public <T> void copyTo(T obj, ConvertOptions ... options) {
        this.copyTo(obj.getClass(), obj, options);
    }

    private <T> void copyTo(Class<? extends T> type, T obj, ConvertOptions ... options) {
        int optionValue = Arrays.stream(options).mapToInt(opt -> opt.getValue()).sum();
        for (String name : row.keySet()) {
            try {
                Field field = type.getDeclaredField(getRealName(name, optionValue));
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
                    } else if (fieldType.isAssignableFrom(Float.class)) {
                        field.set(obj, getFloat(name));
                    } else if (fieldType == float.class) {
                        field.set(obj, getFloat(name));
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
                    } else if (fieldType.isAssignableFrom(Year.class)) {
                        field.set(obj, getYear(name));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } catch (NoSuchFieldException e) {
                if (ConvertOptions.ERROR_ON_NO_MATCHING_NAME.match(optionValue)) {
                    throw new IllegalArgumentException(String.format("not found '%s' field", name));
                }
            }
        }
    }

    private String getRealName(String name, int optionValue) {
        if (ConvertOptions.UNDERSCORE_TO_CAMELCASE.match(optionValue)) {
            StringBuilder builder = new StringBuilder();
            boolean shouldConvertNextCharToUpper = false;
            for (int i = 0; i < name.length(); i++) {
                char currentChar = name.charAt(i);
                if (currentChar == '_') {
                    shouldConvertNextCharToUpper = true;
                } else if (!shouldConvertNextCharToUpper) {
                    builder.append(currentChar);
                } else {
                    builder.append(Character.toUpperCase(currentChar));
                    shouldConvertNextCharToUpper = false;
                }
            }
            return builder.toString();
        }
        return name;
    }

}
