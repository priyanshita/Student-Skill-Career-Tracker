# Student-Skill-Career-Tracker

> **Academic Java Desktop Application for 2nd-Year CSE / AI-ML Students**  
> Demonstrating Object-Oriented Programming (OOP) in Java, Dynamic Skill-Gap Analysis, Data Persistence, Unit Testing, and JavaFX GUI Design.

---

##1. Project Overview & Objectives

The **Student Skill & Career Tracker** is a modular desktop application designed to help engineering students manage their technical competencies, soft skills, project portfolio, and certifications while actively evaluating their readiness for desired industry career roles.

Rather than acting as a static CRUD database, the system executes an **algorithmic career readiness calculation engine** that compares student skills against target career role benchmarks (such as *AI/ML Engineer*, *Software Developer*, *Data Scientist*, *Data Analyst*, *Web Developer*, and *Cloud Engineer*).

---

## 2. Main Functional Modules

### Module 1 — Student Profile Management
- Create, view, and update student profiles (Name, Registration Number, Branch, Academic Year, Email, Target Career Goal).
- Persistent profile synchronization to local storage (`students.txt`).

### Module 2 — Skill Management
- Add technical skills (with primary programming language, framework/tool details) and soft skills (with context application and peer rating).
- Assign proficiency levels: **Beginner** (0.40 weight), **Intermediate** (0.70 weight), **Advanced** (1.00 weight).
- Categorize skills: `Programming`, `AI/ML`, `Web Development`, `Databases`, `Cloud`, `Soft Skills`.
- Custom sorting via **MergeSort** (Proficiency High $\rightarrow$ Low) and **Comparable** (Alphabetical A-Z).
- Search skills using linear and binary search algorithms.

### Module 3 — Project & Certification Portfolio
- Record projects with title, tech stack, role, status (Completed / In Progress), and description.
- Track certifications with issuing authority, date, and associated skill.

### Module 4 — Career Goal & Skill Gap Analysis
- Select pre-configured industry career benchmarks or define custom target roles.
- Dynamic set comparison classifying skills into:
  1. **Acquired Skills** (Advanced proficiency)
  2. **Partially Developed Skills** (Beginner / Intermediate)
  3. **Missing Skills** (Required by role, not yet learned)
- Action plan generating recommended next skills to prioritize.

### Module 5 — Career Readiness Engine & Progress Dashboard
- Mathematical formula calculating overall readiness ($0 - 100\%$):

$$\text{Career Readiness Score} = (0.50 \times S_{\text{score}}) + (0.25 \times P_{\text{score}}) + (0.15 \times C_{\text{score}}) + (0.10 \times B_{\text{score}})$$

Where:
- $S_{\text{score}}$ = Normalized skill match percentage.
- $P_{\text{score}}$ = Project experience score based on completed projects.
- $C_{\text{score}}$ = Certification score based on credentials earned.
- $B_{\text{score}}$ = Overall technical breadth and soft skill bonus score.

---

## 3. Project Package Architecture

```text
src/
├── main/
│   ├── java/com/tracker/
│   │   ├── model/                  # Domain Models
│   │   │   ├── Skill.java          # Abstract Base Class
│   │   │   ├── TechnicalSkill.java # Subclass of Skill
│   │   │   ├── SoftSkill.java      # Subclass of Skill
│   │   │   ├── Student.java        # Student Profile
│   │   │   ├── Project.java        # Project Portfolio
│   │   │   ├── Certification.java  # Certification Credential
│   │   │   ├── CareerRole.java     # Target Role Benchmarks
│   │   │   ├── SkillGapResult.java # Analysis Result DTO
│   │   │   ├── ProficiencyLevel.java # Enum
│   │   │   └── SkillCategory.java  # Enum
│   │   ├── interface_def/          # OOP Interfaces
│   │   │   ├── ProgressCalculable.java
│   │   │   ├── Searchable.java
│   │   │   └── Storable.java
│   │   ├── exception/              # Custom Exceptions
│   │   │   ├── TrackerException.java
│   │   │   ├── InvalidStudentDataException.java
│   │   │   ├── DuplicateSkillException.java
│   │   │   ├── SkillNotFoundException.java
│   │   │   └── DataPersistenceException.java
│   │   ├── repository/             # Data Access Layer
│   │   │   └── FileManager.java    # Text File I/O Engine
│   │   ├── service/                # Business Logic Services
│   │   │   ├── SkillService.java
│   │   │   ├── CareerManager.java
│   │   │   ├── SkillGapAnalyzer.java
│   │   │   └── ProgressTracker.java
│   │   ├── util/                   # DSA & Validation Utilities
│   │   │   ├── ValidationUtil.java
│   │   │   └── AlgorithmUtil.java  # MergeSort, Binary Search
│   │   └── ui/                     # JavaFX GUI Views
│   │       ├── MainApp.java        # Application Entry Point
│   │       ├── DashboardView.java
│   │       ├── ProfileView.java
│   │       ├── SkillsView.java
│   │       ├── ProjectsView.java
│   │       ├── CertificationsView.java
│   │       ├── CareerGoalsView.java
│   │       ├── SkillGapView.java
│   │       └── ProgressReportView.java
│   └── resources/
│       └── styles.css              # Dark/Light Modern Theme
└── test/
    └── java/com/tracker/           # JUnit 5 Test Suite
        ├── SkillServiceTest.java
        ├── SkillGapAnalyzerTest.java
        ├── FileManagerTest.java
        └── ValidationAndAlgorithmTest.java
```

