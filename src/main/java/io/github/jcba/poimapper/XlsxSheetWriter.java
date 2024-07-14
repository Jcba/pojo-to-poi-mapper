package io.github.jcba.poimapper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

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
        rowWriter.writeRow(columnAnnotationParser.findColumnNames());
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

        void writeRow(List<CellData> rowData) {
            startNewRow();
            rowData.forEach(data -> formatCell(createCell(data), data));
        }

        private void startNewRow() {
            columnIndex = new AtomicInteger();
            currentRow = sheet.createRow(rowIndex++);
        }

        private Cell createCell(CellData cellData) {
            var cell = currentRow.createCell(columnIndex.getAndIncrement());

            CellTypeConverter.writeValueToCell(
                    cell,
                    cellData.value(),
                    cellData.type()
            );

            return cell;
        }

        private void formatCell(Cell cell, CellData cellData) {
            Optional.ofNullable(cellData.formatString())
                    .ifPresent(format -> new CellFormatter(workbook).format(cell, format));
        }
    }
}