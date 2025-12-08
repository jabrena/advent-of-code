package info.jab.aoc2025.day8;

import com.putoet.resources.ResourceLines;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Creative 3D JavaFX visualization for Day 8 Point Cluster problem.
 *
 * Features:
 * - Full 3D visualization of points in actual 3D space
 * - Interactive mouse rotation and zoom controls
 * - Animated clustering process showing connections forming
 * - Part 1: Visualizes top 1000 connections forming clusters
 * - Part 2: Visualizes connections until all points merge into one cluster
 * - Color-coded clusters with dynamic coloring
 * - 3D lighting and materials
 * - Interactive controls for play/pause/step/reset
 * - Real-time statistics display
 */
public final class PointClusterVisualization extends Application {

    private static final int WINDOW_WIDTH = 1400;
    private static final int WINDOW_HEIGHT = 900;
    // Input file paths
    private static final String INPUT_FILE_SAMPLE = "/day8/day8-input-sample.txt";
    private static final String INPUT_FILE_REAL = "/day8/day8-input.txt";
    private String currentInputFile = INPUT_FILE_SAMPLE;

    private static final String MONOSPACED_FONT = "Monospaced";
    private static final double POINT_RADIUS = 8.0;
    private static final double CONNECTION_RADIUS = 1.0;

    // Data
    private List<info.jab.aoc2025.day8.Point3D> points;
    private List<Connection> connections;
    private DSU dsu;

    // 3D Scene components
    private SubScene subScene;
    private Group root3D;
    private Group sceneGroup; // Group to rotate the entire scene
    private Group gridGroup; // Group for grid lines
    private PerspectiveCamera camera;
    private final List<Sphere> pointSpheres = new ArrayList<>();
    private final List<Cylinder> connectionCylinders = new ArrayList<>();
    private final Map<Connection, Cylinder> connectionToCylinder = new HashMap<>(); // Track which cylinder belongs to which connection
    private final Map<Integer, Color> clusterColors = new HashMap<>();
    private final Random colorRandom = new Random(42); // Fixed seed for consistent colors

    // Camera controls
    private double cameraDistance = 2000;
    private double cameraRotationX = 30; // Start at 30 degrees
    private double cameraRotationY = 45; // Start at 45 degrees
    private double mousePosX;
    private double mousePosY;
    private boolean mousePressed = false;

    // Animation state
    private boolean isRunning = false;
    private boolean isComplete = false;
    private long lastUpdate = 0;
    private int currentConnectionIndex = 0;
    private Connection lastMergeConnection = null; // The connection that completed the merge (Part 2)
    private final List<AnimatedConnection3D> animatedConnections = new ArrayList<>();

    // Part selection
    private boolean showPart1 = true;
    private int connectionLimit = 10; // Start with sample (10 connections)

    // UI Components
    private Label statusLabel;
    private Label clustersLabel;
    private Label connectionsLabel;
    private Label resultLabel;
    private Slider speedSlider;
    private Slider zoomSlider;
    private Button part1Btn; // Store reference to update button text

    // 3D bounds for centering
    private double centerX, centerY, centerZ;
    private double scale = 1.0;

    private int getConnectionLimitForCurrentInput() {
        return currentInputFile.equals(INPUT_FILE_SAMPLE) ? 10 : 1000;
    }

    private void updatePart1ButtonText() {
        if (part1Btn != null) {
            final int limit = getConnectionLimitForCurrentInput();
            part1Btn.setText("Part 1: Top " + limit);
        }
    }

