package info.jab.aoc2015.day18;

import info.jab.aoc.Solver;
import info.jab.aoc.Utils;

import java.util.List;

public class LightGrid implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        return solvePartOneWithSteps(fileName, 100);
    }
    
    public Integer solvePartOneWithSteps(String fileName, int steps) {
        List<String> lines = Utils.readFileToList(fileName);
        
        // Parse the initial grid
        boolean[][] grid = parseGrid(lines);
        
        // Simulate steps
        for (int step = 0; step < steps; step++) {
            grid = simulateStep(grid);
        }
        
        // Count lights that are on
        return countLightsOn(grid);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        return solvePartTwoWithSteps(fileName, 100);
    }
    
    public Integer solvePartTwoWithSteps(String fileName, int steps) {
        List<String> lines = Utils.readFileToList(fileName);
        
        // Parse the initial grid
        boolean[][] grid = parseGrid(lines);
        
        // Turn on corner lights (stuck on)
        turnOnCorners(grid);
        
        // Simulate steps
        for (int step = 0; step < steps; step++) {
            grid = simulateStepWithStuckCorners(grid);
        }
        
        // Count lights that are on
        return countLightsOn(grid);
    }
    
    private boolean[][] parseGrid(List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).trim().length();
        boolean[][] grid = new boolean[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            String line = lines.get(i).trim();
            int lineLength = Math.min(line.length(), cols);
            for (int j = 0; j < lineLength; j++) {
                grid[i][j] = line.charAt(j) == '#';
            }
        }
        
        return grid;
    }
    
    private boolean[][] simulateStep(boolean[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] newGrid = new boolean[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int neighbors = countNeighbors(grid, i, j);
                boolean currentState = grid[i][j];
                
                if (currentState) {
                    // Light is on: stays on if 2 or 3 neighbors are on
                    newGrid[i][j] = (neighbors == 2 || neighbors == 3);
                } else {
                    // Light is off: turns on if exactly 3 neighbors are on
                    newGrid[i][j] = (neighbors == 3);
                }
            }
        }
        
        return newGrid;
    }
    
    private int countNeighbors(boolean[][] grid, int row, int col) {
        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;
        
        // Check all 8 adjacent cells (including diagonals)
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                if (di == 0 && dj == 0) continue; // Skip the current cell
                
                int newRow = row + di;
                int newCol = col + dj;
                
                // Check bounds and count if light is on
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                    if (grid[newRow][newCol]) {
                        count++;
                    }
                }
            }
        }
        
        return count;
    }
    
    private int countLightsOn(boolean[][] grid) {
        int count = 0;
        for (boolean[] row : grid) {
            for (boolean light : row) {
                if (light) count++;
            }
        }
        return count;
    }
    
    private void printGrid(boolean[][] grid) {
        for (boolean[] row : grid) {
            for (boolean light : row) {
                System.out.print(light ? '#' : '.');
            }
            System.out.println();
        }
        System.out.println();
    }
    
    private void turnOnCorners(boolean[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        
        // Turn on the four corners
        grid[0][0] = true;           // Top-left
        grid[0][cols - 1] = true;    // Top-right
        grid[rows - 1][0] = true;    // Bottom-left
        grid[rows - 1][cols - 1] = true; // Bottom-right
    }
    
    private boolean[][] simulateStepWithStuckCorners(boolean[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] newGrid = new boolean[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Check if this is a corner (stuck on)
                if (isCorner(i, j, rows, cols)) {
                    newGrid[i][j] = true; // Corners are always on
                } else {
                    int neighbors = countNeighbors(grid, i, j);
                    boolean currentState = grid[i][j];
                    
                    if (currentState) {
                        // Light is on: stays on if 2 or 3 neighbors are on
                        newGrid[i][j] = (neighbors == 2 || neighbors == 3);
                    } else {
                        // Light is off: turns on if exactly 3 neighbors are on
                        newGrid[i][j] = (neighbors == 3);
                    }
                }
            }
        }
        
        return newGrid;
    }
    
    private boolean isCorner(int row, int col, int rows, int cols) {
        return (row == 0 && col == 0) ||           // Top-left
               (row == 0 && col == cols - 1) ||    // Top-right
               (row == rows - 1 && col == 0) ||    // Bottom-left
               (row == rows - 1 && col == cols - 1); // Bottom-right
    }
}