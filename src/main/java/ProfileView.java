package com.tracker.ui;

import com.tracker.model.Student;
import com.tracker.service.CareerManager;
import com.tracker.service.ProgressTracker;
import com.tracker.util.ValidationUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class ProfileView {
    private final ProgressTracker progressTracker;
    private final CareerManager careerManager;
    private final Runnable refreshCallback;

    public ProfileView(ProgressTracker progressTracker, CareerManager careerManager, Runnable refreshCallback) {
        this.progressTracker = progressTracker;
        this.careerManager = careerManager;
        this.refreshCallback = refreshCallback;
    }

    public Region getView() {
        VBox mainBox = new VBox(20);
        mainBox.setPadding(new Insets(25));

        Text title = new Text("Student Profile Management");
        title.getStyleClass().add("card-title");
        title.setStyle("-fx-font-size: 24px;");

        VBox formCard = new VBox(15);
        formCard.getStyleClass().add("card-panel");

        Student student = progressTracker.getStudent();

        TextField txtName = new TextField(student.getName());
        TextField txtRegNo = new TextField(student.getRegNo());
        TextField txtBranch = new TextField(student.getBranch());
        TextField txtYear = new TextField(String.valueOf(student.getYear()));
        TextField txtEmail = new TextField(student.getEmail());

        ComboBox<String> comboRole = new ComboBox<>();
        careerManager.getAllCareerRoles().forEach(r -> comboRole.getItems().add(r.getTitle()));
        comboRole.setValue(student.getTargetCareerRole());

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        grid.add(new Label("Full Name:"), 0, 0);
        grid.add(txtName, 1, 0);

        grid.add(new Label("Registration Number:"), 0, 1);
        grid.add(txtRegNo, 1, 1);

        grid.add(new Label("Branch / Stream:"), 0, 2);
        grid.add(txtBranch, 1, 2);

        grid.add(new Label("Academic Year (1-4):"), 0, 3);
        grid.add(txtYear, 1, 3);

        grid.add(new Label("University Email:"), 0, 4);
        grid.add(txtEmail, 1, 4);

        grid.add(new Label("Target Career Goal:"), 0, 5);
        grid.add(comboRole, 1, 5);

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-weight: bold;");

        Button btnSave = new Button("Update Student Profile");
        btnSave.getStyleClass().add("button-primary");
        btnSave.setOnAction(e -> {
            try {
                ValidationUtil.validateNotEmpty(txtName.getText(), "Name");
                ValidationUtil.validateNotEmpty(txtRegNo.getText(), "Registration Number");
                ValidationUtil.validateNotEmpty(txtBranch.getText(), "Branch");
                ValidationUtil.validateEmail(txtEmail.getText());

                int year = Integer.parseInt(txtYear.getText().trim());
                ValidationUtil.validatePositiveYear(year);

                student.setName(txtName.getText().trim());
                student.setRegNo(txtRegNo.getText().trim());
                student.setBranch(txtBranch.getText().trim());
                student.setYear(year);
                student.setEmail(txtEmail.getText().trim());
                if (comboRole.getValue() != null) {
                    student.setTargetCareerRole(comboRole.getValue());
                }

                progressTracker.updateStudent(student);
                statusLabel.setStyle("-fx-text-fill: #34d399;");
                statusLabel.setText("Profile updated successfully!");
                if (refreshCallback != null) refreshCallback.run();
            } catch (Exception ex) {
                statusLabel.setStyle("-fx-text-fill: #ef4444;");
                statusLabel.setText("Error: " + ex.getMessage());
            }
        });

        HBox btnBox = new HBox(15, btnSave, statusLabel);
        btnBox.setAlignment(Pos.CENTER_LEFT);

        formCard.getChildren().addAll(grid, btnBox);
        mainBox.getChildren().addAll(title, formCard);
        return mainBox;
    }
}
