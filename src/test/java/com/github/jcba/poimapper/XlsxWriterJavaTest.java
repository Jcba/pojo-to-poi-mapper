package com.github.jcba.poimapper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

public class XlsxWriterJavaTest {

    @Test
    void writesFields_whenAllAreAnnotated() {
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
    void writesFields_whenAnnotated() {
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
    void formatsFields_whenAnnotated() {
        var testRows = List.of(
                new ColumnAnnotatedAndFormatted("row1", 10.1451345),
                new ColumnAnnotatedAndFormatted("row2", 93030.59393910)
        );

        var actualSheet = createWorkbookWithSheetData(ColumnAnnotatedAndFormatted.class, testRows);

        assertThat(toList(actualSheet.getRow(0))).containsExactly("first", "second");
        assertThat(toList(actualSheet.getRow(1))).containsExactly(testRows.get(0).first, "10.15");
        assertThat(toList(actualSheet.getRow(2))).containsExactly(testRows.get(1).first, "93030.59");
    }

    @Test
    void writesColumns_whenEmptySheet() {
        var actualSheet = createWorkbookWithSheetData(NotAllColumnAnnotated.class, List.of());

        assertThat(toList(actualSheet.getRow(0))).containsExactly("first", "column2");
    }

    @Test
    void writesFields_whenUsingStreamingWorkbook() {
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
                .map(Cell::getStringCellValue)
                .toList();
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
            @Column String first,
            @Column @ColumnFormat(value = "#.##", type = CellType.NUMERIC) Double second
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
