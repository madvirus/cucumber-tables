# cucumber-tables

cucumber-tables is utility library for cucumber DataTable.

The following list is some features:

* Get number type value from DataTable row
* Get Temporal type type value from DataTable row
* Support marker string for getting null
* Support D-day format for LocalDate type
* Support number format using comma for Integer, Long, Float, BigDecimal type
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
    <version>0.3.0</version>
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
    testImplementation 'com.github.madvirus:cucumber-tables:0.3.0'
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


## Using comma for number type value

### feature file using d-day format
```
Feature: sample feature
  Scenario: sample scenario
    Given given table
    | num1 | num2  |
    | 1234 | 1,234 |
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
    }
}
```

## Using D-day format for LocalDate value
MapRowWrap#getLocalDate() supports "D-day" format.

### feature file using d-day format

`D` means today. 

```
Feature: sample feature
  Scenario: sample scenario
    Given given table
    | date1 | date2 | date3 |
    | D+3   | D-3   | D     |
```

### Using DataTableWrap in step definition code

```
public class SampleStep {
    @Given("given table")
    public void given_table(io.cucumber.datatable.DataTable dataTable) {
        DataTableWrap table = DataTableWrap.create(dataTable, "<null>"); // create with null marker
        List<MapRowWrap> rows = table.getMapRows();
        MapRowWrap row = rows.get(0);
        LocalDate d1 = row.getLocalDate("date1"); // D+3 : LocalDate.now().plusDays(3);
        LocalDate d2 = row.getLocalDate("date2"); // D-3 : LocalDate.now().minusDays(3);
        LocalDate d3 = row.getLocalDate("date3"); // D : LocalDate.now();
    }
}
```

## Converting row to object

Use MapRowRap#convertTo method to convert row values to object:

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
        DataTableWrap table = DataTableWrap.create(dataTable, "<null>");
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

## DataTableWrap emptyToNull option

If you use true of emptyToNull option, then empty string will be converted null:

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
        assertThat(rows.get(0).getString("desc")).isNull();
    }
}
```

Default value of emptyToNull is false.
