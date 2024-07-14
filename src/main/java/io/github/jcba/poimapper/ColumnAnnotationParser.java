package io.github.jcba.poimapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class ColumnAnnotationParser<T> {

    private final Class<T> type;

    ColumnAnnotationParser(Class<T> type) {
        this.type = type;
    }

    List<CellData> findColumnNames() {
        return Arrays.stream(type.getDeclaredFields())
                .map(ColumnAnnotationParser::findColumnName)
                .filter(Objects::nonNull)
                .map(value -> new CellData(null, CellType.STRING, value))
                .toList();
    }

    List<CellData> parse(T rowObject) {
        return Arrays.stream(type.getDeclaredFields())
                .map(field -> findAnnotatedFieldProperties(field, rowObject))
                .filter(Objects::nonNull)
                .toList();
    }

    private CellData findAnnotatedFieldProperties(Field field, T rowObject) {
        var columnAnnotation = field.getAnnotation(Column.class);

        if (null == columnAnnotation) {
            return null;
        }

        if (field.trySetAccessible()) {
            try {
                var valueObject = field.get(rowObject);
                return new CellData(
                        findFieldFormat(field),
                        columnAnnotation.type(),
                        valueObject
                );
            } catch (IllegalAccessException e) {
                throw new DataAccessException(e);
            }
        }

        return null;
    }

    private static String findFieldFormat(Field field) {
        var format = field.getAnnotation(ColumnFormat.class);
        if (null == format) {
            return null;
        }
        return format.value();
    }

    private static String findColumnName(Field field) {
        var columnAnnotation = field.getAnnotation(Column.class);

        if (null == columnAnnotation) {
            return null;
        }

        if (Column.USE_FIELD_NAME.equals(columnAnnotation.columnName())) {
            return field.getName();
        }

        return columnAnnotation.columnName();
    }

}