    private void loadData() {
        try {
            // Load points from file
            final List<String> lines = ResourceLines.list(currentInputFile);
            points = lines.stream()
                    .filter(line -> !line.trim().isEmpty())
                    .map(info.jab.aoc2025.day8.Point3D::from)
                    .toList();

            if (points.isEmpty()) {
                throw new IllegalArgumentException("No points found in input file");
            }

            // Calculate connections
            connections = IntStream.range(0, points.size())
                    .boxed()
                    .flatMap(i -> IntStream.range(i + 1, points.size())
                            .mapToObj(j -> new Connection(i, j, points.get(i).distanceSquared(points.get(j)))))
                    .sorted(Comparator.comparingLong(Connection::distanceSquared))
                    .toList();

            // Calculate 3D bounds and center
            calculate3DBounds();

            // Re-create points in 3D scene
            if (root3D != null) {
                // Clear existing points and connections
                root3D.getChildren().removeAll(pointSpheres);
                pointSpheres.clear();
                root3D.getChildren().removeAll(connectionCylinders);
                connectionCylinders.clear();
                animatedConnections.clear();

                // Re-add points
                createPointSpheres();

                // Reset DSU and state
                reset();

                // Update camera
                updateCamera();
            }

        } catch (Exception ex) {
            System.err.println("Error loading input file: " + ex.getMessage());
            System.err.println("Make sure the file exists at: " + currentInputFile);
            ex.printStackTrace();
        }
    }

