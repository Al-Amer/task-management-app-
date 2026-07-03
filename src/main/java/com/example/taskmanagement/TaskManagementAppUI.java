package com.example.taskmanagement;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class TaskManagementAppUI extends Application {

    private ConfigurableApplicationContext context;
    private TaskService taskService;

    // Observable list to hold tasks for the TableView
    private ObservableList<Task> taskList = FXCollections.observableArrayList();

    // NEW: TableView as class-level variable
    private TableView<Task> tableView;

    @Override
    public void init() throws Exception {
        // Start Spring Boot
        context = new SpringApplicationBuilder(TaskManagementAppApplication.class)
                .run();
        System.out.println("✅ Spring Boot has started!");

        // Get the TaskService
        taskService = context.getBean(TaskService.class);
        System.out.println("✅ TaskService is ready!");

        // Load tasks from database
        loadTasks();
    }

    private void loadTasks() {
        taskList.clear();
        taskList.addAll(taskService.getAllTasks());
        System.out.println("✅ Loaded " + taskList.size() + " tasks from database");
    }

    @Override
    public void start(Stage primaryStage) {
        // Create the main layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #ecf0f1;");
        root.setPadding(new Insets(20));

        // 1. Create the header
        Label headerLabel = new Label("📋 Task Management");
        headerLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        HBox header = new HBox(headerLabel);
        header.setPadding(new Insets(0, 0, 20, 0));

        // 2. Create the TableView (assign to class variable)
        tableView = createTableView();

        // 3. Create the button panel
        HBox buttonPanel = createButtonPanel();

        // 4. Create the stats panel
        HBox statsPanel = createStatsPanel();

        // Put everything together
        VBox centerBox = new VBox(10, header, statsPanel, tableView, buttonPanel);
        centerBox.setPadding(new Insets(10));

        root.setCenter(centerBox);

        // Create scene
        Scene scene = new Scene(root, 900, 700);
        primaryStage.setTitle("Task Management App");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("✅ Window is now visible!");
    }

    private HBox createStatsPanel() {
        Label totalTasks = new Label("📊 Total: " + taskList.size());
        totalTasks.setStyle("-fx-font-size: 14px; -fx-text-fill: #2c3e50;");

        long doneCount = taskList.stream().filter(Task::isDone).count();
        Label doneTasks = new Label("✅ Done: " + doneCount);
        doneTasks.setStyle("-fx-font-size: 14px; -fx-text-fill: #27ae60;");

        long pendingCount = taskList.size() - doneCount;
        Label pendingTasks = new Label("⏳ Pending: " + pendingCount);
        pendingTasks.setStyle("-fx-font-size: 14px; -fx-text-fill: #e67e22;");

        HBox statsPanel = new HBox(20, totalTasks, doneTasks, pendingTasks);
        statsPanel.setPadding(new Insets(0, 0, 15, 0));
        return statsPanel;
    }

    @SuppressWarnings("unchecked")
    private TableView<Task> createTableView() {
        TableView<Task> table = new TableView<>();
        table.setItems(taskList);
        table.setStyle("-fx-font-size: 14px;");

        // 1. Done column (with checkbox)
        TableColumn<Task, Boolean> doneColumn = new TableColumn<>("Done");
        doneColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isDone())
        );
        doneColumn.setCellFactory(CheckBoxTableCell.forTableColumn(doneColumn));
        doneColumn.setPrefWidth(60);

        // 2. Title column
        TableColumn<Task, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(200);

        // 3. Description column
        TableColumn<Task, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setPrefWidth(250);

        // 4. Created By column
        TableColumn<Task, String> createdByColumn = new TableColumn<>("Created By");
        createdByColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        createdByColumn.setPrefWidth(120);

        // 5. Created At column
        TableColumn<Task, String> createdAtColumn = new TableColumn<>("Created");
        createdAtColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCreatedAt().toString())
        );
        createdAtColumn.setPrefWidth(150);

        // Add all columns to the table
        table.getColumns().addAll(
                doneColumn,
                titleColumn,
                descriptionColumn,
                createdByColumn,
                createdAtColumn
        );

        // Green color for completed tasks
        table.setRowFactory(tv -> {
            TableRow<Task> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    // Double-click to toggle status
                    Task task = row.getItem();
                    toggleTaskStatus(task);
                }
            });

            // Change row color based on task status
            row.itemProperty().addListener((obs, oldTask, newTask) -> {
                if (newTask != null && newTask.isDone()) {
                    // Green background for completed tasks
                    row.setStyle(
                            "-fx-background-color: #d4edda; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-text-fill: #155724;"
                    );
                } else if (newTask != null && !newTask.isDone()) {
                    // Normal background for pending tasks
                    row.setStyle(
                            "-fx-background-color: transparent; " +
                                    "-fx-text-fill: #2c3e50;"
                    );
                }
            });

            return row;
        });

        return table;
    }

    private HBox createButtonPanel() {
        // 1. Add Task button
        Button addButton = new Button("➕ Add Task");
        addButton.setStyle(
                "-fx-background-color: #2ecc71; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-cursor: hand;"
        );
        addButton.setOnAction(e -> showAddTaskDialog());

        // 2. Delete Task button
        Button deleteButton = new Button("🗑️ Delete Task");
        deleteButton.setStyle(
                "-fx-background-color: #e74c3c; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-cursor: hand;"
        );
        deleteButton.setOnAction(e -> deleteSelectedTask());

        // 3. Refresh button
        Button refreshButton = new Button("🔄 Refresh");
        refreshButton.setStyle(
                "-fx-background-color: #3498db; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-cursor: hand;"
        );
        refreshButton.setOnAction(e -> {
            loadTasks();
            tableView.refresh();
        });

        // 4. Toggle Done button
        Button toggleButton = new Button("✅ Toggle Done");
        toggleButton.setStyle(
                "-fx-background-color: #f39c12; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-cursor: hand;"
        );
        toggleButton.setOnAction(e -> toggleSelectedTask());

        // 5. Filter by creator button
        Button filterButton = new Button("👤 Filter by Creator");
        filterButton.setStyle(
                "-fx-background-color: #9b59b6; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-cursor: hand;"
        );
        filterButton.setOnAction(e -> showFilterDialog());

        // Create HBox for buttons
        HBox buttonPanel = new HBox(10, addButton, deleteButton, toggleButton, filterButton, refreshButton);
        buttonPanel.setPadding(new Insets(20, 0, 0, 0));

        return buttonPanel;
    }

    private void showAddTaskDialog() {
        // Create a dialog for adding new task
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Add New Task");
        dialog.setHeaderText("Enter task details");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField();
        titleField.setPromptText("Task title");
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Task description");
        TextField creatorField = new TextField();
        creatorField.setPromptText("Your name");

        grid.add(new Label("Title:*"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Created By:*"), 0, 2);
        grid.add(creatorField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the title field by default
        titleField.requestFocus();

        // Convert the result to a Task object
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String title = titleField.getText().trim();
                String description = descriptionField.getText().trim();
                String createdBy = creatorField.getText().trim();

                if (!title.isEmpty() && !createdBy.isEmpty()) {
                    return taskService.createTask(title, description, createdBy);
                } else {
                    showAlert("Validation Error", "Title and Created By are required!");
                }
            }
            return null;
        });

        // Show the dialog and wait for result
        dialog.showAndWait().ifPresent(task -> {
            // Add the new task to the table
            taskList.add(task);
            tableView.refresh();
            System.out.println("✅ Added task: " + task.getTitle() + " (by: " + task.getCreatedBy() + ")");
        });
    }

    private void showFilterDialog() {
        // Create a dialog for filtering by creator
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Filter by Creator");
        dialog.setHeaderText("Enter creator name to filter");

        ButtonType filterButtonType = new ButtonType("Filter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(filterButtonType, ButtonType.CANCEL);

        TextField creatorField = new TextField();
        creatorField.setPromptText("Enter creator name");

        VBox vbox = new VBox(10, new Label("Creator Name:"), creatorField);
        vbox.setPadding(new Insets(20));
        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == filterButtonType) {
                return creatorField.getText().trim();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(creator -> {
            if (!creator.isEmpty()) {
                // Filter tasks by creator
                taskList.clear();
                taskList.addAll(taskService.getTasksByCreator(creator));
                tableView.refresh();
                System.out.println("🔍 Filtered by creator: " + creator);
            } else {
                // Show all tasks
                loadTasks();
                tableView.refresh();
                System.out.println("🔍 Showing all tasks");
            }
        });
    }

    private void deleteSelectedTask() {
        Task selectedTask = tableView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            // Confirm deletion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Task");
            alert.setHeaderText("Are you sure you want to delete this task?");
            alert.setContentText("Task: " + selectedTask.getTitle());

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    taskService.deleteTask(selectedTask.getId());
                    taskList.remove(selectedTask);
                    tableView.refresh();
                    System.out.println("🗑️ Deleted task: " + selectedTask.getTitle());
                }
            });
        } else {
            showAlert("No Selection", "Please select a task to delete.");
        }
    }

    private void toggleSelectedTask() {
        Task selectedTask = tableView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            toggleTaskStatus(selectedTask);
        } else {
            showAlert("No Selection", "Please select a task to toggle.");
        }
    }

    private void toggleTaskStatus(Task task) {
        Task updatedTask = taskService.toggleTaskStatus(task.getId());
        // Update the task in the list
        int index = taskList.indexOf(task);
        if (index >= 0) {
            taskList.set(index, updatedTask);
        }
        // Refresh the table to update colors
        tableView.refresh();
        System.out.println("✅ Toggled task: " + task.getTitle() + " (done: " + updatedTask.isDone() + ")");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() throws Exception {
        if (context != null) {
            context.close();
            System.out.println("✅ Spring Boot has stopped!");
        }
    }
}