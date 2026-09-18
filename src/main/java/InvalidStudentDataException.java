package com.tracker.exception;

/**
 * Thrown when student profile input or data fields violate validation rules.
 */
public class InvalidStudentDataException extends TrackerException {
    public InvalidStudentDataException(String message) {
        super(message);
    }
}