    @Override
    public void start(final Stage primaryStage) {
        try {
            loadData();
            // Initialize connection limit based on current input
            connectionLimit = getConnectionLimitForCurrentInput();

            primaryStage.setTitle("Advent of Code 2025 - Day 8 Point Cluster 3D Visualization");

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

            clustersLabel = new Label("Clusters: 0");
            clustersLabel.setTextFill(Color.CYAN);
            clustersLabel.setFont(Font.font(MONOSPACED_FONT, FontWeight.BOLD, 14));

            connectionsLabel = new Label("Connections: 0");
            connectionsLabel.setTextFill(Color.LIME);
            connectionsLabel.setFont(Font.font(MONOSPACED_FONT, FontWeight.BOLD, 14));

            resultLabel = new Label("Result: -");
            resultLabel.setTextFill(Color.YELLOW);
            resultLabel.setFont(Font.font(MONOSPACED_FONT, FontWeight.BOLD, 14));

            statsBox.getChildren().addAll(clustersLabel, connectionsLabel, resultLabel);
            infoPanel.getChildren().addAll(statusLabel, statsBox);
            root.setTop(infoPanel);

            // Setup 3D scene
            setup3DScene();

            // Center 3D scene
            root.setCenter(subScene);

            // Bottom controls
            final HBox controls = new HBox(15);
            controls.setPadding(new Insets(10));
            controls.setAlignment(Pos.CENTER);

            final CheckBox inputCheck = new CheckBox("Use Real Input");
            inputCheck.setTextFill(Color.WHITE);
            inputCheck.setSelected(false); // Start with sample input
            inputCheck.setOnAction(e -> {
                if (inputCheck.isSelected()) {
                    currentInputFile = INPUT_FILE_REAL;
                } else {
                    currentInputFile = INPUT_FILE_SAMPLE;
                }
                loadData();

                // Update zoom slider range based on new dataset
                final double minZoom = points.size() < 100 ? 50 : 100;
                final double maxZoom = points.size() < 100 ? 3000 : 5000;
                zoomSlider.setMin(minZoom);
                zoomSlider.setMax(maxZoom);
                zoomSlider.setValue(cameraDistance);
                
                // Update connection limit if Part 1 is selected
                if (showPart1) {
                    connectionLimit = getConnectionLimitForCurrentInput();
                    updatePart1ButtonText();
                }
            });

            part1Btn = new Button("Part 1: Top 10"); // Start with sample (10)
            part1Btn.setOnAction(e -> {
                showPart1 = true;
                connectionLimit = getConnectionLimitForCurrentInput();
                updatePart1ButtonText();
                reset();
            });

            final Button part2Btn = new Button("Part 2: Full Merge");
            part2Btn.setOnAction(e -> {
                showPart1 = false;
                connectionLimit = connections.size();
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
                    processNextConnection();
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

            // Adjust zoom slider range based on dataset
            final double minZoom = points.size() < 100 ? 50 : 100;
            final double maxZoom = points.size() < 100 ? 3000 : 5000;
            zoomSlider = new Slider(minZoom, maxZoom, cameraDistance);
            zoomSlider.setShowTickLabels(true);
            zoomSlider.setShowTickMarks(true);
            zoomSlider.setPrefWidth(150);
            zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                cameraDistance = newVal.doubleValue();
                updateCamera();
            });
            final Label zoomLabel = new Label("Zoom:");
            zoomLabel.setTextFill(Color.WHITE);

            final Label helpLabel = new Label("Drag to rotate | Scroll to zoom");
            helpLabel.setTextFill(Color.GRAY);
            helpLabel.setFont(Font.font(MONOSPACED_FONT, 10));

            final CheckBox gridCheck = new CheckBox("Show Grid");
            gridCheck.setTextFill(Color.WHITE);
            gridCheck.setSelected(true);
            gridCheck.setOnAction(e -> {
                if (gridGroup != null) {
                    gridGroup.setVisible(gridCheck.isSelected());
                }
            });

            controls.getChildren().addAll(
                    inputCheck, part1Btn, part2Btn, startBtn, pauseBtn, stepBtn, resetBtn,
                    speedLabel, speedSlider, zoomLabel, zoomSlider, gridCheck, helpLabel
            );
            
            // Initialize button text after UI is set up
            updatePart1ButtonText();
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

    private void calculate3DBounds() {
        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
        double minZ = Double.MAX_VALUE, maxZ = Double.MIN_VALUE;

        for (final info.jab.aoc2025.day8.Point3D point : points) {
            minX = Math.min(minX, point.x());
            maxX = Math.max(maxX, point.x());
            minY = Math.min(minY, point.y());
            maxY = Math.max(maxY, point.y());
            minZ = Math.min(minZ, point.z());
            maxZ = Math.max(maxZ, point.z());
        }

        centerX = (minX + maxX) / 2.0;
        centerY = (minY + maxY) / 2.0;
        centerZ = (minZ + maxZ) / 2.0;

        // Calculate scale to fit in reasonable 3D space
        // For larger datasets, use more space to reduce density
        final double rangeX = maxX - minX;
        final double rangeY = maxY - minY;
        final double rangeZ = maxZ - minZ;
        final double maxRange = Math.max(Math.max(rangeX, rangeY), rangeZ);
        if (maxRange > 0) {
            // Adjust scale based on dataset size to reduce density for large datasets
            final double baseSpace = points.size() < 100 ? 300.0 : 800.0;
            // Scale factor increases with dataset size to spread points out more
            final double sizeFactor = Math.max(1.0, Math.log10(points.size()) * 0.5);
            scale = (baseSpace * sizeFactor) / maxRange;
        } else {
            scale = 1.0;
        }

        // Set initial camera distance based on scale
        // For larger datasets with more spacing, start further back to see the full view
        final double initialDistanceFactor = points.size() < 100 ? 2.5 : 3.5;
        cameraDistance = maxRange * scale * initialDistanceFactor;
        if (cameraDistance < 800) {
            cameraDistance = 800;
        } else if (cameraDistance > 5000) {
            cameraDistance = 5000;
        }

        // Update zoom slider range based on dataset size
        if (points.size() < 100) {
            // For smaller datasets, use closer zoom range
            cameraDistance = Math.min(cameraDistance, 2000);
        } else {
            // For larger datasets, allow starting further back to see the spread
            cameraDistance = Math.min(cameraDistance, 4000);
        }
    }

    private void setup3DScene() {
        // Create scene group that will be rotated
        sceneGroup = new Group();

        // Create 3D root group (inside scene group)
        root3D = new Group();
        sceneGroup.getChildren().add(root3D);

        // Create camera
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(100000);
        camera.setFieldOfView(60);

        // Position camera initially
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(-cameraDistance);
        camera.getTransforms().clear();

        // Create lights
        final AmbientLight ambientLight = new AmbientLight(Color.rgb(150, 150, 150));
        final PointLight pointLight1 = new PointLight(Color.WHITE);
        pointLight1.setTranslateX(1000);
        pointLight1.setTranslateY(1000);
        pointLight1.setTranslateZ(1000);
        final PointLight pointLight2 = new PointLight(Color.rgb(200, 200, 255));
        pointLight2.setTranslateX(-1000);
        pointLight2.setTranslateY(-1000);
        pointLight2.setTranslateZ(-1000);

        root3D.getChildren().addAll(ambientLight, pointLight1, pointLight2);

        // Add 3D grid for spatial reference
        add3DGrid();

        // Add coordinate axes for reference
        addCoordinateAxes();

        // Create point spheres
        createPointSpheres();

        // Create subscene with sceneGroup
        subScene = new SubScene(sceneGroup, WINDOW_WIDTH - 40, WINDOW_HEIGHT - 300, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.rgb(20, 20, 30));

        // Mouse controls for rotation
        subScene.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mousePressed = true;
        });

        subScene.setOnMouseReleased((MouseEvent me) -> {
            mousePressed = false;
        });

        subScene.setOnMouseDragged((MouseEvent me) -> {
            if (mousePressed) {
                final double deltaX = me.getSceneX() - mousePosX;
                final double deltaY = me.getSceneY() - mousePosY;
                cameraRotationY += deltaX * 0.5;
                cameraRotationX += deltaY * 0.5;
                cameraRotationX = Math.max(-90, Math.min(90, cameraRotationX));
                updateCamera();
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
            }
        });

        subScene.setOnScroll((javafx.scene.input.ScrollEvent event) -> {
            final double delta = event.getDeltaY();
            final double minZoom = points.size() < 100 ? 50 : 100;
            final double maxZoom = points.size() < 100 ? 3000 : 5000;
            // More responsive zoom: scale the zoom speed based on current distance
            final double zoomSpeed = Math.max(10, cameraDistance * 0.05);
            cameraDistance = Math.max(minZoom, Math.min(maxZoom, cameraDistance - delta * zoomSpeed));
            zoomSlider.setValue(cameraDistance);
            updateCamera();
        });
    }

