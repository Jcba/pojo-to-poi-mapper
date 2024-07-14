package io.github.jcba.poimapper;

record CellData(
        String formatString,
        String columnName,
        CellType type,
        Object value
) {
}