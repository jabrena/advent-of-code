package info.jab.aoc2025.day9;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Solver for finding maximum rectangle areas.
 * Part 1: Finds the maximum area rectangle formed by any two points.
 * Part 2: Finds the maximum area rectangle that fits inside a polygon formed by the points.
 *
 * This implementation follows functional programming principles:
 * - Uses Stream API for declarative transformations
 * - Employs immutable collections where possible
 * - Separates pure functions from I/O operations
 */
public final class MaxRectangleArea implements Solver<Long> {

    /**
     * Finds the maximum area rectangle formed by any two points.
     * Pure transformation after I/O boundary.
     *
     * @param fileName The input file name
     * @return The maximum area rectangle
     */
    @Override
    public Long solvePartOne(final String fileName) {
        final List<Point> points = parsePoints(fileName);
        return findMaxRectangleArea(points);
    }

    /**
     * Pure function that finds the maximum area rectangle formed by any two points.
     * Uses Stream API for declarative pair generation and area calculation.
     *
     * @param points The list of points
     * @return The maximum area rectangle
     */
    private Long findMaxRectangleArea(final List<Point> points) {
        return IntStream.range(0, points.size())
                .boxed()
                .flatMap(i -> IntStream.range(i + 1, points.size())
                        .mapToObj(j -> calculateArea(points.get(i), points.get(j))))
                .max(Long::compareTo)
                .orElse(0L);
    }

    /**
     * Pure function that calculates the area of a rectangle formed by two points.
     *
     * @param p1 The first point
     * @param p2 The second point
     * @return The area of the rectangle
     */
    private long calculateArea(final Point p1, final Point p2) {
        return (Math.abs((long) p1.x() - p2.x()) + 1L) * (Math.abs((long) p1.y() - p2.y()) + 1L);
    }

    /**
     * Finds the maximum area rectangle that fits inside a polygon formed by the points.
     * The rectangle must be completely contained within the polygon without intersecting edges.
     * Pure transformation after I/O boundary.
     *
     * @param fileName The input file name
     * @return The maximum area rectangle that fits inside the polygon
     */
    @Override
    public Long solvePartTwo(final String fileName) {
        final List<Point> points = parsePoints(fileName);
        final List<Edge> edges = buildPolygonEdges(points);
        return findMaxRectangleInPolygon(points, edges);
    }

    /**
     * Pure function that builds polygon edges from a list of points.
     * Uses Stream API to create an immutable list of edges.
     *
     * @param points The list of points forming the polygon
     * @return Immutable list of edges
     */
    private List<Edge> buildPolygonEdges(final List<Point> points) {
        if (points.isEmpty()) {
            return List.of();
        }
        return IntStream.range(0, points.size())
                .mapToObj(i -> createEdge(points, i))
                .toList();
    }

    /**
     * Pure function that creates an edge from a point and its next point (wrapping around).
     *
     * @param points The list of points
     * @param index The index of the first point
     * @return An edge connecting the point at index to the next point
     */
    private Edge createEdge(final List<Point> points, final int index) {
        final Point p1 = points.get(index);
        final Point p2 = points.get((index + 1) % points.size());
        return new Edge(p1, p2);
    }

    /**
     * Pure function that finds the maximum area rectangle that fits inside a polygon.
     * Uses Stream API for declarative pair generation and filtering.
     * Optimized with parallel processing for independent pair validations.
     *
     * @param points The list of points
     * @param edges The edges of the polygon
     * @return The maximum area rectangle that fits inside the polygon
     */
    private Long findMaxRectangleInPolygon(final List<Point> points, final List<Edge> edges) {
        // Pre-compute vertical edges for faster ray casting
        final List<Edge> verticalEdges = edges.stream()
                .filter(Edge::isVertical)
                .toList();
        
        return IntStream.range(0, points.size())
                .boxed()
                .parallel() // Parallel processing for independent pair validations
                .flatMap(i -> IntStream.range(i + 1, points.size())
                        .mapToObj(j -> new PointPair(points.get(i), points.get(j))))
                .map(pair -> calculateValidArea(pair, edges, verticalEdges))
                .filter(area -> area > 0)
                .max(Long::compareTo)
                .orElse(0L);
    }