    private void updateCamera() {
        // Use simpler approach: rotate the scene group, keep camera fixed
        sceneGroup.getTransforms().clear();
        sceneGroup.getTransforms().add(new Rotate(cameraRotationY, Rotate.Y_AXIS));
        sceneGroup.getTransforms().add(new Rotate(cameraRotationX, Rotate.X_AXIS));

        // Position camera looking at origin
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(-cameraDistance);
        camera.getTransforms().clear();
    }

    private void add3DGrid() {
        // Create a 3D grid to help visualize the space
        gridGroup = new Group();
        root3D.getChildren().add(gridGroup);

        // Calculate grid bounds based on point distribution
        final double gridSize = 500.0; // Grid extends 500 units in each direction
        final double gridStep = 50.0; // Grid lines every 50 units
        final int gridLines = (int) (gridSize / gridStep);

        final PhongMaterial gridMaterial = new PhongMaterial(Color.rgb(60, 60, 80, 0.3));
        gridMaterial.setSpecularColor(Color.rgb(80, 80, 100));

        // Create grid lines along X-axis (parallel to YZ plane)
        for (int i = -gridLines; i <= gridLines; i++) {
            final double y = i * gridStep;

            // Line along X direction at fixed Y
            final Cylinder lineX = new Cylinder(0.5, gridSize * 2);
            lineX.setTranslateY(y);
            lineX.setRotationAxis(Rotate.Z_AXIS);
            lineX.setRotate(90);
            lineX.setMaterial(gridMaterial);

            // Line along Z direction at fixed Y
            final Cylinder lineZ = new Cylinder(0.5, gridSize * 2);
            lineZ.setTranslateY(y);
            lineZ.setRotationAxis(Rotate.X_AXIS);
            lineZ.setRotate(90);
            lineZ.setMaterial(gridMaterial);

            gridGroup.getChildren().addAll(lineX, lineZ);
        }

        // Create grid lines along Y-axis (parallel to XZ plane)
        for (int i = -gridLines; i <= gridLines; i++) {
            final double x = i * gridStep;

            // Line along Y direction at fixed X
            final Cylinder lineY1 = new Cylinder(0.5, gridSize * 2);
            lineY1.setTranslateX(x);
            lineY1.setMaterial(gridMaterial);

            // Line along Z direction at fixed X
            final Cylinder lineZ = new Cylinder(0.5, gridSize * 2);
            lineZ.setTranslateX(x);
            lineZ.setRotationAxis(Rotate.X_AXIS);
            lineZ.setRotate(90);
            lineZ.setMaterial(gridMaterial);

            gridGroup.getChildren().addAll(lineY1, lineZ);
        }

        // Create grid lines along Z-axis (parallel to XY plane)
        for (int i = -gridLines; i <= gridLines; i++) {
            final double z = i * gridStep;

            // Line along X direction at fixed Z
            final Cylinder lineX = new Cylinder(0.5, gridSize * 2);
            lineX.setTranslateZ(z);
            lineX.setRotationAxis(Rotate.Z_AXIS);
            lineX.setRotate(90);
            lineX.setMaterial(gridMaterial);

            // Line along Y direction at fixed Z
            final Cylinder lineY = new Cylinder(0.5, gridSize * 2);
            lineY.setTranslateZ(z);
            lineY.setMaterial(gridMaterial);

            gridGroup.getChildren().addAll(lineX, lineY);
        }
    }

