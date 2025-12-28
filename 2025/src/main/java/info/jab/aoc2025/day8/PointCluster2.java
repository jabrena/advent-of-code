package info.jab.aoc2025.day8;

import module java.base;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver2;

/**
 * Solver for point clustering using DSU for union-find and stream-based processing.
 * Uses DSU for union-find and stream-based processing.
 */
public final class PointCluster2 implements Solver2<Long, String, Integer> {

    @Override
    public Long solvePartOne(final String fileName, final Integer connectionLimit) {
        final List<Point3D> points = parsePoints(fileName);
        final DSU dsu = new DSU(points.size());

        // Generate all pairs using stream API
        final int shortestPairs = connectionLimit;
        IntStream.range(0, points.size()).boxed()
                .flatMap(i -> IntStream.range(0, i).mapToObj(j -> {
                    final long distance = points.get(i).distanceSquared(points.get(j));
                    return new Edge(i, j, distance);
                }))
                .sorted(Comparator.comparingLong(Edge::distanceSquared))
                .limit(shortestPairs)
                .forEach(edge -> dsu.union(edge.i(), edge.j()));

        // Calculate product of top 3 component sizes using stream API
        final long product = IntStream.range(0, points.size())
                .mapToObj(i -> dsu.find(i))
                .collect(Collectors.groupingBy(root -> root, Collectors.counting()))
                .values().stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .reduce(1L, (a, b) -> a * b);

        return product;
    }

    @Override
    public Long solvePartTwo(final String fileName, final Integer unused) {
        final List<Point3D> points = parsePoints(fileName);
        final DSU dsu = new DSU(points.size());

        // Generate all pairs using stream API
        final List<Edge> edges = IntStream.range(0, points.size()).boxed()
                .flatMap(i -> IntStream.range(0, i).mapToObj(j -> {
                    final long distance = points.get(i).distanceSquared(points.get(j));
                    return new Edge(i, j, distance);
                }))
                .sorted(Comparator.comparingLong(Edge::distanceSquared))
                .toList();

        int components = points.size();
        for (final Edge edge : edges) {
            if (dsu.union(edge.i(), edge.j())) {
                components--;
                if (components == 1) {
                    final Point3D p1 = points.get(edge.i());
                    final Point3D p2 = points.get(edge.j());
                    return (long) p1.x() * p2.x();
                }
            }
        }

        return 0L;
    }

    /**
     * Parses points from input file using regex pattern matching.
     * Uses Gatherers.windowFixed(3) to group coordinates into points.
     *
     * @param fileName the name of the file containing point data
     * @return a list of Point3D objects
     */
    private List<Point3D> parsePoints(final String fileName) {
        final String input = String.join("\n", ResourceLines.list(fileName));
        final Pattern pattern = Pattern.compile("\\d+");

        return pattern.matcher(input).results()
                .map(r -> Integer.parseInt(r.group()))
                .gather(Gatherers.windowFixed(3))
                .map(list -> new Point3D(list.get(0), list.get(1), list.get(2)))
                .toList();
    }
}
