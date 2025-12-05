import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private User loggedInUser;

    @Override
    public void start(Stage primaryStage) {
        // Show login page first
        LoginPage loginPage = new LoginPage(user -> {
            this.loggedInUser = user; // store logged in user
            openMainPage(primaryStage);
        });

        loginPage.show();
    }

    private void openMainPage(Stage primaryStage) {
        // Pass the logged-in user to MainPage
        MainPage mainPage = new MainPage(loggedInUser);
        Scene scene = new Scene(mainPage.getRoot(), 550, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ToDo App - " + loggedInUser.getUsername());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
