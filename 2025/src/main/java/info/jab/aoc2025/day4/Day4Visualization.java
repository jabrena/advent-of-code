package info.jab.aoc2025.day4;

import com.putoet.grid.GridUtils;
import com.putoet.grid.Point;
import info.jab.aoc.Utils;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JavaFX visualization for Day 4 Part 2 solution.
 * Displays an animated grid showing the iterative removal of '@' symbols
 * that have fewer than 4 neighbors, until no more can be removed.
 */
public class Day4Visualization extends Application {

    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 800;
    private static final String INPUT_FILE_PATH = "day4/day4-part2-input-sample.txt";
    private static final char TARGET_CELL = '@';
    private static final char EMPTY_CELL = '.';
    private static final int MIN_NEIGHBORS = 4;
    private static final int[] NEIGHBOR_DX = {-1, 0, 1, -1, 1, -1, 0, 1};
    private static final int[] NEIGHBOR_DY = {-1, -1, -1, 0, 0, 1, 1, 1};

    // Grid state
    private char[][] grid;
    private int rows;
    private int cols;
    private int cellSize;
    private double gridOffsetX;
    private double gridOffsetY;

    // Animation state
    private int currentIteration = 0;
    private int totalRemoved = 0;
    private List<Point> cellsToRemove = new ArrayList<>();
    private List<Point> recentlyRemoved = new ArrayList<>();
    private boolean isRunning = false;
    private long lastUpdate = 0;
    private long updateInterval = 800_000_000; // 800ms initial delay
    private boolean isComplete = false;

