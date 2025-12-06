package info.jab.aoc2015.day18;

import com.putoet.grid.Grid;
import com.putoet.grid.GridDirections;
import com.putoet.grid.GridUtils;
import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.List;

public class LightGrid implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        return solvePartOneWithSteps(fileName, 100);
    }
    
    public Integer solvePartOneWithSteps(String fileName, int steps) {
        List<String> lines = ResourceLines.list("/" + fileName);
        
        // Parse the initial grid
        Grid grid = parseGrid(lines);
        
        // Simulate steps
        for (int step = 0; step < steps; step++) {
            grid = simulateStep(grid);
        }
        
        // Count lights that are on
        return (int) grid.count('#');
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        return solvePartTwoWithSteps(fileName, 100);
    }
    
    public Integer solvePartTwoWithSteps(String fileName, int steps) {
        List<String> lines = ResourceLines.list("/" + fileName);
        
        // Parse the initial grid
        Grid grid = parseGrid(lines);
        
        // Turn on corner lights (stuck on)
        turnOnCorners(grid);
        
        // Simulate steps
        for (int step = 0; step < steps; step++) {
            grid = simulateStepWithStuckCorners(grid);
        }
        
        // Count lights that are on
        return (int) grid.count('#');
    }
    
    private Grid parseGrid(List<String> lines) {
        char[][] gridData = GridUtils.of(lines);
        return new Grid(gridData);
    }
    
    private Grid simulateStep(Grid grid) {
        Grid newGrid = grid.copy();
        
        for (int y = grid.minY(); y < grid.maxY(); y++) {
            for (int x = grid.minX(); x < grid.maxX(); x++) {
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
    
    
    private void turnOnCorners(Grid grid) {
        int rows = grid.height();
        int cols = grid.width();
        
        // Turn on the four corners
        grid.set(grid.minX(), grid.minY(), '#');           // Top-left
        grid.set(grid.minX() + cols - 1, grid.minY(), '#');    // Top-right
        grid.set(grid.minX(), grid.minY() + rows - 1, '#');    // Bottom-left
        grid.set(grid.minX() + cols - 1, grid.minY() + rows - 1, '#'); // Bottom-right
    }
    
    private Grid simulateStepWithStuckCorners(Grid grid) {
        Grid newGrid = grid.copy();
        int rows = grid.height();
        int cols = grid.width();
        
        for (int y = grid.minY(); y < grid.maxY(); y++) {
            for (int x = grid.minX(); x < grid.maxX(); x++) {
                // Check if this is a corner (stuck on)
                if (isCorner(x, y, grid.minX(), grid.minY(), rows, cols)) {
                    newGrid.set(x, y, '#'); // Corners are always on
                } else {
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
        }
        
        return newGrid;
    }
    
    private boolean isCorner(int x, int y, int minX, int minY, int rows, int cols) {
        return (x == minX && y == minY) ||           // Top-left
               (x == minX + cols - 1 && y == minY) ||    // Top-right
               (x == minX && y == minY + rows - 1) ||    // Bottom-left
               (x == minX + cols - 1 && y == minY + rows - 1); // Bottom-right
    }
}