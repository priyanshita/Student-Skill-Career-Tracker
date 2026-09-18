package com.tracker.util;

import com.tracker.exception.InvalidStudentDataException;
import java.util.regex.Pattern;

/**
 * Utility class providing static input validation methods.
 */
public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    public static void validateNotEmpty(String input, String fieldName) throws InvalidStudentDataException {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidStudentDataException(fieldName + " cannot be empty.");
        }
    }

    public static void validateEmail(String email) throws InvalidStudentDataException {
        validateNotEmpty(email, "Email");
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new InvalidStudentDataException("Invalid email format: " + email);
        }
    }

    public static void validatePositiveYear(int year) throws InvalidStudentDataException {
        if (year < 1 || year > 5) {
            throw new InvalidStudentDataException("Academic year must be between 1 and 5.");
        }
    }

    public static void validateNonNegativeNumber(double value, String fieldName) throws InvalidStudentDataException {
        if (value < 0) {
            throw new InvalidStudentDataException(fieldName + " cannot be negative.");
        }
    }
}