---

##  4. Installation & Execution Guide

### Prerequisites
- **JDK 21** installed (`java -version`, `javac -version`).

### How to Run via Automated Scripts (Recommended)

1. **Option A: Double-Click Batch File (Windows)**
   Double-click `run.bat` in the root project folder.

2. **Option B: Run via PowerShell**
   ```powershell
   powershell -ExecutionPolicy Bypass -File "scripts/run.ps1"
   ```

3. **Option C: Run Unit Tests**
   ```powershell
   powershell -ExecutionPolicy Bypass -File "scripts/build_and_test.ps1"
   ```

4. **Option D: Standard Maven Commands** (if Maven is installed)
   ```bash
   mvn clean compile
   mvn test
   mvn javafx:run
   ```

---

## 5. System Diagrams Description

### A. Architecture Diagram Description
- **Presentation Layer**: JavaFX Views (`MainApp`, `DashboardView`, `SkillsView`, `SkillGapView`, etc.) styled with CSS.
- **Service / Business Layer**: `SkillService`, `CareerManager`, `SkillGapAnalyzer`, `ProgressTracker`. Performs calculations and enforces business rules.
- **Data Access Layer**: `FileManager` reading and writing formatted lines to text files (`students.txt`, `skills.txt`, `projects.txt`, `certifications.txt`, `career_roles.txt`).

### B. Workflow Description
1. Application initializes and loads seed or saved text files via `FileManager`.
2. Student updates profile info or selects target career role in `CareerGoalsView`.
3. Student adds skills, projects, and certifications in respective UI views.
4. `SkillGapAnalyzer` dynamically calculates readiness percentage and generates gap reports.
5. Student reviews dashboard summary or exports `ProgressReportView`.

### C. Use Case Diagram Description
- **Actors**: Student (User).
- **Use Cases**: *Manage Profile*, *Manage Technical Skills*, *Manage Soft Skills*, *Manage Projects*, *Manage Certifications*, *Select Career Goal*, *Analyze Skill Gap*, *View Progress Dashboard*.

### D. Class Diagram Description
- Abstract class `Skill` implementing `Comparable<Skill>` and `Storable`. Extended by `TechnicalSkill` and `SoftSkill`.
- `SkillService` implementing generic `Searchable<Skill>`.
- `SkillGapAnalyzer` implementing `ProgressCalculable`.

---

## 6. Java Concepts Used in This Project

| Java Concept | Location in Code / Implementation Details |
| :--- | :--- |
| **Classes & Objects** | `Student`, `Project`, `Certification`, `CareerRole` models instantiated dynamically. |
| **Encapsulation** | Private member variables with public getter/setter accessors and validation inside models. |
| **Constructors** | Default and parameterized constructors across all model classes. |
| **Inheritance** | `TechnicalSkill` and `SoftSkill` extend abstract class `Skill` using `super()`. |
| **Method Overloading** | Overloaded constructors in `Skill`, `FileManager`, and filter methods in `SkillService`. |
| **Method Overriding** | Subclasses override abstract methods `getProficiencyWeight()`, `getDetails()`, and `toDataString()`. |
| **Polymorphism** | `List<Skill>` storing mixed instances of `TechnicalSkill` and `SoftSkill`, invoking overridden methods dynamically. |
| **Abstraction** | `abstract class Skill`, interfaces `ProgressCalculable`, `Searchable<T>`, `Storable`. |
| **Interfaces** | `Storable` for persistence formatting; `Searchable<T>` for filtering; `ProgressCalculable` for algorithms. |
| **Collections Framework** | `ArrayList` for sequence storage; `HashMap` for $O(1)$ skill gap lookup. |
| **Custom Exception Handling** | Custom checked exceptions (`TrackerException`, `DuplicateSkillException`, `InvalidStudentDataException`, `SkillNotFoundException`, `DataPersistenceException`). |
| **File Handling** | `BufferedReader`, `BufferedWriter`, `FileReader`, `FileWriter` managing text files. |
| **Searching & Sorting Algorithms** | Custom **MergeSort** implementation in `AlgorithmUtil`, **Binary Search**, and Linear Search. |
| **Input Validation** | Regex validation for emails and numeric range checks in `ValidationUtil`. |

---
## 7. Future Enhancement Ideas
- **PDF Export**: Generate downloadable academic PDF certificates/reports.
- **Database Integration**: Add SQLite / H2 database options alongside text files.
- **Interactive Graphs**: Integrate JavaFX LineChart / BarChart for historical progress tracking over semesters.
