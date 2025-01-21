/**
 * The {@code Main} class serves as the entry point for the JavaFX application.
 * It initializes and launches the game by setting up the primary stage and scene.
 * This class is responsible for starting the JavaFX lifecycle by invoking the {@link GameLauncher} class
 * which configures and displays the initial game environment.
 */

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        GameLauncher gameLauncher = new GameLauncher(primaryStage);
        gameLauncher.launch();
    }

    public static void main(String[] args) {
        launch(args);
    }
}