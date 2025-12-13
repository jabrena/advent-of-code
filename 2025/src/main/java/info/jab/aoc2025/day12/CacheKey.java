package info.jab.aoc2025.day12;

import java.util.Arrays;
import java.util.List;

/**
 * Cache key for memoization in shape packing backtracking.
 * Represents grid state and remaining shapes to place.
 * Optimized to avoid grid cloning: computes hash directly from grid array.
 * Immutable class following functional programming principles.
 */
public final class CacheKey {
    private final long[] grid;
    private final int[] remainingShapeIds;
    private final int hashCode;

    /**
     * Creates a cache key from grid state and remaining shapes.
     * Optimized: computes hash directly from grid without cloning.
     *
     * @param grid The bitmask representation of the grid state
     * @param shapeIds The list of all shape IDs to place
     * @param index The starting index of remaining shapes to place
     */
    public CacheKey(long[] grid, List<Integer> shapeIds, int index) {
        // OPTIMIZATION: Don't clone grid - compute hash directly from it
        // We still need to store grid reference for equals(), but avoid expensive clone()
        this.grid = grid;
        // Store only remaining shapes for efficient comparison
        this.remainingShapeIds = new int[shapeIds.size() - index];
        for (int i = index; i < shapeIds.size(); i++) {
            this.remainingShapeIds[i - index] = shapeIds.get(i);
        }
        this.hashCode = computeHashCode();
    }

    /**
     * Computes hash code directly from grid array without cloning.
     * Uses Arrays.hashCode() for efficient hash computation.
     */
    private int computeHashCode() {
        int result = Arrays.hashCode(remainingShapeIds);
        // Compute hash from grid array directly (no clone needed)
        result = 31 * result + Arrays.hashCode(grid);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheKey cacheKey = (CacheKey) o;
        // Compare arrays directly using Arrays.equals() - no clone needed
        return Arrays.equals(remainingShapeIds, cacheKey.remainingShapeIds)
                && Arrays.equals(grid, cacheKey.grid);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}

