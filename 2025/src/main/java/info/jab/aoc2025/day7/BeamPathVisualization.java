package info.jab.aoc2025.day7;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Creative JavaFX visualization for Day 7 beam path counter.
 * 
 * Features:
 * - Animated visualization of beams traveling down the grid
 * - Visual representation of beam splits when hitting splitters
 * - Part 1: Shows split count animation
 * - Part 2: Shows path count visualization
 * - Interactive controls for play/pause/step/reset
 * - Color-coded grid cells and animated beam particles
 */
public final class BeamPathVisualization extends Application {

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 900;
    private static final String INPUT_FILE_PATH = "/day7/day7-input-sample.txt";
    private static final String MONOSPACED_FONT = "Monospaced";
    private static final double BEAM_PARTICLE_SIZE = 6.0;
    private static final double BEAM_TRAIL_LENGTH = 0.3;

    // Grid state
    private Grid grid;
    private int cellSize;
    private double gridOffsetX;
    private double gridOffsetY;
    private Point startPoint;

    // Animation state
    private boolean isRunning = false;
    private boolean isComplete = false;
    private long lastUpdate = 0;
    private int currentRow = 0;
    private Set<Integer> currentBeams = new HashSet<>();
    private List<BeamParticle> beamParticles = new ArrayList<>();
    private long totalSplits = 0;
    private long totalPaths = 0;

    // UI Components
    private Canvas canvas;
    private Label statusLabel;
    private Label splitsLabel;
    private Label pathsLabel;
    private Label currentRowLabel;
    private Slider speedSlider;

    // Part selection
    private boolean showPart1 = true;

