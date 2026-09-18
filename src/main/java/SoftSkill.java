package com.tracker.model;

/**
 * Soft skill entity extending abstract base class Skill.
 * Demonstrates Inheritance, Method Overriding, and specialized context attributes.
 */
public class SoftSkill extends Skill {
    private String contextApplication; // e.g. Team Presentations, Hackathons
    private int peerRating;             // Scale 1 to 5

    public SoftSkill() {
        super();
        setCategory(SkillCategory.SOFT_SKILLS);
        this.contextApplication = "General Academics";
        this.peerRating = 4;
    }

    public SoftSkill(String id, String name, ProficiencyLevel proficiency, 
                     String contextApplication, int peerRating) {
        super(id, name, proficiency, SkillCategory.SOFT_SKILLS);
        this.contextApplication = contextApplication;
        this.peerRating = Math.max(1, Math.min(5, peerRating));
    }

    @Override
    public double getProficiencyWeight() {
        // Soft skills combine proficiency weight with normalized peer rating multiplier
        double ratingMultiplier = 0.8 + (peerRating * 0.04); // 1->0.84, 5->1.0
        return getProficiency().getWeight() * ratingMultiplier;
    }

    @Override
    public String getDetails() {
        return "Context: " + contextApplication + " | Peer Rating: " + peerRating + "/5";
    }

    @Override
    public String getSkillType() {
        return "SOFT";
    }

    public String getContextApplication() {
        return contextApplication;
    }

    public void setContextApplication(String contextApplication) {
        this.contextApplication = contextApplication;
    }

    public int getPeerRating() {
        return peerRating;
    }

    public void setPeerRating(int peerRating) {
        this.peerRating = Math.max(1, Math.min(5, peerRating));
    }

    @Override
    public String toDataString() {
        // Format: SOFT|id|name|proficiency|category|contextApplication|peerRating
        return "SOFT|" + getId() + "|" + getName() + "|" + getProficiency().name() + "|" +
                getCategory().name() + "|" + contextApplication + "|" + peerRating;
    }
}
