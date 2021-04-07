package cucumbertables;

import io.cucumber.datatable.DataTable;

import java.util.List;
import java.util.stream.Collectors;

public class DataTableWrap {
    private DataTable dataTable;
    private String nullString;
    private boolean nullToEmpty;

    public DataTableWrap(DataTable dataTable, String nullString, boolean nullToEmpty) {
        this.dataTable = dataTable;
        this.nullString = nullString;
        this.nullToEmpty = nullToEmpty;
    }

    public static DataTableWrap create(DataTable dataTable) {
        return new DataTableWrap(dataTable, null, false);
    }

    public static DataTableWrap create(DataTable dataTable, String nullString) {
        return new DataTableWrap(dataTable, nullString, false);
    }

    public static DataTableWrap create(DataTable dataTable, String nullString, boolean nullToEmpty) {
        return new DataTableWrap(dataTable, nullString, nullToEmpty);
    }

    public static DataTableWrap create(DataTable dataTable, boolean nullToEmpty) {
        return new DataTableWrap(dataTable, null, nullToEmpty);
    }

    public List<MapRowWrap> getMapRows() {
        return dataTable.asMaps().stream()
                .map(row -> new MapRowWrap(row, nullString, nullToEmpty))
                .collect(Collectors.toList());
    }

    public boolean containsColumn(String columnName) {
        List<String> headers = dataTable.row(0);
        return headers.stream().anyMatch(columnName::equals);
    }
}
