package com.tracker.service;

import com.tracker.model.*;
import com.tracker.repository.FileManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service aggregating dashboard metrics, skill breakdown statistics, and career readiness progress.
 */
public class ProgressTracker {
    private final SkillService skillService;
    private final CareerManager careerManager;
    private final SkillGapAnalyzer analyzer;
    private final FileManager fileManager;

    private Student student;
    private final List<Project> projects;
    private final List<Certification> certifications;

    public ProgressTracker(FileManager fileManager, SkillService skillService, CareerManager careerManager) {
        this.fileManager = fileManager;
        this.skillService = skillService;
        this.careerManager = careerManager;
        this.analyzer = new SkillGapAnalyzer();

        this.student = fileManager.loadStudent();
        this.projects = new ArrayList<>(fileManager.loadProjects());
        this.certifications = new ArrayList<>(fileManager.loadCertifications());
    }

    public Student getStudent() {
        return student;
    }

    public void updateStudent(Student updatedStudent) throws Exception {
        this.student = updatedStudent;
        fileManager.saveStudent(student);
    }

    public List<Project> getProjects() {
        return new ArrayList<>(projects);
    }

    public void addProject(Project project) throws Exception {
        if (project.getId() == null || project.getId().isEmpty()) {
            project.setId("PRJ-" + (projects.size() + 1));
        }
        projects.add(project);
        fileManager.saveProjects(projects);
    }

    public void deleteProject(String projectId) throws Exception {
        projects.removeIf(p -> p.getId().equalsIgnoreCase(projectId));
        fileManager.saveProjects(projects);
    }

    public List<Certification> getCertifications() {
        return new ArrayList<>(certifications);
    }

    public void addCertification(Certification cert) throws Exception {
        if (cert.getId() == null || cert.getId().isEmpty()) {
            cert.setId("CRT-" + (certifications.size() + 1));
        }
        certifications.add(cert);
        fileManager.saveCertifications(certifications);
    }

    public void deleteCertification(String certId) throws Exception {
        certifications.removeIf(c -> c.getId().equalsIgnoreCase(certId));
        fileManager.saveCertifications(certifications);
    }

    public Map<ProficiencyLevel, Integer> getProficiencyBreakdown() {
        Map<ProficiencyLevel, Integer> map = new HashMap<>();
        for (ProficiencyLevel lvl : ProficiencyLevel.values()) {
            map.put(lvl, 0);
        }
        for (Skill s : skillService.getAllSkills()) {
            map.put(s.getProficiency(), map.get(s.getProficiency()) + 1);
        }
        return map;
    }

    public Map<SkillCategory, Integer> getCategoryBreakdown() {
        Map<SkillCategory, Integer> map = new HashMap<>();
        for (SkillCategory cat : SkillCategory.values()) {
            map.put(cat, 0);
        }
        for (Skill s : skillService.getAllSkills()) {
            map.put(s.getCategory(), map.get(s.getCategory()) + 1);
        }
        return map;
    }

    public SkillGapResult getCurrentSkillGapAnalysis() {
        CareerRole targetRole = careerManager.findRoleByTitle(student.getTargetCareerRole());
        return analyzer.analyzeSkillGap(student, targetRole, skillService.getAllSkills(), projects, certifications);
    }
}
