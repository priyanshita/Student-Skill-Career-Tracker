package com.tracker.ui;

import com.tracker.model.*;
import com.tracker.service.ProgressTracker;
import com.tracker.service.SkillService;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.time.LocalDate;

public class ProgressReportView {
    private final ProgressTracker progressTracker;
    private final SkillService skillService;

    public ProgressReportView(ProgressTracker progressTracker, SkillService skillService) {
        this.progressTracker = progressTracker;
        this.skillService = skillService;
    }

    public Region getView() {
        VBox mainBox = new VBox(20);
        mainBox.setPadding(new Insets(25));

        Text title = new Text("Comprehensive Academic Progress Report");
        title.getStyleClass().add("card-title");
        title.setStyle("-fx-font-size: 24px;");

        VBox reportCard = new VBox(20);
        reportCard.getStyleClass().add("card-panel");

        Student student = progressTracker.getStudent();
        SkillGapResult result = progressTracker.getCurrentSkillGapAnalysis();

        // Section 1: Student Metadata Header
        VBox metaSection = new VBox(8);
        metaSection.setStyle("-fx-border-color: #334155; -fx-border-width: 0 0 1px 0; -fx-padding: 0 0 15px 0;");
        metaSection.getChildren().addAll(
                new Label("STUDENT NAME: " + student.getName()),
                new Label("REGISTRATION NO: " + student.getRegNo()),
                new Label("DEPARTMENT / BRANCH: " + student.getBranch() + " (Year " + student.getYear() + ")"),
                new Label("UNIVERSITY EMAIL: " + student.getEmail()),
                new Label("REPORT DATE: " + LocalDate.now())
        );

        // Section 2: Career Readiness Summary
        VBox careerSection = new VBox(8);
        careerSection.getChildren().addAll(
                createSubHeader("1. CAREER GOAL & READINESS SUMMARY"),
                new Label("Target Role: " + result.getTargetRoleTitle()),
                new Label(String.format("Calculated Career Readiness Score: %.1f%%", result.getOverallReadinessPercentage())),
                new Label(String.format("  - Skill Component (50%% Weight): %.1f%%", result.getSkillScoreComponent())),
                new Label(String.format("  - Project Component (25%% Weight): %.1f%%", result.getProjectScoreComponent())),
                new Label(String.format("  - Certification Component (15%% Weight): %.1f%%", result.getCertificationScoreComponent())),
                new Label(String.format("  - Breadth Component (10%% Weight): %.1f%%", result.getBreadthScoreComponent()))
        );

        // Section 3: Skill Inventory
        VBox skillSection = new VBox(8);
        skillSection.getChildren().add(createSubHeader("2. TECHNICAL & SOFT SKILLS INVENTORY"));
        for (Skill s : skillService.getAllSkills()) {
            skillSection.getChildren().add(new Label("• " + s.getName() + " | " + s.getProficiency().getDisplayName() + " | " + s.getCategory().getDisplayName() + " [" + s.getDetails() + "]"));
        }

        // Section 4: Projects Summary
        VBox projSection = new VBox(8);
        projSection.getChildren().add(createSubHeader("3. PROJECT PORTFOLIO SUMMARY"));
        for (Project p : progressTracker.getProjects()) {
            projSection.getChildren().add(new Label("• " + p.getTitle() + " (" + (p.isCompleted() ? "COMPLETED" : "IN PROGRESS") + ") - Tech: " + p.getTechStack()));
        }

        // Section 5: Certifications Summary
        VBox certSection = new VBox(8);
        certSection.getChildren().add(createSubHeader("4. CERTIFICATIONS & CREDENTIALS"));
        for (Certification c : progressTracker.getCertifications()) {
            certSection.getChildren().add(new Label("• " + c.getName() + " by " + c.getIssuingOrganization() + " (Issued: " + c.getIssueDate() + ")"));
        }

        reportCard.getChildren().addAll(metaSection, careerSection, skillSection, projSection, certSection);
        mainBox.getChildren().addAll(title, reportCard);
        return new ScrollPane(mainBox);
    }

    private Label createSubHeader(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #818cf8;");
        return lbl;
    }
}
