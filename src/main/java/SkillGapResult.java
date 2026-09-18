package com.tracker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Result DTO containing skill gap analysis results and dynamic career readiness calculations.
 */
public class SkillGapResult {
    private String targetRoleTitle;
    private double overallReadinessPercentage;
    private double skillScoreComponent;
    private double projectScoreComponent;
    private double certificationScoreComponent;
    private double breadthScoreComponent;

    private List<Skill> acquiredSkills;
    private List<Skill> partiallyDevelopedSkills;
    private List<String> missingSkills;
    private List<String> suggestedNextSkills;

    public SkillGapResult(String targetRoleTitle) {
        this.targetRoleTitle = targetRoleTitle;
        this.acquiredSkills = new ArrayList<>();
        this.partiallyDevelopedSkills = new ArrayList<>();
        this.missingSkills = new ArrayList<>();
        this.suggestedNextSkills = new ArrayList<>();
    }

    public String getTargetRoleTitle() {
        return targetRoleTitle;
    }

    public double getOverallReadinessPercentage() {
        return overallReadinessPercentage;
    }

    public void setOverallReadinessPercentage(double overallReadinessPercentage) {
        this.overallReadinessPercentage = overallReadinessPercentage;
    }

    public double getSkillScoreComponent() {
        return skillScoreComponent;
    }

    public void setSkillScoreComponent(double skillScoreComponent) {
        this.skillScoreComponent = skillScoreComponent;
    }

    public double getProjectScoreComponent() {
        return projectScoreComponent;
    }

    public void setProjectScoreComponent(double projectScoreComponent) {
        this.projectScoreComponent = projectScoreComponent;
    }

    public double getCertificationScoreComponent() {
        return certificationScoreComponent;
    }

    public void setCertificationScoreComponent(double certificationScoreComponent) {
        this.certificationScoreComponent = certificationScoreComponent;
    }

    public double getBreadthScoreComponent() {
        return breadthScoreComponent;
    }

    public void setBreadthScoreComponent(double breadthScoreComponent) {
        this.breadthScoreComponent = breadthScoreComponent;
    }

    public List<Skill> getAcquiredSkills() {
        return acquiredSkills;
    }

    public List<Skill> getPartiallyDevelopedSkills() {
        return partiallyDevelopedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public List<String> getSuggestedNextSkills() {
        return suggestedNextSkills;
    }
}
