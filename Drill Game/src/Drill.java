/**
 * The {@code Drill} class represents the drilling mechanism in the game. It handles the movements,
 * animations, and interactions of the drill within the game environment. This class is crucial for
 * managing the drill's position on the {@link GameBoard}, responding to player inputs, and executing
 * the drilling actions which affect the game's resources and the player's progress.
 *
 * <p>The drill can move in four directions: left, right, up, and down, with animations specific to each
 * direction. It also checks for the feasibility of these movements based on game rules, such as obstacles
 * and boundaries. The class uses images to represent the drill and its animations, and manages the
 * game's physics such as gravity and resource consumption.</p>
 *
 * <p>This class also interacts with the {@link ResourceManager} to update game resources based on the drill's
 * actions and the materials it encounters. This includes managing fuel consumption, storage capacity, and
 * monetary gains from collected resources.</p>
 *
 * @see GameBoard
 * @see ResourceManager
 */

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.scene.control.Label;
import java.util.ArrayList;


public class Drill {
    private ImageView imageView;
    private double x, y;
    private GameBoard gameBoard;
    private final int cellSize = 50;
    private Timeline animationTimeline;
    private Image[] leftImages;
    private Image[] rightImages;
    private Image[] upImages;
    private Image[] downImages;
    private int currentFrameIndex = 0;
    private int size;
    private double fuel = 100;
    private double money = 0;
    private int storageCapacity = 300;
    private int currentStorage = 0;
    private AnchorPane root;
    private Timeline gravityTimeline;
    private boolean isMovingUp = false;
    private Timeline moveUpDelayTimeline;
    private ResourceManager resourceManager;

