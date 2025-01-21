/**
 * Ends the game and displays a game over message based on the specified reason.
 * This method clears the game scene and updates the background color and text of the
 * game over screen depending on why the game ended, such as running out of fuel, storage
 * capacity being exceeded, or falling into lava.
 *
 * The method uses {@link Platform#runLater} to ensure that UI updates are made on the JavaFX
 * application thread, preserving thread safety. It sets the final scene to display the game
 * over message and stops any ongoing resource consumption like fuel, making sure that the game
 * state is correctly finalized.
 *
 * @param reason a String indicating the cause of the game's end, which affects the message and styling of the game over screen
 * @param money the amount of money collected at the time of game over, displayed in the game over message
 * @param root the {@link AnchorPane} that contains the game's UI elements, which will be cleared and used to display the game over message
 */

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class GameOver {

    public static void endGame(String reason, double money, AnchorPane root) {
        Platform.runLater(() -> {
            root.getChildren().clear();
            switch (reason) {
                case "fuel":
                    root.setStyle("-fx-background-color: #0b500b;");
                    displayGameOverMessage("          GAME OVER \nCollected Money: " + String.format("%.2f", money),root);
                    break;
                case "storage":
                    root.setStyle("-fx-background-color: #0b500b;");
                    displayGameOverMessage("          GAME OVER \nCollected Money: " + String.format("%.2f", money),root);
                    break;
                case "lava":
                    root.setStyle("-fx-background-color: #7e0909;");
                    displayGameOverMessage("GAME OVER ",root);
                    break;
                default:
                    root.setStyle("-fx-background-color: #333333;");
                    displayGameOverMessage("GAME OVER - Unknown Reason",root);
                    break;
            }
            ResourceManager.stopFuelConsumption();
        });
    }

    private static void displayGameOverMessage(String message, AnchorPane root) {
        Label gameOverLabel = new Label(message);
        gameOverLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: #ffffff;");
        gameOverLabel.setAlignment(Pos.CENTER);
        gameOverLabel.setPrefWidth(root.getWidth());
        gameOverLabel.setLayoutY((root.getHeight() - gameOverLabel.getHeight()) / 2 - 40);
        root.getChildren().add(gameOverLabel);
    }
}
