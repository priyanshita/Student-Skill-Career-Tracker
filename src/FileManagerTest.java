package com.tracker;

import com.tracker.model.*;
import com.tracker.repository.FileManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileManagerTest {

    @TempDir
    Path tempDir;

    @Test
    void testSaveAndLoadStudent() throws Exception {
        FileManager fm = new FileManager(tempDir.toString());
        Student s = new Student("STU-999", "Test Student", "REG-999", "AI-ML", 3, "test@univ.edu", "Cloud Engineer");

        fm.saveStudent(s);
        Student loaded = fm.loadStudent();

        assertNotNull(loaded);
        assertEquals("Test Student", loaded.getName());
        assertEquals("Cloud Engineer", loaded.getTargetCareerRole());
    }

    @Test
    void testSaveAndLoadPolymorphicSkills() throws Exception {
        FileManager fm = new FileManager(tempDir.toString());
        List<Skill> skills = Arrays.asList(
                new TechnicalSkill("S1", "Docker", ProficiencyLevel.ADVANCED, SkillCategory.CLOUD, "Go", "Docker Engine"),
                new SoftSkill("S2", "Teamwork", ProficiencyLevel.INTERMEDIATE, "Group Projects", 5)
        );

        fm.saveSkills(skills);
        List<Skill> loadedSkills = fm.loadSkills();

        assertEquals(2, loadedSkills.size());
        assertTrue(loadedSkills.get(0) instanceof TechnicalSkill);
        assertTrue(loadedSkills.get(1) instanceof SoftSkill);
    }
}
