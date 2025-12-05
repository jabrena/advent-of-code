package info.jab.aoc2025.day1;

import com.putoet.resources.ResourceLines;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

/**
 * JavaFX visualization for Day 1 Part 1 solution.
 * Displays a dial that rotates based on input commands and counts how many times it points to 0.
 */
public class Day1Visualization extends Application {

    private static final int DIAL_SIZE = 400;
    private static final int CENTER_X = DIAL_SIZE / 2;
    private static final int CENTER_Y = DIAL_SIZE / 2;
    private static final int RADIUS = 180;
    private static final int DIAL_RADIUS = 20;
    private static final int ANIMATION_DELAY_MS = 50;
    private static final int INITIAL_POSITION = 50;

    private final DialRotator dialRotator = new DialRotator();
    private List<String> rotations;
    private int currentRotationIndex = 0;
    private int dial = INITIAL_POSITION;
    private int count = 0;
    private boolean isPlaying = false;
    private Timeline animationTimeline;

    private Label dialLabel;
    private Label countLabel;
    private Label currentRotationLabel;
    private Label progressLabel;
    private Canvas dialCanvas;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Get rotations from parameters or default
            String fileName = getParameters().getRaw().isEmpty()
                ? "/day1/day1-input.txt"
                : getParameters().getRaw().get(0);
            rotations = ResourceLines.list(fileName);

            primaryStage.setTitle("Day 1 Part 1 - Dial Visualization (JavaFX)");

            // Create dial canvas
            dialCanvas = new Canvas(DIAL_SIZE, DIAL_SIZE);
            drawDial(dialCanvas.getGraphicsContext2D());

            // Create info labels
            dialLabel = new Label("Dial Position: " + INITIAL_POSITION);
            dialLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            dialLabel.setAlignment(Pos.CENTER);

            countLabel = new Label("Times at 0: 0");
            countLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: red;");
            countLabel.setAlignment(Pos.CENTER);

            currentRotationLabel = new Label("Current: -");
            currentRotationLabel.setStyle("-fx-font-size: 14px;");
            currentRotationLabel.setAlignment(Pos.CENTER);

            progressLabel = new Label("Progress: 0 / " + rotations.size());
            progressLabel.setStyle("-fx-font-size: 12px;");
            progressLabel.setAlignment(Pos.CENTER);

            // Create info panel
            VBox infoPanel = new VBox(5, dialLabel, countLabel, currentRotationLabel, progressLabel);
            infoPanel.setAlignment(Pos.CENTER);
            infoPanel.setPadding(new Insets(10));

            // Create control buttons
            Button playButton = new Button("Play");
            Button pauseButton = new Button("Pause");
            Button resetButton = new Button("Reset");
            Button stepButton = new Button("Step");

            playButton.setOnAction(e -> startAnimation());
            pauseButton.setOnAction(e -> stopAnimation());
            resetButton.setOnAction(e -> reset());
            stepButton.setOnAction(e -> processNextRotation());

            HBox controlPanel = new HBox(10, playButton, pauseButton, stepButton, resetButton);
            controlPanel.setAlignment(Pos.CENTER);
            controlPanel.setPadding(new Insets(10));

