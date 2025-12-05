package info.jab.aoc2025.day5;

import com.putoet.resources.ResourceLines;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Interactive JavaFX visualization for Day 5 range problems.
 *
 * Features:
 * - Part 1: Visualizes ranges and IDs, highlighting IDs contained in ranges
 * - Part 2: Animated visualization of range merging process
 * - Interactive controls for stepping through the merge process
 * - Color-coded visualization with statistics
 */
public final class RangeVisualization extends Application {

    private static final double CANVAS_WIDTH = 1200.0;
    private static final double CANVAS_HEIGHT = 600.0;
    private static final double RANGE_HEIGHT = 30.0;
    private static final double RANGE_SPACING = 40.0;
    private static final double ID_MARKER_SIZE = 8.0;
    private static final double TIMELINE_OFFSET = 100.0;
    private static final String BACKGROUND_COLOR_STYLE = "-fx-background-color: #16213e;";
    private static final String RESULT_LABEL_PREFIX = "Result: ";

    private Input inputPart1;
    private Input inputPart2;
    private List<Range> sortedRanges;
    private List<Range> mergedRanges;
    private long minValue;
    private long maxValue;
    private double scaleX;

    private Timeline mergeAnimation;
    private int currentMergeStep = 0;
    private List<MergeState> mergeSteps;

    // Part 1 animation state
    private Timeline idCheckAnimation;
    private int currentIdIndex = 0;
    private List<IdCheckStep> idCheckSteps;

    @Override
    public void start(final Stage primaryStage) {
        // Load sample input data for easier visualization
        // Use "/day5/day5-input.txt" for full dataset
        inputPart1 = Input.from(ResourceLines.list("/day5/day5-sample-part1.txt"));
        inputPart2 = Input.from(ResourceLines.list("/day5/day5-sample-part2.txt"));

        // Create UI
        final TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                createPart1Tab(),
                createPart2Tab()
        );

        final Scene scene = new Scene(tabPane, CANVAS_WIDTH + 200, 600);
        primaryStage.setTitle("Day 5 - Range Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void calculateBounds(final Input input) {
        sortedRanges = input.ranges().stream()
                .sorted(Comparator.comparingLong(Range::start))
                .toList();

        mergedRanges = mergeRangesFunctional(sortedRanges);

        final long minRange = sortedRanges.stream().mapToLong(Range::start).min().orElse(0);
        final long maxRange = sortedRanges.stream().mapToLong(Range::end).max().orElse(0);

        if (!input.ids().isEmpty()) {
            final long minId = input.ids().stream().mapToLong(Long::longValue).min().orElse(0);
            final long maxId = input.ids().stream().mapToLong(Long::longValue).max().orElse(0);
            minValue = Math.min(minRange, minId);
            maxValue = Math.max(maxRange, maxId);
        } else {
            minValue = minRange;
            maxValue = maxRange;
        }

        scaleX = (CANVAS_WIDTH - TIMELINE_OFFSET * 2) / (maxValue - minValue);
    }

    private void initializePart2Data(final Input input) {
        calculateBounds(input);
        prepareMergeSteps();
        currentMergeStep = 0;
    }

    private void prepareMergeSteps() {
        mergeSteps = new ArrayList<>();
        if (sortedRanges.isEmpty()) {
            return;
        }

        MergeState state = new MergeState(sortedRanges.get(0), List.of(), 0);
        mergeSteps.add(state);

        while (state.hasNext(sortedRanges)) {
            state = state.next(sortedRanges);
            mergeSteps.add(state);
        }
    }

    /**
     * Record representing a step in the ID checking process.
     */
    private record IdCheckStep(long id, boolean isContained, Range containingRange, int checkedCount) {}

    private void prepareIdCheckSteps(final Input input) {
        idCheckSteps = new ArrayList<>();
        int checkedCount = 0;

        for (final Long id : input.ids()) {
            final Range containingRange = input.ranges().stream()
                    .filter(range -> range.contains(id))
                    .findFirst()
                    .orElse(null);

            final boolean isContained = containingRange != null;
            if (isContained) {
                checkedCount++;
            }

            idCheckSteps.add(new IdCheckStep(id, isContained, containingRange, checkedCount));
        }
    }

    private void initializePart1Data(final Input input) {
        calculateBounds(input);
        prepareIdCheckSteps(input);
        currentIdIndex = 0;
    }

