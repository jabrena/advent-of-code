package info.jab.aoc2025.day8;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver2;
import java.util.Comparator;
import java.util.List;

import java.util.stream.IntStream;

public final class PointCluster implements Solver2<Long, String, Integer> {

    @Override
    public Long solvePartOne(String fileName, Integer connectionLimit) {
        List<Point3D> points = parsePoint3Ds(fileName);
        List<Connection> connections = getSortedConnections(points);

        List<Connection> topConnections = connections.stream()
                .limit(connectionLimit)
                .toList();

        DSU dsu = new DSU(points.size());
        topConnections.forEach(conn -> dsu.union(conn.p1Index(), conn.p2Index()));

        return dsu.getComponentSizes().stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .mapToLong(Integer::longValue)
                .reduce(1L, (acc, size) -> acc * size);
    }

    @Override
    public Long solvePartTwo(String fileName, Integer unused) {
        List<Point3D> points = parsePoint3Ds(fileName);
        List<Connection> connections = getSortedConnections(points);

        DSU dsu = new DSU(points.size());
        for (Connection conn : connections) {
            if (dsu.union(conn.p1Index(), conn.p2Index())) {
                if (dsu.getCount() == 1) {
                    Point3D p1 = points.get(conn.p1Index());
                    Point3D p2 = points.get(conn.p2Index());
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

    @SuppressWarnings("null")
    private List<Connection> getSortedConnections(List<Point3D> points) {
        return IntStream.range(0, points.size())
                .boxed()
                .flatMap(i -> IntStream.range(i + 1, points.size())
                        .mapToObj(j -> new Connection(i, j, points.get(i).distanceSquared(points.get(j)))))
                .sorted(Comparator.comparingLong(Connection::distanceSquared))
                .toList();
    }
}

