package com.tracker.model;

/**
 * Enum defining standard skill categories across technical and soft skill domains.
 */
public enum SkillCategory {
    PROGRAMMING("Programming"),
    AI_ML("AI/ML"),
    WEB_DEVELOPMENT("Web Development"),
    DATABASES("Databases"),
    CLOUD("Cloud"),
    SOFT_SKILLS("Soft Skills");

    private final String displayName;

    SkillCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static SkillCategory fromString(String text) {
        if (text == null) return PROGRAMMING;
        for (SkillCategory cat : SkillCategory.values()) {
            if (cat.displayName.equalsIgnoreCase(text.trim()) || cat.name().equalsIgnoreCase(text.trim())) {
                return cat;
            }
        }
        return PROGRAMMING;
    }
}
