package com.tracker;

import com.tracker.model.*;
import com.tracker.service.SkillGapAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SkillGapAnalyzerTest {

    private SkillGapAnalyzer analyzer;
    private Student student;
    private CareerRole role;

    @BeforeEach
    void setUp() {
        analyzer = new SkillGapAnalyzer();
        student = new Student("STU-1", "Jane Doe", "22CSE555", "CSE", 2, "jane@test.edu", "AI/ML Engineer");
        role = new CareerRole("ROLE-1", "AI/ML Engineer", "AI role",
                Arrays.asList("Python", "Machine Learning", "Statistics", "SQL"),
                Arrays.asList("Deep Learning", "Git"));
    }

    @Test
    void testSkillGapClassificationAndReadinessScore() {
        List<Skill> studentSkills = Arrays.asList(
                new TechnicalSkill("S1", "Python", ProficiencyLevel.ADVANCED, SkillCategory.PROGRAMMING, "Python", "NumPy"),
                new TechnicalSkill("S2", "Machine Learning", ProficiencyLevel.INTERMEDIATE, SkillCategory.AI_ML, "Python", "Scikit"),
                new TechnicalSkill("S3", "SQL", ProficiencyLevel.BEGINNER, SkillCategory.DATABASES, "SQL", "MySQL")
        );

        List<Project> projects = Arrays.asList(
                new Project("P1", "ML Classifier", "Desc", "Python", "Lead", true),
                new Project("P2", "Web Portal", "Desc", "Java", "Dev", true)
        );

        List<Certification> certs = Arrays.asList(
                new Certification("C1", "ML Specialist", "Coursera", "2024", "Machine Learning")
        );

        SkillGapResult result = analyzer.analyzeSkillGap(student, role, studentSkills, projects, certs);

        assertNotNull(result);
        assertEquals(1, result.getAcquiredSkills().size()); // Python (Advanced)
        assertEquals(2, result.getPartiallyDevelopedSkills().size()); // ML (Intermediate), SQL (Beginner)
        assertEquals(1, result.getMissingSkills().size()); // Statistics is missing
        assertTrue(result.getMissingSkills().contains("Statistics"));

        // Verify readiness percentage calculation (must be > 0 and <= 100)
        assertTrue(result.getOverallReadinessPercentage() > 50.0);
        assertTrue(result.getOverallReadinessPercentage() <= 100.0);
    }
}
