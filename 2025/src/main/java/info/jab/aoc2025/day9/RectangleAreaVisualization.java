package info.jab.aoc2025.day9;

import com.putoet.resources.ResourceLines;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Creative JavaFX visualization for Day 9 Maximum Rectangle Area problem.
 *
 * Features:
 * - Part 1: Animated visualization of rectangles formed by point pairs
 *   - Shows all points on the canvas
 *   - Animates through rectangle pairs being evaluated
 *   - Highlights the maximum area rectangle in bright color
 *   - Shows current rectangle being evaluated in semi-transparent color
 * - Part 2: Animated visualization of rectangles inside polygon
 *   - Shows the polygon formed by connecting all points
 *   - Animates through rectangles being tested
 *   - Highlights valid rectangles that fit inside the polygon
 *   - Shows the maximum valid rectangle in bright color
 * - Interactive controls for play/pause/step/reset
 * - Color-coded visualization with statistics
 * - Zoom and pan controls for better viewing
 */
public final class RectangleAreaVisualization extends Application {

    private static final int WINDOW_WIDTH = 1400;
    private static final int WINDOW_HEIGHT = 900;
    private static final String INPUT_FILE_SAMPLE = "/day9/day9-input-sample.txt";
    private static final String INPUT_FILE_REAL = "/day9/day9-input.txt";
    private String currentInputFile = INPUT_FILE_SAMPLE;
    private static final String MONOSPACED_FONT = "Monospaced";
    private static final double POINT_RADIUS = 6.0;
    private static final double MAX_POINT_RADIUS = 10.0;

    // Data
    private List<Point> points;
    private List<Edge> edges;
    private List<RectanglePair> rectanglePairs;
    private List<RectanglePair> validRectangles;

    // Canvas state
    private Canvas canvas;
    private GraphicsContext gc;
    private double scale = 1.0;
    private double offsetX = 0.0;
    private double offsetY = 0.0;
    private double minX, maxX, minY, maxY;
    private double canvasWidth;
    private double canvasHeight;

    // Animation state
    private boolean isRunning = false;
    private boolean isComplete = false;
    private long lastUpdate = 0;
    private int currentPairIndex = 0;
    private long maxArea = 0;
    private RectanglePair maxRectangle = null;
    private RectanglePair currentRectangle = null;

    // Part selection
    private boolean showPart1 = true;

    // UI Components
    private Label statusLabel;
    private Label areaLabel;
    private Label maxAreaLabel;
    private Label pairsLabel;
    private Label resultLabel;
    private Slider speedSlider;
    private Slider zoomSlider;

    // Mouse controls for panning
    private double mouseStartX;
    private double mouseStartY;
    private double panStartOffsetX;
    private double panStartOffsetY;
    private boolean isPanning = false;

    /**
     * Record representing a rectangle formed by two points.
     */
    private record RectanglePair(Point p1, Point p2, long area) {
        int xMin() {
            return Math.min(p1.x(), p2.x());
        }

        int xMax() {
            return Math.max(p1.x(), p2.x());
        }

        int yMin() {
            return Math.min(p1.y(), p2.y());
        }

        int yMax() {
            return Math.max(p1.y(), p2.y());
        }
    }

