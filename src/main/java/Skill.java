package com.tracker.model;

import com.tracker.interface_def.Storable;
import java.util.Objects;

/**
 * Abstract base class representing a student skill.
 * Demonstrates Abstraction, Inheritance base, Encapsulation, and Polymorphism.
 */
public abstract class Skill implements Storable, Comparable<Skill> {
    private String id;
    private String name;
    private ProficiencyLevel proficiency;
    private SkillCategory category;

    // Default Constructor
    public Skill() {
        this.proficiency = ProficiencyLevel.BEGINNER;
        this.category = SkillCategory.PROGRAMMING;
    }

    // Parameterized Constructor
    public Skill(String id, String name, ProficiencyLevel proficiency, SkillCategory category) {
        this.id = id;
        this.name = name;
        this.proficiency = proficiency;
        this.category = category;
    }

    // Abstract method forcing concrete subclasses to return calculated proficiency weight
    public abstract double getProficiencyWeight();

    // Abstract method forcing concrete subclasses to supply specific details string
    public abstract String getDetails();

    // Abstract method identifying skill type (TECHNICAL vs SOFT)
    public abstract String getSkillType();

    // Getters and Setters (Encapsulation)
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProficiencyLevel getProficiency() {
        return proficiency;
    }

    public void setProficiency(ProficiencyLevel proficiency) {
        this.proficiency = proficiency;
    }

    public SkillCategory getCategory() {
        return category;
    }

    public void setCategory(SkillCategory category) {
        this.category = category;
    }

    // Overridden Comparable interface method for sorting skills alphabetically by name
    @Override
    public int compareTo(Skill other) {
        if (other == null) return 1;
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return Objects.equals(name != null ? name.toLowerCase() : "", 
                              skill.name != null ? skill.name.toLowerCase() : "");
    }

    @Override
    public int hashCode() {
        return Objects.hash(name != null ? name.toLowerCase() : "");
    }

    @Override
    public String toString() {
        return name + " (" + proficiency.getDisplayName() + " - " + category.getDisplayName() + ")";
    }
}
