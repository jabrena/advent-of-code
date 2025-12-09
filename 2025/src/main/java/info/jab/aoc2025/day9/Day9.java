package info.jab.aoc2025.day9;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.ArrayList;
import java.util.List;

public class Day9 implements Day<Long> {

    record Point(int x, int y) {}
    record Edge(Point p1, Point p2) {
        boolean isVertical() {
            return p1.x == p2.x;
        }
        boolean isHorizontal() {
            return p1.y == p2.y;
        }
    }

    @Override
    public Long getPart1Result(String fileName) {
        List<Point> points = parsePoints(fileName);

        long maxArea = 0;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Point p1 = points.get(i);
                Point p2 = points.get(j);
                long area = (Math.abs((long)p1.x - p2.x) + 1L) * (Math.abs((long)p1.y - p2.y) + 1L);
                if (area > maxArea) {
                    maxArea = area;
                }
            }
        }
        return maxArea;
    }

    @Override
    public Long getPart2Result(String fileName) {
        List<Point> points = parsePoints(fileName);
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            edges.add(new Edge(points.get(i), points.get((i + 1) % points.size())));
        }

        long maxArea = 0;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Point p1 = points.get(i);
                Point p2 = points.get(j);

                long width = Math.abs((long)p1.x - p2.x) + 1L;
                long height = Math.abs((long)p1.y - p2.y) + 1L;
                long area = width * height;

                if (area <= maxArea) continue;

                int xMin = Math.min(p1.x, p2.x);
                int xMax = Math.max(p1.x, p2.x);
                int yMin = Math.min(p1.y, p2.y);
                int yMax = Math.max(p1.y, p2.y);

                // Check 1: Midpoint inside polygon (or on boundary)
                double midX = (xMin + xMax) / 2.0;
                double midY = (yMin + yMax) / 2.0;
                if (!isPointInPolygon(midX, midY, edges)) {
                    continue;
                }

                // Check 2: No edge intersects interior
                if (edgesIntersectRect(xMin, xMax, yMin, yMax, edges)) {
                    continue;
                }

                maxArea = area;
            }
        }
        return maxArea;
    }

    private List<Point> parsePoints(String fileName) {
        return ResourceLines.list(fileName).stream()
                .filter(line -> !line.trim().isEmpty())
                .map(line -> {
                    String[] parts = line.split(",");
                    return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                })
                .toList();
    }

    private boolean isPointInPolygon(double x, double y, List<Edge> edges) {
        // Check if point is on any edge
        for (Edge edge : edges) {
            if (edge.isVertical()) {
                if (Math.abs(edge.p1.x - x) < 1e-9) {
                    double y1 = Math.min(edge.p1.y, edge.p2.y);
                    double y2 = Math.max(edge.p1.y, edge.p2.y);
                    if (y >= y1 - 1e-9 && y <= y2 + 1e-9) return true;
                }
            } else {
                if (Math.abs(edge.p1.y - y) < 1e-9) {
                    double x1 = Math.min(edge.p1.x, edge.p2.x);
                    double x2 = Math.max(edge.p1.x, edge.p2.x);
                    if (x >= x1 - 1e-9 && x <= x2 + 1e-9) return true;
                }
            }
        }

        // Ray casting
        int intersections = 0;
        for (Edge edge : edges) {
            if (edge.isVertical()) {
                double y1 = Math.min(edge.p1.y, edge.p2.y);
                double y2 = Math.max(edge.p1.y, edge.p2.y);
                double edgeX = edge.p1.x;

                // Use >= y1 and < y2 to handle vertices (Simulation of Simplicity)
                if (y >= y1 && y < y2 && edgeX > x) {
                    intersections++;
                }
            }
        }
        return (intersections % 2) != 0;
    }

    private boolean edgesIntersectRect(int xMin, int xMax, int yMin, int yMax, List<Edge> edges) {
        for (Edge edge : edges) {
            if (edge.isVertical()) {
                int ex = edge.p1.x;
                int ey1 = Math.min(edge.p1.y, edge.p2.y);
                int ey2 = Math.max(edge.p1.y, edge.p2.y);

                if (ex > xMin && ex < xMax) {
                    if (Math.max(ey1, yMin) < Math.min(ey2, yMax)) {
                        return true;
                    }
                }
            } else {
                int ey = edge.p1.y;
                int ex1 = Math.min(edge.p1.x, edge.p2.x);
                int ex2 = Math.max(edge.p1.x, edge.p2.x);

                if (ey > yMin && ey < yMax) {
                    if (Math.max(ex1, xMin) < Math.min(ex2, xMax)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
