package com.github.jcba.poimapper;

import org.apache.poi.ss.usermodel.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * A sheet writer writing Xlsx sheets
 *
 * @param <T> The annotated Java Object Type
 */
public class XlsxSheetWriter<T> implements SheetWriter<T> {
    private final Workbook workbook;
    private final Sheet sheet;
    private final ColumnAnnotationParser<T> columnAnnotationParser;
    private final RowWriter rowWriter;

    /**
     * Constructs a new XslxSheetWriter
     *
     * @param type     the annotated Java Object Class Type
     * @param workbook the Apache POI workbook
     * @param sheet    the sheet to write to
     */
    public XlsxSheetWriter(Class<T> type, Workbook workbook, Sheet sheet) {
        this.workbook = workbook;
        this.sheet = sheet;
        columnAnnotationParser = new ColumnAnnotationParser<>(type);
        rowWriter = new RowWriter();
    }

    @Override
    public void write(Stream<T> input) {
        writeHeader();
        writeContent(input);
    }

    private void writeHeader() {
        rowWriter.writeTextRow(columnAnnotationParser.findColumnNames());
    }

    private void writeContent(Stream<T> input) {
        input
                .map(columnAnnotationParser::parse)
                .forEach(rowWriter::writeRow);
    }

    private class RowWriter {
        private int rowIndex = 0;
        private Row currentRow;
        private AtomicInteger columnIndex;

        void writeTextRow(List<String> textRowData) {
            createRow();
            textRowData.forEach(this::createCell);
        }

        void writeRow(List<AnnotatedFieldData> rowData) {
            createRow();
            rowData.forEach(data -> formatCell(createCell(data), data));
        }

        private void createRow(){
            columnIndex = new AtomicInteger();
            currentRow = sheet.createRow(rowIndex++);
        }

        private void createCell(String data) {
            var cell = currentRow.createCell(columnIndex.getAndIncrement());
            cell.setCellValue(data);
        }

        private Cell createCell(AnnotatedFieldData annotatedFieldData) {
            var cell = currentRow.createCell(columnIndex.getAndIncrement());
            findAnnotation(ColumnType.class, annotatedFieldData).ifPresentOrElse(
                    columnType -> writeValueToCell(cell, annotatedFieldData.value(), columnType.value()),
                    () -> cell.setCellValue(annotatedFieldData.value().toString())
            );
            return cell;
        }

        private void writeValueToCell(Cell cell, Object value, CellType type) {
            switch (type) {
                case BOOLEAN -> cell.setCellValue((boolean) value);
                case NUMERIC -> cell.setCellValue((double) value);
                default -> cell.setCellValue(value.toString());
            }
        }

        private void formatCell(Cell cell, AnnotatedFieldData annotatedFieldData) {
            findAnnotation(ColumnFormat.class, annotatedFieldData)
                    .ifPresent(annotation -> new CellFormatter(workbook).format(cell, annotation));
        }

        private static <A> Optional<A> findAnnotation(Class<A> type, AnnotatedFieldData annotatedFieldData) {
            return annotatedFieldData.annotations().stream()
                    .filter(a -> a.annotationType().equals(type))
                    .map(type::cast)
                    .findFirst();
        }
    }
}