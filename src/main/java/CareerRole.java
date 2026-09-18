package com.tracker.model;

import com.tracker.interface_def.Storable;
import java.util.ArrayList;
import java.util.List;

/**
 * Model representing a Target Career Role and its required/recommended technical & soft skills.
 */
public class CareerRole implements Storable {
    private String roleId;
    private String title;
    private String description;
    private List<String> requiredSkills;
    private List<String> recommendedSkills;

    public CareerRole() {
        this.requiredSkills = new ArrayList<>();
        this.recommendedSkills = new ArrayList<>();
    }

    public CareerRole(String roleId, String title, String description, List<String> requiredSkills, List<String> recommendedSkills) {
        this.roleId = roleId;
        this.title = title;
        this.description = description;
        this.requiredSkills = requiredSkills != null ? requiredSkills : new ArrayList<>();
        this.recommendedSkills = recommendedSkills != null ? recommendedSkills : new ArrayList<>();
    }

    @Override
    public String getId() {
        return roleId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
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

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public List<String> getRecommendedSkills() {
        return recommendedSkills;
    }

    public void setRecommendedSkills(List<String> recommendedSkills) {
        this.recommendedSkills = recommendedSkills;
    }

    @Override
    public String toDataString() {
        // Format: roleId|title|description|reqSkill1,reqSkill2|recSkill1,recSkill2
        String reqStr = String.join(",", requiredSkills);
        String recStr = String.join(",", recommendedSkills);
        return roleId + "|" + title + "|" + description + "|" + reqStr + "|" + recStr;
    }
}
