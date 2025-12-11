package info.jab.aoc2025.day11;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Creative JavaFX visualization for Day 11 graph path counter.
 *
 * Features:
 * - Interactive graph visualization with force-directed layout
 * - Animated path exploration from "you" to "out"
 * - Color-coded nodes and edges based on path exploration
 * - Interactive controls for play/pause/step/reset
 * - Real-time path count display
 * - Memoization visualization showing cached results
 * - Toggle between sample and real input
 */
public final class GraphPathVisualization extends Application {

    private static final int WINDOW_WIDTH = 1400;
    private static final int WINDOW_HEIGHT = 900;
    private static final String INPUT_FILE_PATH = "/day11/day11-input-sample.txt";
    private static final String MONOSPACED_FONT = "Monospaced";
    private static final double NODE_RADIUS = 25.0;
    private static final double NODE_SPACING = 150.0;
    private static final double SPRING_FORCE = 0.01;
    private static final double DAMPING = 0.85;
    private static final double REPULSION_FORCE = 5000.0;

    // Graph data
    private Map<GraphNode, List<GraphNode>> graph;
    private Map<GraphNode, NodeVisual> nodeVisuals;
    private List<EdgeVisual> edgeVisuals;

    // Layout
    private double canvasWidth;
    private double canvasHeight;

    // Animation state
    private boolean isRunning = false;
    private boolean isComplete = false;
    private long lastUpdate = 0;
    private Queue<PathStep> pathQueue;
    private Set<GraphNode> currentPath;
    private GraphNode currentSource;
    private GraphNode currentTarget;
    private long currentPathCount = 0;
    private Map<GraphNode, Long> memoizationCache;
    private List<AnimatedPath> animatedPaths;
    private Set<GraphNode> newlyMemoizedNodes; // Track nodes that were just memoized

    // UI Components
    private Canvas canvas;
    private Label statusLabel;
    private Label pathCountLabel;
    private Slider speedSlider;

    @Override
    public void start(final Stage primaryStage) {
        try {
            // Load graph from file
            loadGraph();

            primaryStage.setTitle("Advent of Code 2025 - Day 11 Graph Path Visualization");

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

            pathCountLabel = new Label("Paths: 0");
            pathCountLabel.setTextFill(Color.CYAN);
            pathCountLabel.setFont(Font.font(MONOSPACED_FONT, FontWeight.BOLD, 14));

            statsBox.getChildren().addAll(pathCountLabel);
            infoPanel.getChildren().addAll(statusLabel, statsBox);
            root.setTop(infoPanel);

            // Center canvas
            canvasWidth = WINDOW_WIDTH - 40;
            canvasHeight = WINDOW_HEIGHT - 250;
            canvas = new Canvas(canvasWidth, canvasHeight);
            root.setCenter(canvas);

            // Bottom controls
            final HBox controls = new HBox(20);
            controls.setPadding(new Insets(10));
            controls.setAlignment(Pos.CENTER);

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
                    startBtn, pauseBtn, stepBtn, resetBtn, speedLabel, speedSlider
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
            initializeLayout();
            reset();
            isRunning = false; // Start paused

            final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception ex) {
            System.err.println("Error loading input file: " + ex.getMessage());
            System.err.println("Make sure the file exists at: " + INPUT_FILE_PATH);
            ex.printStackTrace();
        }
    }

    private void loadGraph() {
        final List<String> lines = ResourceLines.list(INPUT_FILE_PATH);
        graph = lines.stream()
                .filter(line -> !line.isBlank())
                .map(this::parseLine)
                .collect(Collectors.toMap(
                        GraphEdge::from,
                        GraphEdge::to,
                        (existing, replacement) -> {
                            // Merge lists if key already exists
                            final List<GraphNode> merged = new ArrayList<>(existing);
                            merged.addAll(replacement);
                            return merged;
                        }
                ));

        // Initialize node visuals
        nodeVisuals = new HashMap<>();
        for (final GraphNode node : graph.keySet()) {
            nodeVisuals.put(node, new NodeVisual(node));
        }
        for (final List<GraphNode> neighbors : graph.values()) {
            for (final GraphNode neighbor : neighbors) {
                if (!nodeVisuals.containsKey(neighbor)) {
                    nodeVisuals.put(neighbor, new NodeVisual(neighbor));
                }
            }
        }

        // Initialize edge visuals
        edgeVisuals = new ArrayList<>();
        for (final Map.Entry<GraphNode, List<GraphNode>> entry : graph.entrySet()) {
            for (final GraphNode target : entry.getValue()) {
                edgeVisuals.add(new EdgeVisual(entry.getKey(), target));
            }
        }
    }

