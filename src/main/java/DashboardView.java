package com.tracker.ui;

import com.tracker.model.ProficiencyLevel;
import com.tracker.model.SkillGapResult;
import com.tracker.service.ProgressTracker;
import com.tracker.service.SkillService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.Map;

public class DashboardView {
    private final ProgressTracker progressTracker;
    private final SkillService skillService;

    public DashboardView(ProgressTracker progressTracker, SkillService skillService) {
        this.progressTracker = progressTracker;
        this.skillService = skillService;
    }

    public Region getView() {
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(25));

        // Header Section
        VBox headerBox = new VBox(5);
        Text title = new Text("Academic Dashboard & Career Summary");
        title.getStyleClass().add("card-title");
        title.setStyle("-fx-font-size: 24px;");

        Text subtitle = new Text("Welcome back, " + progressTracker.getStudent().getName() + 
                " (" + progressTracker.getStudent().getRegNo() + " - " + progressTracker.getStudent().getBranch() + ")");
        subtitle.getStyleClass().add("card-subtitle");
        headerBox.getChildren().addAll(title, subtitle);

        // Quick Metrics Stat Cards Row
        HBox statsRow = new HBox(15);
        statsRow.setAlignment(Pos.CENTER);

        SkillGapResult result = progressTracker.getCurrentSkillGapAnalysis();
        int totalSkills = skillService.getAllSkills().size();
        int totalProjects = progressTracker.getProjects().size();
        int totalCerts = progressTracker.getCertifications().size();

        statsRow.getChildren().addAll(
                createStatCard("Total Skills", String.valueOf(totalSkills), "Technical & Soft Skills"),
                createStatCard("Target Role", progressTracker.getStudent().getTargetCareerRole(), "Current Focus"),
                createStatCard("Career Readiness", String.format("%.1f%%", result.getOverallReadinessPercentage()), "Algorithmic Weighted Score"),
                createStatCard("Projects & Certs", totalProjects + " Projects / " + totalCerts + " Certs", "Completed Milestones")
        );

        // Readiness Progress & Breakdown Section
        HBox detailsRow = new HBox(20);
        
        // Left Column: Career Readiness Breakdown Card
        VBox readinessCard = new VBox(15);
        readinessCard.getStyleClass().add("card-panel");
        HBox.setHgrow(readinessCard, Priority.ALWAYS);

        Text readinessTitle = new Text("Career Readiness Analysis (" + result.getTargetRoleTitle() + ")");
        readinessTitle.getStyleClass().add("card-title");

        ProgressBar mainProgressBar = new ProgressBar(result.getOverallReadinessPercentage() / 100.0);
        mainProgressBar.setMaxWidth(Double.MAX_VALUE);
        mainProgressBar.setPrefHeight(20);

        Label readinessPercentLabel = new Label(String.format("Overall Career Readiness: %.1f%%", result.getOverallReadinessPercentage()));
        readinessPercentLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #818cf8;");

        VBox componentBreakdownBox = new VBox(10);
        componentBreakdownBox.getChildren().addAll(
                createProgressRow("Skill Match Score (50%)", result.getSkillScoreComponent()),
                createProgressRow("Project Experience (25%)", result.getProjectScoreComponent()),
                createProgressRow("Certifications Score (15%)", result.getCertificationScoreComponent()),
                createProgressRow("Breadth & Soft Skills (10%)", result.getBreadthScoreComponent())
        );

        readinessCard.getChildren().addAll(readinessTitle, readinessPercentLabel, mainProgressBar, componentBreakdownBox);

        // Right Column: Proficiency Breakdown Card
        VBox profCard = new VBox(15);
        profCard.getStyleClass().add("card-panel");
        profCard.setPrefWidth(320);

        Text profTitle = new Text("Skill Proficiency Breakdown");
        profTitle.getStyleClass().add("card-title");

        Map<ProficiencyLevel, Integer> profMap = progressTracker.getProficiencyBreakdown();
        VBox profList = new VBox(12);
        for (ProficiencyLevel lvl : ProficiencyLevel.values()) {
            int count = profMap.getOrDefault(lvl, 0);
            HBox row = new HBox();
            Label lblName = new Label(lvl.getDisplayName());
            lblName.setStyle("-fx-font-weight: bold;");
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            Label lblCount = new Label(count + " skills");
            lblCount.setStyle("-fx-text-fill: #34d399; -fx-font-weight: bold;");
            row.getChildren().addAll(lblName, spacer, lblCount);
            profList.getChildren().add(row);
        }

        profCard.getChildren().addAll(profTitle, profList);

        detailsRow.getChildren().addAll(readinessCard, profCard);

        mainContainer.getChildren().addAll(headerBox, statsRow, detailsRow);
        return mainContainer;
    }

    private VBox createStatCard(String titleText, String valueText, String subText) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card-panel");
        card.setPrefWidth(220);
        HBox.setHgrow(card, Priority.ALWAYS);

        Label title = new Label(titleText);
        title.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 13px; -fx-font-weight: bold;");

        Label value = new Label(valueText);
        value.getStyleClass().add("stat-number");
        value.setWrapText(true);

        Label sub = new Label(subText);
        sub.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");

        card.getChildren().addAll(title, value, sub);
        return card;
    }

    private VBox createProgressRow(String labelText, double value) {
        VBox box = new VBox(4);
        HBox labelRow = new HBox();
        Label lbl = new Label(labelText);
        lbl.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 13px;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label valLbl = new Label(String.format("%.1f%%", value));
        valLbl.setStyle("-fx-text-fill: #38bdf8; -fx-font-weight: bold;");
        labelRow.getChildren().addAll(lbl, spacer, valLbl);

        ProgressBar pb = new ProgressBar(value / 100.0);
        pb.setMaxWidth(Double.MAX_VALUE);
        pb.setPrefHeight(10);

        box.getChildren().addAll(labelRow, pb);
        return box;
    }
}
