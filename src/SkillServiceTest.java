package com.tracker;

import com.tracker.exception.DuplicateSkillException;
import com.tracker.exception.SkillNotFoundException;
import com.tracker.model.*;
import com.tracker.repository.FileManager;
import com.tracker.service.SkillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SkillServiceTest {

    @TempDir
    Path tempFolder;

    private SkillService skillService;

    @BeforeEach
    void setUp() {
        FileManager fileManager = new FileManager(tempFolder.toString(), false);
        skillService = new SkillService(fileManager);
    }

    @Test
    void testAddSkillAndPreventDuplicates() throws Exception {
        Skill skill1 = new TechnicalSkill("SK-01", "Java", ProficiencyLevel.ADVANCED, SkillCategory.PROGRAMMING, "Java 21", "JavaFX");
        skillService.addSkill(skill1);

        assertEquals(1, skillService.getAllSkills().size());

        // Attempting to add same skill with different case should throw DuplicateSkillException
        Skill duplicate = new TechnicalSkill("SK-02", "JAVA", ProficiencyLevel.BEGINNER, SkillCategory.PROGRAMMING, "Java", "None");
        assertThrows(DuplicateSkillException.class, () -> skillService.addSkill(duplicate));
    }

    @Test
    void testUpdateProficiency() throws Exception {
        Skill skill = new TechnicalSkill("SK-10", "Python", ProficiencyLevel.BEGINNER, SkillCategory.PROGRAMMING, "Python 3", "None");
        skillService.addSkill(skill);

        skillService.updateSkillProficiency("SK-10", ProficiencyLevel.ADVANCED);

        Skill updated = skillService.findById("SK-10");
        assertNotNull(updated);
        assertEquals(ProficiencyLevel.ADVANCED, updated.getProficiency());
    }

    @Test
    void testDeleteSkill() throws Exception {
        Skill skill = new SoftSkill("SK-20", "Public Speaking", ProficiencyLevel.INTERMEDIATE, "Seminars", 4);
        skillService.addSkill(skill);

        skillService.deleteSkill("SK-20");

        assertNull(skillService.findById("SK-20"));
        assertThrows(SkillNotFoundException.class, () -> skillService.deleteSkill("SK-20"));
    }

    @Test
    void testSearchAndSort() throws Exception {
        skillService.addSkill(new TechnicalSkill("SK-1", "C++", ProficiencyLevel.BEGINNER, SkillCategory.PROGRAMMING, "C++", "STL"));
        skillService.addSkill(new TechnicalSkill("SK-2", "Python", ProficiencyLevel.ADVANCED, SkillCategory.AI_ML, "Python", "PyTorch"));
        skillService.addSkill(new SoftSkill("SK-3", "Leadership", ProficiencyLevel.INTERMEDIATE, "Clubs", 5));

        List<Skill> searchResults = skillService.searchByNameOrKeyword("python");
        assertEquals(1, searchResults.size());
        assertEquals("Python", searchResults.get(0).getName());

        List<Skill> sortedByProf = skillService.getSkillsSortedByProficiencyDescending();
        assertEquals("Python", sortedByProf.get(0).getName()); // Advanced carries weight 1.0
    }
}
