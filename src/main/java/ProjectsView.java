package com.tracker.ui;

import com.tracker.model.Project;
import com.tracker.service.ProgressTracker;
import com.tracker.util.ValidationUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class ProjectsView {
    private final ProgressTracker progressTracker;
    private final Runnable refreshCallback;

    private TableView<Project> table;
    private ObservableList<Project> masterData;

    public ProjectsView(ProgressTracker progressTracker, Runnable refreshCallback) {
        this.progressTracker = progressTracker;
        this.refreshCallback = refreshCallback;
    }

    public Region getView() {
        VBox mainBox = new VBox(20);
        mainBox.setPadding(new Insets(25));

        Text title = new Text("Project Portfolio Management");
        title.getStyleClass().add("card-title");
        title.setStyle("-fx-font-size: 24px;");

        HBox toolBar = new HBox(15);
        toolBar.setAlignment(Pos.CENTER_LEFT);

        Button btnAdd = new Button("+ Add New Project");
        btnAdd.getStyleClass().add("button-primary");
        btnAdd.setOnAction(e -> showAddProjectDialog());

        Button btnToggle = new Button("Toggle Completion Status");
        btnToggle.getStyleClass().add("button-secondary");
        btnToggle.setOnAction(e -> toggleStatus());

        Button btnDelete = new Button("Delete Selected");
        btnDelete.getStyleClass().add("button-danger");
        btnDelete.setOnAction(e -> deleteSelected());

        toolBar.getChildren().addAll(btnAdd, btnToggle, btnDelete);

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Project, String> colTitle = new TableColumn<>("Project Title");
        colTitle.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));

        TableColumn<Project, String> colTech = new TableColumn<>("Tech Stack");
        colTech.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTechStack()));

        TableColumn<Project, String> colRole = new TableColumn<>("Role");
        colRole.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole()));

        TableColumn<Project, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isCompleted() ? "COMPLETED" : "IN PROGRESS"));

        TableColumn<Project, String> colDesc = new TableColumn<>("Description");
        colDesc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));

        table.getColumns().addAll(colTitle, colTech, colRole, colStatus, colDesc);

        refreshTable();

        mainBox.getChildren().addAll(title, toolBar, table);
        return mainBox;
    }

    private void refreshTable() {
        masterData = FXCollections.observableArrayList(progressTracker.getProjects());
        table.setItems(masterData);
    }

    private void toggleStatus() {
        Project selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setCompleted(!selected.isCompleted());
                progressTracker.addProject(selected);
                refreshTable();
                if (refreshCallback != null) refreshCallback.run();
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        }
    }

    private void deleteSelected() {
        Project selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                progressTracker.deleteProject(selected.getId());
                refreshTable();
                if (refreshCallback != null) refreshCallback.run();
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        }
    }

    private void showAddProjectDialog() {
        Dialog<Project> dialog = new Dialog<>();
        dialog.setTitle("Add Project");

        ButtonType saveButtonType = new ButtonType("Save Project", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField txtTitle = new TextField();
        TextArea txtDesc = new TextArea();
        txtDesc.setPrefRowCount(3);
        TextField txtTech = new TextField();
        TextField txtRole = new TextField();
        CheckBox chkCompleted = new CheckBox("Completed");

        grid.add(new Label("Project Title:"), 0, 0);
        grid.add(txtTitle, 1, 0);

        grid.add(new Label("Tech Stack:"), 0, 1);
        grid.add(txtTech, 1, 1);

        grid.add(new Label("Your Role:"), 0, 2);
        grid.add(txtRole, 1, 2);

        grid.add(new Label("Description:"), 0, 3);
        grid.add(txtDesc, 1, 3);

        grid.add(new Label("Status:"), 0, 4);
        grid.add(chkCompleted, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == saveButtonType) {
                try {
                    ValidationUtil.validateNotEmpty(txtTitle.getText(), "Title");
                    return new Project("PRJ-" + System.currentTimeMillis(), txtTitle.getText().trim(),
                            txtDesc.getText().trim(), txtTech.getText().trim(), txtRole.getText().trim(), chkCompleted.isSelected());
                } catch (Exception ex) {
                    showAlert("Validation Error", ex.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(project -> {
            try {
                progressTracker.addProject(project);
                refreshTable();
                if (refreshCallback != null) refreshCallback.run();
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        });
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("System Notice");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
