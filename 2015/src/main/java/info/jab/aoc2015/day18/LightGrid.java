package info.jab.aoc2015.day18;

import com.putoet.grid.Grid;
import com.putoet.grid.GridDirections;
import com.putoet.grid.GridUtils;
import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LightGrid implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        return solvePartOneWithSteps(fileName, 100);
    }

    public Integer solvePartOneWithSteps(String fileName, int steps) {
        List<String> lines = ResourceLines.list(fileName);
        Grid grid = parseGrid(lines);

        // Simulate steps
        for (int step = 0; step < steps; step++) {
            grid = simulateStep(grid, false);
        }

        return (int) grid.count('#');
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        return solvePartTwoWithSteps(fileName, 100);
    }

    public Integer solvePartTwoWithSteps(String fileName, int steps) {
        List<String> lines = ResourceLines.list(fileName);
        Grid grid = parseGrid(lines);

        // Turn on corner lights (stuck on) and cache corner positions
        Set<String> cornerPositions = turnOnCorners(grid);

        // Simulate steps with stuck corners
        for (int step = 0; step < steps; step++) {
            grid = simulateStep(grid, true, cornerPositions);
        }

        return (int) grid.count('#');
    }

    private Grid parseGrid(List<String> lines) {
        char[][] gridData = GridUtils.of(lines);
        return new Grid(gridData);
    }

    private Grid simulateStep(Grid grid, boolean hasStuckCorners) {
        return simulateStep(grid, hasStuckCorners, null);
    }

    private Grid simulateStep(Grid grid, boolean hasStuckCorners, Set<String> cornerPositions) {
        Grid newGrid = grid.copy();
        int minX = grid.minX();
        int minY = grid.minY();
        int maxX = grid.maxX();
        int maxY = grid.maxY();

        for (int y = minY; y < maxY; y++) {
            for (int x = minX; x < maxX; x++) {
                // Check if this is a stuck corner
                if (hasStuckCorners && cornerPositions != null && isCorner(x, y, cornerPositions)) {
                    newGrid.set(x, y, '#'); // Corners are always on
                    continue;
                }

                // Apply Conway's Game of Life rules
                int neighbors = GridDirections.countNeighbors(grid, x, y, c -> c == '#', true);
                char currentState = grid.get(x, y);

                if (currentState == '#') {
                    // Light is on: stays on if 2 or 3 neighbors are on
                    newGrid.set(x, y, (neighbors == 2 || neighbors == 3) ? '#' : '.');
                } else {
                    // Light is off: turns on if exactly 3 neighbors are on
                    newGrid.set(x, y, (neighbors == 3) ? '#' : '.');
                }
            }
        }

        return newGrid;
    }

    private Set<String> turnOnCorners(Grid grid) {
        int minX = grid.minX();
        int minY = grid.minY();
        int maxX = grid.maxX();
        int maxY = grid.maxY();

        // Calculate corner positions once and cache them
        Set<String> cornerPositions = new HashSet<>();
        cornerPositions.add(key(minX, minY));                    // Top-left
        cornerPositions.add(key(maxX - 1, minY));                // Top-right
        cornerPositions.add(key(minX, maxY - 1));                 // Bottom-left
        cornerPositions.add(key(maxX - 1, maxY - 1));            // Bottom-right

        // Turn on the four corners directly
        grid.set(minX, minY, '#');
        grid.set(maxX - 1, minY, '#');
        grid.set(minX, maxY - 1, '#');
        grid.set(maxX - 1, maxY - 1, '#');

        return cornerPositions;
    }

    private boolean isCorner(int x, int y, Set<String> cornerPositions) {
        return cornerPositions.contains(key(x, y));
    }

    private String key(int x, int y) {
        return x + "," + y;
    }
}
