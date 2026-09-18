package com.tracker.exception;

/**
 * Thrown when file I/O operations (reading, writing, corrupted data) fail.
 */
public class DataPersistenceException extends TrackerException {
    public DataPersistenceException(String message) {
        super(message);
    }

    public DataPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
