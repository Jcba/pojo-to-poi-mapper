package com.github.jcba.poimapper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

class CellTypeConverter {

    static void writeValueToCell(Cell cell, Object value, CellType type) {
        switch (type) {
            case BOOLEAN -> cell.setCellValue((boolean) value);
            case NUMERIC -> cell.setCellValue((double) value);
            default -> cell.setCellValue(value.toString());
        }
    }
}
