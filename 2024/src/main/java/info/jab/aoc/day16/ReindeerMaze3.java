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

public class ReindeerMaze3 {

    final Grid grid;

    private final Coord start;
    private final Coord end;

    private final Set<Coord> bestPathPoints = new HashSet<>();
    private final Map<Raindeer, Integer> seen = new HashMap<>();

    private int minPrice = Integer.MAX_VALUE;

    public ReindeerMaze3(String fileName) {
        var list = ResourceLines.list(fileName);
        grid = new Grid(GridUtils.of(list));
        Point ps = grid.findFirst(c -> c == 'S').get();
        Point pe = grid.findFirst(c -> c == 'E').get();

        start = new Coord(ps.x(), ps.y());
        end = new Coord(pe.x(), pe.y());
        findPaths();
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public Integer getBestPathPoints() {
        return bestPathPoints.size();
    }

    /*
     * This method is like a navigator finding the best route in a maze.
     * It explores all possible paths the reindeer can take, starting from the beginning (S) to the end (E).
     * Along the way, it keeps track of the shortest route (measured by cost) and remembers
     * all the key steps in that best path. It stops early if it knows there's no better path left to explore,
     * saving time.
     *
     * ---
     *
     * findPaths() method uses a priority queue-based approach, which is a key element of the Dijkstra's algorithm,
     * a classical pathfinding algorithm. Here's how it aligns with Dijkstra's principles:
     *
     * Priority Queue for Exploration:
     * The method uses a priority queue (priorityQueue) to always explore the most promising path next. Paths with a lower cost (or "price") are prioritized, just like in Dijkstra's algorithm.
     *
     * Tracking Costs:
     * The seen map keeps track of the lowest cost required to reach each state (a combination of reindeer position and orientation). This ensures that the algorithm avoids revisiting states with higher costs, optimizing the search.
     *
     * Expanding Paths:
     * At each step, the method evaluates all possible next moves (turnLeft, turnRight, and step).
     * This is similar to Dijkstra's algorithm exploring neighboring nodes from the current position.
     *
     * Early Termination:
     * The loop stops early when it determines that further exploration won’t yield a better path,
     * which is an optimization often added to Dijkstra’s algorithm.
     *
     * Differences from Classical Dijkstra
     * While findPaths() borrows heavily from Dijkstra, it also:
     *
     * Tracks additional information like the full set of visited points in the best path.
     * Considers both movement cost (step) and turning cost, making it a variant customized
     * for this specific maze-solving problem.
     */
    private void findPaths() {
        Queue<State> priorityQueue = new PriorityQueue<>(State.PRICE_COMPARATOR);
        Map<Coord, Set<Coord>> bests = new HashMap<>();
        Raindeer startDeer = new Raindeer(start, Coord.EAST);
        seen.put(startDeer, 0);
        priorityQueue.add(new State(startDeer, 0, new HashSet<>(List.of(start))));
        boolean weAreOverBestPaths = false;
        while (!priorityQueue.isEmpty() && !weAreOverBestPaths) {
            State state = priorityQueue.poll();
            for (State next : state.next()) {
                if (isFree(next.raindeer.position) && next.price <= seen.getOrDefault(next.raindeer, Integer.MAX_VALUE)) {
                    if (next.price < seen.getOrDefault(next.raindeer, Integer.MAX_VALUE)) {
                        priorityQueue.add(next);
                        seen.put(next.raindeer, next.price);
                        if (end.equals(next.raindeer.position)) {
                            if (minPrice < next.price) {
                                weAreOverBestPaths = true;
                            }
                            minPrice = Math.min(minPrice, next.price);
                            bestPathPoints.addAll(next.visited);
                        }
                        bests.put(next.raindeer.position, next.visited);
                    } else {
                        bests.get(next.raindeer.position).addAll(next.visited);
                    }
                }
            }
        }
    }

    /**
     * This method checks if a specific spot in the maze is open for movement.
     * It ensures the reindeer doesn’t go off the grid or try to move into walls (#).
     * Think of it as verifying if a square is a valid step.
     *
     * @param position
     * @return boolean
     */
    private boolean isFree(Coord position) {
        return  position.x >= 0 &&
                position.x < grid.maxX() &&
                position.y >= 0 &&
                position.y < grid.maxY() &&
                grid.get(position.x, position.y) != '#';
    }

    /**
     * This represents a specific point on the maze grid, like a set of coordinates (x, y). It’s used to track locations.
     *
     * The key features include:
     * - EAST: A shortcut for moving one step to the right (used to indicate direction).
     * - add: Combines two coordinates to calculate a new position, like moving from one spot to another.
     *
     * Think of it as the reindeer’s "address" at any moment in the maze.
     */
    private record Coord(int x, int y) {
        static final Coord EAST = new Coord(1, 0);
        Coord add(Coord other) {
            return new Coord(this.x + other.x, this.y + other.y);
        }
    }

    /**
     * This represents the reindeer itself.
     * It keeps track of its current position and direction (like facing east, west, etc.).
     *
     * It also includes the reindeer’s abilities:
     * - turnLeft / turnRight: The reindeer can turn to a new direction.
     * - step: The reindeer can move one step forward.
     */
    private record Raindeer(Coord position, Coord orientation) {
        Raindeer turnLeft() {
            return new Raindeer(position, new Coord(-orientation.y, orientation.x));
        }

        Raindeer turnRight() {
            return new Raindeer(position, new Coord(orientation.y, -orientation.x));
        }

        Raindeer step() {
            return new Raindeer(position.add(orientation), orientation);
        }
    }

    /**
     * This represents a single moment in the reindeer’s journey.
     *
     * It includes:
     *
     * - Where the reindeer is.
     * - How much the journey has cost so far.
     * - All the spots the reindeer has visited. It also has actions like turning left, right, or stepping forward.
     *   Plus, it suggests the next possible steps the reindeer could take.
     */
    private record State(Raindeer raindeer, int price, Set<Coord> visited) {
        static Comparator<State> PRICE_COMPARATOR = Comparator.comparing(State::price);

        State turnLeft() {
            return new State(raindeer.turnLeft(), price + 1000, visited);
        }

        State turnRight() {
            return new State(raindeer.turnRight(), price + 1000, visited);
        }

        State step() {
            Raindeer step = raindeer.step();
            var v = new HashSet<>(visited);
            v.add(step.position);
            return new State(step, price + 1, v);
        }

        List<State> next() {
            return List.of(turnLeft(), turnRight(), step());
        }
    }
}
