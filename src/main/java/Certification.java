package com.tracker.model;

import com.tracker.interface_def.Storable;

/**
 * Model representing a student certification.
 */
public class Certification implements Storable {
    private String id;
    private String name;
    private String issuingOrganization;
    private String issueDate;
    private String relevantSkill;

    public Certification() {
    }

    public Certification(String id, String name, String issuingOrganization, String issueDate, String relevantSkill) {
        this.id = id;
        this.name = name;
        this.issuingOrganization = issuingOrganization;
        this.issueDate = issueDate;
        this.relevantSkill = relevantSkill;
    }

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

    public String getIssuingOrganization() {
        return issuingOrganization;
    }

    public void setIssuingOrganization(String issuingOrganization) {
        this.issuingOrganization = issuingOrganization;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getRelevantSkill() {
        return relevantSkill;
    }

    public void setRelevantSkill(String relevantSkill) {
        this.relevantSkill = relevantSkill;
    }

    @Override
    public String toDataString() {
        // Format: id|name|issuingOrganization|issueDate|relevantSkill
        return id + "|" + name + "|" + issuingOrganization + "|" + issueDate + "|" + relevantSkill;
    }
}
