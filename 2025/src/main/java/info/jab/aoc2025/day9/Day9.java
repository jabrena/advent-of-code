package info.jab.aoc2025.day9;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.List;

public class Day9 implements Day<Long> {

    record Point(int x, int y) {}

    @Override
    public Long getPart1Result(String fileName) {
        List<Point> points = ResourceLines.list(fileName).stream()
                .filter(line -> !line.trim().isEmpty())
                .map(line -> {
                    String[] parts = line.split(",");
                    return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                })
                .toList();

        long maxArea = 0;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Point p1 = points.get(i);
                Point p2 = points.get(j);
                long area = (Math.abs(p1.x - p2.x) + 1L) * (Math.abs(p1.y - p2.y) + 1L);
                if (area > maxArea) {
                    maxArea = area;
                }
            }
        }
        return maxArea;
    }

    @Override
    public Long getPart2Result(String fileName) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
