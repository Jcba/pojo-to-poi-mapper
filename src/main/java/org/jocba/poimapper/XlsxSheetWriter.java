package org.jocba.poimapper;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class XlsxSheetWriter<T> implements SheetWriter<T> {
    private final Sheet sheet;
    private final ColumnAnnotationParser<T> columnAnnotationParser;
    private int rowIndex = 0;

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