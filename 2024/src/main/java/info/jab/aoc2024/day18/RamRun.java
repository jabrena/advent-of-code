package info.jab.aoc2024.day18;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.resources.ResourceLines;

public class RamRun {

    private int[][] getInputData(String fileName) {
        var list = ResourceLines.list(fileName);
        return list.stream()
            .map(line -> line.split(","))
            .map(parts -> new int[]{Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim())})
            .toArray(int[][]::new);
    }

    private int findShortestPath(Grid grid, int gridSize, List<int[]> path) {
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
                if (grid.contains(newX, newY) && grid.get(newX, newY) != '#' && !visited[newY][newX]) {
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

    private void visualizeGrid(Grid grid, List<int[]> path) {
        Grid visualGrid = grid.copy();

        // Mark the path with 'O'
        for (int[] coord : path) {
            visualGrid.set(coord[0], coord[1], 'O');
        }

        // Print the grid
        for (int y = visualGrid.minY(); y < visualGrid.maxY(); y++) {
            for (int x = visualGrid.minX(); x < visualGrid.maxX(); x++) {
                System.out.print(visualGrid.get(x, y));
            }
            System.out.println();
        }
    }

    private String initializeCorruptedGrid(Grid grid, int[][] corruptedCoordinates, int limit) {
        var solution = "";
        for (int i = 0; i < limit; i++) {
            int[] coord = corruptedCoordinates[i];
            solution = "" + coord[0] + "," + coord[1];
            grid.set(coord[0], coord[1], '#'); // Mark corrupted cells
        }
        return solution;
    }

    private Grid initializePoints(Grid grid, int gridSize) {
        grid.set(0, 0, '.'); // Start point
        grid.set(gridSize - 1, gridSize - 1, '.'); // Exit point

        return grid;
    }

    public String solve1(String fileName, Integer gridSize, Integer limit, boolean debug) {
        // Input: list of corrupted coordinates
        int[][] corruptedCoordinates = getInputData(fileName);

        // Define the grid size
        Grid grid = new Grid(GridUtils.of(0, gridSize, 0, gridSize, '.'));
        initializeCorruptedGrid(grid, corruptedCoordinates, limit); //I don´t like to mutate input parameter

        initializePoints(grid, gridSize);

        // Find the shortest path using BFS
        List<int[]> path = new ArrayList<>();
        int shortestPath = findShortestPath(grid, gridSize, path);

        // Output the result
        if (shortestPath != -1) {
            System.out.println("Minimum number of steps to reach the exit: " + shortestPath);
            if(debug) {
                visualizeGrid(grid, path);
            }
        } else {
            System.out.println("The exit is unreachable.");
            if(debug) {
                visualizeGrid(grid, path);
            }
        }
        return String.valueOf(shortestPath);
    }

    //TODO Use the method solve1 inside of solve2 directly
    public String solve2(String fileName, Integer gridSize, Integer limit, boolean debug) {
        // Input: list of corrupted coordinates
        int[][] corruptedCoordinates = getInputData(fileName);

        //Big loop
        int until = corruptedCoordinates.length - limit;
        String solution = "";
        for(int x = 0; x < until; x++) {

            //Reset grid in any iteration
            Grid grid = new Grid(GridUtils.of(0, gridSize, 0, gridSize, '.'));
            solution = initializeCorruptedGrid(grid, corruptedCoordinates, limit + x);

            initializePoints(grid, gridSize);

            // Find the shortest path using BFS
            List<int[]> path = new ArrayList<>();
            int shortestPath = findShortestPath(grid, gridSize, path);

            // Output the result
            if (shortestPath != -1) {
                if(debug) {
                    System.out.println("Minimum number of steps to reach the exit: " + shortestPath);
                    visualizeGrid(grid, path);
                }
            } else {
                System.out.println("The exit is unreachable.");
                if(debug) {
                    visualizeGrid(grid, path);
                }

                //If we are not able to find a path, we found the solution
                break;
            }
        }
        return solution;
    }
}
