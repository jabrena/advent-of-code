package info.jab.aoc2025.day12;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a variant of a shape with normalized coordinates.
 * A shape variant is a normalized representation of a shape (translated to origin)
 * with its bounding box dimensions.
 * Immutable record following functional programming principles.
 *
 * @param points The normalized points (translated to origin)
 * @param width The width of the bounding box
 * @param height The height of the bounding box
 */
public record ShapeVariant(List<Point> points, int width, int height) {
    /**
     * Compact constructor ensuring immutability through defensive copying.
     */
    public ShapeVariant {
        // Defensive copy to ensure immutability
        points = List.copyOf(points);
    }

    /**
     * Static factory method that creates a ShapeVariant from a list of points.
     * Normalizes the points to origin and calculates bounding box dimensions.
     * Pure function: depends only on input parameter, no side effects.
     * Follows the "Consider static factory methods instead of constructors" principle.
     *
     * @param inputPoints The points to normalize
     * @return A ShapeVariant record with normalized points and calculated dimensions
     */
    public static ShapeVariant from(List<Point> inputPoints) {
        if (inputPoints.isEmpty()) {
            return new ShapeVariant(List.of(), 0, 0);
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

        return new ShapeVariant(normalized, calculatedWidth, calculatedHeight);
    }
}
