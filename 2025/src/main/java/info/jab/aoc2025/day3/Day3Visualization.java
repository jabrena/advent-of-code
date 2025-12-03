package info.jab.aoc2025.day3;

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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day3Visualization extends Application {

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 600;
    private static final String INPUT_FILE_PATH = "2025/src/test/resources/day3/day3-input.txt";
    private static final int TARGET_LENGTH = 12; // Part 2 length

    private List<String> inputLines;
    private int currentLineIndex = 0;
    private long totalMaxJoltage = 0;

    // UI Components
    private Canvas canvas;
    private Label statusLabel;
    private Label totalLabel;
    private Slider speedSlider;

    // Animation State
    private boolean isRunning = false;
    private long lastUpdate = 0;
    private long updateInterval = 500_000_000; // 500ms initial delay

    // Algorithm State for current line
    private String currentLineStr;
    private List<Integer> digits;
    private List<Integer> selectedIndices; // Indices of digits selected so far
    private int currentTargetIndex = 0; // How many digits we have selected (0 to TARGET_LENGTH)
    private int currentSearchStart = 0;
    private int currentSearchEnd = 0;
    private int scanningIndex = 0; // For animating the scan within the window
    private int currentMaxDigitVal = -1;
    private int currentMaxDigitIdx = -1;

    private enum State {
        IDLE,
        INIT_LINE,
        SCANNING_WINDOW,
        SELECTING_DIGIT,
        FINISHED_LINE
    }

    private State currentState = State.IDLE;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Load data
        try {
            Path path = Path.of(INPUT_FILE_PATH);
            if (Files.exists(path)) {
                inputLines = Files.readAllLines(path);
            } else {
                // Fallback for different CWD or missing file
                path = Path.of("src/test/resources/day3/day3-input.txt");
                if (Files.exists(path)) {
                    inputLines = Files.readAllLines(path);
                } else {
                    System.err.println("Could not find input file at " + INPUT_FILE_PATH + " or " + path);
                    inputLines = new ArrayList<>();
                    inputLines.add("4732321333332463233337712234322122322247222252423773321362313613333336333732233372323328332333322777"); // Sample
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Setup UI
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a1a;");

        // Top Bar
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER);

        statusLabel = new Label("Ready");
        statusLabel.setTextFill(Color.WHITE);
        statusLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));

        totalLabel = new Label("Total Max Joltage: 0");
        totalLabel.setTextFill(Color.CYAN);
        totalLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));

        topBar.getChildren().addAll(statusLabel, totalLabel);
        root.setTop(topBar);

        // Center Canvas
        canvas = new Canvas(WINDOW_WIDTH, 400);
        root.setCenter(canvas);

        // Bottom Controls
        HBox controls = new HBox(20);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER);

        Button startBtn = new Button("Start / Resume");
        startBtn.setOnAction(e -> isRunning = true);

        Button pauseBtn = new Button("Pause");
        pauseBtn.setOnAction(e -> isRunning = false);

        Button nextBtn = new Button("Next Line");
        nextBtn.setOnAction(e -> {
            finishCurrentLine();
            nextLine();
        });

        speedSlider = new Slider(1, 100, 50);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        Label speedLabel = new Label("Speed:");
        speedLabel.setTextFill(Color.WHITE);

        controls.getChildren().addAll(startBtn, pauseBtn, nextBtn, speedLabel, speedSlider);
        root.setBottom(controls);

        // Animation Timer
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(now);
                draw();
            }
        };
        timer.start();

        // Initial setup
        nextLine();
        isRunning = false; // Start paused

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("Advent of Code 2025 - Day 3 Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void nextLine() {
        if (currentLineIndex >= inputLines.size()) {
            statusLabel.setText("All Lines Processed!");
            isRunning = false;
            currentState = State.IDLE;
            return;
        }

        currentLineStr = inputLines.get(currentLineIndex);
        digits = currentLineStr.chars().mapToObj(c -> c - '0').toList();
        selectedIndices = new ArrayList<>();
        currentTargetIndex = 0;

        // Start state
        currentState = State.INIT_LINE;
    }

    private void finishCurrentLine() {
        // Skip animation and calculate result for this line immediately
        // Logic similar to MaxJoltageSolver but we just skip to end state for visualization
        // (Not strictly necessary if we just rely on nextLine clearing state, but good for skipping)
        currentLineIndex++;
    }

    private void update(long now) {
        if (!isRunning) return;

        // Speed control
        double speed = speedSlider.getValue();
        updateInterval = (long) ((101 - speed) * 10_000_000); // map 1-100 to some nanoseconds delay

        if (now - lastUpdate < updateInterval) return;
        lastUpdate = now;

        switch (currentState) {
            case INIT_LINE:
                // Prepare to search for the first digit
                currentSearchStart = 0;
                setupSearchWindow();
                currentState = State.SCANNING_WINDOW;
                break;

            case SCANNING_WINDOW:
                // Animate scanning through the window to find max
                // We simulate "looking" at index 'scanningIndex'
                if (scanningIndex <= currentSearchEnd) {
                    int val = digits.get(scanningIndex);
                    if (val > currentMaxDigitVal) {
                        currentMaxDigitVal = val;
                        currentMaxDigitIdx = scanningIndex;
                    }
                    scanningIndex++;
                } else {
                    // Finished scanning window
                    currentState = State.SELECTING_DIGIT;
                }
                break;

            case SELECTING_DIGIT:
                // Lock in the max found
                selectedIndices.add(currentMaxDigitIdx);
                currentTargetIndex++;

                if (currentTargetIndex >= TARGET_LENGTH) {
                    // Done with this line
                    long lineValue = 0;
                    for (int idx : selectedIndices) {
                        lineValue = lineValue * 10 + digits.get(idx);
                    }
                    totalMaxJoltage += lineValue;
                    totalLabel.setText("Total Max Joltage: " + totalMaxJoltage);

                    currentState = State.FINISHED_LINE;
                } else {
                    // Prepare for next digit
                    currentSearchStart = currentMaxDigitIdx + 1;
                    setupSearchWindow();
                    currentState = State.SCANNING_WINDOW;
                }
                break;

            case FINISHED_LINE:
                // Wait a bit or move to next line automatically?
                // Let's move to next line
                currentLineIndex++;
                nextLine();
                break;

            case IDLE:
                break;
        }
    }

    private void setupSearchWindow() {
        int remaining = TARGET_LENGTH - currentTargetIndex;
        // The last possible index we can pick is size - remaining
        currentSearchEnd = digits.size() - remaining;

        scanningIndex = currentSearchStart;
        currentMaxDigitVal = -1;
        currentMaxDigitIdx = -1;
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, WINDOW_WIDTH, 400);

        if (digits == null) return;

        double boxSize = 20;
        double gap = 5;
        double startX = 20;
        double startY = 100;
        int digitsPerRow = (int) ((WINDOW_WIDTH - 40) / (boxSize + gap));

        // Draw Line Info
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 20));
        gc.fillText("Line " + (currentLineIndex + 1) + " / " + inputLines.size(), 20, 40);

        // Draw digits
        gc.setFont(Font.font("Monospaced", 16));

        for (int i = 0; i < digits.size(); i++) {
            double x = startX + (i % digitsPerRow) * (boxSize + gap);
            double y = startY + (i / digitsPerRow) * (boxSize + gap);

            // Background for digit
            if (selectedIndices.contains(i)) {
                gc.setFill(Color.LIMEGREEN); // Selected
            } else if (currentState == State.SCANNING_WINDOW && i >= currentSearchStart && i <= currentSearchEnd) {
                 // Inside search window
                 if (i == scanningIndex) {
                     gc.setFill(Color.YELLOW); // Currently looking at
                 } else if (i == currentMaxDigitIdx) {
                     gc.setFill(Color.ORANGE); // Current max candidate
                 } else {
                     gc.setFill(Color.DARKGRAY); // Window background
                 }
            } else if (i < currentSearchStart) {
                gc.setFill(Color.rgb(50, 50, 50)); // Skipped/Past
            } else {
                gc.setFill(Color.BLACK); // Future/Outside window
            }

            gc.fillRect(x, y, boxSize, boxSize);
            gc.setStroke(Color.GRAY);
            gc.strokeRect(x, y, boxSize, boxSize);

            // Draw Number
            gc.setFill(selectedIndices.contains(i) ? Color.BLACK : Color.WHITE);
            gc.fillText(String.valueOf(digits.get(i)), x + 5, y + 15);
        }

        // Draw Constructed Number
        double resultY = 300;
        gc.setFill(Color.CYAN);
        gc.setFont(Font.font("Monospaced", FontWeight.BOLD, 30));
        gc.fillText("Result so far: ", 20, resultY);

        double resX = 250;
        for (int i = 0; i < selectedIndices.size(); i++) {
            gc.fillText(String.valueOf(digits.get(selectedIndices.get(i))), resX + (i * 25), resultY);
        }
    }
}

