package com.github.jcba.poimapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

class ColumnAnnotationParser<T> {

    private final Class<T> type;

    ColumnAnnotationParser(Class<T> type) {
        this.type = type;
    }

    List<String> findColumnNames() {
        return Arrays.stream(type.getDeclaredFields())
                .map(ColumnAnnotationParser::findColumnName)
                .filter(Objects::nonNull)
                .toList();
    }

    List<AnnotatedFieldData> parse(T rowObject) {
        return Arrays.stream(type.getDeclaredFields())
                .map(field -> findAnnotatedFieldProperties(field, rowObject))
                .filter(Objects::nonNull)
                .toList();
    }

    private AnnotatedFieldData findAnnotatedFieldProperties(Field field, T rowObject) {
        var columnAnnotation = field.getAnnotation(Column.class);

        if (null == columnAnnotation) {
            return null;
        }

        if (field.trySetAccessible()) {
            try {
                var valueObject = field.get(rowObject);
                return new AnnotatedFieldData(
                        nonNullListOf(columnAnnotation, field.getAnnotation(ColumnFormat.class)),
                        valueObject,
                        field.getName()
                );
            } catch (IllegalAccessException e) {
                throw new DataAccessException(e);
            }
        }

        return null;
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

    @SafeVarargs
    private <A> List<A> nonNullListOf(A... items) {
        return Stream.of(items).filter(Objects::nonNull).toList();
    }

}