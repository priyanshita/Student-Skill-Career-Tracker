package com.tracker.model;

import com.tracker.interface_def.Storable;

/**
 * Model representing a student profile.
 * Demonstrates Encapsulation and File Persistence formatting.
 */
public class Student implements Storable {
    private String id;
    private String name;
    private String regNo;
    private String branch;
    private int year;
    private String email;
    private String targetCareerRole;

    public Student() {
        this.id = "STU-1001";
        this.name = "Alex Johnson";
        this.regNo = "22CSE1042";
        this.branch = "CSE (AI & ML)";
        this.year = 2;
        this.email = "alex.johnson@university.edu";
        this.targetCareerRole = "AI/ML Engineer";
    }

    public Student(String id, String name, String regNo, String branch, int year, String email, String targetCareerRole) {
        this.id = id;
        this.name = name;
        this.regNo = regNo;
        this.branch = branch;
        this.year = year;
        this.email = email;
        this.targetCareerRole = targetCareerRole;
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

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTargetCareerRole() {
        return targetCareerRole;
    }

    public void setTargetCareerRole(String targetCareerRole) {
        this.targetCareerRole = targetCareerRole;
    }

    @Override
    public String toDataString() {
        // Format: id|name|regNo|branch|year|email|targetCareerRole
        return id + "|" + name + "|" + regNo + "|" + branch + "|" + year + "|" + email + "|" + targetCareerRole;
    }
}