    private void addCoordinateAxes() {
        // Add X axis (red)
        final Cylinder xAxis = new Cylinder(2, 200);
        xAxis.setTranslateX(100);
        xAxis.setRotationAxis(Rotate.Z_AXIS);
        xAxis.setRotate(90);
        final PhongMaterial xMaterial = new PhongMaterial(Color.RED);
        xAxis.setMaterial(xMaterial);

        // Add Y axis (green)
        final Cylinder yAxis = new Cylinder(2, 200);
        yAxis.setTranslateY(100);
        final PhongMaterial yMaterial = new PhongMaterial(Color.GREEN);
        yAxis.setMaterial(yMaterial);

        // Add Z axis (blue)
        final Cylinder zAxis = new Cylinder(2, 200);
        zAxis.setTranslateZ(100);
        zAxis.setRotationAxis(Rotate.X_AXIS);
        zAxis.setRotate(90);
        final PhongMaterial zMaterial = new PhongMaterial(Color.BLUE);
        zAxis.setMaterial(zMaterial);

        root3D.getChildren().addAll(xAxis, yAxis, zAxis);
    }

    private void createPointSpheres() {
        // Remove old point spheres (but keep axes which are cylinders)
        root3D.getChildren().removeAll(pointSpheres);
        pointSpheres.clear();

        for (int i = 0; i < points.size(); i++) {
            final info.jab.aoc2025.day8.Point3D point = points.get(i);
            final Sphere sphere = new Sphere(POINT_RADIUS);

            // Position sphere centered at origin
            final double x = (point.x() - centerX) * scale;
            final double y = (point.y() - centerY) * scale;
            final double z = (point.z() - centerZ) * scale;
            sphere.setTranslateX(x);
            sphere.setTranslateY(y);
            sphere.setTranslateZ(z);

            // Initial color (will be updated based on cluster)
            final PhongMaterial material = new PhongMaterial(Color.rgb(200, 200, 200));
            material.setSpecularColor(Color.WHITE);
            material.setSpecularPower(32);
            sphere.setMaterial(material);

            pointSpheres.add(sphere);
            root3D.getChildren().add(sphere);
        }
    }

    private Color getClusterColor(final int root) {
        return clusterColors.computeIfAbsent(root, k -> {
            // Generate a bright, distinct color
            final float hue = colorRandom.nextFloat();
            final float saturation = 0.7f + colorRandom.nextFloat() * 0.3f;
            final float brightness = 0.8f + colorRandom.nextFloat() * 0.2f;
            return Color.hsb(hue * 360, saturation, brightness);
        });
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

        processNextConnection();
    }

    private void processNextConnection() {
        if (isComplete) {
            return;
        }

        final int maxConnections = showPart1 ? Math.min(connectionLimit, connections.size()) : connections.size();

        if (currentConnectionIndex >= maxConnections) {
            isComplete = true;
            isRunning = false;
            updateLabels();
            return;
        }

        final Connection conn = connections.get(currentConnectionIndex);
        final boolean merged = dsu.union(conn.p1Index(), conn.p2Index());

        if (merged) {
            // Check if this connection completed the merge (Part 2)
            if (!showPart1 && dsu.getCount() == 1) {
                lastMergeConnection = conn;
                isComplete = true;
                isRunning = false;
            }

            // Update point colors based on cluster
            updatePointColors();

            // Create 3D connection cylinder
            final info.jab.aoc2025.day8.Point3D p1 = points.get(conn.p1Index());
            final info.jab.aoc2025.day8.Point3D p2 = points.get(conn.p2Index());

            final int root = dsu.find(conn.p1Index());
            final Color clusterColor = getClusterColor(root);

            createConnectionCylinder(conn, p1, p2, clusterColor);
        }

        currentConnectionIndex++;
        updateLabels();
    }

