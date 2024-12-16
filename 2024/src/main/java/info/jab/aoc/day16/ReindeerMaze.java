package info.jab.aoc.day16;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class ReindeerMaze {

    public int findMinimumScore(char[][] maze) {
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
        PriorityQueue<State> pq = new PriorityQueue<>();
        Map<String, Integer> visited = new HashMap<>();

        // Start state
        pq.add(new State(startX, startY, Direction.EAST, 0));

        // A* directions for moving forward
        int[][] deltas = {{0, 1}, {-1, 0}, {0, -1}, {1, 0}}; // East, North, West, South

        while (!pq.isEmpty()) {
            State current = pq.poll();

            // Check if we've reached the end
            if (current.x == endX && current.y == endY) {
                return current.score;
            }

            String stateKey = current.x + "," + current.y + "," + current.direction;
            if (visited.containsKey(stateKey) && visited.get(stateKey) <= current.score) {
                continue;
            }
            visited.put(stateKey, current.score);

            // Try moving forward
            int dirIndex = current.direction.ordinal();
            int newX = current.x + deltas[dirIndex][0];
            int newY = current.y + deltas[dirIndex][1];

            if (isValid(maze, newX, newY)) {
                pq.add(new State(newX, newY, current.direction, current.score + 1));
            }

            // Try rotating clockwise
            Direction cw = current.direction.rotateClockwise();
            pq.add(new State(current.x, current.y, cw, current.score + 1000));

            // Try rotating counterclockwise
            Direction ccw = current.direction.rotateCounterClockwise();
            pq.add(new State(current.x, current.y, ccw, current.score + 1000));
        }

        return -1; // If no path is found
    }

    private boolean isValid(char[][] maze, int x, int y) {
        return x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y] != '#';
    }
}
