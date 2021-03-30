package features;

import cucumbertables.DataTableWrap;
import cucumbertables.MapRowWrap;
import io.cucumber.java.en.Given;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SampleStep {
    @Given("given table")
    public void given_table(io.cucumber.datatable.DataTable dataTable) {
        DataTableWrap table = DataTableWrap.create(dataTable);
        List<MapRowWrap> rows = table.getMapRows();
        MapRowWrap row = rows.get(0);
        Integer no = row.getInteger("no");
        assertThat(no).isEqualTo(1);
        String name = row.getString("name");
        assertThat(name).isEqualTo("Independence Movement Day");
        LocalDate date = row.getLocalDate("date", "yyyy-MM-dd");
        assertThat(date).isEqualTo(LocalDate.of(1919, 3, 1));
    }

}