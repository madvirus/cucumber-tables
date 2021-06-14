# cucumber-tables

cucumber-tables is utility library for cucumber DataTable.

The following list is some features:

* Get number type value from DataTable row
* Get LocalDateTime, LocalDate, YearMonth, Year from DataTable row
* Support marker string for getting null
* Support D-day format for LocalDate type
* Support M-month format for YearMonth type
* Support Y-year format for Year type
* Support now string for LocalDateTime, LocalTime type
* Support number format using comma or underscore for Integer, Long, Float, BigDecimal type
* Get object from DataTable row / Copy DataTable row values to object

## Repository

### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.github.madvirus</groupId>
    <artifactId>cucumber-tables</artifactId>
    <version>0.3.10</version>
    <scope>test</scope>
</dependency>
```

### Gradle
```
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    testImplementation 'com.github.madvirus:cucumber-tables:0.3.7'
}
```

## Using cucumber-tables

### Feature file using table

```
Feature: sample feature
  Scenario: sample scenario
    Given given table
    | no | name                      | date       | refNo | regDt |
    | 1  | Independence Movement Day | 1919-03-01 |       |       |
```

### Using DataTableWrap in step definition code

1. Create DataTableWrap
2. get MapRowWrap list
3. get value by using getXXX method of MapRowWrap or get object by convertTo/copyTo method of MapRowWrap

```
public class SampleStep {
    @Given("given table")
    public void given_table(io.cucumber.datatable.DataTable dataTable) {
        // Create DataTableWrap
        DataTableWrap table = DataTableWrap.create(dataTable);
        // get MapRowWrapList
        List<MapRowWrap> rows = table.getMapRows();
        MapRowWrap row = rows.get(0);
        // get Integer value by using getInteger method of MapRowWrap
        Integer no = row.getInteger("no");
        // get String value by using getString method of MapRowWrap
        String name = row.getString("name");
        // get LocalDate value by using getLocalDate method of MapRowWrap
        LocalDate date = row.getLocalDate("date", "yyyy-MM-dd");
        Integer refno = row.getInteger("refNo"); // null (empty value to null number type)
        LocalDate regDt = row.getLocalDate("regDt", "yyyy-MM-dd"); // null
    }
}
```

## Using marker for null

If you use null marker string as value of table cell,
then DataTableWrap get null for null marker string. 

### feature file using null marker

If null marker string is "`<null>`", you use "`<null>`" string for null. 

```
Feature: sample feature
  Scenario: sample scenario
    Given given table
    | no | name    | date       |
    | 1  | <null>  | 2021-03-31 |
```

### Using DataTableWrap in step definition code

Use DataTableWrap.create(dataTable, "`<null>`"):

```
public class SampleStep {
    @Given("given table")
    public void given_table(io.cucumber.datatable.DataTable dataTable) {
        DataTableWrap table = DataTableWrap.create(dataTable, "<null>"); // create with null marker
        List<MapRowWrap> rows = table.getMapRows();
        MapRowWrap row = rows.get(0);
        String name = row.getString("name"); // name == null   
    }
}
```

## Using comma or underscore for number type value

### feature file using comma or underscore format
```
Feature: sample feature
  Scenario: sample scenario
    Given given table
    | num1 | num2  | num3  |
    | 1234 | 1,234 | 1_234 |
```

### Using DataTableWrap in step definition code

```
public class SampleStep {
    @Given("given table")
    public void given_table(io.cucumber.datatable.DataTable dataTable) {
        DataTableWrap table = DataTableWrap.create(dataTable);
        List<MapRowWrap> rows = table.getMapRows();
        MapRowWrap row = rows.get(0);
        Integer num1 = row.getInteger("num1"); // 1234
        Integer num2 = row.getInteger("num2"); // 1234
        Integer num3 = row.getInteger("num3"); // 1234
    }
}
```

## Using Temporal type

### feature file
```
Feature: sample feature
  Scenario: sample scenario
    Given given table
    | year | yearMonth | localDate  | localDateTime       | localTime | 
    | 2021 | 2021-06   | 2021-06-11 | 2021-06-11 09:30:00 | 10:45:50  |