    private void updatePointColors() {
        // Update point sphere colors
        for (int i = 0; i < pointSpheres.size(); i++) {
            final int root = dsu.find(i);
            final Color clusterColor = getClusterColor(root);
            final PhongMaterial material = new PhongMaterial(clusterColor);
            pointSpheres.get(i).setMaterial(material);
        }
        
        // Update connection cylinder colors based on current cluster assignments
        updateConnectionColors();
    }
    
    private void updateConnectionColors() {
        // For each connection that has been created, update its color based on current cluster
        for (final Map.Entry<Connection, Cylinder> entry : connectionToCylinder.entrySet()) {
            final Connection conn = entry.getKey();
            final Cylinder cylinder = entry.getValue();
            
            // Get the current cluster root for the connection
            // Since the points are connected, they should be in the same cluster
            final int clusterRoot = dsu.find(conn.p1Index());
            final Color clusterColor = getClusterColor(clusterRoot);
            
            // Update cylinder material
            final PhongMaterial material = new PhongMaterial(clusterColor);
            material.setSpecularColor(clusterColor.brighter());
            material.setSpecularPower(32);
            cylinder.setMaterial(material);
        }
    }

    private void createConnectionCylinder(
            final Connection connection,
            final info.jab.aoc2025.day8.Point3D p1,
            final info.jab.aoc2025.day8.Point3D p2,
            final Color color) {

        // Calculate positions in 3D space (centered at origin)
        // These are the actual 3D positions of the points in our coordinate system
        final double x1 = (p1.x() - centerX) * scale;
        final double y1 = (p1.y() - centerY) * scale;
        final double z1 = (p1.z() - centerZ) * scale;
        final double x2 = (p2.x() - centerX) * scale;
        final double y2 = (p2.y() - centerY) * scale;
        final double z2 = (p2.z() - centerZ) * scale;

        // Calculate direction vector and distance between the two points
        final double dx = x2 - x1;
        final double dy = y2 - y1;
        final double dz = z2 - z1;
        final double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (distance < 0.001) {
            // Points are too close, skip connection
            return;
        }

        // Create a cylinder that connects the two points
        // Cylinder will extend from point1 to point2
        // Length is the distance between points, accounting for sphere radii
        final double cylinderLength = Math.max(0.1, distance - 2 * POINT_RADIUS);
        final Cylinder cylinder = new Cylinder(CONNECTION_RADIUS, cylinderLength);

        // Position cylinder at the midpoint between the two points
        // This is the center of the connection
        final double midX = (x1 + x2) / 2.0;
        final double midY = (y1 + y2) / 2.0;
        final double midZ = (z1 + z2) / 2.0;
        cylinder.setTranslateX(midX);
        cylinder.setTranslateY(midY);
        cylinder.setTranslateZ(midZ);

        // Calculate rotation to align cylinder axis (Y-axis by default) with connection vector
        // Normalize direction vector
        final double nx = dx / distance;
        final double ny = dy / distance;
        final double nz = dz / distance;

        // Rotate cylinder to point from point1 to point2
        // Cylinder default axis is along Y-axis (0, 1, 0)
        // We need to align it with direction vector (nx, ny, nz)

        // Calculate rotation using Euler angles to align Y-axis (0,1,0) with (nx, ny, nz)
        // Order of operations:
        // 1. Rotate around X-axis to tip the cylinder up/down (match Z component)
        // 2. Rotate around Z-axis to spin it to the correct direction in XY plane

        // Target: (nx, ny, nz)
        // Start: (0, 1, 0)

        // Matrix multiplication: Rz * Rx * p
        // Rx * (0,1,0) = (0, cos q, sin q)  where q is angle around X
        // Rz * (0, cos q, sin q) = (-sin a * cos q, cos a * cos q, sin q) where a is angle around Z

        // We match terms:
        // sin q = nz  =>  q = atan2(nz, projXY)
        // -sin a * cos q = nx
        // cos a * cos q = ny
        // => tan a = -nx / ny  =>  a = atan2(-nx, ny)

        final double projXY = Math.sqrt(nx * nx + ny * ny);
        final double angleX = Math.toDegrees(Math.atan2(nz, projXY));
        final double angleZ = Math.toDegrees(Math.atan2(-nx, ny));

        // Apply rotations
        // JavaFX applies transforms in the order they are in the list: T = T1 * T2 * ...
        // We want T = Rz * Rx.
        // So we add Rz first, then Rx.
        cylinder.getTransforms().clear();
        cylinder.getTransforms().add(new Rotate(angleZ, Rotate.Z_AXIS));
        cylinder.getTransforms().add(new Rotate(angleX, Rotate.X_AXIS));

        // Set material with cluster color
        final PhongMaterial material = new PhongMaterial(color);
        material.setSpecularColor(color.brighter());
        material.setSpecularPower(32);
        cylinder.setMaterial(material);

        // Add to scene and track
        connectionCylinders.add(cylinder);
        root3D.getChildren().add(cylinder);
        
        // Store mapping from connection to cylinder for color updates
        connectionToCylinder.put(connection, cylinder);

        // Store for animation
        animatedConnections.add(new AnimatedConnection3D(
                cylinder, System.nanoTime()
        ));
    }

