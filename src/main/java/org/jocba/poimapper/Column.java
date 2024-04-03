package org.jocba.poimapper;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Column {

    String USE_FIELD_NAME = "";

    /**
     * Sets a column name.
     * <p>
     * If not set or set to an empty value, the name of the field will be used.
     */
    String columnName() default USE_FIELD_NAME;
}