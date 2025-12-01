package info.jab.aoc2018.day6;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day6 implements Solver<Integer> {

    record Point(int x, int y) {}

    @Override
    public Integer solvePartOne(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        List<Point> coordinates = new ArrayList<>();
        
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());
            coordinates.add(new Point(x, y));
        }
        
        // Find bounding box
        int minX = coordinates.stream().mapToInt(Point::x).min().orElse(0);
        int maxX = coordinates.stream().mapToInt(Point::x).max().orElse(0);
        int minY = coordinates.stream().mapToInt(Point::y).min().orElse(0);
        int maxY = coordinates.stream().mapToInt(Point::y).max().orElse(0);
        
        // Extend bounding box to detect infinite areas
        int extendedMinX = minX - 1;
        int extendedMaxX = maxX + 1;
        int extendedMinY = minY - 1;
        int extendedMaxY = maxY + 1;
        
        // Track which coordinates have infinite areas
        Set<Integer> infiniteCoordinates = new HashSet<>();
        
        // Check edges to find infinite areas
        for (int x = extendedMinX; x <= extendedMaxX; x++) {
            // Top edge
            int closestTop = findClosestCoordinate(x, extendedMinY, coordinates);
            if (closestTop != -1) {
                infiniteCoordinates.add(closestTop);
            }
            // Bottom edge
            int closestBottom = findClosestCoordinate(x, extendedMaxY, coordinates);
            if (closestBottom != -1) {
                infiniteCoordinates.add(closestBottom);
            }
        }
        
        for (int y = extendedMinY; y <= extendedMaxY; y++) {
            // Left edge
            int closestLeft = findClosestCoordinate(extendedMinX, y, coordinates);
            if (closestLeft != -1) {
                infiniteCoordinates.add(closestLeft);
            }
            // Right edge
            int closestRight = findClosestCoordinate(extendedMaxX, y, coordinates);
            if (closestRight != -1) {
                infiniteCoordinates.add(closestRight);
            }
        }
        
        // Count area for each coordinate
        int[] areaCount = new int[coordinates.size()];
        
        for (int x = extendedMinX; x <= extendedMaxX; x++) {
            for (int y = extendedMinY; y <= extendedMaxY; y++) {
                int closest = findClosestCoordinate(x, y, coordinates);
                if (closest != -1) {
                    areaCount[closest]++;
                }
            }
        }
        
        // Find maximum area for finite coordinates
        int maxArea = 0;
        for (int i = 0; i < coordinates.size(); i++) {
            if (!infiniteCoordinates.contains(i)) {
                maxArea = Math.max(maxArea, areaCount[i]);
            }
        }
        
        return maxArea;
    }

    private int findClosestCoordinate(int x, int y, List<Point> coordinates) {
        int minDistance = Integer.MAX_VALUE;
        int closestIndex = -1;
        boolean tie = false;
        
        for (int i = 0; i < coordinates.size(); i++) {
            Point coord = coordinates.get(i);
            int distance = manhattanDistance(x, y, coord.x, coord.y);
            
            if (distance < minDistance) {
                minDistance = distance;
                closestIndex = i;
                tie = false;
            } else if (distance == minDistance) {
                tie = true;
            }
        }
        
        // Return -1 if there's a tie
        return tie ? -1 : closestIndex;
    }

    private int manhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        List<Point> coordinates = new ArrayList<>();
        
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());
            coordinates.add(new Point(x, y));
        }
        
        // Find bounding box
        int minX = coordinates.stream().mapToInt(Point::x).min().orElse(0);
        int maxX = coordinates.stream().mapToInt(Point::x).max().orElse(0);
        int minY = coordinates.stream().mapToInt(Point::y).min().orElse(0);
        int maxY = coordinates.stream().mapToInt(Point::y).max().orElse(0);
        
        // Extend bounding box to ensure we cover all possible locations
        // The region might extend beyond the coordinates
        int extendedMinX = minX - 200;
        int extendedMaxX = maxX + 200;
        int extendedMinY = minY - 200;
        int extendedMaxY = maxY + 200;
        
        int regionSize = 0;
        int threshold = 10000;
        
        for (int x = extendedMinX; x <= extendedMaxX; x++) {
            for (int y = extendedMinY; y <= extendedMaxY; y++) {
                int totalDistance = 0;
                for (Point coord : coordinates) {
                    totalDistance += manhattanDistance(x, y, coord.x, coord.y);
                }
                if (totalDistance < threshold) {
                    regionSize++;
                }
            }
        }
        
        return regionSize;
    }
}
