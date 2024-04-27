package io.github.jcba.poimapper;

import java.lang.annotation.*;

/**
 * Fields annotated with this annotation are exported to a Sheet.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Column {

    /**
     * An empty string indicates that the field name must be used
     */
    String USE_FIELD_NAME = "";

    /**
     * When not empty, sets the name of the column
     *
     * @return the column name.
     * If not set or set to an empty value, the name of the field will be used.
     */
    String columnName() default USE_FIELD_NAME;

    /**
     * Sets the cell type
     * @return the cell type
     */
    CellType type() default CellType.STRING;
}