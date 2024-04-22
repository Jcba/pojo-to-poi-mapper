package com.github.jcba.poimapper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.math.BigDecimal;
import java.util.function.Function;

class CellTypeConverter {

    static void writeValueToCell(Cell cell, Object value, CellType type) {
        switch (type) {
            case BOOLEAN -> cell.setCellValue((boolean) value);
            case NUMERIC -> cell.setCellValue(createNumericConverter(value.getClass()).apply(value));
            default -> cell.setCellValue(value.toString());
        }
    }

    static Function<Object, Double> createNumericConverter(Class<?> type) {
        if (Number.class.isAssignableFrom(type)) {
            return a -> (double) a;
        }
        if (BigDecimal.class.isAssignableFrom(type)) {
            return a -> ((BigDecimal) a).doubleValue();
        }
        throw new UnsupportedOperationException(String.format("Can not convert from %s to Numeric Cell type", type));
    }
}
