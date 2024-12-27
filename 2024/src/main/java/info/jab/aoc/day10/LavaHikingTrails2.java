package info.jab.aoc.day10;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class LavaHikingTrails2 implements Solver<Integer> {

    public int sumTrailheadScoresBFS(Grid grid) {
        var points = grid.findAll(c -> c == '0');
        return points.stream()
            .map(p -> calculateTrailheadScoreBFS(grid.grid(), p.y(), p.x()))
            .reduce(0, Integer::sum);
    }

    public int sumTrailheadRatingsBFS(Grid grid) {
        var points = grid.findAll(c -> c == '0');
        return points.stream()
            .map(p -> calculateTrailheadRatingBFS(grid.grid(), p.y(), p.x()))
            .reduce(0, Integer::sum);
    }

    private boolean isValidPosition(char[][] map, int row, int col) {
        return row >= 0 && row < map.length && col >= 0 && col < map[0].length;
    }

    private int calculateTrailheadScoreBFS(char[][] map, int startRow, int startCol) {
        Set<String> reachableNines = new HashSet<>();
        Queue<int[]> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        // Start BFS from the trailhead
        queue.add(new int[]{startRow, startCol, 0});  // {row, col, current height}
        visited.add(startRow + "," + startCol);
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            int height = current[2];

            // If we are at a position where the height is not matching, skip it
            if (map[row][col] - '0' != height) {
                continue;
            }

            // If height is 9, add this position to the reachable set
            if (height == 9) {
                reachableNines.add(row + "," + col);
                continue;
            }

            // Explore neighboring positions in all four directions
            for (int[] direction : getDirections()) {
                int newRow = row + direction[0];
                int newCol = col + direction[1];

                if (isValidPosition(map, newRow, newCol) && !visited.contains(newRow + "," + newCol)) {
                    // Ensure the next height is exactly 1 more than the current height
                    if (map[newRow][newCol] - '0' == height + 1) {
                        queue.add(new int[]{newRow, newCol, height + 1});
                        visited.add(newRow + "," + newCol);
                    }
                }
            }
        }
        return reachableNines.size();
    }

    private int calculateTrailheadRatingBFS(char[][] map, int startRow, int startCol) {
        int distinctTrailCount = 0;

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startRow, startCol, 0});
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            int height = current[2];

            if (map[row][col] - '0' != height) {
                continue;
            }
            if (height == 9) {
                distinctTrailCount++;
                continue;
            }
            for (int[] direction : getDirections()) {
                int newRow = row + direction[0];
                int newCol = col + direction[1];

                if (isValidPosition(map, newRow, newCol)) {
                    queue.add(new int[]{newRow, newCol, height + 1});
                }
            }
        }
        return distinctTrailCount;
    }

    //TODO Refactor with Points
    private int[][] getDirections() {
        return new int[][]{
                {1, 0},  // Down
                {-1, 0}, // Up
                {0, 1},  // Right
                {0, -1}  // Left
        };
    }

    @Override
    public Integer solvePartOne(String fileName) {
        var list = ResourceLines.list(fileName);
        Grid grid = new Grid(GridUtils.of(list));
        return sumTrailheadScoresBFS(grid);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var list = ResourceLines.list(fileName);
        Grid grid = new Grid(GridUtils.of(list));
        return sumTrailheadRatingsBFS(grid);
    }
}
