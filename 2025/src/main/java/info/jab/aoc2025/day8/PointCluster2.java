package info.jab.aoc2025.day8;

import module java.base;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver2;

public final class PointCluster2 implements Solver2<Long, String, Integer> {

    @Override
    public Long solvePartOne(String fileName, Integer connectionLimit) {
        List<Point3D> points = parsePoints(fileName);
        List<Edge> edges = computeEdges(points);
        edges.sort(Comparator.comparingLong(Edge::distanceSquared));

        DSU dsu = new DSU(points.size());
        int limit = Math.min(connectionLimit, edges.size());
        for (int i = 0; i < limit; i++) {
            Edge edge = edges.get(i);
            dsu.union(edge.i(), edge.j());
        }

        return calculateTop3Product(dsu.getComponentSizes());
    }

    @Override
    public Long solvePartTwo(String fileName, Integer unused) {
        List<Point3D> points = parsePoints(fileName);
        List<Edge> edges = computeEdges(points);
        edges.sort(Comparator.comparingLong(Edge::distanceSquared));

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
        final List<String> lines = ResourceLines.list(fileName);
        // Pre-allocate with estimated size
        final List<Point3D> points = new ArrayList<>(lines.size());
        for (final String line : lines) {
            if (!line.trim().isEmpty()) {
                points.add(Point3D.from(line));
            }
        }
        return points;
    }

    /**
     * Computes all edges between points with their distance squared.
     * <p>
     * This method generates all pairs (i, j) where i < j and calculates
     * the squared Euclidean distance between each pair of points.
     * Using squared distance avoids expensive Math.sqrt() operations while
     * maintaining the same relative ordering for sorting.
     *
     * @param points the list of points
     * @return a list of all edges with their distance squared
     */
    private List<Edge> computeEdges(List<Point3D> points) {
        final int n = points.size();
        // Pre-allocate with exact size: n*(n-1)/2 for all pairs
        final int estimatedSize = n * (n - 1) / 2;
        final List<Edge> edges = new ArrayList<>(estimatedSize);
        
        for (int i = 0; i < n; i++) {
            final Point3D p1 = points.get(i);
            for (int j = i + 1; j < n; j++) {
                edges.add(new Edge(i, j, p1.distanceSquared(points.get(j))));
            }
        }
        return edges;
    }

    /**
     * Calculates the product of the top 3 component sizes without full sort.
     * Optimized to find top 3 using partial selection instead of sorting entire list.
     *
     * @param sizes List of component sizes
     * @return Product of the top 3 sizes, or 0 if less than 3 components
     */
    private long calculateTop3Product(List<Integer> sizes) {
        if (sizes.size() < 3) {
            return 0L;
        }
        
        // Use simple approach: find top 3 by iterating
        int first = 0, second = 0, third = 0;
        for (final int size : sizes) {
            if (size > first) {
                third = second;
                second = first;
                first = size;
            } else if (size > second) {
                third = second;
                second = size;
            } else if (size > third) {
                third = size;
            }
        }
        
        return (long) first * second * third;
    }
}
