package info.jab.aoc2024.day20;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.resources.ResourceLines;

public final class RaceCondition {

    /**
     * Calculates the number of possible cheat saves that can save at least the specified
     * number of picoseconds by leveraging shortcuts within the grid.
     *
     * @param fileName   The file containing the grid representation.
     * @param picoseconds Minimum time savings required to count as a cheat.
     * @param cheatTime  Maximum distance allowed for a cheat move.
     * @return The count of valid cheat saves.
     */
    public Integer countCheatSaves(final String fileName, final int picoseconds, final int cheatTime) {
        var list = ResourceLines.list(fileName);
        var grid = new Grid(GridUtils.of(list));
        var point1 = grid.findFirst(c -> c == 'S').orElseThrow();
        int[] startPosition = new int[]{point1.y(), point1.x()};

        var point2 = grid.findFirst(c -> c == 'E').orElseThrow();
        int[] endPosition = new int[]{point2.y(), point2.x()};

        List<String> path = findPath(grid, startPosition, endPosition);
        if (path.size() == 0) {
            return 0;
        }
        int pathSize = path.size();
        HashSet<String> cheatSpots = new HashSet<>();
        HashSet<String> visited = new HashSet<>();
        int count = 0;
        for (String keys: path) {
            int[] position = fromKeys(keys);
            visited.add(keys);
            int finalCount = count++;
            path.stream()
                .filter(candidate ->
                    distance(position, fromKeys(candidate)) <= cheatTime &&
                    !visited.contains(candidate))
                .forEach(candidate -> {
                    int newPosition = path.indexOf(candidate);
                    if (newPosition > 0) {
                        int finalTime = finalCount + distance(position, fromKeys(candidate)) + (pathSize - newPosition);
                        if (pathSize - finalTime >= picoseconds) {
                            cheatSpots.add(keys + "+" + candidate);
                        }
                    }
                 });
        }
        return cheatSpots.size();
    }

    /**
     * Calculates the Manhattan distance between two points on the grid.
     *
     * @param from Starting point coordinates.
     * @param to   Destination point coordinates.
     * @return The Manhattan distance between the two points.
     */
    private int distance(final int[] from, final int[] to) {
        return Math.abs(to[0] - from[0]) + Math.abs(to[1] - from[1]);
    }

    /**
     * Converts a grid position key (in "y_x" format) back into coordinate array format.
     *
     * @param keys The position key.
     * @return An integer array representing the position coordinates.
     */
    private int[] fromKeys(final String keys) {
        return new int[]{
                Integer.parseInt(keys.split("_")[0]),
                Integer.parseInt(keys.split("_")[1])
        };
    }

    /**
     * Finds the shortest path from the start position to the end position using a grid.
     * Employs Dijkstra's algorithm to determine the shortest path while accounting for valid moves.
     *
     * @param grid          The grid representation.
     * @param startPosition The starting coordinates.
     * @param endPosition   The ending coordinates.
     * @return A list of keys representing the path from start to end, or an empty list if no path exists.
     */
    private List<String> findPath(Grid grid, final int[] startPosition,  final int[] endPosition) {

        //Initialize distances
        int[][] distances = new int[grid.maxY()][grid.maxX()];
        for (int i = 0; i < grid.maxX(); i++) {
            for (int j = 0; j < grid.maxY(); j++) {
                distances[i][j] = Integer.MAX_VALUE;
            }
        }

        HashSet<String> visited = new HashSet<>();
        PriorityQueue<int[]> unvisited = new PriorityQueue<>(Comparator.comparingLong(a -> distances[a[0]][a[1]]));
        distances[startPosition[0]][startPosition[1]] = 0;

        HashMap<String, ArrayList<String>> paths = new HashMap<>();
        ArrayList<String> startList = new ArrayList<>();
        startList.add(asKey(startPosition));
        paths.put(asKey(startPosition), startList);
        unvisited.add(new int[]{startPosition[0], startPosition[1]});
        while (!unvisited.isEmpty()) {
            int[] point = unvisited.poll();
            if (!visited.contains(asKey(point))) {
                int currentDistance = distances[point[0]][point[1]];
                next(point, grid).forEach(position -> {
                    if (distances[position[0]][position[1]] > currentDistance + 1) {
                        distances[position[0]][position[1]] = currentDistance + 1;
                        ArrayList<String> newList = new ArrayList<>(paths.get(asKey(point)));
                        newList.add(asKey(position));
                        paths.put(asKey(position), newList);
                    }
                    unvisited.add(position);
                });
                visited.add(asKey(point));
            }
        }

        return paths.get(asKey(endPosition)).size() > 0 ? paths.get(asKey(endPosition)) : List.of();
    }

    /**
     * Converts a position array into a unique key string in the format "y_x".
     *
     * @param position The position coordinates.
     * @return A string key representing the position.
     */
    private String asKey(final int[] position) {
        return (position[0] + "_" +  position[1]);
    }

    /**
     * Determines valid neighboring positions from the current position based on grid boundaries and allowed cells.
     *
     * @param currentPosition The current position coordinates.
     * @param grid            The grid representation.
     * @return A list of valid neighboring positions.
     */
    private List<int[]> next(final int[] currentPosition, Grid grid) {
        return Arrays.stream(new int[][] {
                        new int[]{currentPosition[0] - 1, currentPosition[1]},
                        new int[]{currentPosition[0], currentPosition[1] + 1},
                        new int[]{currentPosition[0] + 1, currentPosition[1]},
                        new int[]{currentPosition[0], currentPosition[1] - 1}
                })
                .filter(position ->
                        position[0] >= 0 &&
                        position[0] < grid.maxY() &&
                        position[1] >= 0 && position[1] < grid.maxX()  &&
                        ('.' == grid.get(position[1], position[0]) ||
                         'E' == grid.get(position[1], position[0]) ||
                         'S' == grid.get(position[1], position[0]))
                ).toList();
    }
}
