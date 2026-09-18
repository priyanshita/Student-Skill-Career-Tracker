package com.tracker.exception;

/**
 * Base custom checked exception for Student Skill & Career Tracker application.
 */
public class TrackerException extends Exception {
    public TrackerException(String message) {
        super(message);
    }

    public TrackerException(String message, Throwable cause) {
        super(message, cause);
    }
}