            // Create main layout
            BorderPane root = new BorderPane();
            root.setTop(infoPanel);
            root.setCenter(dialCanvas);
            root.setBottom(controlPanel);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            // Create animation timeline
            animationTimeline = new Timeline(new KeyFrame(Duration.millis(ANIMATION_DELAY_MS), e -> {
                if (currentRotationIndex < rotations.size()) {
                    processNextRotation();
                } else {
                    stopAnimation();
                }
            }));
            animationTimeline.setCycleCount(Animation.INDEFINITE);

        } catch (Exception e) {
            showError("Error loading input file: " + e.getMessage() + "\n\n" +
                "Make sure the file exists in the classpath.\n" +
                "For test resources, run with: ./mvnw -pl 2025 exec:java " +
                "-Dexec.mainClass=\"info.jab.aoc.day1.Day1Visualization\" " +
                "-Dexec.classpathScope=test");
            // Suppressed: This is a visualization tool for debugging purposes
        }
    }

    private void drawDial(GraphicsContext gc) {
        // Clear canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, DIAL_SIZE, DIAL_SIZE);

        // Draw circle
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeOval((double) CENTER_X - RADIUS, (double) CENTER_Y - RADIUS, (double) RADIUS * 2, (double) RADIUS * 2);

        // Draw numbers and markers
        gc.setFill(Color.BLACK);
        gc.setFont(javafx.scene.text.Font.font("SansSerif", 12));
        for (int i = 0; i < 100; i++) {
            double angle = Math.toRadians(i * 3.6 - 90); // 0 at top, 3.6 degrees per number
            double x = CENTER_X + Math.cos(angle) * RADIUS;
            double y = CENTER_Y + Math.sin(angle) * RADIUS;

            // Draw small marker
            double markerX = CENTER_X + Math.cos(angle) * (RADIUS - 10);
            double markerY = CENTER_Y + Math.sin(angle) * (RADIUS - 10);
            gc.fillOval(markerX - 2, markerY - 2, 4, 4);

            // Draw number (only every 10th for readability, or special ones)
            if (i % 10 == 0 || i == 0 || i == 50) {
                String num = String.valueOf(i);
                double textWidth = getTextWidth(gc, num);
                double textHeight = 12;
                gc.fillText(num, x - textWidth / 2, y + textHeight / 2);
            }
        }

        // Highlight position 0
        double zeroAngle = Math.toRadians(-90);
        double zeroX = CENTER_X + Math.cos(zeroAngle) * RADIUS;
        double zeroY = CENTER_Y + Math.sin(zeroAngle) * RADIUS;
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        gc.strokeLine(CENTER_X, CENTER_Y, zeroX, zeroY);

        // Draw dial pointer
        double dialAngle = Math.toRadians(dial * 3.6 - 90);
        double dialX = CENTER_X + Math.cos(dialAngle) * RADIUS;
        double dialY = CENTER_Y + Math.sin(dialAngle) * RADIUS;

        gc.setStroke(Color.BLUE);
        gc.setLineWidth(4);
        gc.strokeLine(CENTER_X, CENTER_Y, dialX, dialY);

        // Draw dial center circle
        gc.setFill(Color.BLUE);
        gc.fillOval((double) CENTER_X - DIAL_RADIUS, (double) CENTER_Y - DIAL_RADIUS,
                    (double) DIAL_RADIUS * 2, (double) DIAL_RADIUS * 2);
        gc.setFill(Color.WHITE);
        gc.fillOval((double) CENTER_X - DIAL_RADIUS / 2, (double) CENTER_Y - DIAL_RADIUS / 2,
                    (double) DIAL_RADIUS, (double) DIAL_RADIUS);
    }

    private double getTextWidth(GraphicsContext gc, String text) {
        javafx.scene.text.Font font = gc.getFont();
        javafx.scene.text.Text textNode = new javafx.scene.text.Text(text);
        textNode.setFont(font);
        return textNode.getBoundsInLocal().getWidth();
    }

    private void processNextRotation() {
        if (currentRotationIndex >= rotations.size()) {
            return;
        }

        String rotationStr = rotations.get(currentRotationIndex);
        if (rotationStr == null || rotationStr.trim().isEmpty()) {
            currentRotationIndex++;
            return;
        }

        try {
            Rotation rotation = Rotation.from(rotationStr);
            currentRotationLabel.setText("Current: " + rotationStr);

            dial = dialRotator.rotateDial(dial, rotation.direction(), rotation.distance());

            if (dial == 0) {
                count++;
            }

            currentRotationIndex++;
            updateLabels();
            drawDial(dialCanvas.getGraphicsContext2D());
        } catch (IllegalArgumentException e) {
            // Skip invalid rotations
            currentRotationIndex++;
        }
    }

    private void updateLabels() {
        dialLabel.setText("Dial Position: " + dial);
        countLabel.setText("Times at 0: " + count);
        if (currentRotationIndex < rotations.size()) {
            currentRotationLabel.setText("Next: " + rotations.get(currentRotationIndex));
        } else {
            currentRotationLabel.setText("Completed!");
        }
        progressLabel.setText("Progress: " + currentRotationIndex + " / " + rotations.size());
    }

    private void startAnimation() {
        if (!isPlaying && currentRotationIndex < rotations.size()) {
            isPlaying = true;
            animationTimeline.play();
        }
    }

    private void stopAnimation() {
        isPlaying = false;
        animationTimeline.stop();
    }

    private void reset() {
        stopAnimation();
        dial = INITIAL_POSITION;
        count = 0;
        currentRotationIndex = 0;
        updateLabels();
        drawDial(dialCanvas.getGraphicsContext2D());
    }

    private void showError(String message) {
        if (Platform.isFxApplicationThread()) {
            showErrorDialog(message);
        } else {
            Platform.runLater(() -> showErrorDialog(message));
        }
    }

    private void showErrorDialog(String message) {
        try {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        } catch (Exception e) {
            // Fallback to console if JavaFX Alert is not available
            System.err.println("Error: " + message);
            // Suppressed: This is a visualization tool for debugging purposes
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

