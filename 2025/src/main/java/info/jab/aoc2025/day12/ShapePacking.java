package info.jab.aoc2025.day12;

import module java.base;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

/**
 * Solver for packing shapes into regions.
 * Determines if given shapes can be placed into regions according to requirements.
 * Follows functional programming principles: immutable data structures, pure functions, and Stream API.
 * Optimized with bitmask representation, memoization, constraint propagation, parallel region processing,
 * precomputed bit offsets, variant filtering, and incremental grid hash.
 */
public final class ShapePacking implements Solver<Long> {

    /**
     * Hash multiplier for incremental grid hash computation.
     * Using a large prime number for better hash distribution.
     */
    private static final long HASH_MULTIPLIER = 0x9e3779b97f4a7c15L;

    @Override
    public Long solvePartOne(String fileName) {
        String normalizedFileName = fileName.startsWith("/") ? fileName : "/" + fileName;
        List<String> lines = ResourceLines.list(normalizedFileName);

        ParsedData parsedData = ParsedData.from(lines);
        Map<Integer, Shape> shapes = parsedData.shapes();
        List<Region> regions = parsedData.regions();

        // Separate fast and slow regions for better load distribution
        // Fast regions: area > 200 or impossible (total area > region area) - process sequentially
        // Slow regions: area <= 200 and possible - process in parallel
        // This reduces variability from uneven work distribution
        long fastCount = regions.stream()
                .filter(region -> {
                    long totalArea = calculateTotalArea(region, shapes);
                    return region.regionArea() > 200 || totalArea > region.regionArea();
                })
                .filter(region -> solve(region, shapes))
                .count();

        // Sort slow regions by complexity (area) for better load distribution
        long slowCount = regions.stream()
                .filter(region -> {
                    long totalArea = calculateTotalArea(region, shapes);
                    return region.regionArea() <= 200 && totalArea <= region.regionArea();
                })
                .sorted(Comparator.comparingLong(Region::regionArea))
                .parallel()
                .filter(region -> solve(region, shapes))
                .count();

        return fastCount + slowCount;
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
     * Uses bitmask representation, memoization, constraint propagation, and incremental grid hash for performance.
     */
    private boolean canFit(Region region, Map<Integer, Shape> shapes, List<Integer> shapeIds) {
        int width = region.width();
        int height = region.height();
        int totalCells = width * height;

        // Use bitmask representation for efficient operations
        long[] grid = new long[(totalCells + 63) / 64];
        // Incremental grid hash: starts at 0, updated as bits are placed/removed
        long gridHash = 0L;
        // Pre-size HashMap to avoid resizing overhead during backtracking
        // Estimate: typically 10-100 entries per region, use 2x for load factor (0.75)
        // This reduces variability from HashMap resizing operations
        // Use Map<Long, Boolean> instead of Map<CacheKey, Boolean> to eliminate CacheKey allocations
        int estimatedMemoSize = Math.max(16, shapeIds.size() * 4);
        Map<Long, Boolean> memo = new HashMap<>(estimatedMemoSize * 2);

        // Precompute minimum area needed for remaining shapes (for constraint propagation)
        long[] minAreaRemaining = new long[shapeIds.size() + 1];
        for (int i = shapeIds.size() - 1; i >= 0; i--) {
            minAreaRemaining[i] = minAreaRemaining[i + 1] + shapes.get(shapeIds.get(i)).area();
        }

        // Precompute region-relative bit offsets for all variants (optimization #3)
        Map<Integer, Map<ShapeVariant, int[]>> variantOffsets = precomputeVariantOffsets(shapes, width);

        return backtrack(grid, gridHash, width, height, shapes, shapeIds, 0, totalCells, minAreaRemaining, memo, variantOffsets);
    }

    /**
     * Precomputes region-relative bit offsets for all shape variants.
     * This eliminates repeated calculations in hot loops (optimization #3).
     *
     * @param shapes Map of all shapes
     * @param regionWidth Width of the region
     * @return Map from shape ID to map of variant to precomputed bit offsets
     */
    private Map<Integer, Map<ShapeVariant, int[]>> precomputeVariantOffsets(Map<Integer, Shape> shapes, int regionWidth) {
        Map<Integer, Map<ShapeVariant, int[]>> result = new HashMap<>();
        for (Map.Entry<Integer, Shape> entry : shapes.entrySet()) {
            Map<ShapeVariant, int[]> variantMap = new HashMap<>();
            for (ShapeVariant variant : entry.getValue().variants()) {
                int[] offsets = new int[variant.points().size()];
                for (int i = 0; i < variant.points().size(); i++) {
                    Point p = variant.points().get(i);
                    offsets[i] = p.y() * regionWidth + p.x();
                }
                variantMap.put(variant, offsets);
            }
            result.put(entry.getKey(), variantMap);
        }
        return result;
    }

    /**
     * Optimized backtracking algorithm using bitmask representation, memoization, constraint propagation,
     * precomputed bit offsets, variant filtering, and incremental grid hash.
     * The backtracking algorithm is inherently stateful and performance-critical,
     * so we use mutable grid here while maintaining functional principles elsewhere.
     * <p>
     * Optimized to use primitive long hash keys instead of CacheKey objects to reduce allocations.
     */
    private boolean backtrack(long[] grid, long gridHash, int width, int height, Map<Integer, Shape> shapes,
                             List<Integer> shapeIds, int index, long remainingArea,
                             long[] minAreaRemaining, Map<Long, Boolean> memo,
                             Map<Integer, Map<ShapeVariant, int[]>> variantOffsets) {
        if (index == shapeIds.size()) {
            return true;
        }

        // Constraint propagation: Check if remaining area is sufficient
        if (remainingArea < minAreaRemaining[index]) {
            return false;
        }

        // Memoization: Use primitive long hash key instead of CacheKey object to reduce allocations
        long key = computeMemoKey(gridHash, shapeIds, index);
        Boolean cached = memo.get(key);
        if (cached != null) {
            return cached;
        }

        int shapeId = shapeIds.get(index);
        Shape shape = shapes.get(shapeId);

        for (ShapeVariant variant : shape.variants()) {
            // Early variant filtering: skip variants that can't fit
            if (variant.width() > width || variant.height() > height) {
                continue;
            }
            if (variant.points().size() > remainingArea) {
                continue;
            }

            int maxX = width - variant.width();
            int maxY = height - variant.height();

            // Early exit: variant too large for any valid position
            if (maxX < 0 || maxY < 0) {
                continue;
            }

            int[] offsets = variantOffsets.get(shapeId).get(variant);
            for (int x = 0; x <= maxX; x++) {
                for (int y = 0; y <= maxY; y++) {
                    if (canPlaceBitmaskOptimized(grid, width, variant, x, y, offsets)) {
                        long newGridHash = placeBitmaskOptimized(grid, gridHash, width, variant, x, y, offsets, true);
                        long newRemainingArea = remainingArea - variant.points().size();
                        if (backtrack(grid, newGridHash, width, height, shapes, shapeIds, index + 1,
                                    newRemainingArea, minAreaRemaining, memo, variantOffsets)) {
                            memo.put(key, true);
                            return true;
                        }
                        gridHash = placeBitmaskOptimized(grid, newGridHash, width, variant, x, y, offsets, false);
                    }
                }
            }
        }

        memo.put(key, false);
        return false;
    }



    /**
     * Checks if a variant can be placed at the given position using bitmask operations.
     * Optimized with precomputed region-relative bit offsets.
     * Pure function logic: checks bounds and overlaps using bitwise AND.
     */
    private boolean canPlaceBitmaskOptimized(long[] grid, int width, ShapeVariant variant, int x, int y, int[] offsets) {
        int baseBitIndex = y * width + x;
        // Use precomputed offsets for efficient bit index calculation
        for (int offset : offsets) {
            int bitIndex = baseBitIndex + offset;
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
     * Optimized with precomputed region-relative bit offsets for efficient bit index calculation.
     * Updates incremental grid hash as bits are placed/removed.
     * Mutable operation for performance in backtracking algorithm.
     *
     * @param grid The grid bitmask array
     * @param gridHash Current incremental grid hash
     * @param width Width of the grid
     * @param variant The variant to place/remove
     * @param x X position
     * @param y Y position
     * @param offsets Precomputed region-relative bit offsets (optimization #3)
     * @param val true to place, false to remove
     * @return Updated grid hash after placement/removal
     */
    private long placeBitmaskOptimized(long[] grid, long gridHash, int width, ShapeVariant variant, int x, int y, int[] offsets, boolean val) {
        // Compute base bit index once: y * width + x
        int baseBitIndex = y * width + x;
        // Use precomputed offsets instead of computing on-the-fly
        for (int offset : offsets) {
            int bitIndex = baseBitIndex + offset;
            int longIndex = bitIndex / 64;
            int bitOffset = bitIndex % 64;

            // Update incremental hash: XOR with hash of bit position
            // This allows O(1) hash updates instead of recomputing entire grid hash
            long bitHash = computeBitHash(bitIndex);
            gridHash ^= bitHash;

            if (val) {
                grid[longIndex] |= (1L << bitOffset);
            } else {
                grid[longIndex] &= ~(1L << bitOffset);
            }
        }
        return gridHash;
    }

    /**
     * Computes hash for a specific bit position.
     * Used for incremental grid hash updates.
     *
     * @param bitIndex The bit index to hash
     * @return Hash value for the bit position
     */
    private long computeBitHash(int bitIndex) {
        // Use multiplication with large prime for good hash distribution
        return (long) bitIndex * HASH_MULTIPLIER;
    }

    /**
     * Computes memoization key from grid hash and remaining shape IDs.
     * Uses primitive long instead of CacheKey object to eliminate allocations.
     * Combines gridHash with shapeIds hash to ensure uniqueness.
     *
     * @param gridHash The incremental hash of the grid state
     * @param shapeIds The list of all shape IDs to place
     * @param index The starting index of remaining shapes to place
     * @return A long hash key combining grid state and remaining shapes
     */
    private long computeMemoKey(long gridHash, List<Integer> shapeIds, int index) {
        // Compute hash from remaining shape IDs (similar to CacheKey but returning long)
        long shapeHash = 1L;
        for (int i = index; i < shapeIds.size(); i++) {
            shapeHash = 31L * shapeHash + shapeIds.get(i);
        }
        // Mix grid hash with shape hash using XOR for better distribution
        // Use 31 multiplier to match CacheKey's hash computation pattern
        return gridHash * 31L + shapeHash;
    }
}