    private Tab createPart1Tab() {
        final Tab tab = new Tab("Part 1: IDs in Ranges");

        final BorderPane root = new BorderPane();

        // Canvas for visualization
        final Pane canvas = new Pane();
        canvas.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.setStyle("-fx-background-color: #1a1a2e;");

        // Initialize Part 1 data
        initializePart1Data(inputPart1);

        // Control panel
        final VBox controlPanel = createPart1ControlPanel(canvas, inputPart1);

        // Statistics panel
        final VBox statsPanel = createStatsPanel(inputPart1);

        // Combine controls and stats in a VBox on the left
        final VBox leftPanel = new VBox(10);
        leftPanel.getChildren().addAll(controlPanel, statsPanel);

        // Draw initial Part 1 visualization
        drawPart1Visualization(canvas, inputPart1, 0);
        updatePart1Stats(0);

        final ScrollPane scrollPane = new ScrollPane(canvas);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        root.setCenter(scrollPane);
        root.setLeft(leftPanel);

        tab.setContent(root);
        return tab;
    }

    private Tab createPart2Tab() {
        final Tab tab = new Tab("Part 2: Range Merging");

        final BorderPane root = new BorderPane();

        // Canvas for visualization
        final Pane canvas = new Pane();
        canvas.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.setStyle("-fx-background-color: #1a1a2e;");

        // Initialize Part 2 data first
        initializePart2Data(inputPart2);

        // Control panel
        final VBox controlPanel = createControlPanel(canvas, inputPart2);

        // Statistics panel
        final VBox statsPanel = createPart2StatsPanel(inputPart2);

        // Combine controls and stats in a VBox on the left
        final VBox leftPanel = new VBox(10);
        leftPanel.getChildren().addAll(controlPanel, statsPanel);

        // Draw initial Part 2 visualization
        drawPart2Visualization(canvas, 0);
        updatePart2Stats(0);

        final ScrollPane scrollPane = new ScrollPane(canvas);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        root.setCenter(scrollPane);
        root.setLeft(leftPanel);

        tab.setContent(root);
        return tab;
    }

    private Label part1ContainedLabel;
    private Label part1ResultLabel;

