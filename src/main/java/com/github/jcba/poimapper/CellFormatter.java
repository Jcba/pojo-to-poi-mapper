package com.github.jcba.poimapper;

import org.apache.poi.ss.usermodel.*;

class CellFormatter {
    private final DataFormat dataFormat;
    private final CellStyle cellStyle;

    public CellFormatter(Workbook workbook) {
        this.dataFormat = workbook.createDataFormat();
        this.cellStyle = workbook.createCellStyle();
    }

    public void format(Cell cell, ColumnFormat columnFormat){
        short format = dataFormat.getFormat(columnFormat.value());
        cellStyle.setDataFormat(format);
        cell.setCellStyle(cellStyle);
    }
}