    @Override
    public void start(final Stage primaryStage) {
        try {
            // Load grid from file
            final List<String> lines = ResourceLines.list(INPUT_FILE_PATH);
            grid = new Grid(GridUtils.of(lines, CellType.EMPTY.character()));
            
            startPoint = grid.findFirst(c -> c == CellType.START.character())
                    .orElseThrow(() -> new IllegalArgumentException("No start position S found"));

            // Calculate cell size to fit grid in canvas
            final double canvasWidth = (double) WINDOW_WIDTH - 40;
            final double canvasHeight = (double) WINDOW_HEIGHT - 250;
            final int gridWidth = grid.maxX() - grid.minX();
            final int gridHeight = grid.maxY() - grid.minY();
            cellSize = (int) Math.min(canvasWidth / gridWidth, canvasHeight / gridHeight);
            cellSize = Math.clamp(cellSize, 15, 50);
            gridOffsetX = (canvasWidth - ((double) gridWidth * cellSize)) / 2 + 20;
            gridOffsetY = (canvasHeight - ((double) gridHeight * cellSize)) / 2 + 20;

            primaryStage.setTitle("Advent of Code 2025 - Day 7 Beam Path Visualization");

            // Setup UI
            final BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color: #1a1a1a;");

            // Top info panel
            final VBox infoPanel = new VBox(5);
            infoPanel.setPadding(new Insets(10));
            infoPanel.setAlignment(Pos.CENTER);

            statusLabel = new Label("Ready");
            statusLabel.setTextFill(Color.WHITE);
            statusLabel.setFont(Font.font(MONOSPACED_FONT, FontWeight.BOLD, 18));

            final HBox statsBox = new HBox(30);
            statsBox.setAlignment(Pos.CENTER);

            splitsLabel = new Label("Splits: 0");
            splitsLabel.setTextFill(Color.CYAN);
            splitsLabel.setFont(Font.font(MONOSPACED_FONT, FontWeight.BOLD, 14));

            pathsLabel = new Label("Paths: 0");
            pathsLabel.setTextFill(Color.LIME);
            pathsLabel.setFont(Font.font(MONOSPACED_FONT, FontWeight.BOLD, 14));

            currentRowLabel = new Label("Row: 0");
            currentRowLabel.setTextFill(Color.YELLOW);
            currentRowLabel.setFont(Font.font(MONOSPACED_FONT, 14));

            statsBox.getChildren().addAll(splitsLabel, pathsLabel, currentRowLabel);
            infoPanel.getChildren().addAll(statusLabel, statsBox);
            root.setTop(infoPanel);

            // Center canvas
            canvas = new Canvas(WINDOW_WIDTH, (double) WINDOW_HEIGHT - 250);
            root.setCenter(canvas);

            // Bottom controls
            final HBox controls = new HBox(20);
            controls.setPadding(new Insets(10));
            controls.setAlignment(Pos.CENTER);

            final Button part1Btn = new Button("Part 1: Splits");
            part1Btn.setOnAction(e -> {
                showPart1 = true;
                reset();
            });

            final Button part2Btn = new Button("Part 2: Paths");
            part2Btn.setOnAction(e -> {
                showPart1 = false;
                reset();
            });

            final Button startBtn = new Button("Start / Resume");
            startBtn.setOnAction(e -> {
                if (isComplete) {
                    reset();
                }
                isRunning = true;
            });

            final Button pauseBtn = new Button("Pause");
            pauseBtn.setOnAction(e -> isRunning = false);

            final Button stepBtn = new Button("Step");
            stepBtn.setOnAction(e -> {
                if (!isComplete) {
                    processNextStep();
                }
            });

            final Button resetBtn = new Button("Reset");
            resetBtn.setOnAction(e -> reset());

            speedSlider = new Slider(1, 100, 50);
            speedSlider.setShowTickLabels(true);
            speedSlider.setShowTickMarks(true);
            speedSlider.setPrefWidth(200);
            final Label speedLabel = new Label("Speed:");
            speedLabel.setTextFill(Color.WHITE);

            controls.getChildren().addAll(
                    part1Btn, part2Btn, startBtn, pauseBtn, stepBtn, resetBtn, speedLabel, speedSlider
            );
            root.setBottom(controls);

            // Animation timer
            final AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(final long now) {
                    update(now);
                    draw();
                }
            };
            timer.start();

            // Initial setup
            reset();
            isRunning = false; // Start paused

            final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception ex) {
            System.err.println("Error loading input file: " + ex.getMessage());
            System.err.println("Make sure the file exists at: " + INPUT_FILE_PATH);
        }
    }

    private void update(final long now) {
        if (!isRunning || isComplete) {
            return;
        }

        // Speed control
        final double speed = speedSlider.getValue();
        final long updateInterval = (long) ((101 - speed) * 10_000_000);

        if (now - lastUpdate < updateInterval) {
            return;
        }
        lastUpdate = now;

        processNextStep();
    }

    private void processNextStep() {
        if (isComplete) {
            return;
        }

        if (showPart1) {
            processPart1Step();
        } else {
            processPart2Step();
        }

        updateLabels();
    }

    private void processPart1Step() {
        if (currentBeams.isEmpty() || currentRow >= grid.maxY()) {
            isComplete = true;
            isRunning = false;
            statusLabel.setText("Complete! Total splits: " + totalSplits);
            return;
        }

        // Process current row
        final Set<Integer> nextBeams = new HashSet<>();
        final List<BeamParticle> newParticles = new ArrayList<>();

        for (final Integer x : currentBeams) {
            final Point point = Point.of(x, currentRow);
            if (!grid.contains(point)) {
                continue;
            }

            final CellType cellType = CellType.from(grid.get(point));
            
            // Create beam particle at current position
            final double pixelX = gridOffsetX + (x - grid.minX()) * cellSize + cellSize / 2.0;
            final double pixelY = gridOffsetY + (currentRow - grid.minY()) * cellSize + cellSize / 2.0;
            newParticles.add(new BeamParticle(pixelX, pixelY, x));

            if (cellType == CellType.SPLITTER) {
                totalSplits++;
                // Split into two beams
                nextBeams.add(x - 1);
                nextBeams.add(x + 1);
            } else {
                // Continue straight down
                nextBeams.add(x);
            }
        }

        // Update beam particles
        beamParticles.addAll(newParticles);
        
        // Remove old particles that are too far up
        final double minY = gridOffsetY + (currentRow - grid.minY() - 3) * cellSize;
        beamParticles.removeIf(p -> p.y < minY);

        currentBeams = nextBeams;
        currentRow++;
    }

    private void processPart2Step() {
        // For Part 2, we'll show a different visualization
        // This is a simplified version - full path counting would require more complex visualization
        if (currentRow >= grid.maxY()) {
            isComplete = true;
            isRunning = false;
            statusLabel.setText("Complete! Total paths: " + totalPaths);
            return;
        }

        // Similar to Part 1 but track paths differently
        final Set<Integer> nextBeams = new HashSet<>();
        final List<BeamParticle> newParticles = new ArrayList<>();

        for (final Integer x : currentBeams) {
            final Point point = Point.of(x, currentRow);
            if (!grid.contains(point)) {
                continue;
            }

            final CellType cellType = CellType.from(grid.get(point));
            
            final double pixelX = gridOffsetX + (x - grid.minX()) * cellSize + cellSize / 2.0;
            final double pixelY = gridOffsetY + (currentRow - grid.minY()) * cellSize + cellSize / 2.0;
            newParticles.add(new BeamParticle(pixelX, pixelY, x));

            if (cellType == CellType.SPLITTER) {
                nextBeams.add(x - 1);
                nextBeams.add(x + 1);
            } else {
                nextBeams.add(x);
            }
        }

        beamParticles.addAll(newParticles);
        
        final double minY = gridOffsetY + (currentRow - grid.minY() - 3) * cellSize;
        beamParticles.removeIf(p -> p.y < minY);

        currentBeams = nextBeams;
        currentRow++;

        // Update path count (simplified - actual count requires memoization)
        if (currentBeams.isEmpty()) {
            totalPaths = 0;
        } else if (currentRow >= grid.maxY()) {
            totalPaths = currentBeams.size();
        }
    }

    private void updateLabels() {
        splitsLabel.setText("Splits: " + totalSplits);
        pathsLabel.setText("Paths: " + totalPaths);
        currentRowLabel.setText("Row: " + currentRow);
        statusLabel.setText(isComplete 
                ? "Complete!" 
                : (showPart1 ? "Processing splits..." : "Processing paths..."));
    }

    private void draw() {
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, WINDOW_WIDTH, (double) WINDOW_HEIGHT - 250);

        // Draw grid
        drawGrid(gc);

        // Draw beam particles
        drawBeamParticles(gc);

        // Draw legend
        drawLegend(gc);
    }

    private void drawGrid(final GraphicsContext gc) {
        for (int y = grid.minY(); y < grid.maxY(); y++) {
            for (int x = grid.minX(); x < grid.maxX(); x++) {
                final Point point = Point.of(x, y);
                final double cellX = gridOffsetX + (x - grid.minX()) * cellSize;
                final double cellY = gridOffsetY + (y - grid.minY()) * cellSize;

                final CellType cellType = CellType.from(grid.get(point));
                final boolean isCurrentRow = y == currentRow;
                final boolean hasBeam = currentBeams.contains(x) && y == currentRow;

                // Determine cell color
                final Color[] colors = switch (cellType) {
                    case START -> new Color[] { Color.rgb(100, 200, 255), Color.CYAN };
                    case SPLITTER -> hasBeam
                            ? new Color[] { Color.rgb(255, 150, 50), Color.ORANGE }
                            : new Color[] { Color.rgb(255, 200, 100), Color.YELLOW };
                    case EMPTY -> isCurrentRow && hasBeam
                            ? new Color[] { Color.rgb(100, 255, 100, 0.5), Color.LIME }
                            : new Color[] { Color.rgb(30, 30, 30), Color.GRAY };
                };

                final Color fillColor = colors[0];
                final Color strokeColor = colors[1];

                // Draw cell
                gc.setFill(fillColor);
                gc.fillRect(cellX, cellY, cellSize, cellSize);
                gc.setStroke(strokeColor);
                gc.setLineWidth(1);
                gc.strokeRect(cellX, cellY, cellSize, cellSize);

                // Draw cell character
                if (cellSize >= 20) {
                    gc.setFill(Color.WHITE);
                    gc.setFont(Font.font(MONOSPACED_FONT, FontWeight.BOLD, Math.max(10, cellSize / 2)));
                    final String charStr = String.valueOf(cellType.character());
                    final double textWidth = gc.getFont().getSize() * charStr.length() * 0.6;
                    final double textHeight = gc.getFont().getSize();
                    gc.fillText(charStr,
                            cellX + (cellSize - textWidth) / 2,
                            cellY + (cellSize + textHeight) / 2);
                }
            }
        }
    }

    private void drawBeamParticles(final GraphicsContext gc) {
        for (final BeamParticle particle : beamParticles) {
            // Draw particle with glow effect
            final double alpha = Math.min(1.0, 1.0 - (particle.y - gridOffsetY) / (grid.maxY() * cellSize * BEAM_TRAIL_LENGTH));
            
            // Outer glow
            gc.setFill(Color.rgb(100, 200, 255, alpha * 0.3));
            gc.fillOval(particle.x - BEAM_PARTICLE_SIZE * 1.5, particle.y - BEAM_PARTICLE_SIZE * 1.5,
                    BEAM_PARTICLE_SIZE * 3, BEAM_PARTICLE_SIZE * 3);
            
            // Inner particle
            gc.setFill(Color.rgb(150, 220, 255, alpha));
            gc.fillOval(particle.x - BEAM_PARTICLE_SIZE, particle.y - BEAM_PARTICLE_SIZE,
                    BEAM_PARTICLE_SIZE * 2, BEAM_PARTICLE_SIZE * 2);
            
            // Core
            gc.setFill(Color.CYAN);
            gc.fillOval(particle.x - BEAM_PARTICLE_SIZE * 0.5, particle.y - BEAM_PARTICLE_SIZE * 0.5,
                    BEAM_PARTICLE_SIZE, BEAM_PARTICLE_SIZE);
        }
    }

    private void drawLegend(final GraphicsContext gc) {
        final double legendX = 20;
        final double legendY = (double) WINDOW_HEIGHT - 280;
        final double legendItemHeight = 20;
        final double legendBoxSize = 15;

        gc.setFont(Font.font(MONOSPACED_FONT, 12));
        gc.setFill(Color.WHITE);

        int itemIndex = 0;
        final double spacing = 180;

        // Start cell
        gc.setFill(Color.rgb(100, 200, 255));
        gc.fillRect(legendX, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setStroke(Color.CYAN);
        gc.strokeRect(legendX, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setFill(Color.WHITE);
        gc.fillText("S (Start)", legendX + legendBoxSize + 5, legendY + itemIndex * legendItemHeight + 13);

        // Splitter
        itemIndex++;
        gc.setFill(Color.rgb(255, 200, 100));
        gc.fillRect(legendX + spacing, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setStroke(Color.YELLOW);
        gc.strokeRect(legendX + spacing, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setFill(Color.WHITE);
        gc.fillText("^ (Splitter)", legendX + spacing + legendBoxSize + 5, legendY + itemIndex * legendItemHeight + 13);

        // Empty cell
        itemIndex = 0;
        gc.setFill(Color.rgb(30, 30, 30));
        gc.fillRect(legendX + spacing * 2, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setStroke(Color.GRAY);
        gc.strokeRect(legendX + spacing * 2, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setFill(Color.WHITE);
        gc.fillText(". (Empty)", legendX + spacing * 2 + legendBoxSize + 5, legendY + itemIndex * legendItemHeight + 13);

        // Beam particle
        itemIndex++;
        gc.setFill(Color.CYAN);
        gc.fillOval(legendX + spacing * 3, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setFill(Color.WHITE);
        gc.fillText("Beam", legendX + spacing * 3 + legendBoxSize + 5, legendY + itemIndex * legendItemHeight + 13);
    }

    private void reset() {
        isRunning = false;
        isComplete = false;
        currentRow = startPoint.y() + 1;
        currentBeams = Set.of(startPoint.x());
        beamParticles.clear();
        totalSplits = 0;
        totalPaths = 0;
        updateLabels();
    }

    /**
     * Represents a beam particle for visualization.
     */
    private record BeamParticle(double x, double y, int gridX) {}

    public static void main(final String[] args) {
        launch(args);
    }
}