    private VBox createStatsPanel(final Input input) {
        final VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));
        panel.setStyle(BACKGROUND_COLOR_STYLE);
        panel.setPrefWidth(200);

        final Label question = new Label("How many of the available ingredient IDs are fresh?");
        question.setWrapText(true);
        question.setFont(Font.font(13));
        question.setTextFill(Color.WHITE);
        question.setStyle("-fx-font-style: italic;");

        final Label title = new Label("Part 1 Statistics");
        title.setFont(Font.font(16));
        title.setTextFill(Color.WHITE);

        final Label totalRanges = new Label("Total Ranges: " + input.ranges().size());
        totalRanges.setTextFill(Color.LIGHTBLUE);

        final Label totalIds = new Label("Total IDs: " + input.ids().size());
        totalIds.setTextFill(Color.LIGHTBLUE);

        part1ContainedLabel = new Label("Contained IDs: 0");
        part1ContainedLabel.setTextFill(Color.LIGHTGREEN);
        part1ContainedLabel.setFont(Font.font(14));

        part1ResultLabel = new Label(RESULT_LABEL_PREFIX + "0");
        part1ResultLabel.setTextFill(Color.GOLD);
        part1ResultLabel.setFont(Font.font(16));

        panel.getChildren().addAll(question, title, totalRanges, totalIds, part1ContainedLabel, part1ResultLabel);
        return panel;
    }

    private void updatePart1Stats(final int step) {
        if (part1ContainedLabel != null && part1ResultLabel != null) {
            final int count;
            if (step > 0 && step <= idCheckSteps.size()) {
                final IdCheckStep lastStep = idCheckSteps.get(step - 1);
                count = lastStep.checkedCount();
            } else if (step > idCheckSteps.size()) {
                count = idCheckSteps.stream()
                        .mapToInt(IdCheckStep::checkedCount)
                        .max()
                        .orElse(0);
            } else {
                count = 0;
            }
            part1ContainedLabel.setText("Contained IDs: " + count);
            part1ResultLabel.setText(RESULT_LABEL_PREFIX + count);
        }
    }

    private Label part2MergedCountLabel;
    private Label part2CoverageLabel;
    private Label part2ResultLabel;

    private VBox createPart2StatsPanel(final Input input) {
        final VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));
        panel.setStyle(BACKGROUND_COLOR_STYLE);
        panel.setPrefWidth(200);

        final List<Range> sorted = input.ranges().stream()
                .sorted(Comparator.comparingLong(Range::start))
                .toList();

        final Label question = new Label("How many ingredient IDs are considered to be fresh according to the fresh ingredient ID ranges?");
        question.setWrapText(true);
        question.setFont(Font.font(13));
        question.setTextFill(Color.WHITE);
        question.setStyle("-fx-font-style: italic;");

        final Label title = new Label("Part 2 Statistics");
        title.setFont(Font.font(16));
        title.setTextFill(Color.WHITE);

        final Label originalRanges = new Label("Original Ranges: " + sorted.size());
        originalRanges.setTextFill(Color.LIGHTBLUE);

        part2MergedCountLabel = new Label("Merged Ranges: 0");
        part2MergedCountLabel.setTextFill(Color.LIGHTCORAL);

        part2CoverageLabel = new Label("Total Coverage: 0");
        part2CoverageLabel.setTextFill(Color.LIGHTGREEN);
        part2CoverageLabel.setFont(Font.font(14));

        part2ResultLabel = new Label(RESULT_LABEL_PREFIX + "0");
        part2ResultLabel.setTextFill(Color.GOLD);
        part2ResultLabel.setFont(Font.font(16));

        panel.getChildren().addAll(question, title, originalRanges, part2MergedCountLabel, part2CoverageLabel, part2ResultLabel);
        return panel;
    }

    private void updatePart2Stats(final int step) {
        if (part2MergedCountLabel != null && part2CoverageLabel != null && part2ResultLabel != null) {
            if (step == 0) {
                // Reset state - show initial values
                part2MergedCountLabel.setText("Merged Ranges: 0");
                part2CoverageLabel.setText("Total Coverage: 0");
                part2ResultLabel.setText(RESULT_LABEL_PREFIX + "0");
            } else {
                final List<Range> currentMerged;
                if (step < mergeSteps.size()) {
                    final MergeState state = mergeSteps.get(step);
                    currentMerged = Stream.concat(
                            state.merged().stream(),
                            Stream.of(state.current())
                    ).toList();
                } else {
                    currentMerged = mergedRanges;
                }

                final long currentCoverage = currentMerged.stream()
                        .mapToLong(Range::size)
                        .sum();

                part2MergedCountLabel.setText("Merged Ranges: " + currentMerged.size());
                part2CoverageLabel.setText("Total Coverage: " + currentCoverage);
                part2ResultLabel.setText(RESULT_LABEL_PREFIX + currentCoverage);
            }
        }
    }

    private VBox createPart1ControlPanel(final Pane canvas, final Input input) {
        final VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));
        panel.setStyle(BACKGROUND_COLOR_STYLE);
        panel.setPrefWidth(200);

        final Label title = new Label("Controls");
        title.setFont(Font.font(16));
        title.setTextFill(Color.WHITE);

        final Button playButton = new Button("Play Animation");
        playButton.setPrefWidth(160);
        playButton.setOnAction(e -> {
            initializePart1Data(input);
            playIdCheckAnimation(canvas, input);
        });

        final Label stepLabel = new Label("ID: 0 / " + idCheckSteps.size());
        stepLabel.setTextFill(Color.LIGHTBLUE);
        stepLabel.setId("part1StepLabel");

        final Button stepButton = new Button("Step Forward");
        stepButton.setPrefWidth(160);
        stepButton.setOnAction(e -> {
            if (currentIdIndex <= idCheckSteps.size()) {
                currentIdIndex++;
                drawPart1Visualization(canvas, input, currentIdIndex);
                updatePart1Stats(currentIdIndex);
                stepLabel.setText("ID: " + currentIdIndex + " / " + idCheckSteps.size());
            }
        });

        final Button resetButton = new Button("Reset");
        resetButton.setPrefWidth(160);
        resetButton.setOnAction(e -> {
            initializePart1Data(input);
            if (idCheckAnimation != null) {
                idCheckAnimation.stop();
            }
            drawPart1Visualization(canvas, input, 0);
            updatePart1Stats(0);
            stepLabel.setText("ID: 0 / " + idCheckSteps.size());
        });

        panel.getChildren().addAll(title, playButton, stepButton, resetButton, stepLabel);
        return panel;
    }

    private VBox createControlPanel(final Pane canvas, final Input input) {
        final VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));
        panel.setStyle(BACKGROUND_COLOR_STYLE);
        panel.setPrefWidth(200);

        final Label title = new Label("Controls");
        title.setFont(Font.font(16));
        title.setTextFill(Color.WHITE);

        final Button playButton = new Button("Play Animation");
        playButton.setPrefWidth(160);
        playButton.setOnAction(e -> {
            initializePart2Data(input);
            playMergeAnimation(canvas);
        });

        final Label stepLabel = new Label("Step: 0 / " + (mergeSteps.size() - 1));
        stepLabel.setTextFill(Color.LIGHTBLUE);
        stepLabel.setId("part2StepLabel");

        final Button stepButton = new Button("Step Forward");
        stepButton.setPrefWidth(160);
        stepButton.setOnAction(e -> {
            if (currentMergeStep < mergeSteps.size() - 1) {
                currentMergeStep++;
                drawPart2Visualization(canvas, currentMergeStep);
                updatePart2Stats(currentMergeStep);
                stepLabel.setText("Step: " + currentMergeStep + " / " + (mergeSteps.size() - 1));
            }
        });

        final Button resetButton = new Button("Reset");
        resetButton.setPrefWidth(160);
        resetButton.setOnAction(e -> {
            initializePart2Data(input);
            if (mergeAnimation != null) {
                mergeAnimation.stop();
            }
            drawPart2Visualization(canvas, 0);
            updatePart2Stats(0);
            stepLabel.setText("Step: 0 / " + (mergeSteps.size() - 1));
        });

        panel.getChildren().addAll(title, playButton, stepButton, resetButton, stepLabel);
        return panel;
    }

    private void playMergeAnimation(final Pane canvas) {
        if (mergeAnimation != null) {
            mergeAnimation.stop();
        }

        currentMergeStep = 0;
        final int totalSteps = mergeSteps.size();

        // Find step label to update
        final Label stepLabel = (Label) canvas.getScene().lookup("#part2StepLabel");

        mergeAnimation = new Timeline(
                new KeyFrame(Duration.millis(500), e -> {
                    if (currentMergeStep < totalSteps) {
                        drawPart2Visualization(canvas, currentMergeStep);
                        updatePart2Stats(currentMergeStep);
                        if (stepLabel != null) {
                            stepLabel.setText("Step: " + currentMergeStep + " / " + (mergeSteps.size() - 1));
                        }
                        currentMergeStep++;
                    } else {
                        mergeAnimation.stop();
                    }
                })
        );
        mergeAnimation.setCycleCount(totalSteps);
        mergeAnimation.play();
    }

    private void drawPart1Visualization(final Pane canvas, final Input input, final int step) {
        canvas.getChildren().clear();

        calculateBounds(input);
        drawTimeline(canvas);
        drawRangesWithHighlight(canvas, step);
        drawIdsWithAnimation(canvas, input, step);
    }

    private void drawRangesWithHighlight(final Pane canvas, final int step) {
        Range highlightedRange = getHighlightedRange(step);
        int rangeIndex = 0;
        for (final Range range : sortedRanges) {
            final boolean isHighlighted = isRangeHighlighted(range, highlightedRange);
            final Color rangeColor = isHighlighted
                    ? Color.rgb(255, 200, 100, 0.9)
                    : Color.rgb(100, 150, 255, 0.7);
            drawRange(canvas, range, rangeIndex, rangeColor, isHighlighted);
            rangeIndex++;
        }
    }

    private Range getHighlightedRange(final int step) {
        if (step > 0 && step <= idCheckSteps.size()) {
            final IdCheckStep currentStep = idCheckSteps.get(step - 1);
            if (currentStep.isContained() && currentStep.containingRange() != null) {
                return currentStep.containingRange();
            }
        }
        return null;
    }

    private boolean isRangeHighlighted(final Range range, final Range highlightedRange) {
        return highlightedRange != null &&
                range.start() == highlightedRange.start() &&
                range.end() == highlightedRange.end();
    }

    private void drawIdsWithAnimation(final Pane canvas, final Input input, final int step) {
        final double timelineY = CANVAS_HEIGHT - 50;
        final double idY = Math.min(sortedRanges.size() * RANGE_SPACING + 100, timelineY - 80);
        
        for (int i = 0; i < input.ids().size(); i++) {
            final Long id = input.ids().get(i);
            final double x = TIMELINE_OFFSET + (id - minValue) * scaleX;
            drawIdMarker(canvas, id, x, idY, i, step);
        }
        
        drawIdLabel(canvas, idY);
        drawIdCount(canvas, idY, timelineY, step);
    }

    private void drawIdMarker(final Pane canvas, final Long id, final double x, final double idY,
                              final int index, final int step) {
        final MarkerStyle style = calculateMarkerStyle(index, step);
        final Circle marker = new Circle(x, idY, style.markerSize(), style.color());
        marker.setStroke(Color.WHITE);
        marker.setStrokeWidth(style.strokeWidth());
        canvas.getChildren().add(marker);

        if (index == step - 1 && step > 0 && step <= idCheckSteps.size()) {
            drawIdLabelAndStatus(canvas, id, x, idY, step);
        }
    }

    private MarkerStyle calculateMarkerStyle(final int index, final int step) {
        if (step == 0) {
            return new MarkerStyle(Color.rgb(100, 100, 100, 0.5), ID_MARKER_SIZE, 1);
        } else if (index < step - 1) {
            final IdCheckStep checkStep = idCheckSteps.get(index);
            Color color = checkStep.isContained() ? Color.LIGHTGREEN : Color.LIGHTGRAY;
            return new MarkerStyle(color, ID_MARKER_SIZE, 1);
        } else if (index == step - 1 && step <= idCheckSteps.size()) {
            return new MarkerStyle(Color.YELLOW, ID_MARKER_SIZE * 1.5, 2);
        } else if (index >= step) {
            return new MarkerStyle(Color.rgb(100, 100, 100, 0.5), ID_MARKER_SIZE, 1);
        } else {
            final IdCheckStep checkStep = idCheckSteps.get(index);
            Color color = checkStep.isContained() ? Color.LIGHTGREEN : Color.LIGHTGRAY;
            return new MarkerStyle(color, ID_MARKER_SIZE, 1);
        }
    }

    private record MarkerStyle(Color color, double markerSize, double strokeWidth) {}

    private void drawIdLabelAndStatus(final Pane canvas, final Long id, final double x, final double idY, final int step) {
        final IdCheckStep currentStep = idCheckSteps.get(step - 1);
        final Text idText = new Text(x - 10, idY - 20, String.valueOf(id));
        idText.setFill(Color.WHITE);
        idText.setFont(Font.font(14));
        idText.setStroke(Color.BLACK);
        idText.setStrokeWidth(0.5);
        canvas.getChildren().add(idText);

        final String statusText = currentStep.isContained() ? "✓ Fresh" : "✗ Spoiled";
        final double statusX = currentStep.isContained() ? x - 15 : x - 20;
        final Text status = new Text(statusX, idY + 25, statusText);
        status.setFill(Color.WHITE);
        status.setFont(Font.font(12));
        status.setStroke(Color.BLACK);
        status.setStrokeWidth(0.5);
        canvas.getChildren().add(status);
    }

    private void drawIdLabel(final Pane canvas, final double idY) {
        final Text idLabel = new Text(TIMELINE_OFFSET - 80, idY, "IDs:");
        idLabel.setFill(Color.WHITE);
        idLabel.setFont(Font.font(12));
        canvas.getChildren().add(idLabel);
    }

    private void drawIdCount(final Pane canvas, final double idY, final double timelineY, final int step) {
        if (step > 0) {
            final int count = calculateCheckedCount(step);
            final Text countText = new Text(
                    TIMELINE_OFFSET,
                    Math.min(idY + 50, timelineY - 20),
                    "Fresh IDs found: " + count
            );
            countText.setFill(Color.LIGHTGREEN);
            countText.setFont(Font.font(14));
            canvas.getChildren().add(countText);
        }
    }

    private int calculateCheckedCount(final int step) {
        if (step <= idCheckSteps.size()) {
            final IdCheckStep lastStep = idCheckSteps.get(step - 1);
            return lastStep.checkedCount();
        } else {
            return idCheckSteps.stream()
                    .mapToInt(IdCheckStep::checkedCount)
                    .max()
                    .orElse(0);
        }
    }

    private void playIdCheckAnimation(final Pane canvas, final Input input) {
        if (idCheckAnimation != null) {
            idCheckAnimation.stop();
        }

        currentIdIndex = 0;
        final int totalSteps = idCheckSteps.size() + 1; // +1 to show final state

        // Find step label to update
        final Label stepLabel = (Label) canvas.getScene().lookup("#part1StepLabel");

        idCheckAnimation = new Timeline(
                new KeyFrame(Duration.millis(800), e -> {
                    if (currentIdIndex <= totalSteps) {
                        drawPart1Visualization(canvas, input, currentIdIndex);
                        updatePart1Stats(currentIdIndex);
                        if (stepLabel != null) {
                            stepLabel.setText("ID: " + currentIdIndex + " / " + idCheckSteps.size());
                        }
                        currentIdIndex++;
                    } else {
                        idCheckAnimation.stop();
                    }
                })
        );
        idCheckAnimation.setCycleCount(totalSteps);
        idCheckAnimation.play();
    }

    private void drawPart2Visualization(final Pane canvas, final int step) {
        canvas.getChildren().clear();

        // Draw timeline
        drawTimeline(canvas);

        if (step < mergeSteps.size()) {
            final MergeState state = mergeSteps.get(step);

            // Draw original ranges (faded)
            int rangeIndex = 0;
            for (final Range range : sortedRanges) {
                final boolean isCurrent = rangeIndex == state.index();
                final Color color = isCurrent
                        ? Color.rgb(255, 200, 100, 0.8)
                        : Color.rgb(150, 150, 150, 0.3);
                drawRange(canvas, range, rangeIndex, color, isCurrent);
                rangeIndex++;
            }

            // Draw merged ranges so far
            int mergedIndex = sortedRanges.size() + 1;
            for (final Range merged : state.merged()) {
                drawRange(canvas, merged, mergedIndex, Color.rgb(100, 255, 150, 0.8), false);
                mergedIndex++;
            }

            // Draw current range being processed
            drawRange(canvas, state.current(), sortedRanges.size(), Color.rgb(255, 200, 100, 0.9), true);
        } else {
            // Draw final merged ranges
            int mergedIndex = 0;
            for (final Range merged : mergedRanges) {
                drawRange(canvas, merged, mergedIndex, Color.rgb(100, 255, 150, 0.8), false);
                mergedIndex++;
            }
        }
    }

    private void drawTimeline(final Pane canvas) {
        // Draw horizontal timeline
        final double timelineY = CANVAS_HEIGHT - 100;
        final Line timeline = new Line(
                TIMELINE_OFFSET,
                timelineY,
                CANVAS_WIDTH - TIMELINE_OFFSET,
                timelineY
        );
        timeline.setStroke(Color.WHITE);
        timeline.setStrokeWidth(2);
        canvas.getChildren().add(timeline);

        // Draw scale markers
        final int markerCount = 10;
        for (int i = 0; i <= markerCount; i++) {
            final long value = minValue + (maxValue - minValue) * i / markerCount;
            final double x = TIMELINE_OFFSET + (value - minValue) * scaleX;

            final Line marker = new Line(x, timelineY, x, timelineY - 5);
            marker.setStroke(Color.WHITE);
            canvas.getChildren().add(marker);

            final Text label = new Text(x - 30, timelineY + 15, String.valueOf(value));
            label.setFill(Color.WHITE);
            label.setFont(Font.font(10));
            canvas.getChildren().add(label);
        }
    }

    private void drawRange(final Pane canvas, final Range range, final int index,
                          final Color color, final boolean highlight) {
        final double y = 50 + index * RANGE_SPACING;
        final double startX = TIMELINE_OFFSET + (range.start() - minValue) * scaleX;
        final double endX = TIMELINE_OFFSET + (range.end() - minValue) * scaleX;
        final double width = endX - startX;

        // Draw range rectangle
        final Rectangle rect = new Rectangle(startX, y - RANGE_HEIGHT / 2, width, RANGE_HEIGHT);
        rect.setFill(color);
        rect.setStroke(highlight ? Color.YELLOW : Color.WHITE);
        rect.setStrokeWidth(highlight ? 3 : 1);
        canvas.getChildren().add(rect);

        // Draw range label
        final String labelText = range.start() + "-" + range.end();
        final Text label = new Text(startX + width / 2 - 40, y + 5, labelText);
        label.setFill(Color.WHITE);
        label.setFont(Font.font(10));
        canvas.getChildren().add(label);

        // Draw start/end markers
        final Circle startMarker = new Circle(startX, y, 5, Color.WHITE);
        final Circle endMarker = new Circle(endX, y, 5, Color.WHITE);
        canvas.getChildren().addAll(startMarker, endMarker);
    }

    private List<Range> mergeRangesFunctional(final List<Range> sortedRanges) {
        if (sortedRanges.isEmpty()) {
            return List.of();
        }

        final MergeState initialState = new MergeState(sortedRanges.get(0), List.of(), 0);
        final MergeState finalState = IntStream.range(0, sortedRanges.size() - 1)
                .boxed()
                .reduce(
                        initialState,
                        (state, i) -> state.next(sortedRanges),
                        (s1, s2) -> s2.index() > s1.index() ? s2 : s1
                );
        return finalState.complete();
    }

    public static void main(final String[] args) {
        launch(args);
    }
}

