package info.jab.aoc2025.day8;

import module java.base;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver2;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

/**
 * Optimized PointCluster implementation using FastUtil collections.
 * <p>
 * Performance optimizations:
 * <ul>
 *   <li>PriorityQueue for Part 1: O(n² log k) instead of O(n² log n) where k << n</li>
 *   <li>ObjectArrayList for points: Better memory layout and cache locality</li>
 *   <li>ObjectArrayList for edges: Optimized sorting and iteration</li>
 *   <li>IntList for component sizes: Eliminates boxing overhead</li>
 *   <li>Indexed iteration: Avoids iterator overhead</li>
 *   <li>Cached references: Reduces repeated get() calls</li>
 * </ul>
 */
public final class PointCluster3 implements Solver2<Long, String, Integer> {

    /**
     * Static comparator for edge sorting to avoid repeated allocation.
     * Uses Long.compare for efficient primitive comparison.
     */
    private static final Comparator<Edge> EDGE_COMPARATOR =
        (e1, e2) -> Long.compare(e1.distanceSquared(), e2.distanceSquared());

    @Override
    public Long solvePartOne(String fileName, Integer connectionLimit) {
        ObjectList<Point3D> points = parsePoints(fileName);
        // Optimized: Use PriorityQueue (max-heap) to keep only top k connections
        // Complexity: O(n² log k) instead of O(n² log n) where k=connectionLimit
        PriorityQueue<Edge> topEdges = getTopEdges(points, connectionLimit);

        DSU dsu = new DSU(points.size());
        while (!topEdges.isEmpty()) {
            Edge edge = topEdges.poll();
            dsu.union(edge.i(), edge.j());
        }

        return calculateTop3Product(dsu.getComponentSizesIntList());
    }

    @Override
    public Long solvePartTwo(String fileName, Integer unused) {
        ObjectList<Point3D> points = parsePoints(fileName);
        ObjectList<Edge> edges = computeEdges(points);
        edges.sort(EDGE_COMPARATOR);

        DSU dsu = new DSU(points.size());
        int components = points.size();
        final int edgesSize = edges.size();

        // Use indexed access instead of enhanced for-loop to avoid iterator overhead
        for (int idx = 0; idx < edgesSize; idx++) {
            Edge edge = edges.get(idx);
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
     * Parses points from the input file using FastUtil ObjectArrayList.
     *
     * @param fileName the name of the file containing point data
     * @return an ObjectList of Point3D objects
     */
    private ObjectList<Point3D> parsePoints(String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        // Pre-allocate with estimated size
        final ObjectList<Point3D> points = new ObjectArrayList<>(lines.size());
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
     * <p>
     * Uses FastUtil ObjectArrayList for better memory layout and optimized sorting.
     * Caches Point3D references to avoid repeated get() calls.
     *
     * @param points the list of points
     * @return an ObjectList of all edges with their distance squared
     */
    private ObjectList<Edge> computeEdges(ObjectList<Point3D> points) {
        final int n = points.size();
        // Pre-allocate with exact size: n*(n-1)/2 for all pairs
        final int estimatedSize = n * (n - 1) / 2;
        final ObjectList<Edge> edges = new ObjectArrayList<>(estimatedSize);

        for (int i = 0; i < n; i++) {
            final Point3D p1 = points.get(i); // Cache reference
            for (int j = i + 1; j < n; j++) {
                final Point3D p2 = points.get(j); // Cache reference
                edges.add(new Edge(i, j, p1.distanceSquared(p2)));
            }
        }
        return edges;
    }

    /**
     * Optimized method for Part 1: Uses PriorityQueue (max-heap) to keep only top k connections.
     * Complexity: O(n² log k) instead of O(n² log n) where k is typically much smaller than n.
     * Uses sequential processing to avoid parallel stream overhead for small k.
     * <p>
     * Caches Point3D references to avoid repeated get() calls.
     *
     * @param points The list of points
     * @param k The number of top connections to keep
     * @return PriorityQueue containing the k shortest connections
     */
    private PriorityQueue<Edge> getTopEdges(ObjectList<Point3D> points, int k) {
        // Max-heap: keep largest at top, remove when size > k
        // Pre-allocate with capacity k+1 to avoid reallocations
        PriorityQueue<Edge> heap = new PriorityQueue<>(
                k + 1,
                (e1, e2) -> Long.compare(e2.distanceSquared(), e1.distanceSquared()) // Reversed for max-heap
        );

        // Sequential processing: O(n² log k) - avoids parallel stream overhead for heap operations
        final int n = points.size();
        for (int i = 0; i < n; i++) {
            final Point3D p1 = points.get(i); // Cache reference
            for (int j = i + 1; j < n; j++) {
                final Point3D p2 = points.get(j); // Cache reference
                Edge edge = new Edge(i, j, p1.distanceSquared(p2));
                heap.offer(edge);
                if (heap.size() > k) {
                    heap.poll(); // Remove largest (keep only k smallest)
                }
            }
        }

        return heap;
    }

    /**
     * Calculates the product of the top 3 component sizes without full sort.
     * Optimized to find top 3 using partial selection instead of sorting entire list.
     * Uses FastUtil IntList to avoid boxing overhead during iteration.
     *
     * @param sizes IntList of component sizes
     * @return Product of the top 3 sizes, or 0 if less than 3 components
     */
    private long calculateTop3Product(IntList sizes) {
        if (sizes.size() < 3) {
            return 0L;
        }

        // Use simple approach: find top 3 by iterating
        // FastUtil IntList.getInt() avoids boxing
        int first = 0, second = 0, third = 0;
        final int size = sizes.size();
        for (int i = 0; i < size; i++) {
            final int s = sizes.getInt(i);
            if (s > first) {
                third = second;
                second = first;
                first = s;
            } else if (s > second) {
                third = second;
                second = s;
            } else if (s > third) {
                third = s;
            }
        }

        return (long) first * second * third;
    }
}

