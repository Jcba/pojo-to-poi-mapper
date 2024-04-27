package io.github.jcba.poimapper;

/**
 * Thrown when an error occurred while accessing data
 */
public class DataAccessException extends RuntimeException {

    /**
     * Thrown when an error occurred while accessing data
     *
     * @param exception the underlying exception
     */
    public DataAccessException(Exception exception) {
        super(exception);
    }
}