    private void updateLabels() {
        final int clusterCount = dsu.getCount();
        clustersLabel.setText("Clusters: " + clusterCount);
        connectionsLabel.setText("Connections: " + currentConnectionIndex + " / " +
                (showPart1 ? Math.min(connectionLimit, connections.size()) : connections.size()));

        if (isComplete) {
            if (showPart1) {
                final long result = dsu.getComponentSizes().stream()
                        .sorted(Comparator.reverseOrder())
                        .limit(3)
                        .mapToLong(Integer::longValue)
                        .reduce(1L, (acc, size) -> acc * size);
                resultLabel.setText("Result: " + result);
                statusLabel.setText("Complete! Result: " + result);
            } else {
                // Part 2: Use the connection that completed the merge
                if (lastMergeConnection != null) {
                    final info.jab.aoc2025.day8.Point3D p1 = points.get(lastMergeConnection.p1Index());
                    final info.jab.aoc2025.day8.Point3D p2 = points.get(lastMergeConnection.p2Index());
                    final long result = (long) p1.x() * p2.x();
                    resultLabel.setText("Result: " + result);
                    statusLabel.setText("Complete! Result: " + result);
                } else if (currentConnectionIndex > 0 && dsu.getCount() == 1) {
                    // Fallback: if lastMergeConnection wasn't set, use the last processed connection
                    final Connection lastConn = connections.get(currentConnectionIndex - 1);
                    final info.jab.aoc2025.day8.Point3D p1 = points.get(lastConn.p1Index());
                    final info.jab.aoc2025.day8.Point3D p2 = points.get(lastConn.p2Index());
                    final long result = (long) p1.x() * p2.x();
                    resultLabel.setText("Result: " + result);
                    statusLabel.setText("Complete! Result: " + result);
                } else {
                    resultLabel.setText("Result: 0");
                    statusLabel.setText("Complete!");
                }
            }
        } else {
            final String statusText = showPart1
                    ? "Processing top " + connectionLimit + " connections..."
                    : "Merging all clusters...";
            statusLabel.setText(statusText);
            resultLabel.setText("Result: -");
        }
    }

    private void reset() {
        isRunning = false;
        isComplete = false;
        currentConnectionIndex = 0;
        lastMergeConnection = null;
        animatedConnections.clear();
        clusterColors.clear();
        dsu = new DSU(points.size());

        // Remove all connection cylinders (but keep grid, axes, and point spheres)
        root3D.getChildren().removeAll(connectionCylinders);
        connectionCylinders.clear();
        connectionToCylinder.clear();

        // Reset point colors
        for (final Sphere sphere : pointSpheres) {
            final PhongMaterial material = new PhongMaterial(Color.GRAY);
            sphere.setMaterial(material);
        }

        updateLabels();
    }

    /**
     * Represents an animated connection in 3D space.
     */
    private record AnimatedConnection3D(
            Cylinder cylinder,
            long startTime
    ) {}

    public static void main(final String[] args) {
        launch(args);
    }
}
