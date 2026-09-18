package com.tracker.repository;

import com.tracker.exception.DataPersistenceException;
import com.tracker.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * File I/O manager responsible for reading and writing model data to local text files.
 * Uses BufferedReader, BufferedWriter, FileReader, FileWriter.
 * Demonstrates Exception Handling, File I/O, and robust error recovery.
 */
public class FileManager {
    private final String dataDirectory;

    private static final String STUDENT_FILE = "students.txt";
    private static final String SKILLS_FILE = "skills.txt";
    private static final String PROJECTS_FILE = "projects.txt";
    private static final String CERTS_FILE = "certifications.txt";
    private static final String ROLES_FILE = "career_roles.txt";

    public FileManager() {
        this("data", true);
    }

    public FileManager(String dataDirectory) {
        this(dataDirectory, true);
    }

    public FileManager(String dataDirectory, boolean autoSeed) {
        this.dataDirectory = dataDirectory;
        ensureDirectoryAndFilesExist(autoSeed);
    }

    private void ensureDirectoryAndFilesExist(boolean autoSeed) {
        try {
            Path dirPath = Paths.get(dataDirectory);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            if (autoSeed) {
                checkAndSeedFiles();
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to create data directory: " + e.getMessage());
        }
    }

    private void checkAndSeedFiles() {
        File sFile = new File(dataDirectory, STUDENT_FILE);
        File skFile = new File(dataDirectory, SKILLS_FILE);
        File pFile = new File(dataDirectory, PROJECTS_FILE);
        File cFile = new File(dataDirectory, CERTS_FILE);
        File rFile = new File(dataDirectory, ROLES_FILE);

        if (!sFile.exists()) seedStudentData();
        if (!rFile.exists()) seedCareerRolesData();
        if (!skFile.exists()) seedSkillsData();
        if (!pFile.exists()) seedProjectsData();
        if (!cFile.exists()) seedCertificationsData();
    }

    // ==================== STUDENT PERSISTENCE ====================

    public Student loadStudent() {
        File file = new File(dataDirectory, STUDENT_FILE);
        if (!file.exists()) return new Student();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null && !line.trim().isEmpty()) {
                String[] parts = line.split("\\|", -1);
                if (parts.length >= 7) {
                    return new Student(parts[0], parts[1], parts[2], parts[3], 
                            Integer.parseInt(parts[4]), parts[5], parts[6]);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading student.txt, returning default: " + e.getMessage());
        }
        return new Student();
    }

    public void saveStudent(Student student) throws DataPersistenceException {
        File file = new File(dataDirectory, STUDENT_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            writer.write(student.toDataString());
            writer.newLine();
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to save student profile to " + STUDENT_FILE, e);
        }
    }

    // ==================== SKILLS PERSISTENCE ====================

    public List<Skill> loadSkills() {
        List<Skill> skills = new ArrayList<>();
        File file = new File(dataDirectory, SKILLS_FILE);
        if (!file.exists()) return skills;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                try {
                    String[] parts = line.split("\\|", -1);
                    if (parts.length >= 6) {
                        String type = parts[0];
                        String id = parts[1];
                        String name = parts[2];
                        ProficiencyLevel prof = ProficiencyLevel.fromString(parts[3]);
                        SkillCategory cat = SkillCategory.fromString(parts[4]);

                        if ("TECHNICAL".equalsIgnoreCase(type) && parts.length >= 7) {
                            String lang = parts[5];
                            String frameworks = parts[6];
                            skills.add(new TechnicalSkill(id, name, prof, cat, lang, frameworks));
                        } else if ("SOFT".equalsIgnoreCase(type) && parts.length >= 7) {
                            String context = parts[5];
                            int rating = Integer.parseInt(parts[6]);
                            skills.add(new SoftSkill(id, name, prof, context, rating));
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("Skipping corrupted skill record line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + SKILLS_FILE + ": " + e.getMessage());
        }
        return skills;
    }

    public void saveSkills(List<Skill> skills) throws DataPersistenceException {
        File file = new File(dataDirectory, SKILLS_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (Skill skill : skills) {
                writer.write(skill.toDataString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to write skills to file", e);
        }
    }

    // ==================== PROJECTS PERSISTENCE ====================

    public List<Project> loadProjects() {
        List<Project> list = new ArrayList<>();
        File file = new File(dataDirectory, PROJECTS_FILE);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length >= 6) {
                    list.add(new Project(parts[0], parts[1], parts[2], parts[3], parts[4], 
                            Boolean.parseBoolean(parts[5])));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + PROJECTS_FILE + ": " + e.getMessage());
        }
        return list;
    }

    public void saveProjects(List<Project> projects) throws DataPersistenceException {
        File file = new File(dataDirectory, PROJECTS_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (Project p : projects) {
                writer.write(p.toDataString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to save projects to file", e);
        }
    }

    // ==================== CERTIFICATIONS PERSISTENCE ====================

    public List<Certification> loadCertifications() {
        List<Certification> list = new ArrayList<>();
        File file = new File(dataDirectory, CERTS_FILE);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length >= 5) {
                    list.add(new Certification(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + CERTS_FILE + ": " + e.getMessage());
        }
        return list;
    }

    public void saveCertifications(List<Certification> certs) throws DataPersistenceException {
        File file = new File(dataDirectory, CERTS_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (Certification c : certs) {
                writer.write(c.toDataString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to save certifications to file", e);
        }
    }

    // ==================== CAREER ROLES PERSISTENCE ====================

    public List<CareerRole> loadCareerRoles() {
        List<CareerRole> roles = new ArrayList<>();
        File file = new File(dataDirectory, ROLES_FILE);
        if (!file.exists()) return roles;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length >= 5) {
                    List<String> req = Arrays.asList(parts[3].split(","));
                    List<String> rec = Arrays.asList(parts[4].split(","));
                    roles.add(new CareerRole(parts[0], parts[1], parts[2], req, rec));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + ROLES_FILE + ": " + e.getMessage());
        }
        return roles;
    }

    public void saveCareerRoles(List<CareerRole> roles) throws DataPersistenceException {
        File file = new File(dataDirectory, ROLES_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (CareerRole r : roles) {
                writer.write(r.toDataString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DataPersistenceException("Failed to save career roles", e);
        }
    }

    // ==================== SEED SAMPLE DATA GENERATION ====================

    private void seedStudentData() {
        Student defaultStudent = new Student("STU-1001", "Alex Johnson", "22CSE1042", 
                "CSE (AI & ML)", 2, "alex.johnson@university.edu", "AI/ML Engineer");
        try { saveStudent(defaultStudent); } catch (Exception ignored) {}
    }

    private void seedCareerRolesData() {
        List<CareerRole> seedRoles = Arrays.asList(
            new CareerRole("ROLE-01", "AI/ML Engineer", 
                "Designs and builds machine learning models, neural networks, and scalable AI solutions.",
                Arrays.asList("Python", "Machine Learning", "Statistics & Math", "SQL", "Data Structures", "Deep Learning"),
                Arrays.asList("TensorFlow", "Git", "Problem Solving", "Cloud Computing")),

            new CareerRole("ROLE-02", "Software Developer", 
                "Builds enterprise desktop, web, and backend applications using modern programming languages.",
                Arrays.asList("Java", "Data Structures", "OOP Concepts", "SQL", "Git", "Web Development"),
                Arrays.asList("Spring Boot", "Docker", "Agile Teamwork", "System Design")),

            new CareerRole("ROLE-03", "Data Analyst", 
                "Analyzes structured dataset trends, creates business dashboards, and extracts actionable insights.",
                Arrays.asList("Python", "SQL", "Statistics & Math", "Excel", "Data Visualization"),
                Arrays.asList("Power BI", "Tableau", "Communication", "Critical Thinking")),

            new CareerRole("ROLE-04", "Data Scientist", 
                "Combines predictive modeling, statistics, data analytics, and business acumen.",
                Arrays.asList("Python", "Machine Learning", "Statistics & Math", "SQL", "Data Science Core"),
                Arrays.asList("R", "Big Data", "Storytelling", "Research Methods")),

            new CareerRole("ROLE-05", "Web Developer", 
                "Front-end and full-stack web software development.",
                Arrays.asList("HTML/CSS", "JavaScript", "React/Vue", "SQL", "Web Development"),
                Arrays.asList("TypeScript", "Node.js", "Git", "UI/UX Design")),

            new CareerRole("ROLE-06", "Cloud Engineer", 
                "Architects and deploys infrastructure, pipelines, and microservices in cloud environments.",
                Arrays.asList("Linux", "Networking", "Cloud Computing", "Python", "Docker/DevOps"),
                Arrays.asList("AWS", "Kubernetes", "Security", "Infrastructure as Code"))
        );
        try { saveCareerRoles(seedRoles); } catch (Exception ignored) {}
    }

    private void seedSkillsData() {
        List<Skill> seedSkills = Arrays.asList(
            new TechnicalSkill("SK-101", "Python", ProficiencyLevel.ADVANCED, SkillCategory.PROGRAMMING, "Python 3.11", "NumPy, Pandas, PyTorch"),
            new TechnicalSkill("SK-102", "Machine Learning", ProficiencyLevel.INTERMEDIATE, SkillCategory.AI_ML, "Python", "Scikit-Learn, OpenCV"),
            new TechnicalSkill("SK-103", "SQL", ProficiencyLevel.BEGINNER, SkillCategory.DATABASES, "SQL", "PostgreSQL, MySQL"),
            new TechnicalSkill("SK-104", "Java", ProficiencyLevel.INTERMEDIATE, SkillCategory.PROGRAMMING, "Java 21", "JavaFX, Standard Library"),
            new TechnicalSkill("SK-105", "Data Structures", ProficiencyLevel.INTERMEDIATE, SkillCategory.PROGRAMMING, "C++ / Java", "Trees, Graphs, Sorting"),
            new SoftSkill("SK-201", "Communication", ProficiencyLevel.ADVANCED, "Technical Presentations & Viva", 5),
            new SoftSkill("SK-202", "Problem Solving", ProficiencyLevel.INTERMEDIATE, "Hackathons & Coding Challenges", 4)
        );
        try { saveSkills(seedSkills); } catch (Exception ignored) {}
    }

    private void seedProjectsData() {
        List<Project> seedProjects = Arrays.asList(
            new Project("PRJ-01", "Image Classifier for Plant Diseases", 
                "Built a CNN deep learning model in PyTorch for detecting crop diseases with 94% accuracy.", 
                "Python, PyTorch, OpenCV", "Lead ML Developer", true),
            new Project("PRJ-02", "Student Management System", 
                "JavaFX GUI application for tracking academic registration records and grades.", 
                "Java, JavaFX, File I/O", "Solo Developer", true),
            new Project("PRJ-03", "Predictive House Price Model", 
                "Regression model using Scikit-learn to analyze regional housing prices.", 
                "Python, Pandas, Scikit-learn", "Data Analyst", false)
        );
        try { saveProjects(seedProjects); } catch (Exception ignored) {}
    }

    private void seedCertificationsData() {
        List<Certification> seedCerts = Arrays.asList(
            new Certification("CRT-01", "Supervised Machine Learning", "Coursera / DeepLearning.AI", "2024-03-15", "Machine Learning"),
            new Certification("CRT-02", "Java Programming Masterclass", "Udemy", "2023-11-20", "Java")
        );
        try { saveCertifications(seedCerts); } catch (Exception ignored) {}
    }
}
