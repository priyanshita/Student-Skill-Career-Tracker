package com.tracker.model;

/**
 * Technical skill entity extending abstract base class Skill.
 * Demonstrates Inheritance, Method Overriding, super constructor invocation, and specialized features.
 */
public class TechnicalSkill extends Skill {
    private String primaryLanguage;
    private String frameworksList;

    public TechnicalSkill() {
        super();
        this.primaryLanguage = "Java";
        this.frameworksList = "General";
    }

    public TechnicalSkill(String id, String name, ProficiencyLevel proficiency, SkillCategory category,
                          String primaryLanguage, String frameworksList) {
        super(id, name, proficiency, category);
        this.primaryLanguage = primaryLanguage;
        this.frameworksList = frameworksList;
    }

    @Override
    public double getProficiencyWeight() {
        // Technical skills carry full proficiency weight as configured in enum
        return getProficiency().getWeight();
    }

    @Override
    public String getDetails() {
        return "Lang: " + primaryLanguage + " | Frameworks/Tools: " + frameworksList;
    }

    @Override
    public String getSkillType() {
        return "TECHNICAL";
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    public String getFrameworksList() {
        return frameworksList;
    }

    public void setFrameworksList(String frameworksList) {
        this.frameworksList = frameworksList;
    }

    @Override
    public String toDataString() {
        // Format: TECHNICAL|id|name|proficiency|category|primaryLanguage|frameworksList
        return "TECHNICAL|" + getId() + "|" + getName() + "|" + getProficiency().name() + "|" +
                getCategory().name() + "|" + primaryLanguage + "|" + frameworksList;
    }
}
