package info.jab.aoc.day16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

//KO
public class ReindeerMaze2 {

    public Set<int[]> findOptimalTiles(char[][] maze) {
        int rows = maze.length, cols = maze[0].length;
        int startX = -1, startY = -1, endX = -1, endY = -1;

        // Find Start and End
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 'S') {
                    startX = i;
                    startY = j;
                } else if (maze[i][j] == 'E') {
                    endX = i;
                    endY = j;
                }
            }
        }

        // Priority Queue for A* search
        PriorityQueue<State2> pq = new PriorityQueue<>();
        Map<String, Integer> visited = new HashMap<>();
        Set<String> optimalTileKeys = new HashSet<>(); // Use unique string keys for tiles
        int minScore = Integer.MAX_VALUE;

        // Start state
        pq.add(new State2(startX, startY, Direction.EAST, 0, new ArrayList<>()));

        // A* directions for moving forward
        int[][] deltas = {{0, 1}, {-1, 0}, {0, -1}, {1, 0}}; // East, North, West, South

        while (!pq.isEmpty()) {
            State2 current = pq.poll();

            // Check if we've reached the end
            if (current.x == endX && current.y == endY) {
                if (current.score < minScore) {
                    minScore = current.score;
                    optimalTileKeys.clear(); // Reset tiles for new best score
                }

                if (current.score == minScore) {
                    // Add this path's tiles to the optimal set
                    for (int[] tile : current.path) {
                        if (maze[tile[0]][tile[1]] != '#') { // Ensure walls are not added
                            optimalTileKeys.add(tile[0] + "," + tile[1]);
                        }
                    }
                    continue; // Skip processing further
                }
            }

            // Generate a unique key for this state
            String stateKey = current.x + "," + current.y + "," + current.direction;
            if (!visited.containsKey(stateKey) || visited.get(stateKey) > current.score) {
                visited.put(stateKey, current.score);
            } else {
                continue; // Skip if we've seen a better or equal state
            }

            // Try moving forward
            int dirIndex = current.direction.ordinal();
            int newX = current.x + deltas[dirIndex][0];
            int newY = current.y + deltas[dirIndex][1];

            if (isValid(maze, newX, newY)) {
                List<int[]> newPath = new ArrayList<>(current.path);
                newPath.add(new int[]{newX, newY});
                pq.add(new State2(newX, newY, current.direction, current.score + 1, newPath));
            }

            // Try rotating clockwise
            Direction cw = current.direction.rotateClockwise();
            pq.add(new State2(current.x, current.y, cw, current.score + 1000, current.path));

            // Try rotating counterclockwise
            Direction ccw = current.direction.rotateCounterClockwise();
            pq.add(new State2(current.x, current.y, ccw, current.score + 1000, current.path));
        }

        // Convert optimalTileKeys back to integer coordinates
        Set<int[]> optimalTiles = new HashSet<>();
        for (String key : optimalTileKeys) {
            String[] parts = key.split(",");
            optimalTiles.add(new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])});
        }

        return optimalTiles;
    }

    private boolean isValid(char[][] maze, int x, int y) {
        return x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y] != '#';
    }

}

