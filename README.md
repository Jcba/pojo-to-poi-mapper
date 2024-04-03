# POJO to Apache POI Excel Mapper

This library facilitates the mapping of Java Objects to [Apache POI](https://poi.apache.org/) Excel sheets, like Jackson does for JSON. 
It offers an intuitive and flexible extension of Apache POI for developers looking to use annotated POJO's to create Excel sheets.

## Features
* Mapping: Pojo mapping to sheets using annotations
* Streaming API Support: Compatible with Apache POI's streaming API for efficient memory usage with large datasets.
* Java version: Compatible with Java 17 or higher.

## How to Use
### Add Dependency:

Add the library dependency to your Maven or Gradle project:

```xml
<!-- Maven -->
<dependency>
    <groupId>org.jocba</groupId>
    <artifactId>pojo-to-apache-poi-mapper</artifactId>
    <version>1.0.0</version>
</dependency>
```

```groovy
// Gradle
implementation 'org.jocba:pojo-to-apache-poi-mapper:1.0.0'
```

### Define Java Object and configure mapping

Define your Java object with properties that you want to export to an Excel sheet. Annotate your Java object properties with @Column to specify their mapping to Excel cells. Only annotated fields will be mapped.

```java
record MyObject (

        @Column(columnName = "product")
        String productName,

        @Column
        BigDecimal price,

        @Column
        String description
) {}
```

In the Column annotation, the columnName parameter is optional. If omitted, the name of the field will be used as column name.

The field ordering in the object determines the column ordering in the sheet.

### Map Java Object to Excel:

```java 
var testData = List.of(new MyObject("product1", BigDecimal.TEN, "description"));

// Apache POI workbook and sheet
var workbook = new XSSFWorkbook();
var sheet = workbook.createSheet("my-sheet-name");

// this is what this library adds
new XlsxSheetWriter<>(MyObject.class, sheet).write(testData.stream());

// write workbook to file
```

### Use Streaming API

The streaming API can be more memory-efficient when writing large files. 

```java
// Apache POI workbook and sheet 
// For more information, see https://poi.apache.org/components/spreadsheet/how-to.html#sxssf
var workbook = new SXSSFWorkbook(100); // keep 100 rows in memory, exceeding rows will be flushed to disk
var sheet = wb.createSheet();

// this is what this library adds
new XlsxSheetWriter<>(MyObject.class, sheet).write(testData.stream());

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