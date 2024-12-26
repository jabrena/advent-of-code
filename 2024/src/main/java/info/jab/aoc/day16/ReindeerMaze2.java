package info.jab.aoc.day16;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;

public class ReindeerMaze2 {

    private final Set<Point> bestPathPoints = new HashSet<>();
    private int minPrice = Integer.MAX_VALUE;

    public ReindeerMaze2(String fileName) {
        var list = ResourceLines.list(fileName);
        Grid grid = new Grid(GridUtils.of(list));
        Point start = grid.findFirst(c -> c == 'S').orElseThrow();
        Point end = grid.findFirst(c -> c == 'E').orElseThrow();
        findPaths(grid, start, end);
    }

    public record Results(Integer numberOfBestPaths, Integer minPrice) {}

    /**
     * Dijkstra's algorithm combined with priority-based pathfinding
     *
     * @param grid
     * @param start
     * @param end
     */
    private void findPaths(Grid grid, Point start, Point end) {
        Map<Direction, Integer> seen = new HashMap<>();
        Queue<State> priorityQueue = new PriorityQueue<>(State.PRICE_COMPARATOR);
        Map<Point, Set<Point>> bests = new HashMap<>();

        Direction startDirection = new Direction(start, Point.EAST);
        seen.put(startDirection, 0);

        priorityQueue.add(new State(startDirection, 0, new HashSet<>(List.of(start))));
        boolean weAreOverBestPaths = false;
        while (!priorityQueue.isEmpty() && !weAreOverBestPaths) {
            State state = priorityQueue.poll();
            for (State next : state.next()) {
                if (isValid(grid, next.direction.position) && next.price <= seen.getOrDefault(next.direction, Integer.MAX_VALUE)) {
                    if (next.price < seen.getOrDefault(next.direction, Integer.MAX_VALUE)) {
                        priorityQueue.add(next);
                        seen.put(next.direction, next.price);
                        if (end.equals(next.direction.position)) {
                            if (minPrice < next.price) {
                                weAreOverBestPaths = true;
                            }
                            minPrice = Math.min(minPrice, next.price);
                            bestPathPoints.addAll(next.visited);
                        }
                        bests.put(next.direction.position, next.visited);
                    } else {
                        bests.get(next.direction.position).addAll(next.visited);
                    }
                }
            }
        }
    }

    private boolean isValid(Grid grid, Point position) {
        int x = position.x();
        int y = position.y();
        return x >= 0 &&
               x < grid.maxY() &&
               y >= 0 &&
               y < grid.maxX() &&
               grid.get(y, x) != '#';
    }

    private record Direction(Point position, Point orientation) {
        Direction turnLeft() {
            return new Direction(position, new Point(-orientation.y(), orientation.x()));
        }

        Direction turnRight() {
            return new Direction(position, new Point(orientation.y(), -orientation.x()));
        }

        Direction step() {
            return new Direction(position.add(orientation), orientation);
        }
    }

    private record State(Direction direction, int price, Set<Point> visited) {
        static Comparator<State> PRICE_COMPARATOR = Comparator.comparing(State::price);

        State turnRight() {
            return new State(direction.turnRight(), price + 1000, visited);
        }

        State turnLeft() {
            return new State(direction.turnLeft(), price + 1000, visited);
        }

        State step() {
            Direction step = direction.step();
            var v = new HashSet<>(visited);
            v.add(step.position);
            return new State(step, price + 1, v);
        }

        List<State> next() {
            return List.of(turnLeft(), turnRight(), step());
        }
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public Integer getBestPathPoints() {
        return bestPathPoints.size();
    }
}
