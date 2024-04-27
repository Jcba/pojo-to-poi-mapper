package io.github.jcba.poimapper;

import java.util.stream.Stream;

/**
 * Describes sheet writer behaviour
 *
 * @param <T> The annotated Java Object Type
 */
public interface SheetWriter<T> {

    /**
     * Writes a stream of objects to a sheet.
     * <p>
     * Only object fields annotated with the Column annotations will be written to the sheet
     * @param input the object stream
     */
    void write(Stream<T> input);
}
