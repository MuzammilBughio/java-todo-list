import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainPage {
    private Pane root;
    private User currentUser;
    private VBox taskContainer;
    private List<TodoTask> archivedTasks;

    public MainPage(User user) {
        this.currentUser = user;
        this.archivedTasks = new ArrayList<>();
        initializeUI();
        loadTasksFromDB(); // Load tasks from database
        loadArchivedTasksFromDB(); // Load archived tasks
    }

    private void initializeUI() {
        root = new Pane();
        root.setStyle("-fx-background-color:#222222;");

        setupTaskContainer();

        ImageView backgroundImage = createBackgroundImage();
        Text title = createTitle();
        Line separator = createSeparator();
        Button addButton = createAddButton();
        Button archiveButton = createArchiveButton();
        VBox mainContent = createMainContent(addButton);

        // Add all components to the root pane
        root.getChildren().addAll(backgroundImage, title, separator, archiveButton, mainContent);
    }

    private void setupTaskContainer() {
        taskContainer = new VBox(10);
        taskContainer.setLayoutX(10);
        taskContainer.setLayoutY(80);
        taskContainer.setPrefWidth(525);
        taskContainer.setStyle("-fx-padding: 0 20 0 20;");
    }

    private ImageView createBackgroundImage() {
        Image image = new Image("/photos/img.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(550);
        imageView.setFitHeight(600);
        imageView.setPreserveRatio(true);
        imageView.setLayoutY(100);
        return imageView;
    }

    private Text createTitle() {
        Text title = new Text("TO DO LIST");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        title.setFill(Color.WHITE);
        title.setX(200);
        title.setY(40);
        return title;
    }

    private Line createSeparator() {
        Line line = new Line(50, 55, 500, 55);
        line.setStrokeWidth(2);
        line.setStroke(Color.WHITE);
        return line;
    }

    private Button createAddButton() {
        Button addButton = new Button("+");
        addButton.setPrefWidth(50);
        addButton.setPrefHeight(35);
        applyAddButtonStyle(addButton);
        setupAddButtonHoverEffects(addButton);

        // Open add task page when clicked
        addButton.setOnAction(e -> openAddTaskPage());
        return addButton;
    }

    private void applyAddButtonStyle(Button button) {
        button.setStyle("""
            -fx-background-color: #294220;
            -fx-text-fill: white;
            -fx-font-size: 20px;
            -fx-font-weight: bold;
            -fx-background-radius: 10;
            -fx-border-color: white;
            -fx-border-width: 1.5;
            -fx-border-radius: 10;
            -fx-cursor: hand;
            -fx-scale-x: 1;
            -fx-scale-y: 1;
            -fx-alignment: center;
        """);
    }

    private void setupAddButtonHoverEffects(Button button) {

        button.setOnMouseEntered(e -> {
            button.setScaleX(1.05);
            button.setScaleY(1.05);
            button.setStyle("""
                -fx-background-color: #467535;
                -fx-text-fill: white;
                -fx-font-size: 21px;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                -fx-border-color: white;
                -fx-border-width: 1.5;
                -fx-border-radius: 10;
                -fx-cursor: hand;
            """);
        });


        button.setOnMouseExited(e -> {
            button.setScaleX(1);
            button.setScaleY(1);
            applyAddButtonStyle(button);
        });
    }

    private Button createArchiveButton() {
        Button archiveButton = new Button("Archived");
        archiveButton.setLayoutX(430);
        archiveButton.setLayoutY(10);
        archiveButton.setPrefWidth(100);
        archiveButton.setPrefHeight(35);

        archiveButton.setStyle("""
            -fx-background-color: #000000;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-background-radius: 10;
            -fx-border-color: white;
            -fx-border-width: 1;
            -fx-border-radius: 10;
            -fx-cursor: hand;
        """);

        // Open archive page when clicked
        archiveButton.setOnAction(e -> {
            ArchiveTaskPage archivePage = new ArchiveTaskPage(this::addTaskBackToMain, currentUser);
            archivePage.show();
        });

        return archiveButton;
    }

    private VBox createMainContent(Button addButton) {
        VBox mainContent = new VBox(15);
        mainContent.setLayoutY(100);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPrefWidth(550);
        VBox.setMargin(mainContent, new Insets(0, 5, 0, 5));
        mainContent.getChildren().addAll(taskContainer, addButton);
        return mainContent;
    }

    private void openAddTaskPage() {
        AddTaskPage addTaskPage = new AddTaskPage(task -> {
            try {
                TaskDAO.addTask(task, currentUser); // Save task to database
                addTask(task); // Add task to UI
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }, currentUser);
        addTaskPage.show();
    }

    // Add a new task to the UI
    private void addTask(TodoTask task) {
        HBox taskRow = createTaskRow(task);
        taskContainer.getChildren().add(taskRow);
    }

    private HBox createTaskRow(TodoTask task) {
        HBox taskRow = new HBox(10);
        taskRow.setAlignment(Pos.CENTER_LEFT);
        taskRow.setPrefHeight(40);
        taskRow.setMinHeight(40);
        taskRow.setMaxHeight(40);
        applyTaskRowStyle(taskRow);
        taskRow.setUserData(task);

        // Create task components
        Label titleLabel = createTitleLabel(task);
        Button detailsButton = createDetailsButton(task);
        Line separator1 = createVerticalSeparator();
        Label deadlineLabel = createDeadlineLabel(task);
        Line separator2 = createVerticalSeparator();
        Button archiveButton = createArchiveButton(task, taskRow);

        // Add all components in a task
        taskRow.getChildren().addAll(
                titleLabel, detailsButton, separator1,
                deadlineLabel, separator2, archiveButton
        );

        return taskRow;
    }

    private void applyTaskRowStyle(HBox taskRow) {
        taskRow.setStyle("""
            -fx-background-color: #333333;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
            -fx-padding: 15 25 15 25;
            -fx-border-color: #555555;
            -fx-border-width: 1;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 6, 0, 0, 2);
        """);
    }

    private Label createTitleLabel(TodoTask task) {
        Label titleLabel = new Label(task.getTitle());
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setPrefWidth(250);
        titleLabel.setEllipsisString("...");
        return titleLabel;
    }

    private Button createDetailsButton(TodoTask task) {
        Button detailsButton = new Button("Details");
        detailsButton.setPrefWidth(70);
        detailsButton.setMinWidth(70);

        detailsButton.setStyle("""
            -fx-background-color: #444444;
            -fx-text-fill: white;
            -fx-font-size: 12px;
            -fx-font-weight: bold;
            -fx-background-radius: 8;
            -fx-border-radius: 8;
            -fx-border-color: #666666;
            -fx-border-width: 1;
            -fx-padding: 5 5 5 5;
            -fx-cursor: hand;
        """);

        // Show task details when clicked
        detailsButton.setOnAction(e -> showTaskDetails(task));
        return detailsButton;
    }

    private Line createVerticalSeparator() {
        Line separator = new Line(0, 0, 0, 25);
        separator.setStroke(Color.WHITE);
        separator.setStrokeWidth(1);
        return separator;
    }

    private Label createDeadlineLabel(TodoTask task) {
        String date = task.getDeadlineDate();
        String time = task.getDeadlineTime();
        String deadlineText = "";


        if ((date != null && !date.isEmpty()) || (time != null && !time.isEmpty())) {
            if (!date.isEmpty() && !time.isEmpty()) {
                deadlineText = date + " " + time;
            } else if (!date.isEmpty()) {
                deadlineText = date;
            } else {
                deadlineText = time;
            }
        }

        Label deadlineLabel = new Label(deadlineText);
        deadlineLabel.setTextFill(Color.LIGHTBLUE);
        deadlineLabel.setPrefWidth(150);
        return deadlineLabel;
    }

    private Button createArchiveButton(TodoTask task, HBox taskRow) {
        Button archiveButton = new Button("X");
        archiveButton.setOnAction(e -> archiveTask(task, taskRow));
        archiveButton.setStyle("""
            -fx-background-color: #752330;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-background-radius: 50;
            -fx-border-radius: 50;
            -fx-border-color: white;
            -fx-border-width: 1;
            -fx-pref-width: 30;
            -fx-pref-height: 30;
        """);
        return archiveButton;
    }

    private void showTaskDetails(TodoTask task) {
        Stage detailsWindow = new Stage();
        detailsWindow.initModality(Modality.APPLICATION_MODAL);
        detailsWindow.setTitle("Task Details");

        VBox layout = new VBox(10);
        layout.setStyle("-fx-background-color: #222222; -fx-padding: 20;");
        layout.setPrefWidth(400);
        layout.setPrefHeight(300);

        // Create detail labels
        Label fullTitle = new Label("Title: " + task.getTitle());
        fullTitle.setTextFill(Color.WHITE);
        Label fullDetails = new Label("Details: " + task.getDetails());
        fullDetails.setTextFill(Color.LIGHTGRAY);
        Label deadline = new Label("Deadline: " + task.getDeadlineDate() + " " + task.getDeadlineTime());
        deadline.setTextFill(Color.LIGHTBLUE);

        Button editBtn = createEditButton(task, detailsWindow);
        Button closeBtn = createCloseButton(detailsWindow);

        HBox buttonBox = new HBox(10, editBtn, closeBtn);
        buttonBox.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(fullTitle, fullDetails, deadline, buttonBox);

        Scene scene = new Scene(layout, 400, 250);
        detailsWindow.setScene(scene);
        detailsWindow.show();
    }

    private Button createEditButton(TodoTask task, Stage detailsWindow) {
        Button editBtn = new Button("Edit");
        editBtn.setStyle("""
            -fx-background-color: #555555;
            -fx-text-fill: white;
            -fx-font-size: 13px;
            -fx-background-radius: 8;
            -fx-border-color: white;
            -fx-border-width: 1;
            -fx-border-radius: 8;
        """);

        editBtn.setOnAction(ev -> {
            detailsWindow.close();
            openEditTaskWindow(task);
        });

        return editBtn;
    }

    private Button createCloseButton(Stage window) {
        Button closeBtn = new Button("Close");
        closeBtn.setStyle("""
            -fx-background-color: #752330;
            -fx-text-fill: white;
            -fx-font-size: 13px;
            -fx-background-radius: 8;
            -fx-border-color: white;
            -fx-border-width: 1;
            -fx-border-radius: 8;
        """);
        closeBtn.setOnAction(ev -> window.close());
        return closeBtn;
    }

    private void openEditTaskWindow(TodoTask task) {
        Stage editWindow = new Stage();
        editWindow.initModality(Modality.APPLICATION_MODAL);
        editWindow.setTitle("Edit Task");

        VBox editLayout = new VBox(10);
        editLayout.setStyle("-fx-background-color: #222222; -fx-padding: 20;");
        editLayout.setPrefWidth(400);
        editLayout.setPrefHeight(300);

        TextField titleField = new TextField(task.getTitle());
        TextField detailsField = new TextField(task.getDetails());
        TextField dateField = new TextField(task.getDeadlineDate());
        TextField timeField = new TextField(task.getDeadlineTime());

        titleField.setPromptText("Edit title...");
        detailsField.setPromptText("Edit details...");
        dateField.setPromptText("Edit date (YYYY-MM-DD)");
        timeField.setPromptText("Edit time (HH:mm)");

        for (TextField field : new TextField[]{titleField, detailsField, dateField, timeField}) {
            field.setStyle("""
                -fx-background-color: #000000;
                -fx-text-fill: white;
                -fx-prompt-text-fill: #888888;
                -fx-border-color: white;
                -fx-border-radius: 8;
                -fx-background-radius: 8;
                -fx-border-width: 1;
                -fx-font-size: 14px;
                -fx-padding: 6 10 6 10;
            """);
        }

        Button saveBtn = createSaveButton(task, titleField, detailsField, dateField, timeField, editWindow);
        Button cancelBtn = createCancelButton(editWindow);

        HBox editButtons = new HBox(10, saveBtn, cancelBtn);
        editButtons.setAlignment(Pos.CENTER);

        editLayout.getChildren().addAll(
                new Label("Edit Task:"),
                titleField,
                detailsField,
                dateField,
                timeField,
                editButtons
        );

        Scene editScene = new Scene(editLayout, 400, 300);
        editWindow.setScene(editScene);
        editWindow.show();
    }

    private Button createSaveButton(TodoTask task, TextField titleField, TextField detailsField,
                                    TextField dateField, TextField timeField, Stage editWindow) {
        Button saveBtn = new Button("Save");
        saveBtn.setStyle("""
            -fx-background-color: #294220;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-background-radius: 8;
            -fx-border-color: white;
            -fx-border-width: 1;
            -fx-border-radius: 8;
        """);

        saveBtn.setOnAction(ev -> saveTaskChanges(task, titleField, detailsField, dateField, timeField, editWindow));
        return saveBtn;
    }

    private Button createCancelButton(Stage editWindow) {
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("""
            -fx-background-color: #752330;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-background-radius: 8;
            -fx-border-color: white;
            -fx-border-width: 1;
            -fx-border-radius: 8;
        """);
        cancelBtn.setOnAction(ev -> editWindow.close());
        return cancelBtn;
    }

    private void saveTaskChanges(TodoTask task, TextField titleField, TextField detailsField,
                                 TextField dateField, TextField timeField, Stage editWindow) {
        if (titleField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Title", "Task title cannot be empty!");
            return;
        }

        TodoTask oldTask = new TodoTask(
                task.getTitle(),
                task.getDetails(),
                task.getDeadlineDate(),
                task.getDeadlineTime()
        );

        // Create updated task
        TodoTask updatedTask = new TodoTask(
                titleField.getText().trim(),
                detailsField.getText().trim(),
                dateField.getText().trim(),
                timeField.getText().trim()
        );

        try {
            // Update in database
            TaskDAO.updateTask(oldTask, updatedTask);

            task.setTitle(updatedTask.getTitle());
            task.setDetails(updatedTask.getDetails());
            task.setDeadlineDate(updatedTask.getDeadlineDate());
            task.setDeadlineTime(updatedTask.getDeadlineTime());

            showAlert(Alert.AlertType.INFORMATION, "Task Updated", "Task updated successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update task in database!");
        }

        editWindow.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // Load tasks from database for current user
    private void loadTasksFromDB() {
        try {
            for (TodoTask task : TaskDAO.getTasksByUser(currentUser)) {
                addTask(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load archived tasks from database
    private void loadArchivedTasksFromDB() {
        try {
            archivedTasks.addAll(ArchiveTaskDAO.getArchivedTasksByUser(currentUser));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Archive a task
    private void archiveTask(TodoTask task, HBox taskRow) {
        try {
            ArchiveTaskDAO.addArchivedTask(task, currentUser); // Add to archive DB
            taskContainer.getChildren().remove(taskRow); // Remove from UI
            TaskDAO.deleteTask(task, currentUser); // Remove from active tasks DB
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Archive Error", "Failed to archive task!");
        }
    }

    // Called from ArchiveTaskPage to restore a task
    private void addTaskBackToMain(TodoTask task) {
        addTask(task); // Re-add task to main UI
    }

    // Get the root pane for Main.java
    public Pane getRoot() {
        return root;
    }
}