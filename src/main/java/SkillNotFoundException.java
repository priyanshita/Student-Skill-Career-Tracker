package com.tracker.exception;

/**
 * Thrown when a requested skill is not found during updates, deletions, or searches.
 */
public class SkillNotFoundException extends TrackerException {
    public SkillNotFoundException(String message) {
        super(message);
    }
}