    /**
     * Constructs a new Drill object positioned at the specified starting coordinates on the {@link GameBoard}.
     * This constructor initializes the drill with images for different directions, sets up key resources like
     * fuel and storage, and prepares animation timelines for movement and gravity effects.
     *
     * The drill's initial resources and parameters such as fuel capacity and storage are also set based on
     * inputs provided during instantiation. It also registers itself with the game's resource manager for
     * managing its fuel consumption and updates to storage and monetary values based on the game's ongoing interactions.
     *
     * @param startX the initial horizontal coordinate of the drill on the game board
     * @param startY the initial vertical coordinate of the drill on the game board
     * @param gameBoard the game board on which the drill operates, used for checking valid moves and updating cell contents
     * @param al an ArrayList of {@link Label} objects for displaying game statistics (fuel, money, storage)
     * @param root the root {@link AnchorPane} of the game's scene, used for UI updates and interactions
     */
    public Drill(double startX, double startY, GameBoard gameBoard, ArrayList<Label> al, AnchorPane root){
        this.x = startX;
        this.y = startY;
        this.gameBoard = gameBoard;
        size = gameBoard.getSize();
        Image image = new Image("/assets/drill/drill_38.png");
        imageView = new ImageView(image);
        imageView.setX(x);
        imageView.setY(y);
        resourceManager = new ResourceManager(100, 300, gameBoard, al, root);
        this.root = root;
        initGravityEffect();
        initMoveUpDelayTimeline();
        leftImages = loadImages(1, 8, "/assets/drill/drill_%02d.png");
        rightImages = loadImages(55, 60, "/assets/drill/drill_%02d.png");
        upImages = new Image[]{ new Image("/assets/drill/drill_26.png") };
        downImages = loadImages(38, 44, "/assets/drill/drill_%02d.png");
        initAnimation();
    }
    private Image[] loadImages(int start, int end, String pathFormat) {
        Image[] images = new Image[end - start + 1];
        for (int i = start; i <= end; i++) {
            images[i - start] = new Image(String.format(pathFormat, i));
        }
        return images;
    }
    private void initAnimation() {
        animationTimeline = new Timeline();
        animationTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void startAnimation(Image[] frames) {
        animationTimeline.getKeyFrames().clear();
        animationTimeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(100), e -> updateAnimationFrame(frames))
        );
        currentFrameIndex = 0;
        animationTimeline.playFromStart();
    }

    private void updateAnimationFrame(Image[] frames) {
        imageView.setImage(frames[currentFrameIndex]);
        currentFrameIndex = (currentFrameIndex + 1) % frames.length;
    }

    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Moves the drill one cell if the movement is valid. This method checks for obstacles,
     * fuel availability, and storage capacity before moving. If the move is valid, updates the drill's
     * position to the left, right, up or down, consumes resources, updates storage and money based on minerals collected,
     * and initiates the corresponding animation.
     *
     * @throws IllegalStateException if the drill has no fuel left or storage is full, preventing movement
     */

    public void moveLeft() {
        if (canMove(x - cellSize, y)) {
            x -= cellSize;
            imageView.setX(x);
            resourceManager.consumeResources();
            resourceManager.updateStorageAndMoney(x, y);
            digging(x, y);
            startAnimation(leftImages);
        }
    }

    public void moveRight() {
        if (canMove(x + cellSize, y)) {
            x += cellSize;
            imageView.setX(x);
            resourceManager.consumeResources();
            resourceManager.updateStorageAndMoney(x, y);
            digging(x, y);
            startAnimation(rightImages);
        }
    }


    public void moveUp() {
        if (y>0){
        String cell = gameBoard.getCellContent((int) y / cellSize-1, (int) x / cellSize);
        if (!cell.equals("soil") && !cell.equals("valuable1") && !cell.equals("valuable2") && !cell.equals("valuable3")) {
            if (canMove(x, y - cellSize)) {
                isMovingUp = true;
                y -= cellSize;
                imageView.setY(y);
                resourceManager.consumeResources();
                resourceManager.updateStorageAndMoney(x, y);
                digging(x, y);
                startAnimation(upImages);
                moveUpDelayTimeline.playFromStart();
            }
        }
        }
    }

    public void moveDown() {
        if (canMove(x, y + cellSize)) {
            y += cellSize;
            imageView.setY(y);
            resourceManager.consumeResources();
            resourceManager.updateStorageAndMoney(x, y);
            digging(x, y);
            startAnimation(downImages);
        }
    }

    private boolean canMove(double newX, double newY) {
        if (fuel <= 0 || currentStorage >= storageCapacity) {
            return false;
        }
        return gameBoard.isValidMove(newX, newY);
    }

    private void digging(double x, double y) {
        int col = (int) (x / cellSize);
        int row = (int) (y / cellSize);

        ImageView currentCellView = gameBoard.getCellImageView(row, col);
        currentCellView.setImage(null);
        gameBoard.removeCellContent(row, col);
    }

    /**
     * Applies gravity to the drill, causing it to move downward automatically if the space below is unoccupied.
     * This method is part of the drill's automatic movement logic, where it simulates the effect of gravity by
     * checking the cell directly beneath the drill's current position on the {@link GameBoard}.
     *
     * If the cell below is empty (i.e., contains no obstacles, lava, or other non-navigable elements), the drill
     * will move down one cell. This gravity effect helps in simulating a more realistic mining environment where
     * unsupported objects fall downwards until they hit an obstacle.
     *
     * <p>This method is typically called periodically as part of a {@link Timeline} to ensure continuous application
     * of gravity whenever the drill is not actively being moved upward by the player.</p>
     *
     * @throws IllegalStateException if the movement is blocked by an obstacle or the edge of the board, though this
     * should typically be handled before the method is called.
     */
    private void applyGravity() {
        if (!isMovingUp) {
            int newRow = (int) (y / cellSize) + 1;
            int col = (int) (x / cellSize);
            String cellBelow = gameBoard.getCellContent(newRow, col);

            if (newRow < gameBoard.getSize() && cellBelow.equals("empty")) {
                moveDown();
            }
        }
    }
    private void initMoveUpDelayTimeline() {
        moveUpDelayTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            isMovingUp = false;
        }));
        moveUpDelayTimeline.setCycleCount(1);
    }
    private void initGravityEffect() {
        gravityTimeline = new Timeline(new KeyFrame(Duration.millis(200), e -> applyGravity()));
        gravityTimeline.setCycleCount(Timeline.INDEFINITE);
        gravityTimeline.play();
    }
}
