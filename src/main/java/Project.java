package com.tracker.model;

import com.tracker.interface_def.Storable;

/**
 * Model representing a student project.
 */
public class Project implements Storable {
    private String id;
    private String title;
    private String description;
    private String techStack;
    private String role;
    private boolean isCompleted;

    public Project() {
    }

    public Project(String id, String title, String description, String techStack, String role, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.techStack = techStack;
        this.role = role;
        this.isCompleted = isCompleted;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechStack() {
        return techStack;
    }

    public void setTechStack(String techStack) {
        this.techStack = techStack;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public String toDataString() {
        // Format: id|title|description|techStack|role|isCompleted
        return id + "|" + title + "|" + description + "|" + techStack + "|" + role + "|" + isCompleted;
    }
}
