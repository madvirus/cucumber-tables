# cucumber-tables

cucumber-tables is utility library for cucumber DataTable.

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
    <version>0.1.0</version>
</dependency>
```

### Gradle
```
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.madvirus:cucumber-tables:0.1.0'
}
```


## Using cucumber-tables

### Feature file using table

```
Feature: sample feature
  Scenario: sample scenario
    Given given table
    | no | name                      | date       |
    | 1  | Independence Movement Day | 1919-03-01 |
```

### Using DataTableWrap in step definition code

```
public class SampleStep {
    @Given("given table")
    public void given_table(io.cucumber.datatable.DataTable dataTable) {
        DataTableWrap table = DataTableWrap.create(dataTable);
        List<MapRowWrap> rows = table.getMapRows();
        MapRowWrap row = rows.get(0);
        Integer no = row.getInteger("no");
        assertThat(no).isEqualTo(1);
        String name = row.getString("name");   
    }
}
```

## Using marker for null value

### feature file using null marker
```
Feature: sample feature
  Scenario: sample scenario
    Given given table
    | no | name    | date       |
    | 1  | <null>> | 2021-03-31 |
```

### Using DataTableWrap in step definition code

```
public class SampleStep {
    @Given("given table")
    public void given_table(io.cucumber.datatable.DataTable dataTable) {
        DataTableWrap table = DataTableWrap.create(dataTable, "<null>");
        List<MapRowWrap> rows = table.getMapRows();
        MapRowWrap row = rows.get(0);
        String name = row.getString("name"); // name == null   
    }
}
```
