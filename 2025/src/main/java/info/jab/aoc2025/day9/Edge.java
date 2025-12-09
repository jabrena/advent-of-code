package info.jab.aoc2025.day9;

/**
 * Represents an edge connecting two points.
 * Immutable record following functional programming principles.
 *
 * @param p1 The first point
 * @param p2 The second point
 */
public record Edge(Point p1, Point p2) {
    /**
     * Checks if the edge is vertical (both points have the same x coordinate).
     * Pure function: depends only on the edge's points, no side effects.
     *
     * @return true if the edge is vertical, false otherwise
     */
    public boolean isVertical() {
        return p1.x() == p2.x();
    }
}

