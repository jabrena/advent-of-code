package info.jab.aoc2024.day12;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.stream.IntStream;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

//TODO Pending to be simplified
public class GardenGroups implements Solver<Integer> {

    private record RegionPart1(int area, int perimeter) {}
    private record RegionPart2(int area, int sides) {}

    private int calculateTotalPricePart1(char[][] garden) {
        int rows = garden.length;
        int cols = garden[0].length;
        Set<Point> visited = new HashSet<>();

        return IntStream.range(0, rows)
                .flatMap(row -> IntStream.range(0, cols)
                        .filter(col -> !visited.contains(new Point(row, col)))
                        .mapToObj(col -> exploreRegionPart1(garden, visited, row, col))
                        .mapToInt(region -> region.area() * region.perimeter()))
                .sum();
    }

    private int calculateTotalPricePart2(char[][] garden) {
        int rows = garden.length;
        int cols = garden[0].length;
        Set<Point> visited = new HashSet<>();

        return IntStream.range(0, rows)
                .flatMap(row -> IntStream.range(0, cols)
                        .filter(col -> !visited.contains(new Point(row, col)))
                        .mapToObj(col -> exploreRegionPart2(garden, visited, row, col))
                        .mapToInt(region -> region.area() * region.sides()))
                .sum();
    }

    private RegionPart1 exploreRegionPart1(char[][] garden, Set<Point> visited, int startRow, int startCol) {
        int rows = garden.length;
        int cols = garden[0].length;
        char plantType = garden[startRow][startCol];
        int area = 0;
        int perimeter = 0;

        // Directions for movement (up, down, left, right)
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        Stack<Point> stack = new Stack<>();
        stack.push(new Point(startRow, startCol));

        while (!stack.isEmpty()) {
            Point cell = stack.pop();
            int row = cell.x();
            int col = cell.y();

            if (visited.contains(cell)) continue;

            visited.add(cell);
            area++;

            // Check all four directions
            for (int d = 0; d < 4; d++) {
                int newRow = row + dRow[d];
                int newCol = col + dCol[d];
                Point neighbor = new Point(newRow, newCol);

                if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols || garden[newRow][newCol] != plantType) {
                    // Increment perimeter if out of bounds or different plant type
                    perimeter++;
                } else if (!visited.contains(neighbor)) {
                    // Add to stack if not visited
                    stack.push(neighbor);
                }
            }
        }
        return new RegionPart1(area, perimeter);
    }

    private RegionPart2 exploreRegionPart2(char[][] garden, Set<Point> visited, int startRow, int startCol) {
        int rows = garden.length;
        int cols = garden[0].length;
        char plantType = garden[startRow][startCol];
        int area = 0;
        int sides = 0;

        // Directions for movement (up, down, left, right)
        int[] dRow = {-1, 1, 0, 0};
        int[] dCol = {0, 0, -1, 1};

        Stack<Point> stack = new Stack<>();
        stack.push(new Point(startRow, startCol));

        while (!stack.isEmpty()) {
            Point cell = stack.pop();
            int row = cell.x();
            int col = cell.y();

            if (visited.contains(cell)) continue;

            visited.add(cell);
            area++;

            // Check all four directions for direct neighbors
            for (int d = 0; d < 4; d++) {
                int newRow = row + dRow[d];
                int newCol = col + dCol[d];
                Point neighbor = new Point(newRow, newCol);

                if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
                    // Out of bounds contributes a side
                    sides++;
                } else if (garden[newRow][newCol] != plantType) {
                    // Different plant type contributes a side
                    sides++;
                } else if (!visited.contains(neighbor)) {
                    // Same type and not visited yet
                    stack.push(neighbor);
                }
            }
        }

        return new RegionPart2(area, sides);
    }

    @Override
    public Integer solvePartOne(String fileName) {
        var list = ResourceLines.list(fileName);
        Grid grid = new Grid(GridUtils.of(list));
        return calculateTotalPricePart1(grid.grid());
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'solvePartTwo'");
    }

}
