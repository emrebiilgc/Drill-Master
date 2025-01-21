/**
 * The {@code GameLauncher} class is responsible for setting up the primary game environment in the JavaFX application.
 * It initializes the game's main window (stage) and sets up the UI elements such as labels for displaying fuel, storage,
 * and money. It also initializes and manages the game scene, including setting up event listeners for keyboard inputs
 * that control the drill movements.
 *
 * <p>This class collaborates with the {@link GameBoard}, {@link Drill}, and other game components to assemble
 * the user interface and handle the game logic. The {@code launch} method is called to start the game and set up
 * all necessary configurations for the game to run.</p>
 *
 * @see Drill
 * @see GameBoard
 */

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.util.ArrayList;

public class GameLauncher {
    private Stage stage;

    public GameLauncher(Stage stage) {
        this.stage = stage;
    }

    /**
     * Launches the game by setting up the main user interface and initializing the game scene.
     * This method configures the initial display with labels for fuel, storage, and money,
     * sets up the game board and drill, and attaches all necessary UI elements to the root
     * {@link AnchorPane}. It also establishes keyboard event handlers to control the drill's
     * movements based on user input.
     *
     * The scene is then added to the primary stage and displayed. This setup ensures that the
     * game environment is ready for interaction and gameplay begins immediately upon execution.
     * Additionally, this method sets the game window to be non-resizable to maintain a consistent
     * layout and aspect ratio.
     *
     * @param // Parameters are not explicitly required as this method accesses instance variables.
     */
    public void launch() {
        AnchorPane root = new AnchorPane();
        Label fuelLabel = new Label("Fuel: 50");
        Label storageLabel = new Label("Storage: 0");
        Label moneyLabel = new Label("Money: 0");

        AnchorPane.setTopAnchor(fuelLabel, 10.0);
        AnchorPane.setLeftAnchor(fuelLabel, 10.0);
        AnchorPane.setTopAnchor(storageLabel, 30.0);
        AnchorPane.setLeftAnchor(storageLabel, 10.0);
        AnchorPane.setTopAnchor(moneyLabel, 50.0);
        AnchorPane.setLeftAnchor(moneyLabel, 10.0);

        ArrayList<Label> labels = new ArrayList<>();
        labels.add(fuelLabel);
        labels.add(storageLabel);
        labels.add(moneyLabel);

        GameBoard gameBoard = new GameBoard();
        Drill drill = new Drill(0, 50, gameBoard, labels, root);

        root.getChildren().addAll(gameBoard.getGrid(), drill.getImageView(), fuelLabel, storageLabel, moneyLabel);

        Scene scene = new Scene(root, 750, 750);
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT:
                    drill.moveLeft();
                    break;
                case RIGHT:
                    drill.moveRight();
                    break;
                case UP:
                    drill.moveUp();
                    break;
                case DOWN:
                    drill.moveDown();
                    break;
            }
        });

        stage.setTitle("HU-Load");
        stage.setScene(scene);
        stage.show();
        scene.getRoot().requestFocus();
        stage.setResizable(false);
    }
}