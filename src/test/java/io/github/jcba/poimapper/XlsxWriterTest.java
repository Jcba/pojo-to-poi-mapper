package io.github.jcba.poimapper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

class XlsxWriterTest {

    @Test
    void writesFields_givenAllAreColumnAnnotated() {
        var testRows = List.of(
                new ColumnAnnotated("val1", "val2", "val3"),
                new ColumnAnnotated("val4", "val5", "val6")
        );

        var actualSheet = createWorkbookWithSheetData(ColumnAnnotated.class, testRows);

        assertThat(toList(actualSheet.getRow(0)))
                .containsExactly("first", "second", "third");
        assertThat(toList(actualSheet.getRow(1)))
                .containsExactly(testRows.get(0).first, testRows.get(0).second, testRows.get(0).third);
        assertThat(toList(actualSheet.getRow(2)))
                .containsExactly(testRows.get(1).first, testRows.get(1).second, testRows.get(1).third);
    }

    @Test
    void writesOnlyAnnotatedFields_givenSomeAreColumnAnnotated() {
        var testRows = List.of(
                new NotAllColumnAnnotated("val1", "val2", "val3"),
                new NotAllColumnAnnotated("val4", "val5", "val6")
        );

        var actualSheet = createWorkbookWithSheetData(NotAllColumnAnnotated.class, testRows);

        assertThat(toList(actualSheet.getRow(0))).containsExactly("first", "column2");
        assertThat(toList(actualSheet.getRow(1))).containsExactly(testRows.get(0).first, testRows.get(0).second);
        assertThat(toList(actualSheet.getRow(2))).containsExactly(testRows.get(1).first, testRows.get(1).second);
    }

    @Test
    void formatsFields_givenColumnFormatAnnotated() {
        var testRows = List.of(
                new ColumnAnnotatedAndFormatted("row1", 10.1451345, true),
                new ColumnAnnotatedAndFormatted("row2", 93030.59393910, false)
        );

        var actualSheet = createWorkbookWithSheetData(ColumnAnnotatedAndFormatted.class, testRows);

        assertThat(toList(actualSheet.getRow(0))).containsExactly("stringValue", "doubleValue", "flag");
        assertThat(toList(actualSheet.getRow(1))).containsExactly(
                testRows.get(0).stringValue,
                testRows.get(0).doubleValue().toString(),
                "true"
        );
        assertThat(toList(actualSheet.getRow(2))).containsExactly(
                testRows.get(1).stringValue,
                testRows.get(1).doubleValue().toString(),
                "false"
        );
    }

    @Test
    void convertsToNumericCellType_givenNumericTypeAnnotation() {
        var testRows = List.of(
                new ColumnAnnotatedNumericTypes(10.1451345, BigDecimal.ONE, 10.0, 5),
                new ColumnAnnotatedNumericTypes(93030.59393910, BigDecimal.TEN, 21.1312, 9 )
        );

        var actualSheet = createWorkbookWithSheetData(ColumnAnnotatedNumericTypes.class, testRows);

        assertThat(toList(actualSheet.getRow(0))).containsExactly(
                "doubleValue", "bigDecimalValue", "primitiveDoubleValue", "primitiveIntValue"
        );
        assertThat(toList(actualSheet.getRow(1))).containsExactly(
                testRows.get(0).doubleValue().toString(),
                testRows.get(0).bigDecimalValue().setScale(1, RoundingMode.UNNECESSARY).toString(),
                ""+testRows.get(0).primitiveDoubleValue(),
                ""+(double) testRows.get(0).primitiveIntValue()
        );
        assertThat(toList(actualSheet.getRow(2))).containsExactly(
                testRows.get(1).doubleValue().toString(),
                testRows.get(1).bigDecimalValue().setScale(1, RoundingMode.UNNECESSARY).toString(),
                ""+testRows.get(1).primitiveDoubleValue(),
                ""+(double) testRows.get(1).primitiveIntValue()
        );
    }

    @Test
    void writesColumns_givenEmptySheet() {
        var actualSheet = createWorkbookWithSheetData(NotAllColumnAnnotated.class, List.of());

        assertThat(toList(actualSheet.getRow(0))).containsExactly("first", "column2");
    }

    @Test
    void writesFields_givenStreamingWorkbook() {
        var testRows = List.of(
                new NotAllColumnAnnotated("val1", "val2", "val3"),
                new NotAllColumnAnnotated("val4", "val5", "val6")
        );

        var actualSheet = createStreamingWorkbookWithSheetData(NotAllColumnAnnotated.class, testRows);

        assertThat(toList(actualSheet.getRow(0))).containsExactly("first", "column2");
        assertThat(toList(actualSheet.getRow(1))).containsExactly(testRows.get(0).first, testRows.get(0).second);
        assertThat(toList(actualSheet.getRow(2))).containsExactly(testRows.get(1).first, testRows.get(1).second);
    }

    private List<String> toList(Row row) {
        return StreamSupport.stream(row.spliterator(), false)
                .map(this::getCellValueAsString)
                .toList();
    }

    private String getCellValueAsString(Cell cell) {
        return switch (cell.getCellType()) {
            case BOOLEAN -> Boolean.valueOf(cell.getBooleanCellValue()).toString();
            case NUMERIC -> Double.valueOf(cell.getNumericCellValue()).toString();
            default -> cell.getStringCellValue();
        };
    }

    record ColumnAnnotated(
            @Column String first,
            @Column String second,
            @Column String third
    ) {
    }

    record NotAllColumnAnnotated(
            @Column String first,
            @Column(columnName = "column2") String second,
            String notAnnotated
    ) {
    }

    record ColumnAnnotatedAndFormatted(
            @Column String stringValue,
            @Column(type = CellType.NUMERIC) @ColumnFormat("#.##") Double doubleValue,
            @Column boolean flag
    ) {
    }

    record ColumnAnnotatedNumericTypes(
            @Column(type = CellType.NUMERIC) Double doubleValue,
            @Column(type = CellType.NUMERIC) BigDecimal bigDecimalValue,
            @Column(type = CellType.NUMERIC) double primitiveDoubleValue,
            @Column(type = CellType.NUMERIC) int primitiveIntValue
    ) {
    }

    private <T> Sheet createWorkbookWithSheetData(Class<T> type, List<T> testData) {
        try (var workbook = new XSSFWorkbook()) {
            var sheet = workbook.createSheet("test");
            new XlsxSheetWriter<>(type, workbook, sheet).write(testData.stream());
            return sheet;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> Sheet createStreamingWorkbookWithSheetData(Class<T> type, List<T> testData) {
        try (var workbook = new SXSSFWorkbook(5)) {
            var sheet = workbook.createSheet("test");
            new XlsxSheetWriter<>(type, workbook, sheet).write(testData.stream());
            return sheet;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
