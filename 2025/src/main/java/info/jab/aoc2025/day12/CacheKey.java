package info.jab.aoc2025.day12;

import java.util.List;

/**
 * Cache key for memoization in shape packing backtracking.
 * Represents grid state and remaining shapes to place.
 * Uses efficient representation: grid bitmask and remaining shape IDs starting from index.
 * Immutable class following functional programming principles.
 */
public final class CacheKey {
    private final long[] grid;
    private final int[] remainingShapeIds;
    private final int hashCode;

    /**
     * Creates a cache key from grid state and remaining shapes.
     *
     * @param grid The bitmask representation of the grid state
     * @param shapeIds The list of all shape IDs to place
     * @param index The starting index of remaining shapes to place
     */
    public CacheKey(long[] grid, List<Integer> shapeIds, int index) {
        this.grid = grid.clone();
        // Store only remaining shapes for efficient comparison
        this.remainingShapeIds = new int[shapeIds.size() - index];
        for (int i = index; i < shapeIds.size(); i++) {
            this.remainingShapeIds[i - index] = shapeIds.get(i);
        }
        this.hashCode = computeHashCode();
    }

    private int computeHashCode() {
        int result = remainingShapeIds.length;
        for (int id : remainingShapeIds) {
            result = 31 * result + id;
        }
        for (long l : grid) {
            result = 31 * result + Long.hashCode(l);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheKey cacheKey = (CacheKey) o;
        if (grid.length != cacheKey.grid.length) return false;
        if (remainingShapeIds.length != cacheKey.remainingShapeIds.length) return false;
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] != cacheKey.grid[i]) return false;
        }
        for (int i = 0; i < remainingShapeIds.length; i++) {
            if (remainingShapeIds[i] != cacheKey.remainingShapeIds[i]) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}

