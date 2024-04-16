package com.github.jcba.poimapper;

import org.apache.poi.ss.usermodel.CellType;

import java.lang.annotation.*;

/**
 * Applies formatting to all cells of a column
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ColumnFormat {

    /**
     * Formatting to apply to cells of the annotated column
     * @return the applied formatting
     */
    String value();

    /**
     * Sets the cell type
     * @return the cell type
     */
    CellType type();
}
