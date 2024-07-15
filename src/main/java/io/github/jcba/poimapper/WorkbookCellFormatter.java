package io.github.jcba.poimapper;

import org.apache.poi.ss.usermodel.*;

class WorkbookCellFormatter {
    private final DataFormat dataFormat;
    private final CellStyle cellStyle;

    WorkbookCellFormatter(Workbook workbook) {
        this.dataFormat = workbook.createDataFormat();
        this.cellStyle = workbook.createCellStyle();
    }

    void formatCell(Cell cell, String columnFormat){
        short format = dataFormat.getFormat(columnFormat);
        cellStyle.setDataFormat(format);
        cell.setCellStyle(cellStyle);
    }
}
