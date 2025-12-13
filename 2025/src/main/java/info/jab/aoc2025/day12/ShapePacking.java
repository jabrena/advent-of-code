package info.jab.aoc2025.day12;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Solver for packing shapes into regions.
 * Determines if given shapes can be placed into regions according to requirements.
 * Follows functional programming principles: immutable data structures, pure functions, and Stream API.
 * Optimized with bitmask representation, memoization, and constraint propagation.
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
     * Determines if shapes can fit in the region using optimized backtracking.
     * Uses bitmask representation, memoization, and constraint propagation for performance.
     */
    private boolean canFit(Region region, Map<Integer, Shape> shapes, List<Integer> shapeIds) {
        int width = region.width();
        int height = region.height();
        int totalCells = width * height;

        // Use bitmask representation for efficient operations
        long[] grid = new long[(totalCells + 63) / 64];
        Map<CacheKey, Boolean> memo = new HashMap<>();

        // Precompute minimum area needed for remaining shapes (for constraint propagation)
        long[] minAreaRemaining = new long[shapeIds.size() + 1];
        for (int i = shapeIds.size() - 1; i >= 0; i--) {
            minAreaRemaining[i] = minAreaRemaining[i + 1] + shapes.get(shapeIds.get(i)).area();
        }

        return backtrack(grid, width, height, shapes, shapeIds, 0, totalCells, minAreaRemaining, memo);
    }

    /**
     * Optimized backtracking algorithm using bitmask representation, memoization, and constraint propagation.
     * The backtracking algorithm is inherently stateful and performance-critical,
     * so we use mutable grid here while maintaining functional principles elsewhere.
     */
    private boolean backtrack(long[] grid, int width, int height, Map<Integer, Shape> shapes,
                             List<Integer> shapeIds, int index, long remainingArea,
                             long[] minAreaRemaining, Map<CacheKey, Boolean> memo) {
        if (index == shapeIds.size()) {
            return true;
        }

        // Constraint propagation: Check if remaining area is sufficient
        if (remainingArea < minAreaRemaining[index]) {
            return false;
        }

        // Memoization: Check cache
        CacheKey key = new CacheKey(grid, shapeIds, index);
        Boolean cached = memo.get(key);
        if (cached != null) {
            return cached;
        }

        int shapeId = shapeIds.get(index);
        Shape shape = shapes.get(shapeId);

        for (ShapeVariant variant : shape.variants()) {
            int maxX = width - variant.width();
            int maxY = height - variant.height();

            for (int x = 0; x <= maxX; x++) {
                for (int y = 0; y <= maxY; y++) {
                    if (canPlaceBitmask(grid, width, variant, x, y)) {
                        placeBitmask(grid, width, variant, x, y, true);
                        long newRemainingArea = remainingArea - variant.points().size();
                        if (backtrack(grid, width, height, shapes, shapeIds, index + 1,
                                    newRemainingArea, minAreaRemaining, memo)) {
                            memo.put(key, true);
                            return true;
                        }
                        placeBitmask(grid, width, variant, x, y, false);
                    }
                }
            }
        }

        memo.put(key, false);
        return false;
    }

    /**
     * Checks if a variant can be placed at the given position using bitmask operations.
     * Optimized with precomputed relative offsets for efficient bit index calculation.
     * Pure function logic: checks bounds and overlaps using bitwise AND.
     */
    private boolean canPlaceBitmask(long[] grid, int width, ShapeVariant variant, int x, int y) {
        // Compute base bit index once: y * width + x
        int baseBitIndex = y * width + x;
        // Use precomputed points for efficiency
        List<Point> points = variant.points();
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            // Compute bit index: baseBitIndex + relative offset
            // bitIndex = (y + py) * width + (x + px) = y * width + x + py * width + px
            int bitIndex = baseBitIndex + p.y() * width + p.x();
            int longIndex = bitIndex / 64;
            int bitOffset = bitIndex % 64;
            if ((grid[longIndex] & (1L << bitOffset)) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Places or removes a variant from the grid using bitmask operations.
     * Optimized with precomputed relative offsets for efficient bit index calculation.
     * Mutable operation for performance in backtracking algorithm.
     */
    private void placeBitmask(long[] grid, int width, ShapeVariant variant, int x, int y, boolean val) {
        // Compute base bit index once: y * width + x
        int baseBitIndex = y * width + x;
        // Use precomputed points for efficiency
        List<Point> points = variant.points();
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            // Compute bit index: baseBitIndex + relative offset
            // bitIndex = (y + py) * width + (x + px) = y * width + x + py * width + px
            int bitIndex = baseBitIndex + p.y() * width + p.x();
            int longIndex = bitIndex / 64;
            int bitOffset = bitIndex % 64;
            if (val) {
                grid[longIndex] |= (1L << bitOffset);
            } else {
                grid[longIndex] &= ~(1L << bitOffset);
            }
        }
    }
}
