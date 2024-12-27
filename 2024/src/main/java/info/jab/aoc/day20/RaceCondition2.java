package info.jab.aoc.day20;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.putoet.resources.ResourceLines;

public final class RaceCondition2 {

    private static final boolean SAMPLE = false;

    record Point(int i, int j) {
        int distance(Point p) {
            return Math.abs(p.i - i) + Math.abs(p.j - j);
        }
    }

    private static final int[][] DIFFS = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public static void solve(String fileName) {
        List<String> lines = ResourceLines.list(fileName);

        Point start = null, end = null;
        Set<Point> walls = new HashSet<>();
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                Point t = new Point(i, j);
                char ch = get(lines, t);
                if (ch == '#') {
                    walls.add(t);
                } else if (ch == 'S') {
                    start = t;
                } else if (ch == 'E') {
                    end = t;
                }
            }
        }

        Map<Point, Integer> fromEnd = bfs(end, start, walls);
        Map<Point, Integer> fromStart = bfs(start, end, walls);
        int fullTime = fromStart.get(end);
        int leastTimeSaved = 100;
        if (SAMPLE) {
            leastTimeSaved = 76;
        }

        int maxCheatLength = 2;
        int part1 = getValidCheats(fromStart, fromEnd, fullTime, maxCheatLength, leastTimeSaved);
        System.out.println("Part 1: " + part1); // 1452

        maxCheatLength = 20;
        int part2 = getValidCheats(fromStart, fromEnd, fullTime, maxCheatLength, leastTimeSaved);
        System.out.println("Part 2: " + part2); // 999556
    }

    private static int getValidCheats(Map<Point, Integer> fromStart, Map<Point, Integer> fromEnd,
                                        int fullTime, int maxCheatLength, int leastTimeSaved) {
        int count = 0;
        for (Point p1 : fromStart.keySet()) {
            // Go around p1 with distance upto `maxCheatLength`
            for (int i = -maxCheatLength; i <= maxCheatLength; i++) {
                for (int j = -maxCheatLength; j <= maxCheatLength; j++) {
                    Point p2 = new Point(p1.i + i, p1.j + j);
                    if (!fromEnd.containsKey(p2) || p2.equals(p1) || p2.distance(p1) > maxCheatLength) continue;
                    // [start -> p1] + [p1 -> p2] + [p2 -> end]
                    int totalTime = fromStart.get(p1) + p2.distance(p1) + fromEnd.get(p2);
                    if (fullTime - totalTime >= leastTimeSaved) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private static Map<Point, Integer> bfs(Point start, Point end, Set<Point> walls) {
        Queue<Point> queue = new ArrayDeque<>();
        Map<Point, Integer> visited = new LinkedHashMap<>();
        queue.add(start);
        visited.put(start, 0);

        while (!queue.isEmpty()) {
            Point current = queue.remove();
            int curlen = visited.get(current);
            if (current.equals(end)) {
                break;
            }
            for (int[] d : DIFFS) {
                Point tmp = new Point(current.i + d[0], current.j + d[1]);
                if (!visited.containsKey(tmp) && !walls.contains(tmp)) {
                    queue.add(tmp);
                    visited.put(tmp, curlen + 1);
                }
            }
        }
        return visited;
    }

    private static char get(List<String> lines, Point p) {
        try {
            return lines.get(p.i).charAt(p.j);
        } catch (Exception e) {
            return '\0';
        }
    }
}