    private GraphEdge parseLine(final String line) {
        final String[] parts = line.split(": ", 2);
        if (parts.length == 0 || parts[0].trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid line format: missing source node");
        }
        final GraphNode from = GraphNode.from(parts[0]);
        final List<GraphNode> toList = parts.length > 1 && !parts[1].trim().isEmpty()
                ? java.util.stream.Stream.of(parts[1].trim().split("\\s+"))
                        .filter(node -> !node.isEmpty())
                        .map(GraphNode::from)
                        .toList()
                : List.<GraphNode>of();
        return new GraphEdge(from, toList);
    }

    private void initializeLayout() {
        // Use hierarchical layout: place nodes in layers based on distance from "you"
        final GraphNode startNode = GraphNode.from("you");
        final Map<GraphNode, Integer> distances = calculateDistances(startNode);

        // Group nodes by distance
        final Map<Integer, List<GraphNode>> layers = new HashMap<>();
        for (final Map.Entry<GraphNode, Integer> entry : distances.entrySet()) {
            layers.computeIfAbsent(entry.getValue(), k -> new ArrayList<>()).add(entry.getKey());
        }

        // Position nodes in layers
        final int maxDistance = layers.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);
        final double layerSpacing = canvasHeight / Math.max(1, maxDistance + 1);

        for (final Map.Entry<Integer, List<GraphNode>> layer : layers.entrySet()) {
            final int distance = layer.getKey();
            final List<GraphNode> nodes = layer.getValue();
            final double y = 50 + distance * layerSpacing;
            final double nodeSpacing = Math.min(NODE_SPACING, canvasWidth / Math.max(1, nodes.size() + 1));
            final double startX = (canvasWidth - (nodes.size() - 1) * nodeSpacing) / 2.0;

            for (int i = 0; i < nodes.size(); i++) {
                final GraphNode node = nodes.get(i);
                final NodeVisual visual = nodeVisuals.get(node);
                visual.x = startX + i * nodeSpacing;
                visual.y = y;
            }
        }

