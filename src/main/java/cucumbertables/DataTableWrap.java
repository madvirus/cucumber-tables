package cucumbertables;

import io.cucumber.datatable.DataTable;

import java.util.List;
import java.util.stream.Collectors;

public class DataTableWrap {
    private DataTable dataTable;
    private String nullString;

    public DataTableWrap(DataTable dataTable, String nullString) {
        this.dataTable = dataTable;
        this.nullString = nullString;
    }

    public static DataTableWrap create(DataTable dataTable) {
        return new DataTableWrap(dataTable, null);
    }

    public static DataTableWrap create(DataTable dataTable, String nullString) {
        return new DataTableWrap(dataTable, nullString);
    }

    public List<MapRowWrap> getMapRows() {
        return dataTable.asMaps().stream()
                .map(row -> new MapRowWrap(row, nullString))
                .collect(Collectors.toList());
    }
}