    // UI Components
    private Canvas canvas;
    private Label statusLabel;
    private Label iterationLabel;
    private Label removedThisIterationLabel;
    private Label totalRemovedLabel;
    private Slider speedSlider;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load grid from file
            final List<String> lines = Utils.readFileToList(INPUT_FILE_PATH).stream()
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toList());
            grid = GridUtils.of(lines);
            rows = grid.length;
            cols = grid[0].length;

            // Calculate cell size to fit grid in canvas
            final double canvasWidth = WINDOW_WIDTH - 40;
            final double canvasHeight = WINDOW_HEIGHT - 200;
            cellSize = (int) Math.min(canvasWidth / cols, canvasHeight / rows);
            cellSize = Math.max(10, Math.min(cellSize, 50)); // Clamp between 10 and 50
            gridOffsetX = (canvasWidth - (cols * cellSize)) / 2 + 20;
            gridOffsetY = (canvasHeight - (rows * cellSize)) / 2 + 20;

            primaryStage.setTitle("Advent of Code 2025 - Day 4 Part 2 Visualization");

            // Setup UI
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color: #1a1a1a;");

            // Top info panel
            VBox infoPanel = new VBox(5);
            infoPanel.setPadding(new Insets(10));
            infoPanel.setAlignment(Pos.CENTER);

            statusLabel = new Label("Ready");
            statusLabel.setTextFill(Color.WHITE);
            statusLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 18));

            iterationLabel = new Label("Iteration: 0");
            iterationLabel.setTextFill(Color.CYAN);
            iterationLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 14));

            removedThisIterationLabel = new Label("Removed this iteration: 0");
            removedThisIterationLabel.setTextFill(Color.YELLOW);
            removedThisIterationLabel.setFont(Font.font("Monospaced", 14));

            totalRemovedLabel = new Label("Total removed: 0");
            totalRemovedLabel.setTextFill(Color.LIME);
            totalRemovedLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));

            infoPanel.getChildren().addAll(statusLabel, iterationLabel, removedThisIterationLabel, totalRemovedLabel);
            root.setTop(infoPanel);

            // Center canvas
            canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT - 200);
            root.setCenter(canvas);

            // Bottom controls
            HBox controls = new HBox(20);
            controls.setPadding(new Insets(10));
            controls.setAlignment(Pos.CENTER);

            Button startBtn = new Button("Start / Resume");
            startBtn.setOnAction(e -> {
                if (isComplete) {
                    reset();
                }
                isRunning = true;
            });

            Button pauseBtn = new Button("Pause");
            pauseBtn.setOnAction(e -> isRunning = false);

            Button stepBtn = new Button("Step");
            stepBtn.setOnAction(e -> {
                if (!isComplete) {
                    processNextIteration();
                }
            });

            Button resetBtn = new Button("Reset");
            resetBtn.setOnAction(e -> reset());

            speedSlider = new Slider(1, 100, 50);
            speedSlider.setShowTickLabels(true);
            speedSlider.setShowTickMarks(true);
            speedSlider.setPrefWidth(200);
            Label speedLabel = new Label("Speed:");
            speedLabel.setTextFill(Color.WHITE);

            controls.getChildren().addAll(startBtn, pauseBtn, stepBtn, resetBtn, speedLabel, speedSlider);
            root.setBottom(controls);

            // Animation timer
            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    update(now);
                    draw();
                }
            };
            timer.start();

            // Initial setup
            findCellsToRemove();
            isRunning = false; // Start paused

            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading input file: " + e.getMessage());
            System.err.println("Make sure the file exists at: " + INPUT_FILE_PATH);
        }
    }

    private void update(long now) {
        if (!isRunning || isComplete) {
            return;
        }

        // Speed control
        double speed = speedSlider.getValue();
        updateInterval = (long) ((101 - speed) * 8_000_000); // Map 1-100 to delay

        if (now - lastUpdate < updateInterval) {
            return;
        }
        lastUpdate = now;

        processNextIteration();
    }

    private void processNextIteration() {
        if (isComplete) {
            return;
        }

        if (cellsToRemove.isEmpty()) {
            // No more cells to remove
            isComplete = true;
            isRunning = false;
            statusLabel.setText("Complete! All cells with < 4 neighbors removed.");
            return;
        }

        // Remove cells
        recentlyRemoved = new ArrayList<>(cellsToRemove);
        removeCells(cellsToRemove);
        totalRemoved += cellsToRemove.size();
        currentIteration++;

        // Find next batch of cells to remove
        findCellsToRemove();

        updateLabels();
    }

    private void findCellsToRemove() {
        cellsToRemove = new ArrayList<>();
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (grid[y][x] == TARGET_CELL) {
                    if (countNeighbors(x, y) < MIN_NEIGHBORS) {
                        cellsToRemove.add(Point.of(x, y));
                    }
                }
            }
        }
    }

    private int countNeighbors(final int x, final int y) {
        int count = 0;
        for (int i = 0; i < NEIGHBOR_DX.length; i++) {
            final int nx = x + NEIGHBOR_DX[i];
            final int ny = y + NEIGHBOR_DY[i];
            if (nx >= 0 && nx < cols && ny >= 0 && ny < rows
                    && grid[ny][nx] == TARGET_CELL) {
                count++;
            }
        }
        return count;
    }

    private void removeCells(final List<Point> toRemove) {
        toRemove.forEach(p -> grid[p.y()][p.x()] = EMPTY_CELL);
    }

    private void updateLabels() {
        iterationLabel.setText("Iteration: " + currentIteration);
        removedThisIterationLabel.setText("Removed this iteration: " + recentlyRemoved.size());
        totalRemovedLabel.setText("Total removed: " + totalRemoved);
        statusLabel.setText(isComplete ? "Complete!" : "Processing iteration " + currentIteration);
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT - 200);

        // Draw grid
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                final double cellX = gridOffsetX + x * cellSize;
                final double cellY = gridOffsetY + y * cellSize;

                final Point currentPoint = Point.of(x, y);
                final boolean isMarkedForRemoval = cellsToRemove.contains(currentPoint);
                final boolean wasRecentlyRemoved = recentlyRemoved.contains(currentPoint);
                final char cell = grid[y][x];

                // Determine cell color
                Color fillColor;
                Color strokeColor = Color.GRAY;

                if (cell == EMPTY_CELL) {
                    if (wasRecentlyRemoved) {
                        // Fade effect for recently removed cells
                        fillColor = Color.rgb(100, 50, 50);
                        strokeColor = Color.rgb(150, 100, 100);
                    } else {
                        fillColor = Color.BLACK;
                    }
                } else if (cell == TARGET_CELL) {
                    if (isMarkedForRemoval) {
                        // Highlight cells marked for removal
                        fillColor = Color.rgb(255, 100, 100); // Red-orange
                        strokeColor = Color.RED;
                    } else {
                        final int neighborCount = countNeighbors(x, y);
                        if (neighborCount >= MIN_NEIGHBORS) {
                            // Stable cell with enough neighbors
                            fillColor = Color.rgb(100, 200, 100); // Green
                        } else {
                            // Cell that will be removed next iteration
                            fillColor = Color.rgb(255, 200, 100); // Orange-yellow
                        }
                    }
                } else {
                    fillColor = Color.BLACK;
                }

                // Draw cell
                gc.setFill(fillColor);
                gc.fillRect(cellX, cellY, cellSize, cellSize);
                gc.setStroke(strokeColor);
                gc.setLineWidth(1);
                gc.strokeRect(cellX, cellY, cellSize, cellSize);

                // Draw neighbor count for '@' cells
                if (cell == TARGET_CELL && cellSize >= 20) {
                    final int neighborCount = countNeighbors(x, y);
                    gc.setFill(Color.WHITE);
                    gc.setFont(Font.font("Monospaced", FontWeight.BOLD, Math.max(8, cellSize / 3)));
                    final String countStr = String.valueOf(neighborCount);
                    final double textWidth = gc.getFont().getSize() * countStr.length() * 0.6;
                    final double textHeight = gc.getFont().getSize();
                    gc.fillText(countStr,
                            cellX + (cellSize - textWidth) / 2,
                            cellY + (cellSize + textHeight) / 2);
                }
            }
        }

        // Draw legend
        drawLegend(gc);
    }

    private void drawLegend(final GraphicsContext gc) {
        final double legendX = 20;
        final double legendY = WINDOW_HEIGHT - 250;
        final double legendItemHeight = 20;
        final double legendBoxSize = 15;

        gc.setFont(Font.font("Monospaced", 12));
        gc.setFill(Color.WHITE);

        int itemIndex = 0;
        final double spacing = 180;

        // Stable cell (green)
        gc.setFill(Color.rgb(100, 200, 100));
        gc.fillRect(legendX, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setStroke(Color.GRAY);
        gc.strokeRect(legendX, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setFill(Color.WHITE);
        gc.fillText("@ with ≥4 neighbors", legendX + legendBoxSize + 5, legendY + itemIndex * legendItemHeight + 13);

        // Marked for removal (red-orange)
        itemIndex++;
        gc.setFill(Color.rgb(255, 100, 100));
        gc.fillRect(legendX + spacing, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setStroke(Color.RED);
        gc.strokeRect(legendX + spacing, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setFill(Color.WHITE);
        gc.fillText("@ marked for removal", legendX + spacing + legendBoxSize + 5, legendY + itemIndex * legendItemHeight + 13);

        // Empty cell (black)
        itemIndex = 0;
        gc.setFill(Color.BLACK);
        gc.fillRect(legendX + spacing * 2, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setStroke(Color.GRAY);
        gc.strokeRect(legendX + spacing * 2, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setFill(Color.WHITE);
        gc.fillText(". (empty)", legendX + spacing * 2 + legendBoxSize + 5, legendY + itemIndex * legendItemHeight + 13);
    }

    private void reset() {
        isRunning = false;
        isComplete = false;
        currentIteration = 0;
        totalRemoved = 0;
        cellsToRemove.clear();
        recentlyRemoved.clear();

        // Reload grid
        try {
            final List<String> lines = Utils.readFileToList(INPUT_FILE_PATH).stream()
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toList());
            grid = GridUtils.of(lines);
            findCellsToRemove();
            updateLabels();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

