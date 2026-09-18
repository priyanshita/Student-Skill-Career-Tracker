package com.tracker.ui;

import com.tracker.model.CareerRole;
import com.tracker.model.Student;
import com.tracker.service.CareerManager;
import com.tracker.service.ProgressTracker;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;

public class CareerGoalsView {
    private final CareerManager careerManager;
    private final ProgressTracker progressTracker;
    private final Runnable refreshCallback;

    public CareerGoalsView(CareerManager careerManager, ProgressTracker progressTracker, Runnable refreshCallback) {
        this.careerManager = careerManager;
        this.progressTracker = progressTracker;
        this.refreshCallback = refreshCallback;
    }

    public Region getView() {
        VBox mainBox = new VBox(20);
        mainBox.setPadding(new Insets(25));

        Text title = new Text("Career Goals & Role Definitions");
        title.getStyleClass().add("card-title");
        title.setStyle("-fx-font-size: 24px;");

        VBox roleContainer = new VBox(15);
        
        List<CareerRole> roles = careerManager.getAllCareerRoles();
        Student student = progressTracker.getStudent();

        for (CareerRole role : roles) {
            VBox roleCard = new VBox(10);
            roleCard.getStyleClass().add("card-panel");

            HBox header = new HBox();
            Label roleTitle = new Label(role.getTitle());
            roleTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #818cf8;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            boolean isCurrent = role.getTitle().equalsIgnoreCase(student.getTargetCareerRole());
            Button btnSelect = new Button(isCurrent ? "Active Goal" : "Select Goal");
            btnSelect.getStyleClass().add(isCurrent ? "button-secondary" : "button-primary");
            btnSelect.setDisable(isCurrent);

            btnSelect.setOnAction(e -> {
                try {
                    student.setTargetCareerRole(role.getTitle());
                    progressTracker.updateStudent(student);
                    if (refreshCallback != null) refreshCallback.run();
                } catch (Exception ex) {
                    System.err.println("Error setting role: " + ex.getMessage());
                }
            });

            header.getChildren().addAll(roleTitle, spacer, btnSelect);

            Label desc = new Label(role.getDescription());
            desc.setStyle("-fx-text-fill: #94a3b8;");

            Label reqLbl = new Label("Required Skills: " + String.join(", ", role.getRequiredSkills()));
            reqLbl.setStyle("-fx-text-fill: #34d399; -fx-font-weight: bold;");

            Label recLbl = new Label("Recommended Bonus Skills: " + String.join(", ", role.getRecommendedSkills()));
            recLbl.setStyle("-fx-text-fill: #38bdf8;");

            roleCard.getChildren().addAll(header, desc, reqLbl, recLbl);
            roleContainer.getChildren().add(roleCard);
        }

        Button btnAddCustomRole = new Button("+ Define Custom Career Role");
        btnAddCustomRole.getStyleClass().add("button-primary");
        btnAddCustomRole.setOnAction(e -> showAddCustomRoleDialog());

        mainBox.getChildren().addAll(title, btnAddCustomRole, roleContainer);
        return new ScrollPane(mainBox);
    }

    private void showAddCustomRoleDialog() {
        Dialog<CareerRole> dialog = new Dialog<>();
        dialog.setTitle("Create Custom Career Role");

        ButtonType saveBtn = new ButtonType("Create Role", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField txtTitle = new TextField();
        TextArea txtDesc = new TextArea();
        txtDesc.setPrefRowCount(2);
        TextField txtReq = new TextField();
        txtReq.setPromptText("Comma separated: Python, SQL, Java");
        TextField txtRec = new TextField();
        txtRec.setPromptText("Comma separated: AWS, Docker");

        grid.add(new Label("Role Title:"), 0, 0);
        grid.add(txtTitle, 1, 0);

        grid.add(new Label("Description:"), 0, 1);
        grid.add(txtDesc, 1, 1);

        grid.add(new Label("Required Skills:"), 0, 2);
        grid.add(txtReq, 1, 2);

        grid.add(new Label("Recommended Skills:"), 0, 3);
        grid.add(txtRec, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn && !txtTitle.getText().trim().isEmpty()) {
                List<String> reqList = Arrays.asList(txtReq.getText().split(","));
                List<String> recList = Arrays.asList(txtRec.getText().split(","));
                return new CareerRole("ROLE-" + System.currentTimeMillis(), txtTitle.getText().trim(),
                        txtDesc.getText().trim(), reqList, recList);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(role -> {
            careerManager.addOrUpdateCareerRole(role);
            if (refreshCallback != null) refreshCallback.run();
        });
    }
}