        // Apply force-directed layout for refinement
        for (int iteration = 0; iteration < 100; iteration++) {
            applyForceDirectedLayout();
        }
    }

    private Map<GraphNode, Integer> calculateDistances(final GraphNode start) {
        final Map<GraphNode, Integer> distances = new HashMap<>();
        final Queue<GraphNode> queue = new LinkedList<>();
        queue.offer(start);
        distances.put(start, 0);

        while (!queue.isEmpty()) {
            final GraphNode current = queue.poll();
            final int currentDist = distances.get(current);
            final List<GraphNode> neighbors = graph.getOrDefault(current, List.of());

            for (final GraphNode neighbor : neighbors) {
                if (!distances.containsKey(neighbor)) {
                    distances.put(neighbor, currentDist + 1);
                    queue.offer(neighbor);
                }
            }
        }

        // Set distance for nodes not reachable from start
        for (final GraphNode node : nodeVisuals.keySet()) {
            if (!distances.containsKey(node)) {
                distances.put(node, Integer.MAX_VALUE);
            }
        }

        return distances;
    }

    private void applyForceDirectedLayout() {
        // Spring forces between connected nodes
        for (final EdgeVisual edge : edgeVisuals) {
            final NodeVisual from = nodeVisuals.get(edge.from);
            final NodeVisual to = nodeVisuals.get(edge.to);

            final double dx = to.x - from.x;
            final double dy = to.y - from.y;
            final double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance > 0) {
                final double force = (distance - NODE_SPACING) * SPRING_FORCE;
                final double fx = (dx / distance) * force;
                final double fy = (dy / distance) * force;

                from.vx += fx;
                from.vy += fy;
                to.vx -= fx;
                to.vy -= fy;
            }
        }

        // Repulsion forces between all nodes
        final List<NodeVisual> visuals = new ArrayList<>(nodeVisuals.values());
        for (int i = 0; i < visuals.size(); i++) {
            for (int j = i + 1; j < visuals.size(); j++) {
                final NodeVisual v1 = visuals.get(i);
                final NodeVisual v2 = visuals.get(j);

                final double dx = v2.x - v1.x;
                final double dy = v2.y - v1.y;
                final double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance > 0) {
                    final double force = REPULSION_FORCE / (distance * distance);
                    final double fx = (dx / distance) * force;
                    final double fy = (dy / distance) * force;

                    v1.vx -= fx;
                    v1.vy -= fy;
                    v2.vx += fx;
                    v2.vy += fy;
                }
            }
        }

        // Update positions
        for (final NodeVisual visual : nodeVisuals.values()) {
            visual.x += visual.vx;
            visual.y += visual.vy;
            visual.vx *= DAMPING;
            visual.vy *= DAMPING;

            // Keep nodes within bounds
            visual.x = Math.max(NODE_RADIUS, Math.min(canvasWidth - NODE_RADIUS, visual.x));
            visual.y = Math.max(NODE_RADIUS, Math.min(canvasHeight - NODE_RADIUS, visual.y));
        }
    }

    private void update(final long now) {
        // Apply continuous force-directed layout
        applyForceDirectedLayout();

        // Clear newly memoized nodes after a short delay (for visual effect)
        if (newlyMemoizedNodes != null && !newlyMemoizedNodes.isEmpty()) {
            // Keep the highlight for a few frames, then clear
            if (now % 10 == 0) { // Clear every 10th frame
                newlyMemoizedNodes.clear();
            }
        }

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

        if (currentSource == null) {
            currentSource = GraphNode.from("you");
            currentTarget = GraphNode.from("out");
            pathQueue = new LinkedList<>();
            currentPath = new HashSet<>();
            memoizationCache = new HashMap<>();
            animatedPaths = new ArrayList<>();
            newlyMemoizedNodes = new HashSet<>();
            pathQueue.offer(new PathStep(currentSource, new ArrayList<>()));
        }

        // Process next path step
        final PathStep step = pathQueue.poll();
        if (step == null) {
            // Calculate final result using memoization
            currentPathCount = countPaths(currentSource, currentTarget, memoizationCache);
            isComplete = true;
            isRunning = false;
            statusLabel.setText("Complete! Total paths: " + currentPathCount);
            updateLabels();
            return;
        }

        final GraphNode currentNode = step.node();
        final List<GraphNode> path = step.path();

        // Highlight current path
        currentPath.clear();
        currentPath.addAll(path);
        currentPath.add(currentNode);

        // Create animated path
        animatedPaths.add(new AnimatedPath(new ArrayList<>(currentPath), System.nanoTime()));

        // Compute and memoize path count for current node to show memoization in action
        if (!memoizationCache.containsKey(currentNode)) {
            countPaths(currentNode, currentTarget, memoizationCache);
            newlyMemoizedNodes.add(currentNode);
        }

        if (currentNode.equals(currentTarget)) {
            // Found a path
            currentPathCount++;
        } else {
            // Explore neighbors
            final List<GraphNode> neighbors = graph.getOrDefault(currentNode, List.of());
            for (final GraphNode neighbor : neighbors) {
                final List<GraphNode> newPath = new ArrayList<>(path);
                newPath.add(currentNode);
                pathQueue.offer(new PathStep(neighbor, newPath));
            }
        }

        updateLabels();
    }

    private long countPaths(
            final GraphNode current,
            final GraphNode target,
            final Map<GraphNode, Long> memo) {
        if (current.equals(target)) {
            return 1L;
        }

        // Check if already memoized
        final Long cached = memo.get(current);
        if (cached != null) {
            return cached;
        }

        // Compute and memoize
        final List<GraphNode> neighbors = graph.getOrDefault(current, List.of());
        final long count = neighbors.stream()
                .mapToLong(neighbor -> countPaths(neighbor, target, memo))
                .sum();
        memo.put(current, count);
        return count;
    }

    private void updateLabels() {
        pathCountLabel.setText("Paths: " + currentPathCount);

        final String statusText;
        if (isComplete) {
            statusText = "Complete! Total paths: " + currentPathCount;
        } else {
            statusText = "Exploring paths from 'you' to 'out'...";
        }
        statusLabel.setText(statusText);
    }

    private void draw() {
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasWidth, canvasHeight);

        // Draw edges
        drawEdges(gc);

        // Draw animated paths
        drawAnimatedPaths(gc);

        // Draw nodes
        drawNodes(gc);

        // Draw legend
        drawLegend(gc);
    }

    private void drawEdges(final GraphicsContext gc) {
        for (final EdgeVisual edge : edgeVisuals) {
            final NodeVisual from = nodeVisuals.get(edge.from);
            final NodeVisual to = nodeVisuals.get(edge.to);

            // Determine edge color (keep original colors, don't change for memoization)
            final boolean isInCurrentPath = currentPath != null &&
                    currentPath.contains(edge.from) && currentPath.contains(edge.to);

            Color edgeColor;
            if (isInCurrentPath) {
                edgeColor = Color.rgb(100, 255, 100, 0.8);
            } else {
                // Keep original gray color regardless of memoization status
                edgeColor = Color.rgb(60, 60, 60, 0.3);
            }

            gc.setStroke(edgeColor);
            gc.setLineWidth(isInCurrentPath ? 3.0 : 1.5);
            gc.strokeLine(from.x, from.y, to.x, to.y);
        }
    }

    private void drawAnimatedPaths(final GraphicsContext gc) {
        if (animatedPaths == null) {
            return;
        }

        final long now = System.nanoTime();
        final long fadeDuration = 500_000_000L; // 0.5 seconds

        animatedPaths.removeIf(path -> {
            final long age = now - path.startTime();
            if (age > fadeDuration) {
                return true;
            }

            // Draw path with fade effect
            final double alpha = 1.0 - ((double) age / fadeDuration);
            final List<GraphNode> pathNodes = path.path();

            if (pathNodes.size() < 2) {
                return false;
            }

            gc.setStroke(Color.rgb(255, 200, 0, alpha * 0.6));
            gc.setLineWidth(4.0);

            for (int i = 0; i < pathNodes.size() - 1; i++) {
                final NodeVisual from = nodeVisuals.get(pathNodes.get(i));
                final NodeVisual to = nodeVisuals.get(pathNodes.get(i + 1));
                if (from != null && to != null) {
                    gc.strokeLine(from.x, from.y, to.x, to.y);
                }
            }

            return false;
        });
    }

    private void drawNodes(final GraphicsContext gc) {
        for (final Map.Entry<GraphNode, NodeVisual> entry : nodeVisuals.entrySet()) {
            final GraphNode node = entry.getKey();
            final NodeVisual visual = entry.getValue();

            // Determine node color (keep original colors, don't change for memoization)
            final boolean isSource = node.equals(currentSource);
            final boolean isTarget = node.equals(currentTarget);
            final boolean isInCurrentPath = currentPath != null && currentPath.contains(node);

            Color nodeColor;
            if (isSource) {
                nodeColor = Color.rgb(100, 255, 100);
            } else if (isTarget) {
                nodeColor = Color.rgb(255, 100, 100);
            } else if (isInCurrentPath) {
                nodeColor = Color.rgb(255, 200, 0);
            } else {
                // Keep original gray color regardless of memoization status
                nodeColor = Color.rgb(150, 150, 150);
            }

            // Draw node
            gc.setFill(nodeColor);
            gc.fillOval(visual.x - NODE_RADIUS, visual.y - NODE_RADIUS,
                    NODE_RADIUS * 2, NODE_RADIUS * 2);

            gc.setStroke(Color.WHITE);
            gc.setLineWidth(2.0);
            gc.strokeOval(visual.x - NODE_RADIUS, visual.y - NODE_RADIUS,
                    NODE_RADIUS * 2, NODE_RADIUS * 2);

            // Draw node label
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(MONOSPACED_FONT, FontWeight.BOLD, 12));
            final String label = node.value();
            final double textWidth = gc.getFont().getSize() * label.length() * 0.6;
            gc.fillText(label, visual.x - textWidth / 2, visual.y + 4);

            // Draw count value for all nodes only when simulation is complete
            if (isComplete && memoizationCache != null && currentTarget != null) {
                // Calculate path count for this node if not already memoized
                Long nodeValue = memoizationCache.get(node);
                if (nodeValue == null) {
                    nodeValue = countPaths(node, currentTarget, memoizationCache);
                    memoizationCache.put(node, nodeValue);
                }

                // Draw count value below the node
                gc.setFill(Color.rgb(200, 255, 255));
                gc.setFont(Font.font(MONOSPACED_FONT, FontWeight.BOLD, 10));
                final String countText = String.valueOf(nodeValue);
                final double countTextWidth = gc.getFont().getSize() * countText.length() * 0.5;
                gc.fillText(countText, visual.x - countTextWidth / 2, visual.y + NODE_RADIUS + 15);
            }
        }
    }

    private void drawLegend(final GraphicsContext gc) {
        final double legendX = 20;
        final double legendY = canvasHeight - 100;
        final double legendItemHeight = 20;
        final double legendBoxSize = 15;

        gc.setFont(Font.font(MONOSPACED_FONT, 12));
        gc.setFill(Color.WHITE);

        int itemIndex = 0;
        final double spacing = 200;

        // Source node
        gc.setFill(Color.rgb(100, 255, 100));
        gc.fillOval(legendX, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setStroke(Color.WHITE);
        gc.strokeOval(legendX, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setFill(Color.WHITE);
        gc.fillText("Source", legendX + legendBoxSize + 5, legendY + itemIndex * legendItemHeight + 13);

        // Target node
        itemIndex++;
        gc.setFill(Color.rgb(255, 100, 100));
        gc.fillOval(legendX, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setStroke(Color.WHITE);
        gc.strokeOval(legendX, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setFill(Color.WHITE);
        gc.fillText("Target", legendX + legendBoxSize + 5, legendY + itemIndex * legendItemHeight + 13);

        // Current path node
        itemIndex = 0;
        gc.setFill(Color.rgb(255, 200, 0));
        gc.fillOval(legendX + spacing, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setStroke(Color.WHITE);
        gc.strokeOval(legendX + spacing, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setFill(Color.WHITE);
        gc.fillText("Current Path", legendX + spacing + legendBoxSize + 5, legendY + itemIndex * legendItemHeight + 13);

        // Memoized node
        itemIndex++;
        gc.setFill(Color.rgb(100, 200, 255));
        gc.fillOval(legendX + spacing, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setStroke(Color.WHITE);
        gc.strokeOval(legendX + spacing, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setFill(Color.WHITE);
        gc.fillText("Memoized", legendX + spacing + legendBoxSize + 5, legendY + itemIndex * legendItemHeight + 13);

        // Regular node
        itemIndex = 0;
        gc.setFill(Color.rgb(150, 150, 150));
        gc.fillOval(legendX + spacing * 2, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setStroke(Color.WHITE);
        gc.strokeOval(legendX + spacing * 2, legendY + itemIndex * legendItemHeight, legendBoxSize, legendBoxSize);
        gc.setFill(Color.WHITE);
        gc.fillText("Regular", legendX + spacing * 2 + legendBoxSize + 5, legendY + itemIndex * legendItemHeight + 13);
    }

    private void reset() {
        isRunning = false;
        isComplete = false;
        currentSource = null;
        currentTarget = null;
        pathQueue = null;
        currentPath = null;
        memoizationCache = null;
        animatedPaths = null;
        newlyMemoizedNodes = null;
        currentPathCount = 0;
        updateLabels();
    }

    /**
     * Represents a visual node in the graph.
     */
    private static final class NodeVisual {
        final GraphNode node;
        double x;
        double y;
        double vx = 0.0;
        double vy = 0.0;

        NodeVisual(final GraphNode node) {
            this.node = node;
        }
    }

    /**
     * Represents a visual edge in the graph.
     */
    private record EdgeVisual(GraphNode from, GraphNode to) {}

    /**
     * Represents a step in path exploration.
     */
    private record PathStep(GraphNode node, List<GraphNode> path) {}

    /**
     * Represents an animated path for visualization.
     */
    private record AnimatedPath(List<GraphNode> path, long startTime) {}

    public static void main(final String[] args) {
        launch(args);
    }
}
