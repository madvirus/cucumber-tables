package cucumbertables;

import io.cucumber.datatable.DataTable;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DataTableWrapTest {
    @Test
    void tableWrapBasic() {
        DataTable dataTable = DataTable.create(Arrays.asList(
                Arrays.asList("no", "name", "etc", "date"),
                Arrays.asList("1", "Independence Movement Day", "str13", "1919-03-01"),
                Arrays.asList("2", "null", "", ""),
                Arrays.asList("", "value1", "value2", "")
        ));
        DataTableWrap table = DataTableWrap.create(dataTable);

        List<MapRowWrap> rows = table.getMapRows();
        assertThat(rows).hasSize(3);
        assertThat(rows.get(0).getInteger("no")).isEqualTo(1);
        assertThat(rows.get(0).getString("name")).isEqualTo("Independence Movement Day");
        assertThat(rows.get(0).getString("etc")).isEqualTo("str13");
        assertThat(rows.get(0).getLocalDate("date", "yyyy-MM-dd")).isEqualTo(LocalDate.of(1919, 3, 1));

        assertThat(rows.get(1).getInteger("no")).isEqualTo(2);
        assertThat(rows.get(1).getString("name")).isEqualTo("null");
        assertThat(rows.get(1).getString("etc")).isEmpty();
        assertThat(rows.get(1).getLocalDate("date", "yyyy-MM-dd")).isNull();

        assertThat(rows.get(2).getInteger("no")).isNull();
        assertThat(rows.get(2).getString("name")).isEqualTo("value1");
        assertThat(rows.get(2).getString("etc")).isEqualTo("value2");
        assertThat(rows.get(2).getLocalDate("date", "yyyy-MM-dd")).isNull();
    }

    @Test
    void tableWrapNumber() {
        DataTable dataTable = DataTable.create(Arrays.asList(
                Arrays.asList("integer", "double", "long", "comma"),
                Arrays.asList("1", "0.123", "12345", "12,345")
        ));
        DataTableWrap table = DataTableWrap.create(dataTable);
        MapRowWrap row = table.getMapRows().get(0);
        assertThat(row.getInteger("integer")).isEqualTo(Integer.valueOf(1));
        assertThat(row.getDouble("double")).isEqualTo(Double.valueOf(0.123));
        assertThat(row.getLong("long")).isEqualTo(Long.valueOf(12345));
        assertThat(row.getBigDecimal("double")).isEqualTo(BigDecimal.valueOf(0.123));
        assertThat(row.getInteger("comma")).isEqualTo(Integer.valueOf(12345));
    }

    @Test
    void tableWrapLocalDate() {
        DataTable dataTable = DataTable.create(Arrays.asList(
                Arrays.asList("date1", "date2"),
                Arrays.asList("2021-03-31", "20210331")
        ));
        DataTableWrap table = DataTableWrap.create(dataTable);
        MapRowWrap row = table.getMapRows().get(0);
        assertThat(row.getLocalDate("date1")).isEqualTo(LocalDate.of(2021, 3, 31));
        assertThat(row.getLocalDate("date1", "yyyy-MM-dd")).isEqualTo(LocalDate.of(2021, 3, 31));
        assertThat(row.getLocalDate("date2", "yyyyMMdd")).isEqualTo(LocalDate.of(2021, 3, 31));
    }

    @Test
    void tableWrapLocalDateTime() {
        DataTable dataTable = DataTable.create(Arrays.asList(
                Arrays.asList("datetime1", "datetime2"),
                Arrays.asList("2021-03-31 14:50:15", "20210331145015")
        ));
        DataTableWrap table = DataTableWrap.create(dataTable);
        MapRowWrap row = table.getMapRows().get(0);
        assertThat(row.getLocalDateTime("datetime1")).isEqualTo(LocalDateTime.of(2021, 3, 31, 14, 50, 15));
        assertThat(row.getLocalDateTime("datetime1", "yyyy-MM-dd HH:mm:ss")).isEqualTo(LocalDateTime.of(2021, 3, 31, 14, 50, 15));
        assertThat(row.getLocalDateTime("datetime2", "yyyyMMddHHmmss")).isEqualTo(LocalDateTime.of(2021, 3, 31, 14, 50, 15));
    }

    @Test
    void tableWrapLocalTime() {
        DataTable dataTable = DataTable.create(Arrays.asList(
                Arrays.asList("time1", "time2"),
                Arrays.asList("14:50:15", "145015")
        ));
        DataTableWrap table = DataTableWrap.create(dataTable);
        MapRowWrap row = table.getMapRows().get(0);
        assertThat(row.getLocalTime("time1")).isEqualTo(LocalTime.of(14, 50, 15));
        assertThat(row.getLocalTime("time1", "HH:mm:ss")).isEqualTo(LocalTime.of(14, 50, 15));
        assertThat(row.getLocalTime("time2", "HHmmss")).isEqualTo(LocalTime.of(14, 50, 15));
    }

    @Test
    void tableWrapWithNullString() {
        DataTable dataTable = DataTable.create(Arrays.asList(
                Arrays.asList("no", "name"),
                Arrays.asList("1", "\\N"),
                Arrays.asList("\\N", "")
        ));
        DataTableWrap table = DataTableWrap.create(dataTable, "\\N");

        List<MapRowWrap> rows = table.getMapRows();
        assertThat(rows).hasSize(2);
        assertThat(rows.get(0).getInteger("no")).isEqualTo(1);
        assertThat(rows.get(0).getString("name")).isNull();
        assertThat(rows.get(1).getInteger("no")).isNull();
        assertThat(rows.get(1).getString("name")).isEqualTo("");
    }

    @Test
    void ignoreEmptyNullString() {
        DataTable dataTable = DataTable.create(Arrays.asList(
                Arrays.asList("no", "name"),
                Arrays.asList("1", "")
        ));
        DataTableWrap table = DataTableWrap.create(dataTable, "");

        List<MapRowWrap> rows = table.getMapRows();
        assertThat(rows.get(0).getInteger("no")).isEqualTo(1);
        assertThat(rows.get(0).getString("name")).isEqualTo("");
    }

    @Test
    void ignoreBlankNullString() {
        DataTable dataTable = DataTable.create(Arrays.asList(
                Arrays.asList("no", "name"),
                Arrays.asList("1", " ")
        ));
        DataTableWrap table = DataTableWrap.create(dataTable, " ");

        List<MapRowWrap> rows = table.getMapRows();
        assertThat(rows.get(0).getInteger("no")).isEqualTo(1);
        assertThat(rows.get(0).getString("name")).isEqualTo(" ");
    }

}
