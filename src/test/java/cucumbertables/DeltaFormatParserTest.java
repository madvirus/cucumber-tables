package cucumbertables;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeltaFormatParserTest {

    private void assertDelta(String s, long expectedYearDelta, long expectedMonthDelta, long expectedDayDelta) {
        TimeDelta delta = DeltaFormatParser.parse(s);
        assertThat(delta.getYearDelta()).describedAs("year delta")
                .isEqualTo(expectedYearDelta);
        assertThat(delta.getMonthDelta())
                .describedAs("month delta").isEqualTo(expectedMonthDelta);
        assertThat(delta.getDayDelta())
                .describedAs("day delta").isEqualTo(expectedDayDelta);
    }

    @Test
    void dday() {
        assertDelta("D", 0L, 0L, 0L);
        assertDelta("D+1", 0L, 0L, 1L);
        assertDelta("D-1", 0L, 0L, -1L);
        assertDelta("D+3D", 0L, 0L, 3L);
        assertDelta("D-3D", 0L, 0L, -3L);
        assertDelta("D+1+1M", 0L, 1L, 1L);
        assertDelta("D-3M+1D", 0L, -3L, 1L);
        assertDelta("D+1Y", 1L, 0L, 0L);
    }

    @Test
    void mday() {
        assertDelta("M/1", 0L, 0L, 0L);
        assertDelta("M/1+1", 0L, 0L, 1L);
        assertDelta("M/1-3", 0L, 0L, -3L);
    }

    @Test
    void mmonth() {
        assertDelta("M", 0L, 0L, 0L);
        assertDelta("M+1", 0L, 1L, 0L);
        assertDelta("M-1", 0L, -1L, 0L);
        assertDelta("M+3M", 0L, 3L, 0L);
        assertDelta("M-5M", 0L, -5L, 0L);
        assertDelta("M-1Y", -1L, 0L, 0L);
    }

    @Test
    void yyear() {
        assertDelta("Y", 0L, 0L, 0L);
        assertDelta("Y+1", 1L, 0L, 0L);
        assertDelta("Y-1", -1L, 0L, 0L);
        assertDelta("Y+5Y", 5L, 0L, 0L);
        assertDelta("Y-3Y", -3L, 0L, 0L);
    }

}