    /**
     * Record representing a pair of points for rectangle calculation.
     * Immutable data structure for functional programming.
     *
     * @param p1 The first point
     * @param p2 The second point
     */
    private record PointPair(Point p1, Point p2) {}

    /**
     * Pure function that calculates the area of a rectangle if it's valid within the polygon.
     * Returns 0 if the rectangle is invalid.
     * Optimized with pre-computed vertical edges and combined point-in-polygon check.
     *
     * @param pair The pair of points
     * @param edges The edges of the polygon
     * @param verticalEdges Pre-computed list of vertical edges for ray casting
     * @return The area if valid, 0 otherwise
     */
    private long calculateValidArea(final PointPair pair, final List<Edge> edges, final List<Edge> verticalEdges) {
        final long width = Math.abs((long) pair.p1().x() - pair.p2().x()) + 1L;
        final long height = Math.abs((long) pair.p1().y() - pair.p2().y()) + 1L;
        final long area = width * height;

        final int xMin = Math.min(pair.p1().x(), pair.p2().x());
        final int xMax = Math.max(pair.p1().x(), pair.p2().x());
        final int yMin = Math.min(pair.p1().y(), pair.p2().y());
        final int yMax = Math.max(pair.p1().y(), pair.p2().y());

        // Check 1: Midpoint inside polygon (or on boundary) - optimized with pre-computed vertical edges
        final double midX = (xMin + xMax) / 2.0;
        final double midY = (yMin + yMax) / 2.0;
        if (!isPointInPolygon(midX, midY, edges, verticalEdges)) {
            return 0L;
        }

        // Check 2: No edge intersects interior - optimized with quick bounds filtering
        if (edgesIntersectRect(xMin, xMax, yMin, yMax, edges)) {
            return 0L;
        }

        return area;
    }

    /**
     * Parses points from the input file.
     * Each line contains a point in the format "x,y".
     *
     * @param fileName The input file name
     * @return List of parsed points
     */
    private List<Point> parsePoints(final String fileName) {
        return ResourceLines.list(fileName).stream()
                .filter(line -> !line.trim().isEmpty())
                .map(Point::from)
                .toList();
    }

    /**
     * Checks if a point is inside or on the boundary of a polygon using ray casting algorithm.
     * This is a pure function that determines point-in-polygon relationship.
     * Optimized to use pre-computed vertical edges and combine checks in single pass.
     *
     * @param x The x coordinate of the point
     * @param y The y coordinate of the point
     * @param edges The edges of the polygon
     * @param verticalEdges Pre-computed list of vertical edges for ray casting
     * @return true if the point is inside or on the boundary of the polygon
     */
    private boolean isPointInPolygon(final double x, final double y, final List<Edge> edges, final List<Edge> verticalEdges) {
        // Combined check: point on edge OR ray casting
        // First check if point is on any edge (short-circuit)
        for (final Edge edge : edges) {
            if (isPointOnEdge(x, y, edge)) {
                return true;
            }
        }

        // Ray casting: count intersections with vertical edges (using pre-computed list)
        long intersections = 0;
        for (final Edge edge : verticalEdges) {
            if (intersectsRay(x, y, edge)) {
                intersections++;
            }
        }

        return (intersections % 2) != 0;
    }

    /**
     * Pure function that checks if a point lies on an edge.
     *
     * @param x The x coordinate of the point
     * @param y The y coordinate of the point
     * @param edge The edge to check
     * @return true if the point is on the edge
     */
    private boolean isPointOnEdge(final double x, final double y, final Edge edge) {
        if (edge.isVertical()) {
            if (Math.abs(edge.p1().x() - x) < 1e-9) {
                final double y1 = Math.min(edge.p1().y(), edge.p2().y());
                final double y2 = Math.max(edge.p1().y(), edge.p2().y());
                return y >= y1 - 1e-9 && y <= y2 + 1e-9;
            }
        } else {
            if (Math.abs(edge.p1().y() - y) < 1e-9) {
                final double x1 = Math.min(edge.p1().x(), edge.p2().x());
                final double x2 = Math.max(edge.p1().x(), edge.p2().x());
                return x >= x1 - 1e-9 && x <= x2 + 1e-9;
            }
        }
        return false;
    }

