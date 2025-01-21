/**
 * Initializes a new GameBoard which sets up the grid structure and populates it with various cell contents.
 * This constructor creates a grid of predefined size, filled with cells that represent different elements
 * such as soil, obstacles, valuable minerals, and lava. The distribution of these elements is randomly
 * determined, except for the fixed positions of certain elements like the top layer and boundaries.
 *
 * Each cell is visually represented with a {@link ImageView} that may change based on the game interactions.
 * The initialization process includes setting up the visual elements of each cell and assigning the
 * appropriate content type to each based on random generation and specific game rules.
 *
 * @param none No parameters are required as the size and initial setup are predetermined within the constructor.
 */

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.Random;

public class GameBoard {
    private GridPane grid = new GridPane();
    private String[][] gridContents;
    private final int size = 15;
    private final int cellSize = 50;
    private Random rand = new Random();

    public GameBoard() {
        gridContents = new String[size][size];
        initializeGrid();
    }

    /**
     * Initializes the grid by setting up each cell with a default visual representation and content type.
     * This method loops through each row and column of the grid, creating and placing visual elements such as
     * {@link Rectangle} for the background and {@link ImageView} for potentially changing cell contents.
     * Specific cell types such as obstacles, valuable minerals, and different layers of soil are assigned based on
     * predetermined rules and random generation to enhance gameplay variety.
     */
    private void initializeGrid() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Rectangle background = new Rectangle(cellSize, cellSize);
                if(i<2){
                background.setFill(Color.DEEPSKYBLUE);
                } else {
                    background.setFill(Color.ROSYBROWN);
                }

                ImageView imageView = new ImageView();
                imageView.setFitHeight(cellSize);
                imageView.setFitWidth(cellSize);

                setupGrid(imageView, i, j);
                grid.add(background, j, i);
                grid.add(imageView, j, i);
            }
        }
    }

    /**
     * Configures an individual cell in the grid with appropriate visual and content settings.
     * This method determines what type of content (e.g., soil, valuable, obstacle) should be placed in each cell
     * based on its position and random factors. It then assigns an image to represent this content visually
     * in the game's grid. The configuration for the top layer and boundaries is fixed, while other contents are
     * randomly distributed according to the game's rules.
     *
     * @param imageView the {@link ImageView} that will visually represent the cell content
     * @param i the row index of the cell to be set up
     * @param j the column index of the cell to be set up
     */
    private void setupGrid(ImageView imageView, int i, int j) {
        if (i <= 1) {
            gridContents[i][j] = "empty";
        }
        if (i == 2) {
            imageView.setImage(new Image("/assets/underground/top_01.png"));
            gridContents[i][j] = "soil";
        }
        if (i >= 3) {
            int element = rand.nextInt(100);
            if (j == 0 || j == size - 1 || i == size - 1 || (element >= 93 && element < 96)) {
                imageView.setImage(new Image("/assets/underground/obstacle_01.png"));
                gridContents[i][j] = "obstacle";
            } else if (element < 70) {
                imageView.setImage(new Image("/assets/underground/soil_01.png"));
                gridContents[i][j] = "soil";
            } else if (element < 78) {
                imageView.setImage(new Image("/assets/underground/valuable_goldium.png"));
                gridContents[i][j] = "valuable1";
            } else if (element < 86) {
                imageView.setImage(new Image("/assets/underground/valuable_ruby.png"));
                gridContents[i][j] = "valuable2";
            } else if (element < 90) {
                imageView.setImage(new Image("/assets/underground/valuable_emerald.png"));
                gridContents[i][j] = "valuable3";
            } else {
                imageView.setImage(new Image("/assets/underground/lava_02.png"));
                gridContents[i][j] = "lava";
            }
        }

    }

    public String getCellContent(int row, int col) {
        return gridContents[row][col];
    }

    public ImageView getCellImageView(int row, int col) {
        int index = (row * size + col) * 2 + 1;
        return (ImageView) grid.getChildren().get(index);
    }

    public int getSize() {
        return size;
    }
    public GridPane getGrid() {
        return grid;
    }

    public void removeCellContent(int row, int col) {
        gridContents[row][col] = "empty";
    }

    /**
     * Checks if a move to a specified position in the grid is valid based on the game's rules.
     * This method verifies if the target coordinates are within the grid bounds and whether the destination cell
     * contains an obstacle, lava, or is otherwise non-navigable. It is primarily used to determine if the drill can
     * move to a new position without encountering game-ending conditions or obstructions.
     *
     * @param x the x-coordinate (horizontal) of the position to move to, in grid cells
     * @param y the y-coordinate (vertical) of the position to move to, in grid cells
     * @return {@code true} if the move is valid, {@code false} otherwise
     */
    public boolean isValidMove(double x, double y) {
        int col = (int) (x / cellSize);
        int row = (int) (y / cellSize);
        if (col < 0 || col >= size || row < 0 || row >= size) {
            return false;
        }
        String content = getCellContent(row, col);
        if (content.equals("lava")) {
            AnchorPane root = null;
            try {
                root = (AnchorPane) grid.getParent();
            } catch (Exception e) {
                //
            }
            GameOver.endGame("lava",0,root);
            return false;
        }
        return !content.equals("obstacle");
    }


}