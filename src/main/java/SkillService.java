package com.tracker.service;

import com.tracker.exception.DuplicateSkillException;
import com.tracker.exception.SkillNotFoundException;
import com.tracker.interface_def.Searchable;
import com.tracker.model.ProficiencyLevel;
import com.tracker.model.Skill;
import com.tracker.model.SkillCategory;
import com.tracker.repository.FileManager;
import com.tracker.util.AlgorithmUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Service managing student skill CRUD, duplicate checking, filtering, searching, and sorting.
 * Implements Searchable<Skill> interface.
 */
public class SkillService implements Searchable<Skill> {
    private final List<Skill> skills;
    private final FileManager fileManager;

    public SkillService(FileManager fileManager) {
        this.fileManager = fileManager;
        this.skills = new ArrayList<>(fileManager.loadSkills());
    }

    public List<Skill> getAllSkills() {
        return new ArrayList<>(skills);
    }

    public void addSkill(Skill skill) throws DuplicateSkillException {
        if (skill == null || skill.getName() == null || skill.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Skill name cannot be empty.");
        }

        // Duplicate Check using equals() dynamic matching
        for (Skill existing : skills) {
            if (existing.equals(skill)) {
                throw new DuplicateSkillException("Skill '" + skill.getName() + "' already exists in profile.");
            }
        }

        if (skill.getId() == null || skill.getId().isEmpty()) {
            skill.setId("SK-" + (System.currentTimeMillis() % 10000));
        }

        skills.add(skill);
        saveChanges();
    }

    public void updateSkillProficiency(String skillId, ProficiencyLevel newLevel) throws SkillNotFoundException {
        Skill skill = findById(skillId);
        if (skill == null) {
            throw new SkillNotFoundException("Skill with ID " + skillId + " not found.");
        }
        skill.setProficiency(newLevel);
        saveChanges();
    }

    public void deleteSkill(String skillId) throws SkillNotFoundException {
        Skill skill = findById(skillId);
        if (skill == null) {
            throw new SkillNotFoundException("Skill with ID " + skillId + " not found.");
        }
        skills.remove(skill);
        saveChanges();
    }

    public Skill findById(String skillId) {
        for (Skill s : skills) {
            if (s.getId().equalsIgnoreCase(skillId)) {
                return s;
            }
        }
        return null;
    }

    // Searchable Interface Method Implementation
    @Override
    public List<Skill> searchByNameOrKeyword(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllSkills();
        }
        String q = query.trim().toLowerCase();
        return skills.stream()
                .filter(s -> s.getName().toLowerCase().contains(q) || 
                             s.getCategory().getDisplayName().toLowerCase().contains(q) ||
                             s.getDetails().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    @Override
    public List<Skill> filter(Predicate<Skill> predicate) {
        if (predicate == null) return getAllSkills();
        return skills.stream().filter(predicate).collect(Collectors.toList());
    }

    public List<Skill> filterByCategory(SkillCategory category) {
        return filter(s -> s.getCategory() == category);
    }

    public List<Skill> filterByProficiency(ProficiencyLevel level) {
        return filter(s -> s.getProficiency() == level);
    }

    // Sorting Methods using AlgorithmUtil & Comparable
    public List<Skill> getSkillsSortedByNameAlphabetical() {
        List<Skill> sorted = new ArrayList<>(skills);
        Collections.sort(sorted); // Uses Comparable implementation
        return sorted;
    }

    public List<Skill> getSkillsSortedByProficiencyDescending() {
        return AlgorithmUtil.sortByProficiencyDescending(skills); // Custom MergeSort algorithm
    }

    private void saveChanges() {
        try {
            fileManager.saveSkills(skills);
        } catch (Exception e) {
            System.err.println("Error saving skills to file: " + e.getMessage());
        }
    }
}
