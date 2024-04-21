package com.github.jcba.poimapper;

import org.apache.poi.ss.usermodel.CellType;

import java.lang.annotation.*;

/**
 * Applies a cell Type to all cells of a column
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ColumnType {

    /**
     * Sets the cell type
     * @return the cell type
     */
    CellType value();
}
