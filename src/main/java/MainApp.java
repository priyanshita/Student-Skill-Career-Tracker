package com.tracker.ui;

import com.tracker.repository.FileManager;
import com.tracker.service.CareerManager;
import com.tracker.service.ProgressTracker;
import com.tracker.service.SkillService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Main JavaFX Desktop Application entry point.
 * Provides sidebar navigation across 8 functional application views.
 */
public class MainApp extends Application {
    private FileManager fileManager;
    private SkillService skillService;
    private CareerManager careerManager;
    private ProgressTracker progressTracker;

    private BorderPane rootLayout;
    private List<Button> sidebarButtons;

    @Override
    public void start(Stage primaryStage) {
        // Initialize 3-tier architecture components
        fileManager = new FileManager();
        skillService = new SkillService(fileManager);
        careerManager = new CareerManager(fileManager);
        progressTracker = new ProgressTracker(fileManager, skillService, careerManager);

        sidebarButtons = new ArrayList<>();
        rootLayout = new BorderPane();

        // Create Sidebar
        VBox sidebar = createSidebar();
        rootLayout.setLeft(sidebar);

        // Load Default View (Dashboard)
        showView(0);

        Scene scene = new Scene(rootLayout, 1150, 720);
        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Note: External CSS stylesheet loaded via fallback.");
        }

        primaryStage.setTitle("Student Skill & Career Tracker - CSE / AI-ML Academic Desktop Suite");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(8);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(240);

        Label title = new Label("Skill & Career\nTracker");
        title.getStyleClass().add("sidebar-title");

        sidebar.getChildren().add(title);

        String[] menuLabels = {
                "📊  Dashboard",
                "👤  Student Profile",
                "⚡  My Skills",
                "📁  Projects",
                "📜  Certifications",
                "🎯  Career Goals",
                "🔍  Skill Gap Analysis",
                "📑  Progress Report"
        };

        for (int i = 0; i < menuLabels.length; i++) {
            final int index = i;
            Button btn = new Button(menuLabels[i]);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.getStyleClass().add("sidebar-button");
            btn.setOnAction(e -> showView(index));

            sidebarButtons.add(btn);
            sidebar.getChildren().add(btn);
        }

        return sidebar;
    }

    private void showView(int index) {
        for (int i = 0; i < sidebarButtons.size(); i++) {
            Button btn = sidebarButtons.get(i);
            if (i == index) {
                if (!btn.getStyleClass().contains("sidebar-button-active")) {
                    btn.getStyleClass().add("sidebar-button-active");
                }
            } else {
                btn.getStyleClass().remove("sidebar-button-active");
            }
        }

        Runnable refreshCurrent = () -> showView(index);

        Region view;
        switch (index) {
            case 0:
                view = new DashboardView(progressTracker, skillService).getView();
                break;
            case 1:
                view = new ProfileView(progressTracker, careerManager, refreshCurrent).getView();
                break;
            case 2:
                view = new SkillsView(skillService, refreshCurrent).getView();
                break;
            case 3:
                view = new ProjectsView(progressTracker, refreshCurrent).getView();
                break;
            case 4:
                view = new CertificationsView(progressTracker, refreshCurrent).getView();
                break;
            case 5:
                view = new CareerGoalsView(careerManager, progressTracker, refreshCurrent).getView();
                break;
            case 6:
                view = new SkillGapView(progressTracker).getView();
                break;
            case 7:
                view = new ProgressReportView(progressTracker, skillService).getView();
                break;
            default:
                view = new DashboardView(progressTracker, skillService).getView();
        }

        rootLayout.setCenter(view);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
