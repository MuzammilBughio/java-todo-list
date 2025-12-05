import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.function.Consumer;

public class AddTaskPage {
    private Consumer<TodoTask> onSave; // callback to send task back
    private User currentUser; // logged-in user

    public AddTaskPage(Consumer<TodoTask> onSave, User currentUser) {
        this.onSave = onSave;
        this.currentUser = currentUser;
    }

    public void show() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add New Task");
        window.setResizable(false);

        Label titleLabel = new Label("Task Title:");
        titleLabel.setTextFill(Color.WHITE);

        TextField taskInput = new TextField();
        taskInput.setPromptText("Enter your task...");
        taskInput.setPrefWidth(350);
        taskInput.setStyle("""
            -fx-background-color: #000000;
            -fx-text-fill: #FFFFFF;
            -fx-prompt-text-fill: #999999;
            -fx-font-size: 14px;
            -fx-background-radius: 8;
            -fx-border-color: white;
            -fx-border-radius: 8;
            -fx-border-width: 1;
            -fx-padding: 6 10 6 10;
        """);

        TextArea taskDetail = new TextArea();
        taskDetail.setPromptText("Task detail...");
        taskDetail.setPrefWidth(350);
        taskDetail.setPrefHeight(200);
        taskDetail.setWrapText(true);
        taskDetail.setStyle("""
            -fx-control-inner-background: #000000;
            -fx-background-color: #000000;
            -fx-text-fill: white;
            -fx-prompt-text-fill: #888888;
            -fx-background-radius: 8;
            -fx-border-color: white;
            -fx-border-radius: 8;
            -fx-border-width: 1;
            -fx-font-size: 14px;
            -fx-padding: 6 10 6 10;
            -fx-control-inner-background-alt: transparent;
            -fx-focus-color: transparent;
            -fx-faint-focus-color: transparent;
        """);

        Label deadlineLabel = new Label("Deadline:");
        deadlineLabel.setTextFill(Color.WHITE);

        DatePicker deadlineDatePicker = new DatePicker();
        deadlineDatePicker.setPrefWidth(180);
        deadlineDatePicker.setStyle("""
            -fx-background-color: #000000;
            -fx-control-inner-background: #000000;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-background-radius: 8;
            -fx-border-color: white;
            -fx-border-radius: 8;
            -fx-border-width: 1;
            -fx-prompt-text-fill: #AAAAAA;
        """);

        TextField deadlineTimeField = new TextField();
        deadlineTimeField.setPromptText("HH:mm");
        deadlineTimeField.setPrefWidth(100);
        deadlineTimeField.setStyle("""
            -fx-background-color: #000000;
            -fx-control-inner-background: #000000;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-background-radius: 8;
            -fx-border-color: white;
            -fx-border-radius: 8;
            -fx-border-width: 1;
            -fx-prompt-text-fill: #AAAAAA;
        """);

        HBox deadlineBox = new HBox(10, deadlineDatePicker, deadlineTimeField);

        Button saveButton = new Button("Save");
        saveButton.setPrefWidth(100);
        saveButton.setPrefHeight(35);
        saveButton.setStyle("""
            -fx-background-color: #A3001E;
            -fx-text-fill: white;
            -fx-font-size: 13px;
            -fx-background-radius: 10;
            -fx-border-color: white;
            -fx-border-width: 1;
            -fx-border-radius: 10;
        """);

        saveButton.setOnAction(e -> {
            String title = taskInput.getText().trim();
            String details = taskDetail.getText().trim();
            String date = (deadlineDatePicker.getValue() != null) ? deadlineDatePicker.getValue().toString() : "";
            String time = deadlineTimeField.getText().trim();

            if (title.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Missing Title");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a task title before saving.");
                alert.showAndWait();
                return;
            }

            TodoTask newTask = new TodoTask(title, details, date, time);

            try {
                onSave.accept(newTask);
                window.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to save task to database!");
                alert.showAndWait();
            }
        });

        VBox layout = new VBox(15, titleLabel, taskInput, taskDetail, deadlineLabel, deadlineBox, saveButton);
        layout.setStyle("-fx-background-color: #222222; -fx-padding: 20;");
        layout.setPrefSize(600, 500);

        Scene scene = new Scene(layout, 450, 350);
        scene.setFill(Color.TRANSPARENT);
        window.setScene(scene);
        window.showAndWait();
    }
}