    /**
     * Pure function that checks if a vertical edge intersects a horizontal ray.
     * Uses >= y1 and < y2 to handle vertices (Simulation of Simplicity).
     *
     * @param x The x coordinate of the ray origin
     * @param y The y coordinate of the ray origin
     * @param edge The vertical edge to check
     * @return true if the edge intersects the ray
     */
    private boolean intersectsRay(final double x, final double y, final Edge edge) {
        final double y1 = Math.min(edge.p1().y(), edge.p2().y());
        final double y2 = Math.max(edge.p1().y(), edge.p2().y());
        final double edgeX = edge.p1().x();
        return y >= y1 && y < y2 && edgeX > x;
    }

    /**
     * Checks if any polygon edge intersects the interior of a rectangle.
     * This is a pure function that determines geometric intersection.
     * Optimized with quick bounds filtering before expensive intersection checks.
     *
     * @param xMin Minimum x coordinate of the rectangle
     * @param xMax Maximum x coordinate of the rectangle
     * @param yMin Minimum y coordinate of the rectangle
     * @param yMax Maximum y coordinate of the rectangle
     * @param edges The edges of the polygon
     * @return true if any edge intersects the interior of the rectangle
     */
    private boolean edgesIntersectRect(final int xMin, final int xMax, final int yMin, final int yMax, final List<Edge> edges) {
        for (final Edge edge : edges) {
            // Quick bounds check before expensive intersection calculation
            if (edge.isVertical()) {
                final int ex = edge.p1().x();
                // Quick filter: edge must be strictly inside rectangle x bounds
                if (ex <= xMin || ex >= xMax) {
                    continue;
                }
                final int ey1 = Math.min(edge.p1().y(), edge.p2().y());
                final int ey2 = Math.max(edge.p1().y(), edge.p2().y());
                // Quick filter: edge must overlap rectangle y bounds
                if (ey2 <= yMin || ey1 >= yMax) {
                    continue;
                }
            } else {
                final int ey = edge.p1().y();
                // Quick filter: edge must be strictly inside rectangle y bounds
                if (ey <= yMin || ey >= yMax) {
                    continue;
                }
                final int ex1 = Math.min(edge.p1().x(), edge.p2().x());
                final int ex2 = Math.max(edge.p1().x(), edge.p2().x());
                // Quick filter: edge must overlap rectangle x bounds
                if (ex2 <= xMin || ex1 >= xMax) {
                    continue;
                }
            }
            // Only perform expensive intersection check if edge passes quick filters
            if (edgeIntersectsRect(xMin, xMax, yMin, yMax, edge)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Pure function that checks if a single edge intersects the interior of a rectangle.
     *
     * @param xMin Minimum x coordinate of the rectangle
     * @param xMax Maximum x coordinate of the rectangle
     * @param yMin Minimum y coordinate of the rectangle
     * @param yMax Maximum y coordinate of the rectangle
     * @param edge The edge to check
     * @return true if the edge intersects the interior of the rectangle
     */
    private boolean edgeIntersectsRect(final int xMin, final int xMax, final int yMin, final int yMax, final Edge edge) {
        if (edge.isVertical()) {
            final int ex = edge.p1().x();
            final int ey1 = Math.min(edge.p1().y(), edge.p2().y());
            final int ey2 = Math.max(edge.p1().y(), edge.p2().y());

            return ex > xMin && ex < xMax && Math.max(ey1, yMin) < Math.min(ey2, yMax);
        } else {
            final int ey = edge.p1().y();
            final int ex1 = Math.min(edge.p1().x(), edge.p2().x());
            final int ex2 = Math.max(edge.p1().x(), edge.p2().x());

            return ey > yMin && ey < yMax && Math.max(ex1, xMin) < Math.min(ex2, xMax);
        }
    }
}
