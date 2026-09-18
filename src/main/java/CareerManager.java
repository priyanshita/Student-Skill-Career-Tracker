package com.tracker.service;

import com.tracker.model.CareerRole;
import com.tracker.repository.FileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Service managing target career roles, required/recommended skill mappings, and custom role definitions.
 */
public class CareerManager {
    private final List<CareerRole> careerRoles;
    private final FileManager fileManager;

    public CareerManager(FileManager fileManager) {
        this.fileManager = fileManager;
        this.careerRoles = new ArrayList<>(fileManager.loadCareerRoles());
    }

    public List<CareerRole> getAllCareerRoles() {
        return new ArrayList<>(careerRoles);
    }

    public CareerRole findRoleByTitle(String title) {
        if (title == null) return null;
        for (CareerRole r : careerRoles) {
            if (r.getTitle().equalsIgnoreCase(title.trim())) {
                return r;
            }
        }
        // Fallback default role
        return careerRoles.isEmpty() ? null : careerRoles.get(0);
    }

    public void addOrUpdateCareerRole(CareerRole role) {
        if (role == null || role.getTitle() == null) return;
        CareerRole existing = findRoleByTitle(role.getTitle());
        if (existing != null) {
            existing.setDescription(role.getDescription());
            existing.setRequiredSkills(role.getRequiredSkills());
            existing.setRecommendedSkills(role.getRecommendedSkills());
        } else {
            if (role.getRoleId() == null || role.getRoleId().isEmpty()) {
                role.setRoleId("ROLE-" + (careerRoles.size() + 1));
            }
            careerRoles.add(role);
        }
        saveChanges();
    }

    private void saveChanges() {
        try {
            fileManager.saveCareerRoles(careerRoles);
        } catch (Exception e) {
            System.err.println("Error saving career roles: " + e.getMessage());
        }
    }
}
