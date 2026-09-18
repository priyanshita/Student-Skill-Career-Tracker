package com.tracker.model;

/**
 * Enum representing student proficiency levels with assigned readiness weights.
 */
public enum ProficiencyLevel {
    BEGINNER("Beginner", 0.40),
    INTERMEDIATE("Intermediate", 0.70),
    ADVANCED("Advanced", 1.00);

    private final String displayName;
    private final double weight;

    ProficiencyLevel(String displayName, double weight) {
        this.displayName = displayName;
        this.weight = weight;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getWeight() {
        return weight;
    }

    public static ProficiencyLevel fromString(String text) {
        if (text == null) return BEGINNER;
        for (ProficiencyLevel level : ProficiencyLevel.values()) {
            if (level.displayName.equalsIgnoreCase(text.trim()) || level.name().equalsIgnoreCase(text.trim())) {
                return level;
            }
        }
        return BEGINNER;
    }
}
