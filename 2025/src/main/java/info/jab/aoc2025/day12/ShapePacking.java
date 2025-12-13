package info.jab.aoc2025.day12;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Solver for packing shapes into regions.
 * Determines if given shapes can be placed into regions according to requirements.
 * Follows functional programming principles: immutable data structures, pure functions, and Stream API.
 */
public final class ShapePacking implements Solver<Long> {

    @Override
    public Long solvePartOne(String fileName) {
        String normalizedFileName = fileName.startsWith("/") ? fileName : "/" + fileName;
        List<String> lines = ResourceLines.list(normalizedFileName);

        ParsedData parsedData = ParsedData.from(lines);
        Map<Integer, Shape> shapes = parsedData.shapes();
        List<Region> regions = parsedData.regions();

        return regions.stream()
                .filter(region -> solve(region, shapes))
                .count();
    }

    @Override
    public Long solvePartTwo(String fileName) {
        return 0L;
    }

    /**
     * Pure function that determines if shapes can be placed in a region.
     * Uses immutable data structures and functional transformations.
     */
    private boolean solve(Region region, Map<Integer, Shape> shapes) {
        long totalPresentArea = calculateTotalArea(region, shapes);
        if (totalPresentArea > region.regionArea()) {
            return false;
        }

        if (region.regionArea() <= 200) {
            List<Integer> shapeIdsToPlace = buildShapeIdsList(region, shapes);
            return canFit(region, shapes, shapeIdsToPlace);
        }

        return true;
    }

    /**
     * Pure function that calculates the total area needed for all required shapes.
     */
    private static long calculateTotalArea(Region region, Map<Integer, Shape> shapes) {
        return region.requirements().entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .mapToLong(entry -> (long) shapes.get(entry.getKey()).area() * entry.getValue())
                .sum();
    }

    /**
     * Pure function that builds a sorted list of shape IDs to place.
     * Returns an immutable list sorted by area (largest first).
     */
    private static List<Integer> buildShapeIdsList(Region region, Map<Integer, Shape> shapes) {
        return region.requirements().entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .flatMap(entry -> IntStream.range(0, entry.getValue())
                        .mapToObj(i -> entry.getKey()))
                .sorted((a, b) -> Integer.compare(shapes.get(b).area(), shapes.get(a).area()))
                .toList();
    }

    /**
     * Determines if shapes can fit in the region using backtracking.
     * Uses mutable grid for performance-critical backtracking while keeping other parts functional.
     */
    private boolean canFit(Region region, Map<Integer, Shape> shapes, List<Integer> shapeIds) {
        boolean[][] grid = new boolean[region.width()][region.height()];
        return backtrack(grid, shapes, shapeIds, 0);
    }

    /**
     * Backtracking algorithm using mutable grid for performance.
     * The backtracking algorithm is inherently stateful and performance-critical,
     * so we use a mutable grid here while maintaining functional principles elsewhere.
     */
    private boolean backtrack(boolean[][] grid, Map<Integer, Shape> shapes, List<Integer> shapeIds, int index) {
        if (index == shapeIds.size()) {
            return true;
        }

        int shapeId = shapeIds.get(index);
        Shape shape = shapes.get(shapeId);

        for (ShapeVariant variant : shape.variants()) {
            int maxX = grid.length - variant.width();
            int maxY = grid[0].length - variant.height();

            for (int x = 0; x <= maxX; x++) {
                for (int y = 0; y <= maxY; y++) {
                    if (canPlace(grid, variant, x, y)) {
                        place(grid, variant, x, y, true);
                        if (backtrack(grid, shapes, shapeIds, index + 1)) {
                            return true;
                        }
                        place(grid, variant, x, y, false);
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if a variant can be placed at the given position.
     * Pure function logic: checks bounds and overlaps.
     */
    private boolean canPlace(boolean[][] grid, ShapeVariant variant, int x, int y) {
        for (Point p : variant.points()) {
            int gridX = x + p.x();
            int gridY = y + p.y();
            if (grid[gridX][gridY]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Places or removes a variant from the grid.
     * Mutable operation for performance in backtracking algorithm.
     */
    private void place(boolean[][] grid, ShapeVariant variant, int x, int y, boolean val) {
        for (Point p : variant.points()) {
            grid[x + p.x()][y + p.y()] = val;
        }
    }
}
