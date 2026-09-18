package com.tracker.ui;

import com.tracker.exception.DuplicateSkillException;
import com.tracker.model.*;
import com.tracker.service.SkillService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.List;

public class SkillsView {
    private final SkillService skillService;
    private final Runnable refreshCallback;

    private TableView<Skill> table;
    private ObservableList<Skill> masterData;

    public SkillsView(SkillService skillService, Runnable refreshCallback) {
        this.skillService = skillService;
        this.refreshCallback = refreshCallback;
    }

    public Region getView() {
        VBox mainBox = new VBox(20);
        mainBox.setPadding(new Insets(25));

        Text title = new Text("Technical & Soft Skills Management");
        title.getStyleClass().add("card-title");
        title.setStyle("-fx-font-size: 24px;");

        // Action Toolbar
        HBox toolBar = new HBox(15);
        toolBar.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Search skill by name or category...");
        searchField.setPrefWidth(250);
        searchField.textProperty().addListener((obs, oldV, newV) -> filterData(newV));

        ComboBox<String> sortCombo = new ComboBox<>();
        sortCombo.getItems().addAll("Default", "Sorted by Name (A-Z)", "Sorted by Proficiency (High->Low)");
        sortCombo.setValue("Default");
        sortCombo.setOnAction(e -> applySorting(sortCombo.getValue()));

        Button btnAddTech = new Button("+ Add Technical Skill");
        btnAddTech.getStyleClass().add("button-primary");
        btnAddTech.setOnAction(e -> showAddSkillDialog(true));

        Button btnAddSoft = new Button("+ Add Soft Skill");
        btnAddSoft.getStyleClass().add("button-secondary");
        btnAddSoft.setOnAction(e -> showAddSkillDialog(false));

        Button btnDelete = new Button("Delete Selected");
        btnDelete.getStyleClass().add("button-danger");
        btnDelete.setOnAction(e -> deleteSelectedSkill());

        toolBar.getChildren().addAll(searchField, sortCombo, btnAddTech, btnAddSoft, btnDelete);

        // Table Setup
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Skill, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSkillType()));

        TableColumn<Skill, String> colName = new TableColumn<>("Skill Name");
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Skill, String> colProf = new TableColumn<>("Proficiency");
        colProf.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProficiency().getDisplayName()));

        TableColumn<Skill, String> colCat = new TableColumn<>("Category");
        colCat.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory().getDisplayName()));

        TableColumn<Skill, String> colDetails = new TableColumn<>("Specific Details");
        colDetails.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDetails()));

        table.getColumns().addAll(colType, colName, colProf, colCat, colDetails);

        refreshTableData();

        mainBox.getChildren().addAll(title, toolBar, table);
        return mainBox;
    }

    private void refreshTableData() {
        masterData = FXCollections.observableArrayList(skillService.getAllSkills());
        table.setItems(masterData);
    }

    private void filterData(String query) {
        if (query == null || query.trim().isEmpty()) {
            table.setItems(masterData);
        } else {
            List<Skill> filtered = skillService.searchByNameOrKeyword(query);
            table.setItems(FXCollections.observableArrayList(filtered));
        }
    }

    private void applySorting(String sortOption) {
        if ("Sorted by Name (A-Z)".equals(sortOption)) {
            table.setItems(FXCollections.observableArrayList(skillService.getSkillsSortedByNameAlphabetical()));
        } else if ("Sorted by Proficiency (High->Low)".equals(sortOption)) {
            table.setItems(FXCollections.observableArrayList(skillService.getSkillsSortedByProficiencyDescending()));
        } else {
            refreshTableData();
        }
    }

    private void deleteSelectedSkill() {
        Skill selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                skillService.deleteSkill(selected.getId());
                refreshTableData();
                if (refreshCallback != null) refreshCallback.run();
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        } else {
            showAlert("No Selection", "Please select a skill from the table to delete.");
        }
    }

    private void showAddSkillDialog(boolean isTechnical) {
        Dialog<Skill> dialog = new Dialog<>();
        dialog.setTitle(isTechnical ? "Add Technical Skill" : "Add Soft Skill");

        ButtonType saveButtonType = new ButtonType("Add Skill", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField txtName = new TextField();
        ComboBox<ProficiencyLevel> comboProf = new ComboBox<>(FXCollections.observableArrayList(ProficiencyLevel.values()));
        comboProf.setValue(ProficiencyLevel.INTERMEDIATE);

        ComboBox<SkillCategory> comboCat = new ComboBox<>(FXCollections.observableArrayList(SkillCategory.values()));
        comboCat.setValue(isTechnical ? SkillCategory.PROGRAMMING : SkillCategory.SOFT_SKILLS);

        TextField txtExtra1 = new TextField();
        TextField txtExtra2 = new TextField();

        grid.add(new Label("Skill Name:"), 0, 0);
        grid.add(txtName, 1, 0);
        grid.add(new Label("Proficiency Level:"), 0, 1);
        grid.add(comboProf, 1, 1);
        grid.add(new Label("Category:"), 0, 2);
        grid.add(comboCat, 1, 2);

        if (isTechnical) {
            txtExtra1.setPromptText("e.g. Java, Python");
            txtExtra2.setPromptText("e.g. Spring, JavaFX");
            grid.add(new Label("Primary Language:"), 0, 3);
            grid.add(txtExtra1, 1, 3);
            grid.add(new Label("Frameworks/Tools:"), 0, 4);
            grid.add(txtExtra2, 1, 4);
        } else {
            txtExtra1.setPromptText("e.g. Presentations, Team Projects");
            txtExtra2.setPromptText("Rating (1-5)");
            grid.add(new Label("Context Application:"), 0, 3);
            grid.add(txtExtra1, 1, 3);
            grid.add(new Label("Peer Rating (1-5):"), 0, 4);
            grid.add(txtExtra2, 1, 4);
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String name = txtName.getText().trim();
                    if (name.isEmpty()) return null;

                    if (isTechnical) {
                        return new TechnicalSkill("SK-" + System.currentTimeMillis(), name, 
                                comboProf.getValue(), comboCat.getValue(), 
                                txtExtra1.getText().trim(), txtExtra2.getText().trim());
                    } else {
                        int rating = 4;
                        try { rating = Integer.parseInt(txtExtra2.getText().trim()); } catch (Exception ignored) {}
                        return new SoftSkill("SK-" + System.currentTimeMillis(), name, 
                                comboProf.getValue(), txtExtra1.getText().trim(), rating);
                    }
                } catch (Exception ex) {
                    showAlert("Invalid Input", ex.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(skill -> {
            try {
                skillService.addSkill(skill);
                refreshTableData();
                if (refreshCallback != null) refreshCallback.run();
            } catch (DuplicateSkillException e) {
                showAlert("Duplicate Skill Error", e.getMessage());
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
