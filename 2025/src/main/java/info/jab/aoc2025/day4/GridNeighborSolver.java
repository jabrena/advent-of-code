package info.jab.aoc2025.day4;

import com.putoet.grid.GridUtils;
import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;
import java.util.ArrayList;
import java.util.List;

/**
 * Solver for counting and removing grid cells based on neighbor count.
 * Counts '@' symbols that have fewer than 4 neighbors, and iteratively
 * removes such cells until no more can be removed.
 */
public final class GridNeighborSolver implements Solver<Integer> {

    private static final char TARGET_CELL = '@';
    private static final char EMPTY_CELL = '.';
    private static final int MIN_NEIGHBORS = 4;

    /**
     * Counts '@' symbols that have fewer than 4 neighbors.
     *
     * @param fileName The input file name
     * @return The count of '@' symbols with fewer than 4 neighbors
     */
    @Override
    public Integer solvePartOne(final String fileName) {
        final char[][] grid = createGrid(fileName);
        final int rows = grid.length;
        final int cols = grid[0].length;
        int count = 0;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (grid[y][x] == TARGET_CELL) {
                    if (countNeighbors(grid, x, y, rows, cols) < MIN_NEIGHBORS) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Iteratively removes '@' symbols that have fewer than 4 neighbors
     * until no more can be removed, then returns the total count removed.
     *
     * @param fileName The input file name
     * @return The total number of cells removed
     */
    @Override
    public Integer solvePartTwo(final String fileName) {
        final char[][] grid = createGrid(fileName);
        final int rows = grid.length;
        final int cols = grid[0].length;
        int totalRemoved = 0;

        while (true) {
            final List<Point> toRemove = findCellsToRemove(grid, rows, cols);

            if (toRemove.isEmpty()) {
                break;
            }

            removeCells(grid, toRemove);
            totalRemoved += toRemove.size();
        }

        return totalRemoved;
    }

    /**
     * Pure function that creates a grid from file input.
     * Separates I/O from pure transformation logic.
     *
     * @param fileName The input file name
     * @return A 2D character array representing the grid
     */
    private char[][] createGrid(final String fileName) {
        final List<String> lines = ResourceLines.list("/" + fileName).stream()
                .filter(line -> !line.isEmpty())
                .toList();
        return GridUtils.of(lines);
    }

    /**
     * Pure function that counts the number of '@' neighbors around a cell.
     * Checks all 8 adjacent cells (including diagonals).
     *
     * @param grid The grid
     * @param x    The x coordinate of the cell
     * @param y    The y coordinate of the cell
     * @param rows The number of rows in the grid
     * @param cols The number of columns in the grid
     * @return The count of '@' neighbors
     */
    private int countNeighbors(final char[][] grid, final int x, final int y,
                               final int rows, final int cols) {
        int neighbors = 0;
        final int[] dx = {-1, 0, 1, -1, 1, -1, 0, 1};
        final int[] dy = {-1, -1, -1, 0, 0, 1, 1, 1};

        for (int i = 0; i < 8; i++) {
            final int nx = x + dx[i];
            final int ny = y + dy[i];

            if (nx >= 0 && nx < cols && ny >= 0 && ny < rows) {
                if (grid[ny][nx] == TARGET_CELL) {
                    neighbors++;
                }
            }
        }
        return neighbors;
    }

    /**
     * Pure function that finds all cells that should be removed.
     * Returns an immutable list of points to remove.
     *
     * @param grid The grid
     * @param rows The number of rows in the grid
     * @param cols The number of columns in the grid
     * @return A list of points to remove
     */
    private List<Point> findCellsToRemove(final char[][] grid, final int rows, final int cols) {
        final List<Point> toRemove = new ArrayList<>();
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (grid[y][x] == TARGET_CELL) {
                    if (countNeighbors(grid, x, y, rows, cols) < MIN_NEIGHBORS) {
                        toRemove.add(Point.of(x, y));
                    }
                }
            }
        }
        return toRemove;
    }

    /**
     * Mutates the grid by removing cells at the specified points.
     * This is the only mutation point in the algorithm.
     *
     * @param grid     The grid to mutate
     * @param toRemove The list of points to remove
     */
    private void removeCells(final char[][] grid, final List<Point> toRemove) {
        for (final Point p : toRemove) {
            grid[p.y()][p.x()] = EMPTY_CELL;
        }
    }
}

