package info.jab.aoc2025.day12;

import module java.base;

/**
 * Represents a variant of a shape with normalized coordinates.
 * A shape variant is a normalized representation of a shape (translated to origin)
 * with its bounding box dimensions.
 * Immutable record following functional programming principles.
 * Optimized with precomputed bit offsets and bitmasks for efficient placement operations.
 *
 * @param points The normalized points (translated to origin)
 * @param width The width of the bounding box
 * @param height The height of the bounding box
 * @param bitOffsets Precomputed bit offsets for each point: offset = py * width + px
 * @param bitmaskChunks Precomputed bitmask chunks for fast overlap checks (one long per 64 cells)
 * @param bitmaskLongIndices Array mapping each point to its long index in bitmaskChunks
 */
public record ShapeVariant(
        List<Point> points,
        int width,
        int height,
        int[] bitOffsets,
        long[] bitmaskChunks,
        int[] bitmaskLongIndices) {
    /**
     * Compact constructor ensuring immutability through defensive copying.
     */
    public ShapeVariant {
        // Defensive copy to ensure immutability
        points = List.copyOf(points);
        // bitOffsets, bitmaskChunks, and bitmaskLongIndices are already arrays (mutable internally but treated as immutable)
    }

    /**
     * Static factory method that creates a ShapeVariant from a list of points.
     * Normalizes the points to origin and calculates bounding box dimensions.
     * Precomputes bit offsets and bitmasks for efficient placement operations.
     * Pure function: depends only on input parameter, no side effects.
     * Follows the "Consider static factory methods instead of constructors" principle.
     *
     * @param inputPoints The points to normalize
     * @return A ShapeVariant record with normalized points and calculated dimensions
     */
    public static ShapeVariant from(List<Point> inputPoints) {
        if (inputPoints.isEmpty()) {
            return new ShapeVariant(List.of(), 0, 0, new int[0], new long[0], new int[0]);
        }

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Point p : inputPoints) {
            minX = Math.min(minX, p.x());
            minY = Math.min(minY, p.y());
            maxX = Math.max(maxX, p.x());
            maxY = Math.max(maxY, p.y());
        }

        List<Point> normalized = new ArrayList<>();
        for (Point p : inputPoints) {
            normalized.add(new Point(p.x() - minX, p.y() - minY));
        }

        int calculatedWidth = maxX - minX + 1;
        int calculatedHeight = maxY - minY + 1;

        // Precompute bit offsets: offset = py * width + px
        int[] bitOffsets = new int[normalized.size()];
        for (int i = 0; i < normalized.size(); i++) {
            Point p = normalized.get(i);
            bitOffsets[i] = p.y() * calculatedWidth + p.x();
        }

        // Precompute bitmask chunks for fast overlap checks
        int totalCells = calculatedWidth * calculatedHeight;
        int numLongs = (totalCells + 63) / 64;
        long[] bitmaskChunks = new long[numLongs];
        int[] bitmaskLongIndices = new int[normalized.size()];

        for (int i = 0; i < normalized.size(); i++) {
            int bitIndex = bitOffsets[i];
            int longIndex = bitIndex / 64;
            int bitOffset = bitIndex % 64;
            bitmaskChunks[longIndex] |= (1L << bitOffset);
            bitmaskLongIndices[i] = longIndex;
        }

        return new ShapeVariant(normalized, calculatedWidth, calculatedHeight,
                bitOffsets, bitmaskChunks, bitmaskLongIndices);
    }
}
