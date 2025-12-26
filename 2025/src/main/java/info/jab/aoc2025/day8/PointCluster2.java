package info.jab.aoc2025.day8;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver2;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * PointCluster2 - A simplified implementation based on TypeScript approach.
 * <p>
 * This version calculates all edges upfront, sorts them, and processes them sequentially.
 * It follows a cleaner, more straightforward approach compared to the optimized PointCluster.
 */
public final class PointCluster2 implements Solver2<Long, String, Integer> {

    /**
     * Represents an edge between two points with their indices and distance.
     */
    private record Edge(int i, int j, double distance) {
        static Edge of(int i, int j, double distance) {
            return new Edge(i, j, distance);
        }
    }

    @Override
    public Long solvePartOne(String fileName, Integer connectionLimit) {
        List<Point3D> points = parsePoints(fileName);
        List<Edge> edges = computeEdges(points);
        edges.sort(Comparator.comparingDouble(Edge::distance));

        DSU dsu = new DSU(points.size());
        int limit = Math.min(connectionLimit, edges.size());
        for (int i = 0; i < limit; i++) {
            Edge edge = edges.get(i);
            dsu.union(edge.i(), edge.j());
        }

        return dsu.getComponentSizes().stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .mapToLong(Integer::longValue)
                .reduce(1L, (acc, size) -> acc * size);
    }

    @Override
    public Long solvePartTwo(String fileName, Integer unused) {
        List<Point3D> points = parsePoints(fileName);
        List<Edge> edges = computeEdges(points);
        edges.sort(Comparator.comparingDouble(Edge::distance));

        DSU dsu = new DSU(points.size());
        int components = points.size();

        for (Edge edge : edges) {
            if (dsu.union(edge.i(), edge.j())) {
                components--;
                if (components == 1) {
                    Point3D p1 = points.get(edge.i());
                    Point3D p2 = points.get(edge.j());
                    return (long) p1.x() * p2.x();
                }
            }
        }

        return 0L;
    }

    /**
     * Parses points from the input file.
     *
     * @param fileName the name of the file containing point data
     * @return a list of Point3D objects
     */
    private List<Point3D> parsePoints(String fileName) {
        return ResourceLines.list(fileName).stream()
                .filter(line -> !line.trim().isEmpty())
                .map(Point3D::from)
                .toList();
    }

    /**
     * Computes all edges between points with their distances.
     * <p>
     * This method generates all pairs (i, j) where i < j and calculates
     * the Euclidean distance between each pair of points.
     *
     * @param points the list of points
     * @return a list of all edges with their distances
     */
    private List<Edge> computeEdges(List<Point3D> points) {
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                double distance = distance(points.get(i), points.get(j));
                edges.add(Edge.of(i, j, distance));
            }
        }
        return edges;
    }

    /**
     * Calculates the Euclidean distance between two 3D points.
     * <p>
     * This is a helper method that computes the standard Euclidean distance:
     * sqrt((x1-x2)² + (y1-y2)² + (z1-z2)²)
     *
     * @param p1 the first point
     * @param p2 the second point
     * @return the Euclidean distance between the two points
     */
    private double distance(Point3D p1, Point3D p2) {
        long dx = p1.x() - p2.x();
        long dy = p1.y() - p2.y();
        long dz = p1.z() - p2.z();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
