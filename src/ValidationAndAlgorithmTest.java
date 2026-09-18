package com.tracker;

import com.tracker.exception.InvalidStudentDataException;
import com.tracker.model.ProficiencyLevel;
import com.tracker.model.Skill;
import com.tracker.model.SkillCategory;
import com.tracker.model.TechnicalSkill;
import com.tracker.util.AlgorithmUtil;
import com.tracker.util.ValidationUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationAndAlgorithmTest {

    @Test
    void testEmailValidation() {
        assertDoesNotThrow(() -> ValidationUtil.validateEmail("student@university.edu"));
        assertThrows(InvalidStudentDataException.class, () -> ValidationUtil.validateEmail("invalid-email"));
    }

    @Test
    void testBinarySearchByName() {
        List<Skill> sortedSkills = new ArrayList<>();
        sortedSkills.add(new TechnicalSkill("1", "C++", ProficiencyLevel.BEGINNER, SkillCategory.PROGRAMMING, "C++", ""));
        sortedSkills.add(new TechnicalSkill("2", "Java", ProficiencyLevel.INTERMEDIATE, SkillCategory.PROGRAMMING, "Java", ""));
        sortedSkills.add(new TechnicalSkill("3", "Python", ProficiencyLevel.ADVANCED, SkillCategory.PROGRAMMING, "Python", ""));

        Skill found = AlgorithmUtil.binarySearchByName(sortedSkills, "Java");
        assertNotNull(found);
        assertEquals("Java", found.getName());

        Skill notFound = AlgorithmUtil.binarySearchByName(sortedSkills, "Rust");
        assertNull(notFound);
    }
}
