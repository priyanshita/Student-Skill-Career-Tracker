package com.tracker.exception;

/**
 * Thrown when an attempt is made to add a skill that already exists in the student's skill profile.
 */
public class DuplicateSkillException extends TrackerException {
    public DuplicateSkillException(String message) {
        super(message);
    }
}
