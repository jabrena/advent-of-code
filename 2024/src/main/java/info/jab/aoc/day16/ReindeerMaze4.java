package info.jab.aoc.day16;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

//KO
public class ReindeerMaze4 {

    private static class Coord {
        int x, y;

        Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Coord add(Coord other) {
            return new Coord(this.x + other.x, this.y + other.y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coord coord = (Coord) o;
            return x == coord.x && y == coord.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private static class State {
        Coord position;
        Direction direction;
        int cost;
        Set<Coord> visited;

        State(Coord position, Direction direction, int cost, Set<Coord> visited) {
            this.position = position;
            this.direction = direction;
            this.cost = cost;
            this.visited = visited;
        }

        List<State> nextStates(char[][] maze) {
            List<State> nextStates = new ArrayList<>();

            // Move forward
            Coord newPos = position.add(direction.toCoord());
            if (isValid(maze, newPos)) {
                Set<Coord> newVisited = new HashSet<>(visited);
                newVisited.add(newPos);
                nextStates.add(new State(newPos, direction, cost + 1, newVisited));
            }

            // Rotate clockwise
            nextStates.add(new State(position, direction.rotateClockwise(), cost + 1000, new HashSet<>(visited)));

            // Rotate counter-clockwise
            nextStates.add(new State(position, direction.rotateCounterClockwise(), cost + 1000, new HashSet<>(visited)));

            return nextStates;
        }

        private boolean isValid(char[][] maze, Coord pos) {
            return pos.x >= 0 && pos.x < maze.length && pos.y >= 0 && pos.y < maze[0].length && maze[pos.x][pos.y] != '#';
        }
    }

    private enum Direction {
        EAST(1, 0), NORTH(0, -1), WEST(-1, 0), SOUTH(0, 1);

        final int dx, dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        Coord toCoord() {
            return new Coord(dx, dy);
        }

        Direction rotateClockwise() {
            switch (this) {
                case EAST: return SOUTH;
                case SOUTH: return WEST;
                case WEST: return NORTH;
                case NORTH: return EAST;
                default: throw new IllegalStateException("Unknown direction");
            }
        }

        Direction rotateCounterClockwise() {
            switch (this) {
                case EAST: return NORTH;
                case NORTH: return WEST;
                case WEST: return SOUTH;
                case SOUTH: return EAST;
                default: throw new IllegalStateException("Unknown direction");
            }
        }
    }

    public Set<int[]> findOptimalTiles(char[][] maze) {
        int rows = maze.length, cols = maze[0].length;
        Coord start = null, end = null;

        // Find start (S) and end (E)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 'S') start = new Coord(i, j);
                else if (maze[i][j] == 'E') end = new Coord(i, j);
            }
        }

        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> s.cost));
        Map<String, Integer> seen = new HashMap<>(); // Track states with position and visited info
        Set<Coord> bestPathPoints = new HashSet<>();
        int minCost = Integer.MAX_VALUE;

        // Start state
        pq.add(new State(start, Direction.EAST, 0, new HashSet<>(Set.of(start))));
        seen.put(startKey(start, new HashSet<>(Set.of(start))), 0);

        while (!pq.isEmpty()) {
            State current = pq.poll();

            // If we reached the end
            if (current.position.equals(end)) {
                if (current.cost < minCost) {
                    minCost = current.cost;
                    bestPathPoints.clear(); // Reset for the new best cost
                }
                if (current.cost == minCost) {
                    bestPathPoints.addAll(current.visited);
                }
                continue;
            }

            // Explore next states
            for (State nextState : current.nextStates(maze)) {
                String key = startKey(nextState.position, nextState.visited);
                if (nextState.cost <= seen.getOrDefault(key, Integer.MAX_VALUE)) {
                    seen.put(key, nextState.cost);
                    pq.add(nextState);
                }
            }
        }

        // Convert bestPathPoints to int[]
        Set<int[]> optimalTiles = new HashSet<>();
        for (Coord tile : bestPathPoints) {
            optimalTiles.add(new int[]{tile.x, tile.y});
        }

        return optimalTiles;
    }

    // Helper: Generate a unique key for position and visited
    private String startKey(Coord position, Set<Coord> visited) {
        return position.x + "," + position.y + "-" + visited.hashCode();
    }

}
