package com.github.jcba.poimapper;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * A sheet writer writing Xlsx sheets
 *
 * @param <T> The annotated Java Object Type
 */
public class XlsxSheetWriter<T> implements SheetWriter<T> {
    private final Sheet sheet;
    private final ColumnAnnotationParser<T> columnAnnotationParser;
    private int rowIndex = 0;

    /**
     * Constructs a new XslxSheetWriter
     *
     * @param type the annotated Java Object Class Type
     * @param sheet the sheet to write to
     */
    public XlsxSheetWriter(Class<T> type, Sheet sheet) {
        this.sheet = sheet;
        columnAnnotationParser = new ColumnAnnotationParser<>(type);
    }

    @Override
    public void write(Stream<T> input) {
        writeHeader();

        input.map(columnAnnotationParser::parse)
                .forEach(annotatedFieldData -> writeRow(toCellValues(annotatedFieldData)));
    }

    private void writeRow(List<?> rowData) {
        var row = sheet.createRow(rowIndex++);
        AtomicInteger columnIndex = new AtomicInteger();
        rowData.forEach(cellData -> row.createCell(columnIndex.getAndIncrement()).setCellValue(cellData.toString()));
    }

    private void writeHeader() {
        writeRow(columnAnnotationParser.findColumnNames());
    }

    private static List<String> toCellValues(List<AnnotatedFieldData> annotatedFieldData) {
        return annotatedFieldData
                .stream()
                .map(data -> data.value().toString())
                .toList();
    }
}