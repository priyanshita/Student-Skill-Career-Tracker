package com.tracker.interface_def;

import com.tracker.model.CareerRole;
import com.tracker.model.SkillGapResult;
import com.tracker.model.Student;
import com.tracker.model.Skill;
import com.tracker.model.Project;
import com.tracker.model.Certification;

import java.util.List;

/**
 * Interface defining contract for career readiness and progress calculation algorithms.
 */
public interface ProgressCalculable {
    /**
     * Calculates skill gap analysis and overall readiness percentage for a given student and target career role.
     *
     * @param student The student profile
     * @param targetRole Target career role
     * @param studentSkills List of skills owned by student
     * @param projects List of projects
     * @param certifications List of certifications
     * @return SkillGapResult containing readiness %, acquired, partial, and missing skills
     */
    SkillGapResult analyzeSkillGap(Student student, CareerRole targetRole, List<Skill> studentSkills, List<Project> projects, List<Certification> certifications);
}
