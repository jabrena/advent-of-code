package info.jab.aoc2025.day8;

import module java.base;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver2;

public final class PointCluster implements Solver2<Long, String, Integer> {

    @Override
    public Long solvePartOne(String fileName, Integer connectionLimit) {
        List<Point3D> points = parsePoint3Ds(fileName);
        // Optimized: Use priority queue (max-heap) to keep only top k connections
        // Complexity: O(n² log k) instead of O(n² log n) where k=connectionLimit
        PriorityQueue<Edge> topConnections = getTopConnections(points, connectionLimit);

        DSU dsu = new DSU(points.size());
        topConnections.forEach(conn -> dsu.union(conn.i(), conn.j()));

        return calculateTop3Product(dsu.getComponentSizes());
    }

    @Override
    public Long solvePartTwo(String fileName, Integer unused) {
        List<Point3D> points = parsePoint3Ds(fileName);
        // Part 2 needs all connections sorted - use parallel sort for better performance
        List<Edge> connections = getSortedConnectionsParallel(points);

        DSU dsu = new DSU(points.size());
        for (Edge conn : connections) {
            if (dsu.union(conn.i(), conn.j())) {
                if (dsu.getCount() == 1) {
                    Point3D p1 = points.get(conn.i());
                    Point3D p2 = points.get(conn.j());
                    return (long) p1.x() * p2.x();
                }
            }
        }
        return 0L;
    }

    private List<Point3D> parsePoint3Ds(String fileName) {
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
     * Optimized method for Part 1: Uses priority queue (max-heap) to keep only top k connections.
     * Complexity: O(n² log k) instead of O(n² log n) where k is typically much smaller than n.
     * Uses sequential processing to avoid parallel stream overhead for small k.
     *
     * @param points The list of points
     * @param k The number of top connections to keep
     * @return Priority queue containing the k shortest connections
     */
    private PriorityQueue<Edge> getTopConnections(List<Point3D> points, int k) {
        // Max-heap: keep largest at top, remove when size > k
        // Pre-allocate with capacity k+1 to avoid reallocations
        PriorityQueue<Edge> heap = new PriorityQueue<>(
                k + 1,
                Comparator.comparingLong(Edge::distanceSquared).reversed()
        );

        // Sequential processing: O(n² log k) - avoids parallel stream overhead for heap operations
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Edge conn = new Edge(i, j, points.get(i).distanceSquared(points.get(j)));
                heap.offer(conn);
                if (heap.size() > k) {
                    heap.poll(); // Remove largest (keep only k smallest)
                }
            }
        }

        return heap;
    }

    /**
     * Optimized method for Part 2: Uses sequential processing and pre-allocated list.
     * Complexity: O(n² log n) with better constant factors due to sequential processing.
     *
     * @param points The list of points
     * @return Sorted list of all connections
     */
    private List<Edge> getSortedConnectionsParallel(List<Point3D> points) {
        final int n = points.size();
        final int estimatedSize = n * (n - 1) / 2;
        final List<Edge> connections = new ArrayList<>(estimatedSize);
        
        // Sequential processing: avoids parallel stream overhead
        for (int i = 0; i < n; i++) {
            final Point3D p1 = points.get(i);
            for (int j = i + 1; j < n; j++) {
                connections.add(new Edge(i, j, p1.distanceSquared(points.get(j))));
            }
        }
        
        connections.sort(Comparator.comparingLong(Edge::distanceSquared));
        return connections;
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
