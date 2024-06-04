# POJO to Apache POI Excel Mapper

This library facilitates the mapping of annotated Java Objects to [Apache POI](https://poi.apache.org/) Excel sheets. 
It offers an intuitive and flexible extension of Apache POI for developers looking to use annotated POJO's to create Excel sheets.

## Features
* Mapping: object mapping to sheets using annotations.
* Streaming API Support: Compatible with Apache POI's streaming API for efficient memory usage.
* Java version: Compatible with Java 17 or higher.

## How to Use
### Add Dependency:

Add the library dependency to your Maven or Gradle project:

```xml
<!-- Maven -->
<dependency>
    <groupId>io.github.jcba</groupId>
    <artifactId>pojo-to-apache-poi-mapper</artifactId>
    <version>0.0.1</version>
</dependency>
```

```groovy
// Gradle
implementation 'io.github.jcba:pojo-to-apache-poi-mapper:0.0.1'
```

### Define Java Object and configure mapping

Define your Java object and annotate fields with the @Column annotation. Only annotated fields will be mapped.

```java
record MyObject (

        @Column(columnName = "product")
        String productName,

        @Column(type = CellType.NUMERIC)
        BigDecimal price,

        @Column
        String description
) {}
```

The columnName parameter is optional. If omitted, the name of the field will be used as column name.

By default, all values will be written as String. It is possible to specify the desired cell type with the 'type' option. 

The field ordering in the object determines the column ordering in the sheet.

### Map Java Object to Excel:

```java 
var testData = List.of(new MyObject("product1", BigDecimal.TEN, "description"));

// Apache POI workbook and sheet
var workbook = new XSSFWorkbook();
var sheet = workbook.createSheet("my-sheet-name");

// this is what this library adds
new XlsxSheetWriter<>(MyObject.class, workbook, sheet).write(testData.stream());

// write workbook to file
var out = new FileOutputStream("/tmp/demo.xlsx");
workbook.write(out);
out.close();
```

### Use Streaming API

The streaming API can be more memory-efficient when writing large files. 

```java
// Apache POI workbook and sheet 
// For more information, see https://poi.apache.org/components/spreadsheet/how-to.html#sxssf
var workbook = new SXSSFWorkbook(100); // keep 100 rows in memory, exceeding rows will be flushed to disk
var sheet = wb.createSheet();

// this is what this library adds
new XlsxSheetWriter<>(MyObject.class, workbook, sheet).write(testData.stream());

// write workbook to file
var out = new FileOutputStream("/tmp/demo.xlsx");
workbook.write(out);
out.close();

// dispose of temporary files backing this workbook on disk
workbook.dispose();
```

## Contribution
Contributions are welcome! If you find any issues or have suggestions for improvements, feel free to open an issue or create a pull request on GitHub.

## License
This library is licensed under the Apache v2 license. See the LICENSE file for details.

For more information, please visit the GitHub repository.
