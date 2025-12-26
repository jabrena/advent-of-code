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

        return dsu.getComponentSizes().stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .mapToLong(Integer::longValue)
                .reduce(1L, (acc, size) -> acc * size);
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
        List<String> lines = ResourceLines.list(fileName);
        return lines.stream()
                .filter(line -> !line.trim().isEmpty())
                .map(Point3D::from)
                .toList();
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
        PriorityQueue<Edge> heap = new PriorityQueue<>(
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
     * Optimized method for Part 2: Uses parallel sort for better performance on large datasets.
     * Complexity: O(n² log n) but with better constant factors due to parallelization.
     *
     * @param points The list of points
     * @return Sorted list of all connections
     */
    @SuppressWarnings("null")
    private List<Edge> getSortedConnectionsParallel(List<Point3D> points) {
        return IntStream.range(0, points.size())
                .boxed()
                .parallel() // Parallel processing for pair generation
                .flatMap(i -> IntStream.range(i + 1, points.size())
                        .mapToObj(j -> new Edge(i, j, points.get(i).distanceSquared(points.get(j)))))
                .parallel() // Parallel sort
                .sorted(Comparator.comparingLong(Edge::distanceSquared))
                .toList();
    }
}
