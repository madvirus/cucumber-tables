package cucumbertables;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeltaFormatParser {
    public static TimeDelta parse(String s) {
        String defaultUnit;
        if (s.startsWith("M/") || s.startsWith("m/")) {
            defaultUnit = "D";
        } else {
            defaultUnit = s.substring(0, 1).toUpperCase();
        }
        int firstPlusIdx = s.indexOf("+");
        int firstMinusIdx = s.indexOf("-");
        if (firstPlusIdx == -1 && firstMinusIdx == -1) return TimeDelta.zero();
        int deltaStartIdx = firstPlusIdx == -1 ? firstMinusIdx :
            firstMinusIdx == -1 ? firstPlusIdx :
            firstPlusIdx < firstMinusIdx ? firstPlusIdx : firstMinusIdx;

        String deltaStr = s.substring(deltaStartIdx);
        Pattern pattern = Pattern.compile("([+-][1-9][0-9]*)([MDY])?");
        Matcher matcher = pattern.matcher(deltaStr);

        long yDelta = 0;
        long mDelta = 0;
        long dDelta = 0;

        int findStart = 0;
        while (matcher.find(findStart)) {
            if (matcher.start() != findStart) {
                throw new IllegalArgumentException(String.format("invalid time delta format: '%s'", deltaStr));
            }
            String number = matcher.group(1);
            String unit = Optional.ofNullable(matcher.group(2)).orElse(defaultUnit);

            long delta = Long.parseLong(number);
            if ("D".equals(unit)) {
                dDelta = +delta;
            } else if ("M".equals(unit)) {
                mDelta = +delta;
            } else if ("Y".equals(unit)) {
                yDelta += delta;
            }
            findStart = matcher.end();
        }
        return new TimeDelta(yDelta, mDelta, dDelta);
    }
}
