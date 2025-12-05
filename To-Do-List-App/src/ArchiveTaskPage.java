import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ArchiveTaskPage {

    private List<TodoTask> archivedTasks;
    private final Consumer<TodoTask> restoreToMainCallback;
    private final User currentUser;

    public ArchiveTaskPage(Consumer<TodoTask> restoreToMainCallback, User currentUser) {
        this.restoreToMainCallback = restoreToMainCallback;
        this.currentUser = currentUser;

        try {
            archivedTasks = ArchiveTaskDAO.getAllArchivedTasks();
        } catch (SQLException e) {
            e.printStackTrace();
            archivedTasks = new ArrayList<>();
        }
    }

    public void show() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Archived Tasks");

        VBox container = new VBox(10);
        container.setPadding(new Insets(15));
        container.setStyle("-fx-background-color: #222222;");

        Label title = new Label("Archived Tasks");
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        container.getChildren().add(title);

        for (TodoTask task : archivedTasks) {
            HBox taskRow = createTaskRow(task, container);
            container.getChildren().add(taskRow);
        }

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #222222; -fx-border-color: transparent;");

        Scene scene = new Scene(scrollPane, 400, 500);
        window.setScene(scene);
        window.showAndWait();
    }

    private HBox createTaskRow(TodoTask task, VBox container) {
        HBox taskRow = new HBox(10);
        taskRow.setAlignment(Pos.CENTER_LEFT);
        taskRow.setPrefHeight(40);
        taskRow.setStyle("""
            -fx-background-color: #333333;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
            -fx-padding: 10 20 10 20;
            -fx-border-color: #555555;
            -fx-border-width: 1;
        """);

        Label titleLabel = new Label(task.getTitle());
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setPrefWidth(250);

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
            -fx-padding: 5 10 5 10;
            -fx-cursor: hand;
        """);
        detailsButton.setOnAction(e -> showDetailsWindow(task, taskRow, container));

        Line sep = new Line(0, 0, 0, 25);
        sep.setStroke(Color.WHITE);
        sep.setStrokeWidth(1);

        Button removeButton = new Button("X");
        removeButton.setStyle("""
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
        removeButton.setOnAction(e -> {
            try {
                ArchiveTaskDAO.deleteArchivedTask(task.getTitle());
                archivedTasks.remove(task);
                container.getChildren().remove(taskRow);
            } catch (SQLException ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to delete task from database!").showAndWait();
            }
        });

        taskRow.getChildren().addAll(titleLabel, detailsButton, sep, removeButton);
        return taskRow;
    }

    private void showDetailsWindow(TodoTask task, HBox taskRow, VBox container) {
        Stage detailsWindow = new Stage();
        detailsWindow.initModality(Modality.APPLICATION_MODAL);
        detailsWindow.setTitle("Task Details");

        VBox layout = new VBox(10);
        layout.setStyle("-fx-background-color: #222222; -fx-padding: 20;");
        layout.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("Title: " + task.getTitle());
        titleLabel.setTextFill(Color.WHITE);

        Label detailLabel = new Label("Details: " + task.getDetails());
        detailLabel.setTextFill(Color.LIGHTGRAY);
        detailLabel.setWrapText(true);

        Label deadlineLabel = new Label("Deadline: " + task.getDeadlineDate() + " " + task.getDeadlineTime());
        deadlineLabel.setTextFill(Color.LIGHTBLUE);

        Button editButton = new Button("Edit");
        editButton.setStyle("""
            -fx-background-color: #444444;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-background-radius: 8;
            -fx-border-radius: 8;
            -fx-border-color: #666666;
            -fx-border-width: 1;
        """);
        editButton.setOnAction(e -> openEditView(detailsWindow, task, taskRow, container));

        Button closeButton = new Button("Close");
        closeButton.setStyle("""
            -fx-background-color: #752330;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-background-radius: 8;
            -fx-border-radius: 8;
        """);
        closeButton.setOnAction(e -> detailsWindow.close());

        layout.getChildren().addAll(
                titleLabel,
                detailLabel,
                deadlineLabel,
                new HBox(10, editButton, closeButton)
        );

        Scene scene = new Scene(layout, 400, 300);
        detailsWindow.setScene(scene);
        detailsWindow.showAndWait();
    }

    private void openEditView(Stage parentStage, TodoTask task, HBox taskRow, VBox container) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-background-color: #222222; -fx-padding: 20;");
        layout.setAlignment(Pos.TOP_LEFT);

        TextField titleField = new TextField(task.getTitle());
        TextArea detailField = new TextArea(task.getDetails());
        TextField dateField = new TextField(task.getDeadlineDate());
        TextField timeField = new TextField(task.getDeadlineTime());

        titleField.setPromptText("Title");
        detailField.setPromptText("Details");
        dateField.setPromptText("Deadline Date");
        timeField.setPromptText("Deadline Time");

        String darkFieldStyle = """
            -fx-background-color: #000000;
            -fx-control-inner-background: #000000;
            -fx-text-fill: white;
            -fx-prompt-text-fill: #888888;
            -fx-border-color: white;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-border-width: 1;
            -fx-font-size: 14px;
            -fx-padding: 6 10 6 10;
        """;

        titleField.setStyle(darkFieldStyle);
        dateField.setStyle(darkFieldStyle);
        timeField.setStyle(darkFieldStyle);

        detailField.setStyle("""
            -fx-background-color: black, black, black, black;
            -fx-control-inner-background: black;
            -fx-control-inner-background-alt: black;
            -fx-text-fill: white;
            -fx-prompt-text-fill: #888888;
            -fx-border-color: white;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-border-width: 1;
            -fx-font-size: 14px;
            -fx-padding: 6 10 6 10;
            -fx-focus-color: transparent;
            -fx-faint-focus-color: transparent;
        """);
        detailField.setWrapText(true);

        Button addBackBtn = new Button("Add Back to Main");
        addBackBtn.setStyle("""
            -fx-background-color: #2d6a4f;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-background-radius: 8;
            -fx-border-radius: 8;
        """);
        addBackBtn.setOnAction(e -> {
            TodoTask updatedTask = new TodoTask(
                    titleField.getText(),
                    detailField.getText(),
                    dateField.getText(),
                    timeField.getText()
            );

            try {
                TaskDAO.addTask(updatedTask, currentUser);
                ArchiveTaskDAO.deleteArchivedTask(task.getTitle());
                archivedTasks.remove(task);
                container.getChildren().remove(taskRow);
                restoreToMainCallback.accept(updatedTask);
                new Alert(Alert.AlertType.INFORMATION, "Task moved back to main list!").showAndWait();
                parentStage.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to move task back to main list.").showAndWait();
            }
        });

        Button discardBtn = new Button("Discard");
        discardBtn.setStyle("""
            -fx-background-color: #752330;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-background-radius: 8;
            -fx-border-radius: 8;
        """);
        discardBtn.setOnAction(e -> parentStage.close());

        HBox buttons = new HBox(10, addBackBtn, discardBtn);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        layout.getChildren().addAll(
                new Label("Edit Task:"),
                titleField,
                detailField,
                dateField,
                timeField,
                buttons
        );

        parentStage.setScene(new Scene(layout, 400, 350));
    }
}
