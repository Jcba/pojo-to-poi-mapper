package io.github.jcba.poimapper;

import org.apache.poi.ss.usermodel.Cell;

import java.util.function.Function;

class CellTypeConverter {

    static void writeValueToCell(Cell cell, Object value, CellType type) {
        switch (type) {
            case BOOLEAN -> cell.setCellValue(createBooleanConverter(value.getClass()).apply(value));
            case NUMERIC -> cell.setCellValue(createNumericConverter(value.getClass()).apply(value));
            default -> cell.setCellValue(value.toString());
        }
    }

    private static Function<Object, Boolean> createBooleanConverter(Class<?> type) {
        if (Boolean.class.isAssignableFrom(type)) {
            return b -> ((Boolean) b);
        }
        throw new UnsupportedOperationException(String.format("Can not convert from %s to Boolean Cell type", type));
    }

    private static Function<Object, Double> createNumericConverter(Class<?> type) {
        if (Number.class.isAssignableFrom(type)) {
            return a -> ((Number) a).doubleValue();
        }
        throw new UnsupportedOperationException(String.format("Can not convert from %s to Numeric Cell type", type));
    }
}