    @Override
    public void start(final Stage primaryStage) {
        try {
            loadData();

            primaryStage.setTitle("Advent of Code 2025 - Day 9 Maximum Rectangle Area Visualization");

            // Setup UI
            final BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color: #0a0a0a;");

            // Top info panel
            final VBox infoPanel = new VBox(5);
            infoPanel.setPadding(new Insets(10));
            infoPanel.setAlignment(Pos.CENTER);

            statusLabel = new Label("Ready");
            statusLabel.setTextFill(Color.WHITE);
            statusLabel.setFont(Font.font(MONOSPACED_FONT, FontWeight.BOLD, 18));

            final HBox statsBox = new HBox(30);
            statsBox.setAlignment(Pos.CENTER);

            areaLabel = new Label("Current Area: -");
            areaLabel.setTextFill(Color.CYAN);
            areaLabel.setFont(Font.font(MONOSPACED_FONT, FontWeight.BOLD, 14));

            maxAreaLabel = new Label("Max Area: 0");
            maxAreaLabel.setTextFill(Color.LIME);
            maxAreaLabel.setFont(Font.font(MONOSPACED_FONT, FontWeight.BOLD, 14));

            pairsLabel = new Label("Pairs: 0 / 0");
            pairsLabel.setTextFill(Color.YELLOW);
            pairsLabel.setFont(Font.font(MONOSPACED_FONT, FontWeight.BOLD, 14));

            resultLabel = new Label("Result: -");
            resultLabel.setTextFill(Color.ORANGE);
            resultLabel.setFont(Font.font(MONOSPACED_FONT, FontWeight.BOLD, 14));

            statsBox.getChildren().addAll(areaLabel, maxAreaLabel, pairsLabel, resultLabel);
            infoPanel.getChildren().addAll(statusLabel, statsBox);
            root.setTop(infoPanel);

            // Center canvas
            canvasWidth = WINDOW_WIDTH - 40;
            canvasHeight = WINDOW_HEIGHT - 300;
            canvas = new Canvas(canvasWidth, canvasHeight);
            gc = canvas.getGraphicsContext2D();
            root.setCenter(canvas);

            // Mouse controls for panning
            canvas.setOnMousePressed(e -> {
                mouseStartX = e.getX();
                mouseStartY = e.getY();
                panStartOffsetX = offsetX;
                panStartOffsetY = offsetY;
                isPanning = true;
            });

            canvas.setOnMouseReleased(e -> {
                isPanning = false;
            });

            canvas.setOnMouseDragged(e -> {
                if (isPanning) {
                    final double deltaX = e.getX() - mouseStartX;
                    final double deltaY = e.getY() - mouseStartY;
                    offsetX = panStartOffsetX + deltaX;
                    offsetY = panStartOffsetY + deltaY;
                    draw();
                }
            });

            canvas.setOnScroll(e -> {
                final double delta = e.getDeltaY();
                final double zoomFactor = delta > 0 ? 1.1 : 0.9;
                final double oldScale = scale;
                scale = Math.max(0.1, Math.min(10.0, scale * zoomFactor));

                // Zoom towards mouse position
                final double mouseX = e.getX();
                final double mouseY = e.getY();
                final double worldX = (mouseX - offsetX) / oldScale;
                final double worldY = (mouseY - offsetY) / oldScale;
                offsetX = mouseX - worldX * scale;
                offsetY = mouseY - worldY * scale;

                zoomSlider.setValue(scale);
                draw();
            });

            // Bottom controls
            final HBox controls = new HBox(15);
            controls.setPadding(new Insets(10));
            controls.setAlignment(Pos.CENTER);

            final CheckBox inputCheck = new CheckBox("Use Real Input");
            inputCheck.setTextFill(Color.WHITE);
            inputCheck.setSelected(false);
            inputCheck.setOnAction(e -> {
                if (inputCheck.isSelected()) {
                    currentInputFile = INPUT_FILE_REAL;
                } else {
                    currentInputFile = INPUT_FILE_SAMPLE;
                }
                loadData();
                reset();
            });

            final Button part1Btn = new Button("Part 1: Max Rectangle");
            part1Btn.setOnAction(e -> {
                showPart1 = true;
                reset();
            });

            final Button part2Btn = new Button("Part 2: Max Rectangle in Polygon");
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
                    processNextPair();
                }
            });

            final Button resetBtn = new Button("Reset");
            resetBtn.setOnAction(e -> reset());

            speedSlider = new Slider(1, 100, 50);
            speedSlider.setShowTickLabels(true);
            speedSlider.setShowTickMarks(true);
            speedSlider.setPrefWidth(150);
            final Label speedLabel = new Label("Speed:");
            speedLabel.setTextFill(Color.WHITE);

            zoomSlider = new Slider(0.1, 10.0, scale);
            zoomSlider.setShowTickLabels(true);
            zoomSlider.setShowTickMarks(true);
            zoomSlider.setPrefWidth(150);
            zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                scale = newVal.doubleValue();
                draw();
            });
            final Label zoomLabel = new Label("Zoom:");
            zoomLabel.setTextFill(Color.WHITE);

            final Button centerBtn = new Button("Center View");
            centerBtn.setOnAction(e -> {
                centerView();
                zoomSlider.setValue(scale);
                draw();
            });

            final Label helpLabel = new Label("Drag to pan | Scroll to zoom");
            helpLabel.setTextFill(Color.GRAY);
            helpLabel.setFont(Font.font(MONOSPACED_FONT, 10));

            controls.getChildren().addAll(
                    inputCheck, part1Btn, part2Btn, startBtn, pauseBtn, stepBtn, resetBtn,
                    speedLabel, speedSlider, zoomLabel, zoomSlider, centerBtn, helpLabel
            );
            root.setBottom(controls);

            // Animation timer
            final AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(final long now) {
                    update(now);
                }
            };
            timer.start();

            // Initial setup
            centerView();
            reset();
            isRunning = false; // Start paused

            final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception ex) {
            System.err.println("Error loading input file: " + ex.getMessage());
            System.err.println("Make sure the file exists at: " + currentInputFile);
            ex.printStackTrace();
        }
    }

    private void loadData() {
        try {
            // Load points from file
            final List<String> lines = ResourceLines.list(currentInputFile);
            points = lines.stream()
                    .filter(line -> !line.trim().isEmpty())
                    .map(Point::from)
                    .toList();

            if (points.isEmpty()) {
                throw new IllegalArgumentException("No points found in input file");
            }

            // Calculate bounds
            calculateBounds();

            // Build polygon edges for Part 2
            edges = buildPolygonEdges(points);

            // Generate rectangle pairs for Part 1
            rectanglePairs = IntStream.range(0, points.size())
                    .boxed()
                    .flatMap(i -> IntStream.range(i + 1, points.size())
                            .mapToObj(j -> {
                                final Point p1 = points.get(i);
                                final Point p2 = points.get(j);
                                final long area = calculateArea(p1, p2);
                                return new RectanglePair(p1, p2, area);
                            }))
                    .sorted((a, b) -> Long.compare(b.area(), a.area())) // Sort by area descending
                    .toList();

            // Generate valid rectangles for Part 2
            validRectangles = IntStream.range(0, points.size())
                    .boxed()
                    .flatMap(i -> IntStream.range(i + 1, points.size())
                            .mapToObj(j -> {
                                final Point p1 = points.get(i);
                                final Point p2 = points.get(j);
                                final long area = calculateArea(p1, p2);
                                return new RectanglePair(p1, p2, area);
                            }))
                    .filter(pair -> isValidRectangle(pair, edges))
                    .sorted((a, b) -> Long.compare(b.area(), a.area())) // Sort by area descending
                    .toList();

        } catch (Exception ex) {
            System.err.println("Error loading input file: " + ex.getMessage());
            System.err.println("Make sure the file exists at: " + currentInputFile);
            ex.printStackTrace();
        }
    }

    private void calculateBounds() {
        minX = points.stream().mapToInt(Point::x).min().orElse(0);
        maxX = points.stream().mapToInt(Point::x).max().orElse(0);
        minY = points.stream().mapToInt(Point::y).min().orElse(0);
        maxY = points.stream().mapToInt(Point::y).max().orElse(0);

        // Add padding
        final double padding = Math.max((maxX - minX), (maxY - minY)) * 0.1;
        minX -= padding;
        maxX += padding;
        minY -= padding;
        maxY += padding;
    }

    private void centerView() {
        final double rangeX = maxX - minX;
        final double rangeY = maxY - minY;
        final double maxRange = Math.max(rangeX, rangeY);

        if (maxRange > 0) {
            scale = Math.min(canvasWidth / maxRange, canvasHeight / maxRange) * 0.9;
        } else {
            scale = 1.0;
        }

        final double centerX = (minX + maxX) / 2.0;
        final double centerY = (minY + maxY) / 2.0;
        offsetX = canvasWidth / 2.0 - centerX * scale;
        offsetY = canvasHeight / 2.0 - centerY * scale;
    }

    private List<Edge> buildPolygonEdges(final List<Point> points) {
        if (points.isEmpty()) {
            return List.of();
        }
        return IntStream.range(0, points.size())
                .mapToObj(i -> {
                    final Point p1 = points.get(i);
                    final Point p2 = points.get((i + 1) % points.size());
                    return new Edge(p1, p2);
                })
                .toList();
    }

    private long calculateArea(final Point p1, final Point p2) {
        return (Math.abs((long) p1.x() - p2.x()) + 1L) * (Math.abs((long) p1.y() - p2.y()) + 1L);
    }

    private boolean isValidRectangle(final RectanglePair pair, final List<Edge> edges) {
        final int xMin = pair.xMin();
        final int xMax = pair.xMax();
        final int yMin = pair.yMin();
        final int yMax = pair.yMax();

        // Check 1: Midpoint inside polygon (or on boundary)
        final double midX = (xMin + xMax) / 2.0;
        final double midY = (yMin + yMax) / 2.0;
        if (!isPointInPolygon(midX, midY, edges)) {
            return false;
        }

        // Check 2: No edge intersects interior
        return !edgesIntersectRect(xMin, xMax, yMin, yMax, edges);
    }

    private boolean isPointInPolygon(final double x, final double y, final List<Edge> edges) {
        // Check if point is on any edge
        for (final Edge edge : edges) {
            if (isPointOnEdge(x, y, edge)) {
                return true;
            }
        }

        // Ray casting: count intersections with vertical edges
        final List<Edge> verticalEdges = edges.stream()
                .filter(Edge::isVertical)
                .toList();

        long intersections = 0;
        for (final Edge edge : verticalEdges) {
            if (intersectsRay(x, y, edge)) {
                intersections++;
            }
        }

        return (intersections % 2) != 0;
    }

    private boolean isPointOnEdge(final double x, final double y, final Edge edge) {
        if (edge.isVertical()) {
            if (Math.abs(edge.p1().x() - x) < 1e-9) {
                final double y1 = Math.min(edge.p1().y(), edge.p2().y());
                final double y2 = Math.max(edge.p1().y(), edge.p2().y());
                return y >= y1 - 1e-9 && y <= y2 + 1e-9;
            }
        } else {
            if (Math.abs(edge.p1().y() - y) < 1e-9) {
                final double x1 = Math.min(edge.p1().x(), edge.p2().x());
                final double x2 = Math.max(edge.p1().x(), edge.p2().x());
                return x >= x1 - 1e-9 && x <= x2 + 1e-9;
            }
        }
        return false;
    }

    private boolean intersectsRay(final double x, final double y, final Edge edge) {
        final double y1 = Math.min(edge.p1().y(), edge.p2().y());
        final double y2 = Math.max(edge.p1().y(), edge.p2().y());
        final double edgeX = edge.p1().x();
        return y >= y1 && y < y2 && edgeX > x;
    }

    private boolean edgesIntersectRect(final int xMin, final int xMax, final int yMin, final int yMax, final List<Edge> edges) {
        for (final Edge edge : edges) {
            if (edge.isVertical()) {
                final int ex = edge.p1().x();
                if (ex <= xMin || ex >= xMax) {
                    continue;
                }
                final int ey1 = Math.min(edge.p1().y(), edge.p2().y());
                final int ey2 = Math.max(edge.p1().y(), edge.p2().y());
                if (ey2 <= yMin || ey1 >= yMax) {
                    continue;
                }
            } else {
                final int ey = edge.p1().y();
                if (ey <= yMin || ey >= yMax) {
                    continue;
                }
                final int ex1 = Math.min(edge.p1().x(), edge.p2().x());
                final int ex2 = Math.max(edge.p1().x(), edge.p2().x());
                if (ex2 <= xMin || ex1 >= xMax) {
                    continue;
                }
            }
            if (edgeIntersectsRect(xMin, xMax, yMin, yMax, edge)) {
                return true;
            }
        }
        return false;
    }

    private boolean edgeIntersectsRect(final int xMin, final int xMax, final int yMin, final int yMax, final Edge edge) {
        if (edge.isVertical()) {
            final int ex = edge.p1().x();
            final int ey1 = Math.min(edge.p1().y(), edge.p2().y());
            final int ey2 = Math.max(edge.p1().y(), edge.p2().y());
            return ex > xMin && ex < xMax && Math.max(ey1, yMin) < Math.min(ey2, yMax);
        } else {
            final int ey = edge.p1().y();
            final int ex1 = Math.min(edge.p1().x(), edge.p2().x());
            final int ex2 = Math.max(edge.p1().x(), edge.p2().x());
            return ey > yMin && ey < yMax && Math.max(ex1, xMin) < Math.min(ex2, xMax);
        }
    }

    private void update(final long now) {
        if (!isRunning || isComplete) {
            return;
        }

        // Speed control
        final double speed = speedSlider.getValue();
        final long updateInterval = (long) ((101 - speed) * 1_000_000);

        if (now - lastUpdate < updateInterval) {
            return;
        }
        lastUpdate = now;

        processNextPair();
    }

    private void processNextPair() {
        if (isComplete) {
            return;
        }

        final List<RectanglePair> pairs = showPart1 ? rectanglePairs : validRectangles;
        final int maxPairs = pairs.size();

        if (currentPairIndex >= maxPairs) {
            isComplete = true;
            isRunning = false;
            updateLabels();
            return;
        }

        final RectanglePair pair = pairs.get(currentPairIndex);
        currentRectangle = pair;

        if (pair.area() > maxArea) {
            maxArea = pair.area();
            maxRectangle = pair;
        }

        currentPairIndex++;
        updateLabels();
        draw();
    }

    private void updateLabels() {
        final List<RectanglePair> pairs = showPart1 ? rectanglePairs : validRectangles;
        final int maxPairs = pairs.size();

        pairsLabel.setText("Pairs: " + currentPairIndex + " / " + maxPairs);

        if (currentRectangle != null) {
            areaLabel.setText("Current Area: " + currentRectangle.area());
        } else {
            areaLabel.setText("Current Area: -");
        }

        maxAreaLabel.setText("Max Area: " + maxArea);

        if (isComplete) {
            resultLabel.setText("Result: " + maxArea);
            statusLabel.setText("Complete! Result: " + maxArea);
        } else {
            final String statusText = showPart1
                    ? "Finding maximum rectangle..."
                    : "Finding maximum rectangle in polygon...";
            statusLabel.setText(statusText);
            resultLabel.setText("Result: -");
        }
    }

    private void draw() {
        gc.clearRect(0, 0, canvasWidth, canvasHeight);
        gc.setFill(Color.rgb(20, 20, 30));
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        // Draw polygon edges for Part 2
        if (!showPart1 && edges != null) {
            gc.setStroke(Color.rgb(100, 100, 150));
            gc.setLineWidth(2.0);
            for (final Edge edge : edges) {
                final double x1 = edge.p1().x() * scale + offsetX;
                final double y1 = edge.p1().y() * scale + offsetY;
                final double x2 = edge.p2().x() * scale + offsetX;
                final double y2 = edge.p2().y() * scale + offsetY;
                gc.strokeLine(x1, y1, x2, y2);
            }
        }

        // Draw maximum rectangle
        if (maxRectangle != null) {
            drawRectangle(maxRectangle, Color.rgb(255, 200, 0), 3.0, 0.6);
        }

        // Draw current rectangle being evaluated
        if (currentRectangle != null && currentRectangle != maxRectangle) {
            final Color currentColor = showPart1
                    ? Color.rgb(100, 150, 255)  // Blue for Part 1 (all rectangles are valid)
                    : Color.rgb(100, 255, 100);   // Green for Part 2 (all shown rectangles are valid)
            drawRectangle(currentRectangle, currentColor, 2.0, 0.3);
        }

        // Draw all points
        for (final Point point : points) {
            final double x = point.x() * scale + offsetX;
            final double y = point.y() * scale + offsetY;
            final double radius = Math.min(POINT_RADIUS * scale, MAX_POINT_RADIUS);

            // Highlight points that are part of max rectangle
            if (maxRectangle != null
                    && (point.equals(maxRectangle.p1()) || point.equals(maxRectangle.p2()))) {
                gc.setFill(Color.rgb(255, 200, 0));
            } else if (currentRectangle != null
                    && (point.equals(currentRectangle.p1()) || point.equals(currentRectangle.p2()))) {
                gc.setFill(Color.rgb(100, 150, 255));
            } else {
                gc.setFill(Color.rgb(200, 200, 200));
            }

            gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);

            // Draw point outline
            gc.setStroke(Color.rgb(50, 50, 50));
            gc.setLineWidth(1.0);
            gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);
        }
    }

    private void drawRectangle(final RectanglePair rect, final Color color, final double lineWidth, final double alpha) {
        final int xMin = rect.xMin();
        final int xMax = rect.xMax();
        final int yMin = rect.yMin();
        final int yMax = rect.yMax();

        final double screenXMin = xMin * scale + offsetX;
        final double screenYMin = yMin * scale + offsetY;
        final double screenXMax = xMax * scale + offsetX;
        final double screenYMax = yMax * scale + offsetY;
        final double width = screenXMax - screenXMin;
        final double height = screenYMax - screenYMin;

        // Draw filled rectangle
        final Color fillColor = Color.color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        gc.setFill(fillColor);
        gc.fillRect(screenXMin, screenYMin, width, height);

        // Draw rectangle outline
        gc.setStroke(color);
        gc.setLineWidth(lineWidth);
        gc.strokeRect(screenXMin, screenYMin, width, height);
    }

    private void reset() {
        isRunning = false;
        isComplete = false;
        currentPairIndex = 0;
        maxArea = 0;
        maxRectangle = null;
        currentRectangle = null;
        updateLabels();
        draw();
    }

    public static void main(final String[] args) {
        launch(args);
    }
}

