package com.tracker.service;

import com.tracker.interface_def.ProgressCalculable;
import com.tracker.model.*;

import java.util.*;

/**
 * Core analytical engine implementing ProgressCalculable.
 * Performs dynamic set comparison between student skills and target career role requirements,
 * and calculates weighted career readiness score components.
 */
public class SkillGapAnalyzer implements ProgressCalculable {

    @Override
    public SkillGapResult analyzeSkillGap(Student student, CareerRole targetRole, 
                                          List<Skill> studentSkills, 
                                          List<Project> projects, 
                                          List<Certification> certifications) {
        if (targetRole == null) {
            return new SkillGapResult("Unspecified Career Role");
        }

        SkillGapResult result = new SkillGapResult(targetRole.getTitle());

        List<String> requiredSkillNames = targetRole.getRequiredSkills();
        if (requiredSkillNames == null) requiredSkillNames = new ArrayList<>();

        // Map student skills for fast O(1) lookup by lower-case name
        Map<String, Skill> studentSkillMap = new HashMap<>();
        for (Skill s : studentSkills) {
            if (s != null && s.getName() != null) {
                studentSkillMap.put(s.getName().trim().toLowerCase(), s);
            }
        }

        double totalRequiredSkillScore = 0.0;
        int acquiredCount = 0;
        int partialCount = 0;

        for (String reqName : requiredSkillNames) {
            String key = reqName.trim().toLowerCase();
            Skill studentSkill = studentSkillMap.get(key);

            if (studentSkill != null) {
                double weight = studentSkill.getProficiencyWeight();
                totalRequiredSkillScore += weight;

                if (studentSkill.getProficiency() == ProficiencyLevel.ADVANCED) {
                    result.getAcquiredSkills().add(studentSkill);
                    acquiredCount++;
                } else {
                    result.getPartiallyDevelopedSkills().add(studentSkill);
                    partialCount++;
                }
            } else {
                result.getMissingSkills().add(reqName);
            }
        }

        // 1. Skill Score Component (50% weight)
        double skillScore = 0.0;
        if (!requiredSkillNames.isEmpty()) {
            skillScore = (totalRequiredSkillScore / requiredSkillNames.size()) * 100.0;
        } else {
            skillScore = 100.0;
        }
        skillScore = Math.min(100.0, Math.max(0.0, skillScore));
        result.setSkillScoreComponent(skillScore);

        // 2. Project Experience Component (25% weight)
        // 2+ completed relevant projects = 100%, 1 completed project = 65%, 0 = 0%
        long completedProjectCount = projects.stream().filter(Project::isCompleted).count();
        double projectScore = 0.0;
        if (completedProjectCount >= 2) {
            projectScore = 100.0;
        } else if (completedProjectCount == 1) {
            projectScore = 65.0;
        } else {
            projectScore = 20.0; // Base effort for ongoing projects
        }
        result.setProjectScoreComponent(projectScore);

        // 3. Certifications Component (15% weight)
        // 2+ certs = 100%, 1 cert = 65%, 0 certs = 0%
        int certCount = certifications.size();
        double certScore = 0.0;
        if (certCount >= 2) {
            certScore = 100.0;
        } else if (certCount == 1) {
            certScore = 65.0;
        } else {
            certScore = 0.0;
        }
        result.setCertificationScoreComponent(certScore);

        // 4. Breadth & Soft Skills Component (10% weight)
        long softSkillCount = studentSkills.stream()
                .filter(s -> s.getCategory() == SkillCategory.SOFT_SKILLS)
                .count();
        double breadthScore = Math.min(100.0, (studentSkills.size() * 10.0) + (softSkillCount * 15.0));
        result.setBreadthScoreComponent(breadthScore);

        // Overall Weighted Formula
        // 50% Skills + 25% Projects + 15% Certifications + 10% Breadth
        double overallReadiness = (0.50 * skillScore) + 
                                  (0.25 * projectScore) + 
                                  (0.15 * certScore) + 
                                  (0.10 * breadthScore);

        result.setOverallReadinessPercentage(Math.round(overallReadiness * 10.0) / 10.0);

        // Derive Suggested Next Skills to Learn
        // Priority 1: Missing Required Skills
        result.getSuggestedNextSkills().addAll(result.getMissingSkills());

        // Priority 2: Partially Developed Required Skills (to upgrade to Advanced)
        for (Skill pSkill : result.getPartiallyDevelopedSkills()) {
            result.getSuggestedNextSkills().add(pSkill.getName() + " (Upgrade to Advanced)");
        }

        // Priority 3: Recommended Skills from role definition
        if (targetRole.getRecommendedSkills() != null) {
            for (String rec : targetRole.getRecommendedSkills()) {
                if (!studentSkillMap.containsKey(rec.trim().toLowerCase()) && 
                    !result.getSuggestedNextSkills().contains(rec)) {
                    result.getSuggestedNextSkills().add(rec + " (Recommended Bonus)");
                }
            }
        }

        return result;
    }
}
