package io.github.jcba.poimapper;

import org.apache.poi.ss.usermodel.*;

class CellFormatter {
    private final DataFormat dataFormat;
    private final CellStyle cellStyle;

    CellFormatter(Workbook workbook) {
        this.dataFormat = workbook.createDataFormat();
        this.cellStyle = workbook.createCellStyle();
    }

    void format(Cell cell, String columnFormat){
        short format = dataFormat.getFormat(columnFormat);
        cellStyle.setDataFormat(format);
        cell.setCellStyle(cellStyle);
    }
}
