package info.jab.aoc2025.day12;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a shape with all its rotation and reflection variants.
 * A shape is defined by its ID, area, and all possible variants (rotations and flips).
 * Immutable record following functional programming principles.
 *
 * @param id The unique identifier of the shape
 * @param area The number of cells occupied by the shape
 * @param variants List of all unique rotation and reflection variants of the shape
 */
public record Shape(int id, int area, List<ShapeVariant> variants) {
    /**
     * Compact constructor ensuring immutability through defensive copying.
     */
    public Shape {
        // Defensive copy to ensure immutability
        variants = List.copyOf(variants);
    }

    /**
     * Static factory method that creates a Shape from an ID and list of lines.
     * Pure function: depends only on input parameters, no side effects.
     * Follows the "Consider static factory methods instead of constructors" principle.
     *
     * @param id The unique identifier of the shape
     * @param lines The lines representing the shape (using '#' for filled cells)
     * @return A Shape record with parsed points and generated variants
     */
    public static Shape from(int id, List<String> lines) {
        List<Point> points = new ArrayList<>();
        int calculatedArea = 0;
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '#') {
                    points.add(new Point(x, y));
                    calculatedArea++;
                }
            }
        }
        List<ShapeVariant> variants = generateVariants(points);
        return new Shape(id, calculatedArea, variants);
    }

    private static List<ShapeVariant> generateVariants(List<Point> original) {
        Set<Set<Point>> uniqueVariants = new HashSet<>();
        List<ShapeVariant> result = new ArrayList<>();

        List<Point> current = original;
        List<Point> flipped = flip(original);
        List<List<Point>> bases = List.of(original, flipped);

        for (List<Point> base : bases) {
            current = base;
            for (int j = 0; j < 4; j++) {
                ShapeVariant v = ShapeVariant.from(current);
                if (uniqueVariants.add(new HashSet<>(v.points()))) {
                    result.add(v);
                }
                current = rotate90(current);
            }
        }
        return result;
    }

    private static List<Point> rotate90(List<Point> points) {
        List<Point> newPoints = new ArrayList<>();
        for (Point p : points) {
            newPoints.add(new Point(-p.y(), p.x()));
        }
        return newPoints;
    }

    private static List<Point> flip(List<Point> points) {
        List<Point> newPoints = new ArrayList<>();
        for (Point p : points) {
            newPoints.add(new Point(-p.x(), p.y()));
        }
        return newPoints;
    }
}
