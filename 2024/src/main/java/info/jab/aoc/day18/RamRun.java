package info.jab.aoc.day18;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.putoet.resources.ResourceLines;

public class RamRun {

    private int[][] getInputData(String fileName) {
        var list = ResourceLines.list(fileName);
        return list.stream()
            .map(line -> line.split(","))
            .map(parts -> new int[]{Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim())})
            .toArray(int[][]::new);
    }

    private int findShortestPath(boolean[][] grid, int gridSize, List<int[]> path) {
        // Directions for moving up, down, left, and right
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        // BFS queue to store the current position and steps taken
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{0, 0, 0}); // Start at (0, 0) with 0 steps

        // Visited array to avoid revisiting cells
        boolean[][] visited = new boolean[gridSize][gridSize];
        visited[0][0] = true;

        // Parent map to reconstruct the path
        Map<String, int[]> parentMap = new HashMap<>();

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];
            int steps = current[2];

            // Check if we've reached the bottom-right corner
            if (x == gridSize - 1 && y == gridSize - 1) {
                reconstructPath(parentMap, path, x, y);
                return steps;
            }

            // Explore all possible moves
            for (int[] direction : directions) {
                int newX = x + direction[0];
                int newY = y + direction[1];

                // Check bounds and if the cell is safe to enter
                if (newX >= 0 && newX < gridSize && newY >= 0 && newY < gridSize
                        && !grid[newY][newX] && !visited[newY][newX]) {
                    queue.add(new int[]{newX, newY, steps + 1});
                    visited[newY][newX] = true;
                    parentMap.put(newX + "," + newY, new int[]{x, y});
                }
            }
        }

        // If we exhaust the queue without reaching the target, it's unreachable
        return -1;
    }

    private void reconstructPath(Map<String, int[]> parentMap, List<int[]> path, int x, int y) {
        while (parentMap.containsKey(x + "," + y)) {
            path.add(0, new int[]{x, y});
            int[] parent = parentMap.get(x + "," + y);
            x = parent[0];
            y = parent[1];
        }
        path.add(0, new int[]{0, 0}); // Add the starting point
    }

    private void visualizeGrid(boolean[][] grid, int gridSize, List<int[]> path) {
        char[][] visualGrid = new char[gridSize][gridSize];

        // Initialize the grid with '.' for safe cells and '#' for corrupted cells
        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                visualGrid[y][x] = grid[y][x] ? '#' : '.';
            }
        }

        // Mark the path with 'O'
        for (int[] coord : path) {
            visualGrid[coord[1]][coord[0]] = 'O';
        }

        // Print the grid
        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                System.out.print(visualGrid[y][x]);
            }
            System.out.println();
        }
    }

    private String initializeCorruptedGrid(boolean[][] grid, int[][] corruptedCoordinates, int limit) {
        var solution = "";
        for (int i = 0; i < limit; i++) {
            int[] coord = corruptedCoordinates[i];
            solution = "" + coord[0] + "," + coord[1];
            grid[coord[1]][coord[0]] = true; // Mark corrupted cells
        }
        return solution;
    }

    private boolean[][] initializePoints(boolean[][] grid, int gridSize) {
        grid[0][0] = false; // Start point
        grid[gridSize - 1][gridSize - 1] = false; // Exit point

        return grid;
    }

    public String solve1(String fileName, Integer gridSize, Integer limit, Boolean debug) {
        // Input: list of corrupted coordinates
        int[][] corruptedCoordinates = getInputData(fileName);

        // Define the grid size
        boolean[][] grid = new boolean[gridSize][gridSize];
        initializeCorruptedGrid(grid, corruptedCoordinates, limit); //I don´t like to mutate input parameter

        grid = initializePoints(grid, gridSize);

        // Find the shortest path using BFS
        List<int[]> path = new ArrayList<>();
        int shortestPath = findShortestPath(grid, gridSize, path);

        // Output the result
        if (shortestPath != -1) {
            System.out.println("Minimum number of steps to reach the exit: " + shortestPath);
            if(debug) {
                visualizeGrid(grid, gridSize, path);
            }
        } else {
            System.out.println("The exit is unreachable.");
            if(debug) {
                visualizeGrid(grid, gridSize, path);
            }
        }
        return String.valueOf(shortestPath);
    }

    public String solve2(String fileName, Integer gridSize, Integer limit, Boolean debug) {
        // Input: list of corrupted coordinates
        int[][] corruptedCoordinates = getInputData(fileName);

        // Define the grid size
        boolean[][] grid = new boolean[gridSize][gridSize];

        //Big loop
        String solution = "";
        int until = corruptedCoordinates.length - limit;
        for(int x = 0; x < until; x++) {

            //Reset grid in any iteration
            grid = new boolean[gridSize][gridSize];
            solution = initializeCorruptedGrid(grid, corruptedCoordinates, limit + x);

            grid = initializePoints(grid, gridSize);

            // Find the shortest path using BFS
            List<int[]> path = new ArrayList<>();
            int shortestPath = findShortestPath(grid, gridSize, path);

            // Output the result
            if (shortestPath != -1) {
                if(debug) {
                    System.out.println("Minimum number of steps to reach the exit: " + shortestPath);
                    visualizeGrid(grid, gridSize, path);
                }
            } else {
                System.out.println("The exit is unreachable.");
                if(debug) {
                    visualizeGrid(grid, gridSize, path);
                }

                //If we are not able to find a path, we found the solution
                break;
            }
        }
        return solution;
    }
}
