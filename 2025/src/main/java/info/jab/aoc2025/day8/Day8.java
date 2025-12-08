package info.jab.aoc2025.day8;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

    @Override
    public Long getPart1Result(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        List<Point> points = lines.stream()
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

        List<Connection> connections = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                connections.add(new Connection(i, j, points.get(i).distanceSquared(points.get(j))));
            }
        }

        connections.sort(Comparator.comparingLong(Connection::distanceSquared));

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
        throw new UnsupportedOperationException("Not implemented");
    }

    private static class DSU {
        private final int[] parent;
        private final int[] size;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];
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

        public void union(int i, int j) {
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
            }
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
