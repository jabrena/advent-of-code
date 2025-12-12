package info.jab.aoc2025.day12;

import info.jab.aoc.Day;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.Collections;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.URL;

public class Day12 implements Day<Long> {

    record Point(int x, int y) {}

    static class ShapeVariant {
        List<Point> points;
        int width;
        int height;

        public ShapeVariant(List<Point> points) {
            if (points.isEmpty()) {
                this.points = points;
                this.width = 0;
                this.height = 0;
                return;
            }
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            for (Point p : points) {
                minX = Math.min(minX, p.x());
                minY = Math.min(minY, p.y());
                maxX = Math.max(maxX, p.x());
                maxY = Math.max(maxY, p.y());
            }
            List<Point> normalized = new ArrayList<>();
            for (Point p : points) {
                normalized.add(new Point(p.x() - minX, p.y() - minY));
            }
            this.points = normalized;
            this.width = maxX - minX + 1;
            this.height = maxY - minY + 1;
        }
    }

    static class Shape {
        int id;
        int area;
        List<ShapeVariant> variants;

        public Shape(int id, List<String> lines) {
            this.id = id;
            List<Point> points = new ArrayList<>();
            this.area = 0;
            for (int y = 0; y < lines.size(); y++) {
                String line = lines.get(y);
                for (int x = 0; x < line.length(); x++) {
                    if (line.charAt(x) == '#') {
                        points.add(new Point(x, y));
                        area++;
                    }
                }
            }
            this.variants = generateVariants(points);
        }

        private List<ShapeVariant> generateVariants(List<Point> original) {
            Set<Set<Point>> uniqueVariants = new HashSet<>();
            List<ShapeVariant> result = new ArrayList<>();
            
            List<Point> current = original;
            List<Point> flipped = flip(original);
            List<List<Point>> bases = List.of(original, flipped);
            
            for (List<Point> base : bases) {
                current = base;
                for (int j = 0; j < 4; j++) {
                    ShapeVariant v = new ShapeVariant(current);
                    if (uniqueVariants.add(new HashSet<>(v.points))) {
                        result.add(v);
                    }
                    current = rotate90(current);
                }
            }
            return result;
        }

        private List<Point> rotate90(List<Point> points) {
            List<Point> newPoints = new ArrayList<>();
            for (Point p : points) {
                newPoints.add(new Point(-p.y(), p.x()));
            }
            return newPoints;
        }

        private List<Point> flip(List<Point> points) {
            List<Point> newPoints = new ArrayList<>();
            for (Point p : points) {
                newPoints.add(new Point(-p.x(), p.y()));
            }
            return newPoints;
        }
    }

    static class Region {
        int width;
        int height;
        Map<Integer, Integer> requirements;
        long regionArea;

        public Region(String line) {
            String[] parts = line.split(": ");
            String[] dims = parts[0].split("x");
            width = Integer.parseInt(dims[0]);
            height = Integer.parseInt(dims[1]);
            regionArea = (long) width * height;
            
            requirements = new HashMap<>();
            String[] counts = parts[1].trim().split("\\s+");
            for (int i = 0; i < counts.length; i++) {
                requirements.put(i, Integer.parseInt(counts[i]));
            }
        }
    }

    @Override
    public Long getPart1Result(String fileName) {
        List<String> lines = readFileToList(fileName);
        
        Map<Integer, Shape> shapes = new HashMap<>();
        List<Region> regions = new ArrayList<>();
        
        List<String> currentShapeBuffer = new ArrayList<>();
        Integer currentShapeId = null;

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            
            if (Character.isDigit(line.charAt(0)) && line.contains(":")) {
                if (line.contains("x")) {
                    regions.add(new Region(line));
                } else {
                    if (currentShapeId != null) {
                        shapes.put(currentShapeId, new Shape(currentShapeId, currentShapeBuffer));
                        currentShapeBuffer.clear();
                    }
                    currentShapeId = Integer.parseInt(line.replace(":", ""));
                }
            } else {
                currentShapeBuffer.add(line);
            }
        }
        if (currentShapeId != null && !currentShapeBuffer.isEmpty()) {
            shapes.put(currentShapeId, new Shape(currentShapeId, currentShapeBuffer));
        }

        long count = 0;
        for (Region region : regions) {
            if (solve(region, shapes)) {
                count++;
            }
        }
        return count;
    }

    private List<String> readFileToList(String fileName) {
        try {
            URL resource = getClass().getClassLoader().getResource(fileName);
            if (resource == null) {
                 return Files.readAllLines(Paths.get("src/test/resources/" + fileName));
            }
            return Files.readAllLines(Paths.get(resource.toURI()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean solve(Region region, Map<Integer, Shape> shapes) {
        long totalPresentArea = 0;
        List<Integer> shapeIdsToPlace = new ArrayList<>();
        
        for (Map.Entry<Integer, Integer> entry : region.requirements.entrySet()) {
            int shapeId = entry.getKey();
            int countNeeded = entry.getValue();
            if (countNeeded > 0) {
                totalPresentArea += (long) shapes.get(shapeId).area * countNeeded;
                for (int i = 0; i < countNeeded; i++) {
                    shapeIdsToPlace.add(shapeId);
                }
            }
        }

        if (totalPresentArea > region.regionArea) {
            return false;
        }
        
        if (region.regionArea <= 200) { 
             shapeIdsToPlace.sort((a, b) -> Integer.compare(shapes.get(b).area, shapes.get(a).area));
             return canFit(region, shapes, shapeIdsToPlace);
        }

        return true;
    }

    private boolean canFit(Region region, Map<Integer, Shape> shapes, List<Integer> shapeIds) {
        boolean[][] grid = new boolean[region.width][region.height];
        return backtrack(grid, shapes, shapeIds, 0);
    }

    private boolean backtrack(boolean[][] grid, Map<Integer, Shape> shapes, List<Integer> shapeIds, int index) {
        if (index == shapeIds.size()) {
            return true;
        }

        int shapeId = shapeIds.get(index);
        Shape shape = shapes.get(shapeId);
        
        for (ShapeVariant variant : shape.variants) {
            int maxX = grid.length - variant.width;
            int maxY = grid[0].length - variant.height;

            for (int x = 0; x <= maxX; x++) {
                for (int y = 0; y <= maxY; y++) {
                    if (canPlace(grid, variant, x, y)) {
                        place(grid, variant, x, y, true);
                        if (backtrack(grid, shapes, shapeIds, index + 1)) {
                            return true;
                        }
                        place(grid, variant, x, y, false);
                    }
                }
            }
        }
        
        return false;
    }

    private boolean canPlace(boolean[][] grid, ShapeVariant variant, int x, int y) {
        for (Point p : variant.points) {
            if (grid[x + p.x()][y + p.y()]) {
                return false;
            }
        }
        return true;
    }

    private void place(boolean[][] grid, ShapeVariant variant, int x, int y, boolean val) {
        for (Point p : variant.points) {
            grid[x + p.x()][y + p.y()] = val;
        }
    }

    @Override
    public Long getPart2Result(String fileName) {
         return 0L;
    }
}
