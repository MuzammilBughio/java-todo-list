import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.util.function.Consumer;

public class LoginPage {
    private final Consumer<User> onLoginSuccess;
    public LoginPage(Consumer<User> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Welcome");

        Label title = new Label("Welcome to ToDo App!");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");

        Button loginOption = styledButton("Login", "#A3001E");
        Button createOption = styledButton("Create New Account", "#444444");

        loginOption.setOnAction(e -> showLoginForm(stage));
        createOption.setOnAction(e -> showCreateAccountForm(stage));

        VBox mainLayout = new VBox(20, title, loginOption, createOption);
        mainLayout.setStyle("-fx-padding: 40; -fx-alignment: center; -fx-background-color: #1c1c1c;");

        Scene scene = new Scene(mainLayout, 350, 350);
        stage.setScene(scene);
        stage.show();
    }

    private void showLoginForm(Stage stage) {
        Label userLabel = new Label("Username:");
        Label passLabel = new Label("Password:");
        userLabel.setTextFill(Color.WHITE);
        passLabel.setTextFill(Color.WHITE);

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        usernameField.setPromptText("Enter username");
        passwordField.setPromptText("Enter password");
        styleTextField(usernameField);
        styleTextField(passwordField);

        Button loginBtn = styledButton("Login", "#A3001E");
        Button backBtn = styledButton("Back", "#444444");

        loginBtn.setOnAction(e -> {
            try {
                User user = UserDAO.validateLogin(usernameField.getText(), passwordField.getText());
                if (user != null) {
                    stage.close();
                    onLoginSuccess.accept(user);
                } else {
                    new Alert(Alert.AlertType.ERROR, "Invalid username or password!").showAndWait();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        backBtn.setOnAction(e -> show());

        VBox layout = new VBox(15, userLabel, usernameField, passLabel, passwordField, loginBtn, backBtn);
        layout.setStyle("-fx-padding: 25; -fx-alignment: center; -fx-background-color: #1c1c1c;");

        stage.setScene(new Scene(layout, 400, 350));
    }

    private void showCreateAccountForm(Stage stage) {
        Label userLabel = new Label("Choose a Username:");
        Label passLabel = new Label("Choose a Password:");
        userLabel.setTextFill(Color.WHITE);
        passLabel.setTextFill(Color.WHITE);

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        usernameField.setPromptText("New username");
        passwordField.setPromptText("New password");
        styleTextField(usernameField);
        styleTextField(passwordField);

        Button createBtn = styledButton("Create Account", "#A3001E");
        Button backBtn = styledButton("Back", "#444444");

        createBtn.setOnAction(e -> {
            try {
                if (UserDAO.createUser(usernameField.getText(), passwordField.getText())) {
                    new Alert(Alert.AlertType.INFORMATION, "Account created! You can log in now.").showAndWait();
                    showLoginForm(stage);
                } else {
                    new Alert(Alert.AlertType.ERROR, "Username already exists!").showAndWait();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        backBtn.setOnAction(e -> show());

        VBox layout = new VBox(15, userLabel, usernameField, passLabel, passwordField, createBtn, backBtn);
        layout.setStyle("-fx-padding: 25; -fx-alignment: center; -fx-background-color: #1c1c1c;");

        stage.setScene(new Scene(layout, 400, 350));
    }

    // --- 🔧 Helper for text fields ---
    private void styleTextField(TextField field) {
        field.setStyle("""
        -fx-background-color: #000000;
        -fx-text-fill: white;
        -fx-prompt-text-fill: #888888;
        -fx-background-radius: 8;
        -fx-border-color: white;
        -fx-border-radius: 8;
        -fx-border-width: 1;
        -fx-font-size: 14px;
        -fx-padding: 6 10 6 10;
    """);
    }

    // --- 🔧 Helper for buttons ---
    private Button styledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle(String.format("""
        -fx-background-color: %s;
        -fx-text-fill: white;
        -fx-font-size: 14px;
        -fx-background-radius: 8;
        -fx-border-color: white;
        -fx-border-radius: 8;
        -fx-border-width: 1;
        -fx-padding: 8 16 8 16;
    """, color));

        btn.setOnMouseEntered(e -> btn.setStyle(String.format("""
        -fx-background-color: derive(%s, 20%%);
        -fx-text-fill: white;
        -fx-font-size: 14px;
        -fx-background-radius: 8;
        -fx-border-color: #cccccc;
        -fx-border-radius: 8;
        -fx-border-width: 1;
        -fx-padding: 8 16 8 16;
    """, color)));

        btn.setOnMouseExited(e -> btn.setStyle(String.format("""
        -fx-background-color: %s;
        -fx-text-fill: white;
        -fx-font-size: 14px;
        -fx-background-radius: 8;
        -fx-border-color: white;
        -fx-border-radius: 8;
        -fx-border-width: 1;
        -fx-padding: 8 16 8 16;
    """, color)));

        return btn;
    }}
