package info.jab.aoc2025.day12;

import module java.base;

/**
 * Cache key for memoization in shape packing backtracking.
 * Represents grid state and remaining shapes to place.
 * Optimized with incremental grid hash: uses hash for fast comparison, but stores grid reference
 * for collision handling to ensure correctness.
 * Immutable class following functional programming principles.
 * 
 * Performance optimization: Computes hash directly from list without array allocation.
 * Array is only created lazily when needed for equality comparison (hash collision case).
 */
public final class CacheKey {
    private final long[] grid;
    private final long gridHash;
    private final List<Integer> shapeIds;
    private final int index;
    private final int hashCode;

    /**
     * Creates a cache key from grid state and remaining shapes.
     * Optimized: computes hash directly from list without array allocation.
     * Array is only created lazily when needed for equality comparison.
     *
     * @param gridHash The incremental hash of the grid state
     * @param grid The bitmask representation of the grid state (for collision handling)
     * @param shapeIds The list of all shape IDs to place
     * @param index The starting index of remaining shapes to place
     */
    public CacheKey(long gridHash, long[] grid, List<Integer> shapeIds, int index) {
        this.gridHash = gridHash;
        this.grid = grid; // Store reference for collision handling (no clone needed)
        this.shapeIds = shapeIds;
        this.index = index;
        this.hashCode = computeHashCode();
    }

    /**
     * Computes hash code from incremental grid hash and remaining shape IDs.
     * Computes hash directly from list without array allocation for better performance.
     */
    private int computeHashCode() {
        // Compute hash directly from list without creating array
        int result = 1;
        for (int i = index; i < shapeIds.size(); i++) {
            result = 31 * result + shapeIds.get(i);
        }
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
        // Use list comparison first (fast path), only create arrays if lists differ
        if (shapeIds.size() - index != cacheKey.shapeIds.size() - cacheKey.index) {
            return false;
        }
        for (int i = 0; i < shapeIds.size() - index; i++) {
            if (!shapeIds.get(index + i).equals(cacheKey.shapeIds.get(cacheKey.index + i))) {
                return false;
            }
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

