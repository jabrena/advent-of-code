package info.jab.aoc2025.day8;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day8 implements Day<Long> {

    record Point(int x, int y, int z) {
        long distanceSquared(Point other) {
            long dx = this.x - other.x;
            long dy = this.y - other.y;
            long dz = this.z - other.z;
            return dx * dx + dy * dy + dz * dz;
        }
    }

    record Connection(int p1Index, int p2Index, long distanceSquared) {}

    private List<Point> parsePoints(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        return lines.stream()
                .filter(line -> !line.trim().isEmpty())
                .map(line -> {
                    String[] parts = line.split(",");
                    return new Point(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2])
                    );
                })
                .collect(Collectors.toList());
    }

    private List<Connection> getSortedConnections(List<Point> points) {
        List<Connection> connections = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                connections.add(new Connection(i, j, points.get(i).distanceSquared(points.get(j))));
            }
        }
        connections.sort(Comparator.comparingLong(Connection::distanceSquared));
        return connections;
    }

    @Override
    public Long getPart1Result(String fileName) {
        List<Point> points = parsePoints(fileName);
        List<Connection> connections = getSortedConnections(points);

        int limit = 1000;
        List<Connection> topConnections = connections.stream()
                .limit(limit)
                .collect(Collectors.toList());

        DSU dsu = new DSU(points.size());
        for (Connection conn : topConnections) {
            dsu.union(conn.p1Index, conn.p2Index);
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
    public Long getPart2Result(String fileName) {
        List<Point> points = parsePoints(fileName);
        List<Connection> connections = getSortedConnections(points);

        DSU dsu = new DSU(points.size());
        for (Connection conn : connections) {
            if (dsu.union(conn.p1Index, conn.p2Index)) {
                if (dsu.getCount() == 1) {
                    Point p1 = points.get(conn.p1Index);
                    Point p2 = points.get(conn.p2Index);
                    return (long) p1.x * p2.x;
                }
            }
        }
        return 0L;
    }

    private static class DSU {
        private final int[] parent;
        private final int[] size;
        private int count;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];
            count = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int i) {
            if (parent[i] != i) {
                parent[i] = find(parent[i]);
            }
            return parent[i];
        }

        public boolean union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);

            if (rootI != rootJ) {
                if (size[rootI] < size[rootJ]) {
                    int temp = rootI;
                    rootI = rootJ;
                    rootJ = temp;
                }
                parent[rootJ] = rootI;
                size[rootI] += size[rootJ];
                count--;
                return true;
            }
            return false;
        }

        public int getCount() {
            return count;
        }

        public List<Integer> getComponentSizes() {
            Set<Integer> roots = new HashSet<>();
            for (int i = 0; i < parent.length; i++) {
                roots.add(find(i));
            }
            
            return roots.stream()
                .map(root -> size[root])
                .collect(Collectors.toList());
        }
    }
}