```

### Using DataTableWrap in step definition code

```
public class SampleStep {
    @Given("given table")
    public void given_table(io.cucumber.datatable.DataTable dataTable) {
        DataTableWrap table = DataTableWrap.create(dataTable);
        List<MapRowWrap> rows = table.getMapRows();
        MapRowWrap row = rows.get(0);
        Year y = row.getYear("year"); // Year.of(2021)
        YearMonth ym = row.getYearMonth("yearMonth"); // YearMonth.of(2021, 6)
        LocalDate ld = row.getLocalDate("localDate"); // LocalDate.of(2021, 6, 11)
        LocalDateTime ldt = row.getLocalDateTime("localDateTime"); // LocalDateTime.of(2021, 6, 11, 9, 30, 0)
        LocalTime lt = row.getLocalTime("localTime"); // LocalTime.of(10, 45, 50)
    }
}
```

## Using D-day format for LocalDate value
MapRowWrap#getLocalDate() supports "D-day" format.

### D-day format

Examples:
* D : today
* D+1 : LocalDate.now().plusDays(1)
* D-5D : LocalDate.now().minusDays(5)
* D+1M : LocalDate.now().plusMonths(1)
* D+1M+1D : LocalDate.now().plusMonths(1).plusDays(1)
* D+1Y : LocalDate.now().plusYears(1)
* D+1Y+1M : LocalDate.now().plusYears(1).plusMonths(1)
* D+1Y+1M+1D : LocalDate.now().plusYears(1).plusMonths(1).plusDays(1)

### feature file using d-day format

```
Feature: sample feature
  Scenario: sample scenario
    Given given table
    | date1 | date2 | date3 | date4 | date5 |
    | D+3   | D-3   | D     | D+5D  | D+1M  |
```

### Using DataTableWrap in step definition code

```
public class SampleStep {
    @Given("given table")
    public void given_table(io.cucumber.datatable.DataTable dataTable) {
        DataTableWrap table = DataTableWrap.create(dataTable);
        List<MapRowWrap> rows = table.getMapRows();
        MapRowWrap row = rows.get(0);
        LocalDate d1 = row.getLocalDate("date1"); // D+3 : LocalDate.now().plusDays(3);
        LocalDate d2 = row.getLocalDate("date2"); // D-3 : LocalDate.now().minusDays(3);
        LocalDate d3 = row.getLocalDate("date3"); // D : LocalDate.now();
        LocalDate d4 = row.getLocalDate("date4"); // D+5D : LocalDate.now().plusDays(5);
        LocalDate d5 = row.getLocalDate("date5"); // D+1M : LocalDate.now().plusMonths(1);
    }
}
```

## Using M-month format for YearMonth value
MapRowWrap#getYearMonth() supports "M-month" format.

### M-month format

Examples:
* M : YearMonth.now()
* M+1 : YearMonth.now().plusMonths(1)
* M+-3M : YearMonth.now().minusMonths(3)
* M+1Y : YearMonth.now().plusYears(1)
* M+1Y+1M : YearMonth.now().plusYears(1).plusMonths(1)

### feature file using M-month format

```
Feature: sample feature
  Scenario: sample scenario
    Given given table
    | mon1 | mon2 | mon3 |
    | M+1  | M-1  | M    |
```

### Using DataTableWrap in step definition code

```
public class SampleStep {
    @Given("given table")
    public void given_table(io.cucumber.datatable.DataTable dataTable) {
        DataTableWrap table = DataTableWrap.create(dataTable);
        List<MapRowWrap> rows = table.getMapRows();
        MapRowWrap row = rows.get(0);
        YearMonth m1 = row.getYearMonth("mon1"); // M+1 : YearMonth.now().plusMonths(1);
        YearMonth m2 = row.getYearMonth("mon2"); // M-1 : YearMonth.now().plusMonths(-1);
        YearMonth m3 = row.getYearMonth("mon3"); // M : YearMonth.now();
    }
}
```

## Using Y-year format for Year value
MapRowWrap#getYear() supports "Y-year" format.

### Y-year format

Examples:
* Y : Year.now()
* Y+1 : Year.now().plusYears(1)
* Y+5Y : Year.now().plusYears(5)

### feature file using M-month format

```
Feature: sample feature
  Scenario: sample scenario
    Given given table
    | mon1 | mon2 | mon3 |
    | M+1  | M-1  | M    |
