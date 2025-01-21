/**
 * Initializes a new ResourceManager with specified parameters to manage game resources such as fuel and storage.
 * This constructor sets up the initial state of the game's resources, including fuel levels, storage capacity,
 * and money, and attaches labels to display these values dynamically during gameplay.
 *
 * It also initializes and starts a fuel consumption timeline that decrements the fuel resource periodically,
 * simulating ongoing resource usage as the game progresses. The ResourceManager is responsible for updating
 * the display labels based on resource changes, ensuring that the player is always informed of their current
 * resource statuses.
 *
 * @param fuel the initial amount of fuel available for the drill
 * @param storageCapacity the maximum storage capacity for resources collected by the drill
 * @param gameBoard the game board on which the game operates, used to check for resources at specific locations
 * @param labels an ArrayList of {@link Label} that display fuel, storage, and money values on the UI
 * @param root the root {@link AnchorPane} of the game's UI, where updates to resource displays will be managed
 */

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import java.util.ArrayList;

public class ResourceManager {
    private static double fuel;
    private static double money;
    private static int storageCapacity;
    private static int currentStorage;
    private static Label fuelLabel, storageLabel, moneyLabel;
    private static GameBoard gameBoard;
    private static Timeline fuelConsumptionTimeline;
    private static AnchorPane root;

    public ResourceManager(double fuel, int storageCapacity, GameBoard gameBoard, ArrayList<Label> al, AnchorPane root) {
        this.fuel = fuel;
        this.storageCapacity = storageCapacity;
        this.gameBoard = gameBoard;
        this.fuelLabel = al.get(0);
        this.storageLabel = al.get(1);
        this.moneyLabel = al.get(2);
        this.root = root;
        initFuelConsumption();
    }

    private void initFuelConsumption() {
        fuelConsumptionTimeline = new Timeline(new KeyFrame(Duration.millis(500), e -> consumeFuel()));
        fuelConsumptionTimeline.setCycleCount(Timeline.INDEFINITE);
        fuelConsumptionTimeline.play();
    }

    private void consumeFuel() {
        if (fuel > 0) {
            fuel -= 1;
            updateLabels();
        } else {
            GameOver.endGame("fuel", money, root);
        }
    }

    public static void stopFuelConsumption() {
        fuelConsumptionTimeline.stop();
    }

    public void updateLabels() {
        if (fuelLabel != null) {
            fuelLabel.setText("Fuel: " + String.format("%.2f", fuel));
        }
        storageLabel.setText("Storage: " + currentStorage + "/" + storageCapacity);
        moneyLabel.setText("Money: $" + String.format("%.2f", money));
    }

    public void consumeResources() {
        fuel -= 1;
        updateLabels();
        if (fuel < 0) {
            fuel = 0;
        }
    }

    public void updateStorageAndMoney(double x, double y) {
        String mineral = gameBoard.getCellContent((int) (y / 50), (int) (x / 50));
        if (mineral.equals("valuable1")) {
            money += 250;
            currentStorage += 20;
        } else if (mineral.equals("valuable2")) {
            money += 20000;
            currentStorage += 80;
        } else if (mineral.equals("valuable3")) {
            money += 50000;
            currentStorage += 60;
        }
        updateLabels();
        if (currentStorage >= storageCapacity) {
            GameOver.endGame("storage", money, root);
        }
    }
}
