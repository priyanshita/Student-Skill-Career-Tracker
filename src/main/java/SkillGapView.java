package com.tracker.ui;

import com.tracker.model.Skill;
import com.tracker.model.SkillGapResult;
import com.tracker.service.ProgressTracker;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class SkillGapView {
    private final ProgressTracker progressTracker;

    public SkillGapView(ProgressTracker progressTracker) {
        this.progressTracker = progressTracker;
    }

    public Region getView() {
        VBox mainBox = new VBox(20);
        mainBox.setPadding(new Insets(25));

        Text title = new Text("Algorithmic Skill Gap & Career Readiness Analysis");
        title.getStyleClass().add("card-title");
        title.setStyle("-fx-font-size: 24px;");

        SkillGapResult result = progressTracker.getCurrentSkillGapAnalysis();

        // Target Summary Header Card
        VBox summaryCard = new VBox(12);
        summaryCard.getStyleClass().add("card-panel");

        Text targetTitle = new Text("Target Career Goal: " + result.getTargetRoleTitle());
        targetTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #818cf8;");

        Label readinessLabel = new Label(String.format("Calculated Career Readiness Score: %.1f%%", result.getOverallReadinessPercentage()));
        readinessLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #10b981;");

        ProgressBar pb = new ProgressBar(result.getOverallReadinessPercentage() / 100.0);
        pb.setMaxWidth(Double.MAX_VALUE);
        pb.setPrefHeight(18);

        summaryCard.getChildren().addAll(targetTitle, readinessLabel, pb);

        // 3 Column Grid for Acquired, Partial, Missing
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        // Column 1: Acquired Skills
        VBox acqCard = createSkillCategoryCard("Acquired Skills (Advanced)", "badge-acquired");
        for (Skill s : result.getAcquiredSkills()) {
            Label lbl = new Label("✔ " + s.getName() + " (" + s.getCategory().getDisplayName() + ")");
            lbl.setStyle("-fx-text-fill: #34d399; -fx-font-weight: bold;");
            acqCard.getChildren().add(lbl);
        }
        if (result.getAcquiredSkills().isEmpty()) {
            acqCard.getChildren().add(new Label("No advanced skills acquired yet."));
        }

        // Column 2: Partially Developed Skills
        VBox partCard = createSkillCategoryCard("Partially Developed", "badge-partial");
        for (Skill s : result.getPartiallyDevelopedSkills()) {
            Label lbl = new Label("⚡ " + s.getName() + " (" + s.getProficiency().getDisplayName() + ")");
            lbl.setStyle("-fx-text-fill: #fbbf24; -fx-font-weight: bold;");
            partCard.getChildren().add(lbl);
        }
        if (result.getPartiallyDevelopedSkills().isEmpty()) {
            partCard.getChildren().add(new Label("No partially developed skills."));
        }

        // Column 3: Missing Required Skills
        VBox missCard = createSkillCategoryCard("Missing Skills (To Learn)", "badge-missing");
        for (String m : result.getMissingSkills()) {
            Label lbl = new Label("✖ " + m);
            lbl.setStyle("-fx-text-fill: #fca5a5; -fx-font-weight: bold;");
            missCard.getChildren().add(lbl);
        }
        if (result.getMissingSkills().isEmpty()) {
            missCard.getChildren().add(new Label("Great job! All required skills matched."));
        }

        grid.add(acqCard, 0, 0);
        grid.add(partCard, 1, 0);
        grid.add(missCard, 2, 0);

        ColumnConstraints col1 = new ColumnConstraints(); col1.setPercentWidth(33.3);
        ColumnConstraints col2 = new ColumnConstraints(); col2.setPercentWidth(33.3);
        ColumnConstraints col3 = new ColumnConstraints(); col3.setPercentWidth(33.3);
        grid.getColumnConstraints().addAll(col1, col2, col3);

        // Learning Action Plan Card
        VBox planCard = new VBox(12);
        planCard.getStyleClass().add("card-panel");

        Text planTitle = new Text("Recommended Next Steps & Action Plan");
        planTitle.getStyleClass().add("card-title");

        VBox planList = new VBox(8);
        int step = 1;
        for (String next : result.getSuggestedNextSkills()) {
            Label stepLbl = new Label(step + ". " + next);
            stepLbl.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 14px;");
            planList.getChildren().add(stepLbl);
            step++;
        }

        planCard.getChildren().addAll(planTitle, planList);

        mainBox.getChildren().addAll(title, summaryCard, grid, planCard);
        return new ScrollPane(mainBox);
    }

    private VBox createSkillCategoryCard(String headerText, String badgeClass) {
        VBox card = new VBox(10);
        card.getStyleClass().add("card-panel");

        Label badge = new Label(headerText);
        badge.getStyleClass().add(badgeClass);

        card.getChildren().add(badge);
        return card;
    }
}
