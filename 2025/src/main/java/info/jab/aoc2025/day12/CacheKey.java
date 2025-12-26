package info.jab.aoc2025.day12;

import module java.base;

/**
 * Cache key for memoization in shape packing backtracking.
 * Represents grid state and remaining shapes to place.
 * Optimized with incremental grid hash: uses hash for fast comparison, but stores grid reference
 * for collision handling to ensure correctness.
 * Immutable class following functional programming principles.
 */
public final class CacheKey {
    private final long[] grid;
    private final long gridHash;
    private final int[] remainingShapeIds;
    private final int hashCode;

    /**
     * Creates a cache key from grid state and remaining shapes.
     * Optimized: uses incremental hash for fast comparison, but stores grid reference
     * for handling hash collisions.
     *
     * @param gridHash The incremental hash of the grid state
     * @param grid The bitmask representation of the grid state (for collision handling)
     * @param shapeIds The list of all shape IDs to place
     * @param index The starting index of remaining shapes to place
     */
    public CacheKey(long gridHash, long[] grid, List<Integer> shapeIds, int index) {
        this.gridHash = gridHash;
        this.grid = grid; // Store reference for collision handling (no clone needed)
        // Store only remaining shapes for efficient comparison
        this.remainingShapeIds = new int[shapeIds.size() - index];
        for (int i = index; i < shapeIds.size(); i++) {
            this.remainingShapeIds[i - index] = shapeIds.get(i);
        }
        this.hashCode = computeHashCode();
    }

    /**
     * Computes hash code from incremental grid hash and remaining shape IDs.
     * Much faster than computing hash from full grid array.
     */
    private int computeHashCode() {
        int result = Arrays.hashCode(remainingShapeIds);
        // Mix grid hash into result (using XOR for better distribution)
        result = 31 * result + (int) (gridHash ^ (gridHash >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheKey cacheKey = (CacheKey) o;
        // Fast path: compare hash first (most comparisons will succeed here)
        if (gridHash != cacheKey.gridHash) {
            return false;
        }
        // Hash matches: compare remaining shapes
        if (!Arrays.equals(remainingShapeIds, cacheKey.remainingShapeIds)) {
            return false;
        }
        // Hash collision check: compare actual grid arrays
        // This handles rare hash collisions while maintaining performance
        return Arrays.equals(grid, cacheKey.grid);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}

