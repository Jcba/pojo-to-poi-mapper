package com.github.jcba.poimapper;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ColumnAnnotationParserTest {

    @Test
    void findsFieldValue_whenColumnAnnotated() {
        var testRow = new TestRow("value1", "value2", BigDecimal.TEN, "value3");

        var annotationFieldData = new ColumnAnnotationParser<>(TestRow.class).parse(testRow);

        assertThat(annotationFieldData)
                .hasSize(3)
                .extracting(AnnotatedFieldData::value)
                .containsExactly(testRow.first, testRow.second, testRow.third);
    }

    record TestRow(
            @Column
            String first,
            @Column(columnName = "second") String second,
            @Column(columnName = "bigDecimal") BigDecimal third,
            String notAnnotated
    ) {
    }
}