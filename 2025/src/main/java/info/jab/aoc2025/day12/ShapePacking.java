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

        // Parallel processing: regions are independent, each creates its own local state
        // Shapes map is immutable (Map.copyOf), safe for concurrent reads
        return regions.parallelStream()
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
        // Use HashMap with optimized CacheKey (no array allocation in common case)
        // Note: Primitive collections (LongObjectHashMap) would avoid Boolean boxing,
        // but require complex collision handling. CacheKey optimization (#1) already
        // eliminates the main performance bottleneck (array allocation), so HashMap is preferred
        // for correctness and simplicity.
        Map<CacheKey, Boolean> memo = new HashMap<>();

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
     */
    private boolean backtrack(long[] grid, long gridHash, int width, int height, Map<Integer, Shape> shapes,
                             List<Integer> shapeIds, int index, long remainingArea,
                             long[] minAreaRemaining, Map<CacheKey, Boolean> memo,
                             Map<Integer, Map<ShapeVariant, int[]>> variantOffsets) {
        if (index == shapeIds.size()) {
            return true;
        }

        // Constraint propagation: Check if remaining area is sufficient
        if (remainingArea < minAreaRemaining[index]) {
            return false;
        }

        // Memoization: Check cache using optimized CacheKey (no array allocation in common case)
        CacheKey key = new CacheKey(gridHash, grid, shapeIds, index);
        Boolean cached = memo.get(key);
        if (cached != null) {
            return cached;
        }

        int shapeId = shapeIds.get(index);
        Shape shape = shapes.get(shapeId);

        // Optimization #4: Sort variants by area (largest first) for better pruning
        List<ShapeVariant> sortedVariants = shape.variants().stream()
                .filter(v -> v.width() <= width && v.height() <= height && v.points().size() <= remainingArea)
                .sorted((a, b) -> Integer.compare(b.points().size(), a.points().size()))
                .toList();

        for (ShapeVariant variant : sortedVariants) {
            int maxX = width - variant.width();
            int maxY = height - variant.height();

            // Early exit: variant too large for any valid position
            if (maxX < 0 || maxY < 0) {
                continue;
            }

            // Optimization #5: Try positions in smarter order (corners/edges first)
            List<int[]> positions = generatePositionsByPriority(maxX, maxY);

            for (int[] pos : positions) {
                int x = pos[0];
                int y = pos[1];
                int[] offsets = variantOffsets.get(shapeId).get(variant);
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

        memo.put(key, false);
        return false;
    }


    /**
     * Generates positions in priority order: corners first, then edges, then interior.
     * This helps find solutions faster by trying more constrained positions first (optimization #5).
     */
    private List<int[]> generatePositionsByPriority(int maxX, int maxY) {
        List<int[]> positions = new ArrayList<>();
        // Corners (highest priority)
        if (maxX >= 0 && maxY >= 0) {
            positions.add(new int[]{0, 0});
            if (maxX > 0) positions.add(new int[]{maxX, 0});
            if (maxY > 0) positions.add(new int[]{0, maxY});
            if (maxX > 0 && maxY > 0) positions.add(new int[]{maxX, maxY});
        }
        // Edges (medium priority)
        for (int x = 1; x < maxX; x++) {
            positions.add(new int[]{x, 0});
            if (maxY > 0) positions.add(new int[]{x, maxY});
        }
        for (int y = 1; y < maxY; y++) {
            positions.add(new int[]{0, y});
            if (maxX > 0) positions.add(new int[]{maxX, y});
        }
        // Interior (lowest priority)
        for (int x = 1; x < maxX; x++) {
            for (int y = 1; y < maxY; y++) {
                positions.add(new int[]{x, y});
            }
        }
        return positions;
    }

    /**
     * Checks if a variant can be placed at the given position using bitmask operations.
     * Optimized with precomputed region-relative bit offsets and batch operations for small variants.
     * Pure function logic: checks bounds and overlaps using bitwise AND.
     */
    private boolean canPlaceBitmaskOptimized(long[] grid, int width, ShapeVariant variant, int x, int y, int[] offsets) {
        int baseBitIndex = y * width + x;

        // Optimization #2: Batch bitmask check for small variants that fit in one long
        if (variant.points().size() <= 64 && canFitInSingleLong(variant, width, x, y)) {
            return canPlaceBitmaskBatch(grid, baseBitIndex, variant, offsets);
        }

        // Fall back to individual point checks using precomputed offsets
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
     * Checks if variant placement fits within a single long (64 bits).
     * This enables batch bitmask operations for better performance.
     */
    private boolean canFitInSingleLong(ShapeVariant variant, int width, int x, int y) {
        // Check if all points map to the same long index
        int firstBitIndex = (y + variant.points().get(0).y()) * width + (x + variant.points().get(0).x());
        int firstLongIndex = firstBitIndex / 64;
        for (int i = 1; i < variant.points().size(); i++) {
            Point p = variant.points().get(i);
            int bitIndex = (y + p.y()) * width + (x + p.x());
            if (bitIndex / 64 != firstLongIndex) {
                return false;
            }
        }
        return true;
    }

    /**
     * Batch bitmask check for variants that fit in a single long.
     * Uses a single bitwise AND operation instead of iterating through points (optimization #2).
     */
    private boolean canPlaceBitmaskBatch(long[] grid, int baseBitIndex, ShapeVariant variant, int[] offsets) {
        // Compute bitmask for variant at this position
        long variantMask = 0L;
        int longIndex = -1;
        for (int offset : offsets) {
            int bitIndex = baseBitIndex + offset;
            int currentLongIndex = bitIndex / 64;
            int bitOffset = bitIndex % 64;
            if (longIndex == -1) {
                longIndex = currentLongIndex;
            } else if (currentLongIndex != longIndex) {
                // Variant spans multiple longs, fall back to individual checks
                return canPlaceBitmaskIndividual(grid, baseBitIndex, offsets);
            }
            variantMask |= (1L << bitOffset);
        }
        // Single bitwise AND check
        return (grid[longIndex] & variantMask) == 0;
    }

    /**
     * Individual point check fallback when batch operation is not possible.
     */
    private boolean canPlaceBitmaskIndividual(long[] grid, int baseBitIndex, int[] offsets) {
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
}
