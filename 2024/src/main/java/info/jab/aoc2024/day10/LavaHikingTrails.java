package info.jab.aoc2024.day10;

import java.util.HashSet;
import java.util.Set;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class LavaHikingTrails implements Solver<Integer> {

    public static int sumTrailheadScores(Grid grid) {
        var points = grid.findAll(c -> c == '0');
        return points.stream()
            .map(p -> calculateTrailheadScore(grid.grid(), p.y(), p.x()))
            .reduce(0, Integer::sum);
    }

    public static int sumTrailheadRatings(Grid grid) {
        var points = grid.findAll(c -> c == '0');
        return points.stream()
            .map(p -> calculateTrailheadRating(grid.grid(), p.y(), p.x()))
            .reduce(0, Integer::sum);
    }

    private static int calculateTrailheadScore(char[][] map, int startRow, int startCol) {
        Set<String> visited = new HashSet<>();
        Set<String> reachableNines = new HashSet<>();
        dfsScore(map, startRow, startCol, 0, visited, reachableNines);
        return reachableNines.size();
    }

    private static int calculateTrailheadRating(char[][] map, int startRow, int startCol) {
        Set<String> visited = new HashSet<>();
        return dfsRating(map, startRow, startCol, 0, visited);
    }

    private static boolean isValidPosition(char[][] map, int row, int col) {
        return row >= 0 && row < map.length && col >= 0 && col < map[0].length;
    }

    private static void dfsScore(char[][] map, int row, int col, int currentHeight, Set<String> visited, Set<String> reachableNines) {
        String positionKey = row + "," + col;
        if (!isValidPosition(map, row, col) || visited.contains(positionKey)) {
            return;
        }

        int height = map[row][col] - '0';
        if (height != currentHeight) {
            return;
        }

        visited.add(positionKey);

        if (height == 9) {
            reachableNines.add(positionKey);
            return;
        }

        // Explore all four directions
        dfsScore(map, row + 1, col, height + 1, visited, reachableNines);
        dfsScore(map, row - 1, col, height + 1, visited, reachableNines);
        dfsScore(map, row, col + 1, height + 1, visited, reachableNines);
        dfsScore(map, row, col - 1, height + 1, visited, reachableNines);
    }

    private static int dfsRating(char[][] map, int row, int col, int currentHeight, Set<String> visited) {
        String positionKey = row + "," + col;
        if (!isValidPosition(map, row, col) || visited.contains(positionKey)) {
            return 0;
        }

        int height = map[row][col] - '0';
        if (height != currentHeight) {
            return 0;
        }

        visited.add(positionKey);

        if (height == 9) {
            return 1; // Count this path
        }

        int totalPaths = 0;

        // Explore all four directions
        totalPaths += dfsRating(map, row + 1, col, height + 1, new HashSet<>(visited));
        totalPaths += dfsRating(map, row - 1, col, height + 1, new HashSet<>(visited));
        totalPaths += dfsRating(map, row, col + 1, height + 1, new HashSet<>(visited));
        totalPaths += dfsRating(map, row, col - 1, height + 1, new HashSet<>(visited));

        return totalPaths;
    }


    @Override
    public Integer solvePartOne(String fileName) {
        var list = ResourceLines.list(fileName);
        Grid grid = new Grid(GridUtils.of(list));
        return sumTrailheadScores(grid);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var list = ResourceLines.list(fileName);
        Grid grid = new Grid(GridUtils.of(list));
        return sumTrailheadRatings(grid);
    }
}