```

### Using DataTableWrap in step definition code

```
public class SampleStep {
    @Given("given table")
    public void given_table(io.cucumber.datatable.DataTable dataTable) {
        DataTableWrap table = DataTableWrap.create(dataTable);
        List<MapRowWrap> rows = table.getMapRows();
        MapRowWrap row = rows.get(0);
        YearMonth m1 = row.getYearMonth("mon1"); // M+1 : YearMonth.now().plusMonths(1);
        YearMonth m2 = row.getYearMonth("mon2"); // M-1 : YearMonth.now().plusMonths(-1);
        YearMonth m3 = row.getYearMonth("mon3"); // M : YearMonth.now();
    }
}
```

## Using 'now' format for LocalDateTime, LocalTime value
MapRowWrap#getLocalDateTime() and MapRowWrap#getLocalTime() supports "now" format.

### feature file using d-day format

`now` means current date & time. 

```
Feature: sample feature
  Scenario: sample scenario
    Given given table
    | datetime | time |
    | now      | now  |
```

### Using DataTableWrap in step definition code

```
public class SampleStep {
    @Given("given table")
    public void given_table(io.cucumber.datatable.DataTable dataTable) {
        DataTableWrap table = DataTableWrap.create(dataTable);
        List<MapRowWrap> rows = table.getMapRows();
        MapRowWrap row = rows.get(0);
        LocalDateTime now1 = row.getLocalDateTime("datetime"); // LocalDateTime.now()
        LocalTime now2 = row.getLocalTime("time"); // LocalTime.now()
    }
}
```


## Using D-day format for LocalDateTime value
MapRowWrap#getLocalDateTime() supports "D-day" format.

### D-day format

Examples:
* D 00:00:00: LocalDate.now().atTime(0, 0, 0)
* D+1 13:50:45: LocalDate.now().plusDays(1).atTime(13, 50, 45)

### feature file using d-day format

```
Feature: sample feature
  Scenario: sample scenario
    Given given table
    | dt1        | dt2              |
    | D 00:00:00 | D+1M-1D 12:00:00 |
```

### Using DataTableWrap in step definition code

```
public class SampleStep {
    @Given("given table")
    public void given_table(io.cucumber.datatable.DataTable dataTable) {
        DataTableWrap table = DataTableWrap.create(dataTable);
        List<MapRowWrap> rows = table.getMapRows();
        MapRowWrap row = rows.get(0);
        LocalDateTime dt1 = row.getLocalDateTime("dt1"); // LocalDate.now().atTime(0, 0, 0);
        LocalDateTime dt2 = row.getLocalDate("dt2"); // D-3 : LocalDate.now().plusMonths(1).plusDays(-1).atTime(12, 0, 0);
    }
}
```

## DataTableWrap nullToEmpty option

If you use true of nullToEmpty option, then null cell will be converted empty string:

```
Feature: sample feature
  Scenario: sample scenario
    Given given table
    | name | desc |
    | bk   |      |
```

```
public class SampleStep {
    @Given("given table")
    public void given_table(io.cucumber.datatable.DataTable dataTable) {
        DataTableWrap table = DataTableWrap.create(dataTable, true); // or DataTableWrap.create(dataTable, nullMarker, true); 
        List<MapRowWrap> rows = table.getMapRows();
        assertThat(rows.get(0).getString("desc")).isEmpty();
    }
}
```

Default value of nullToEmpty is false.

## Converting row to object

Use DataTableWrap#getListAs method to convert row values to object:

```
Feature: sample feature
  Scenario: sample scenario
    Given given table
    | name | age |
    | bk   | 10  |
```

```
public class Member {
    private String name;
    private int age;
    
    // getter ...
}

public class SampleStep {
    @Given("given table")
    public void given_table(io.cucumber.datatable.DataTable dataTable) {
        DataTableWrap table = DataTableWrap.create(dataTable);
        List<Member> members = table.getListAs(Member.class);
        Member member = members.get(0);
        assertThat(member.getName()).isEqualTo("bk");
        assertThat(member.getAge()).isEqualTo(10);
    }
}
```

Or use MapRowRap#convertTo method to convert row values to object:

```
public class SampleStep {
    @Given("given table")
    public void given_table(io.cucumber.datatable.DataTable dataTable) {
        DataTableWrap table = DataTableWrap.create(dataTable);
        List<MapRowWrap> rows = table.getMapRows();
        Member member = rows.get(0).convertTo(Member.class);
        assertThat(member.getName()).isEqualTo("bk");
        assertThat(member.getAge()).isEqualTo(10);
    }
}
```

Or use MapRowRap#copyTo method to copy row values to existing object:

```
public class SampleStep {
    @Given("given table")
    public void given_table(io.cucumber.datatable.DataTable dataTable) {
        DataTableWrap table = DataTableWrap.create(dataTable);
        List<MapRowWrap> rows = table.getMapRows();
        Member mem = new Member();
        rows.get(0).copyTo(mem);
        ...
    }
}
```
