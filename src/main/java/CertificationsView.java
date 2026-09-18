package com.tracker.ui;

import com.tracker.model.Certification;
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

public class CertificationsView {
    private final ProgressTracker progressTracker;
    private final Runnable refreshCallback;

    private TableView<Certification> table;
    private ObservableList<Certification> masterData;

    public CertificationsView(ProgressTracker progressTracker, Runnable refreshCallback) {
        this.progressTracker = progressTracker;
        this.refreshCallback = refreshCallback;
    }

    public Region getView() {
        VBox mainBox = new VBox(20);
        mainBox.setPadding(new Insets(25));

        Text title = new Text("Certifications & Credentials");
        title.getStyleClass().add("card-title");
        title.setStyle("-fx-font-size: 24px;");

        HBox toolBar = new HBox(15);
        toolBar.setAlignment(Pos.CENTER_LEFT);

        Button btnAdd = new Button("+ Add Certification");
        btnAdd.getStyleClass().add("button-primary");
        btnAdd.setOnAction(e -> showAddDialog());

        Button btnDelete = new Button("Delete Selected");
        btnDelete.getStyleClass().add("button-danger");
        btnDelete.setOnAction(e -> deleteSelected());

        toolBar.getChildren().addAll(btnAdd, btnDelete);

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Certification, String> colName = new TableColumn<>("Certification Name");
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Certification, String> colOrg = new TableColumn<>("Issuing Organization");
        colOrg.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIssuingOrganization()));

        TableColumn<Certification, String> colDate = new TableColumn<>("Issue Date");
        colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIssueDate()));

        TableColumn<Certification, String> colSkill = new TableColumn<>("Associated Skill");
        colSkill.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRelevantSkill()));

        table.getColumns().addAll(colName, colOrg, colDate, colSkill);

        refreshTable();

        mainBox.getChildren().addAll(title, toolBar, table);
        return mainBox;
    }

    private void refreshTable() {
        masterData = FXCollections.observableArrayList(progressTracker.getCertifications());
        table.setItems(masterData);
    }

    private void deleteSelected() {
        Certification selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                progressTracker.deleteCertification(selected.getId());
                refreshTable();
                if (refreshCallback != null) refreshCallback.run();
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        }
    }

    private void showAddDialog() {
        Dialog<Certification> dialog = new Dialog<>();
        dialog.setTitle("Add Certification");

        ButtonType saveButtonType = new ButtonType("Add Certification", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField txtName = new TextField();
        TextField txtOrg = new TextField();
        DatePicker datePicker = new DatePicker();
        TextField txtSkill = new TextField();

        grid.add(new Label("Certification Name:"), 0, 0);
        grid.add(txtName, 1, 0);

        grid.add(new Label("Issuing Organization:"), 0, 1);
        grid.add(txtOrg, 1, 1);

        grid.add(new Label("Issue Date:"), 0, 2);
        grid.add(datePicker, 1, 2);

        grid.add(new Label("Associated Skill:"), 0, 3);
        grid.add(txtSkill, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == saveButtonType) {
                try {
                    ValidationUtil.validateNotEmpty(txtName.getText(), "Certification Name");
                    ValidationUtil.validateNotEmpty(txtOrg.getText(), "Issuing Organization");
                    String dateStr = datePicker.getValue() != null ? datePicker.getValue().toString() : "2024-01-01";

                    return new Certification("CRT-" + System.currentTimeMillis(), txtName.getText().trim(),
                            txtOrg.getText().trim(), dateStr, txtSkill.getText().trim());
                } catch (Exception ex) {
                    showAlert("Validation Error", ex.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(cert -> {
            try {
                progressTracker.addCertification(cert);
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
