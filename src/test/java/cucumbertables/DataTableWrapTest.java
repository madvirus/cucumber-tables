package cucumbertables;

import io.cucumber.datatable.DataTable;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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
    void nullToEmpty() {
        DataTable dataTable = DataTable.create(Arrays.asList(
                Arrays.asList("no", "name"),
                Arrays.asList("1", "")
        ));
        DataTableWrap table = DataTableWrap.create(dataTable, "", true);
        assertThat(table.getMapRows().get(0).getString("name")).isEmpty();
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

    @Test
    void containsColumn() {
        DataTable dataTable = DataTable.create(Arrays.asList(
                Arrays.asList("no", "name"),
                Arrays.asList("1", " ")
        ));
        DataTableWrap table = DataTableWrap.create(dataTable, " ");
        assertThat(table.containsColumn("name")).isTrue();
        assertThat(table.containsColumn("name2")).isFalse();
    }

    @Test
    void columnNames() {
        DataTable dataTable = DataTable.create(Arrays.asList(
                Arrays.asList("no", "name", "etc"),
                Arrays.asList("1", "", "")
        ));
        DataTableWrap table = DataTableWrap.create(dataTable);
        List<String> colNames = table.columnNames();
        assertThat(colNames).containsExactly("no", "name", "etc");
    }
}
