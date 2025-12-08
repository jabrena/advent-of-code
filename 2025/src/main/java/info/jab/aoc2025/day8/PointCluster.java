package info.jab.aoc2025.day8;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class PointCluster implements Solver<Long> {

    @Override
    public Long solvePartOne(String fileName) {
        List<Point3D> points = parsePoint3Ds(fileName);
        List<Connection> connections = getSortedConnections(points);

        int limit = 1000;
        List<Connection> topConnections = connections.stream()
                .limit(limit)
                .toList();

        DSU dsu = new DSU(points.size());
        for (Connection conn : topConnections) {
            dsu.union(conn.p1Index(), conn.p2Index());
        }

        List<Integer> sizes = dsu.getComponentSizes();
        sizes.sort(Comparator.reverseOrder());

        long result = 1;
        for (int i = 0; i < Math.min(3, sizes.size()); i++) {
            result *= sizes.get(i);
        }

        return result;
    }

    @Override
    public Long solvePartTwo(String fileName) {
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

    private List<Connection> getSortedConnections(List<Point3D> points) {
        List<Connection> connections = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                connections.add(new Connection(i, j, points.get(i).distanceSquared(points.get(j))));
            }
        }
        connections.sort(Comparator.comparingLong(Connection::distanceSquared));
        return connections;
    }
}

