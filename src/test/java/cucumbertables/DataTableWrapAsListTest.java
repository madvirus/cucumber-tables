package cucumbertables;

import io.cucumber.datatable.DataTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DataTableWrapAsListTest {

    @Test
    void tableWrapAsList() {
        DataTable dataTable = DataTable.create(Arrays.asList(
                Arrays.asList("no", "name", "etc", "date"),
                Arrays.asList("1", "Independence Movement Day", "str13", "1919-03-01"),
                Arrays.asList("2", "null", "", ""),
                Arrays.asList("", "value1", "value2", "")
        ));
        DataTableWrap table = DataTableWrap.create(dataTable);

        List<DateInfo> rows = table.getListAs(DateInfo.class);
        assertThat(rows).hasSize(3);
        DateInfo dateInfo0 = rows.get(0);
        assertThat(dateInfo0.getNo()).isEqualTo(1);
        assertThat(dateInfo0.getName()).isEqualTo("Independence Movement Day");
        assertThat(dateInfo0.getEtc()).isEqualTo("str13");
        assertThat(dateInfo0.getDate()).isEqualTo(LocalDate.of(1919, 3, 1));
    }

    @Test
    void nameNotMatchingError() {
        DataTable dataTable = DataTable.create(Arrays.asList(
                Arrays.asList("no", "name", "etcx", "date"),
                Arrays.asList("1", "Independence Movement Day", "str13", "1919-03-01"),
                Arrays.asList("2", "null", "", ""),
                Arrays.asList("", "value1", "value2", "")
        ));
        DataTableWrap table = DataTableWrap.create(dataTable);

        Assertions.assertThatThrownBy(() -> table.getListAs(DateInfo.class, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("etcx");
    }

    @Test
    void nameNotMatchingErrorByOption() {
        DataTable dataTable = DataTable.create(Arrays.asList(
                Arrays.asList("no", "name", "etcx", "date"),
                Arrays.asList("1", "Independence Movement Day", "str13", "1919-03-01"),
                Arrays.asList("2", "null", "", ""),
                Arrays.asList("", "value1", "value2", "")
        ));
        DataTableWrap table = DataTableWrap.create(dataTable);

        Assertions.assertThatThrownBy(() -> table.getListAs(DateInfo.class, ConvertOptions.ERROR_ON_NO_MATCHING_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("etcx");
    }

    public static class DateInfo {
        private Integer no;
        private String name;
        private String etc;
        private LocalDate date;

        public Integer getNo() {
            return no;
        }

        public String getName() {
            return name;
        }

        public String getEtc() {
            return etc;
        }

        public LocalDate getDate() {
            return date;
        }
    }
}
