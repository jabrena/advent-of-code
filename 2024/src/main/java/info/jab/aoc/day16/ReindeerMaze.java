package info.jab.aoc.day16;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;

public class ReindeerMaze {

    public int findMinimumScore(String fileName) {
        var list = ResourceLines.list(fileName);
        Grid grid = new Grid(GridUtils.of(list));

        // Find Start and End
        Point start = grid.findFirst(c -> c == 'S').get();
        Point end = grid.findFirst(c -> c == 'E').get();

        // Execute the A* search
        return executeAStarSearch(grid, start, end);
    }

    private int executeAStarSearch(Grid grid, Point start, Point end) {
        PriorityQueue<State> pq = new PriorityQueue<>();
        Map<String, Integer> visited = new HashMap<>();

        // Start state
        pq.add(new State(start, Direction.EAST, 0));

        while (!pq.isEmpty()) {
            State current = pq.poll();

            // Check if we've reached the end
            if (current.position.equals(end)) {
                return current.score;
            }

            String stateKey = current.position.x() + "," + current.position.y() + "," + current.direction;
            if (visited.containsKey(stateKey) && visited.get(stateKey) <= current.score) {
                continue;
            }
            visited.put(stateKey, current.score);

            // Try moving forward
            Point forwardPosition = current.direction.move(current.position);
            if (isValid(grid, forwardPosition)) {
                pq.add(new State(forwardPosition, current.direction, current.score + 1));
            }

            // Try rotating clockwise
            Direction cw = current.direction.rotateClockwise();
            pq.add(new State(current.position, cw, current.score + 1000));

            // Try rotating counterclockwise
            Direction ccw = current.direction.rotateCounterClockwise();
            pq.add(new State(current.position, ccw, current.score + 1000));
        }

        return -1; // If no path is found
    }

    private boolean isValid(Grid grid, Point position) {
        int x = position.x();
        int y = position.y();
        return  x >= 0 &&
                x < grid.maxY() &&
                y >= 0 &&
                y < grid.maxX() &&
                grid.get(y, x) != '#';
    }

    enum Direction {
        EAST(0, 1),
        NORTH(-1, 0),
        WEST(0, -1),
        SOUTH(1, 0);

        private final int dx;
        private final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public Direction rotateClockwise() {
            return values()[(this.ordinal() + 1) % 4];
        }

        public Direction rotateCounterClockwise() {
            return values()[(this.ordinal() + 3) % 4];
        }

        public Point move(Point point) {
            return new Point(point.x() + dx, point.y() + dy);
        }
    }

    static class State implements Comparable<State> {
        final Point position;
        final Direction direction;
        final int score;

        State(Point position, Direction direction, int score) {
            this.position = position;
            this.direction = direction;
            this.score = score;
        }

        @Override
        public int compareTo(State other) {
            return Integer.compare(this.score, other.score);
        }
    }
}
