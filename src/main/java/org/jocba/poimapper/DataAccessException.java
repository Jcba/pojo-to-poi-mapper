package org.jocba.poimapper;

public class DataAccessException extends RuntimeException {

    /**
     * Thrown when an error occurred while accessing data
     *
     * @param exception the underlying exception
     */
    public DataAccessException(Exception exception) {
    }
}
