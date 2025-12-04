package info.jab.aoc2025.day4;

import com.putoet.grid.GridUtils;
import com.putoet.grid.Point;
import info.jab.aoc.Solver;
import info.jab.aoc.Utils;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Collectors;

/**
 * Solver for counting and removing grid cells based on neighbor count.
 * Counts '@' symbols that have fewer than 4 neighbors, and iteratively
 * removes such cells until no more can be removed.
 *
 * This implementation follows functional programming principles:
 * - Uses Stream API for declarative transformations
 * - Employs immutable collections where possible
 * - Separates pure functions from side effects
 * - Uses Stream.iterate for iterative processes
 */
public final class GridNeighborSolverV2 implements Solver<Integer> {

    private static final char TARGET_CELL = '@';
    private static final char EMPTY_CELL = '.';
    private static final int MIN_NEIGHBORS = 4;
    private static final int[] NEIGHBOR_DX = {-1, 0, 1, -1, 1, -1, 0, 1};
    private static final int[] NEIGHBOR_DY = {-1, -1, -1, 0, 0, 1, 1, 1};

    /**
     * Counts '@' symbols that have fewer than 4 neighbors.
     * Uses Stream API for declarative processing.
     *
     * @param fileName The input file name
     * @return The count of '@' symbols with fewer than 4 neighbors
     */
    @Override
    public Integer solvePartOne(final String fileName) {
        final char[][] grid = createGrid(fileName);
        final int rows = grid.length;
        final int cols = grid[0].length;

        return generateCoordinates(rows, cols)
                .filter(p -> grid[p.y()][p.x()] == TARGET_CELL)
                .filter(p -> countNeighbors(grid, p.x(), p.y(), rows, cols) < MIN_NEIGHBORS)
                .mapToInt(p -> 1)
                .sum();
    }

    /**
     * Iteratively removes '@' symbols that have fewer than 4 neighbors
     * until no more can be removed, then returns the total count removed.
     * Uses Stream.iterate for functional iteration.
     *
     * @param fileName The input file name
     * @return The total number of cells removed
     */
    @Override
    public Integer solvePartTwo(final String fileName) {
        final char[][] grid = createGrid(fileName);
        final int rows = grid.length;
        final int cols = grid[0].length;

        final List<Point> initialCellsToRemove = findCellsToRemove(grid, rows, cols);
        if (initialCellsToRemove.isEmpty()) {
            return 0;
        }

        return Stream.iterate(
                        new RemovalState(grid, initialCellsToRemove, 0),
                        state -> !state.cellsToRemove().isEmpty(),
                        state -> {
                            final int cellsRemovedThisIteration = state.cellsToRemove().size();
                            removeCells(state.grid(), state.cellsToRemove());
                            final List<Point> nextCellsToRemove = findCellsToRemove(
                                    state.grid(), rows, cols);
                            return new RemovalState(
                                    state.grid(),
                                    nextCellsToRemove,
                                    state.totalRemoved() + cellsRemovedThisIteration
                            );
                        }
                )
                .reduce((first, second) -> second)
                .map(state -> {
                    // Stream.iterate stops when the predicate becomes false, which means
                    // the last state in the stream has cellsToRemove that were removed
                    // in the 'next' function when generating the following state (with empty cellsToRemove).
                    // That following state's totalRemoved includes the count, but it's not in the stream.
                    // Therefore, we need to add state.cellsToRemove().size() to account for
                    // the cells removed in the final iteration.
                    return state.totalRemoved() + state.cellsToRemove().size();
                })
                .orElse(0);
    }

    /**
     * Pure function that creates a grid from file input.
     * Separates I/O from pure transformation logic.
     *
     * @param fileName The input file name
     * @return A 2D character array representing the grid
     */
    private char[][] createGrid(final String fileName) {
        return Utils.readFileToList(fileName).stream()
                .filter(line -> !line.isEmpty())
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        GridUtils::of
                ));
    }

    /**
     * Pure function that counts the number of '@' neighbors around a cell.
     * Checks all 8 adjacent cells (including diagonals).
     * Uses IntStream for functional neighbor checking.
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
        return IntStream.range(0, NEIGHBOR_DX.length)
                .map(i -> {
                    final int nx = x + NEIGHBOR_DX[i];
                    final int ny = y + NEIGHBOR_DY[i];
                    return (nx >= 0 && nx < cols && ny >= 0 && ny < rows
                            && grid[ny][nx] == TARGET_CELL) ? 1 : 0;
                })
                .sum();
    }

    /**
     * Pure function that finds all cells that should be removed.
     * Returns an immutable list of points to remove.
     * Uses Stream API for declarative processing.
     *
     * @param grid The grid
     * @param rows The number of rows in the grid
     * @param cols The number of columns in the grid
     * @return An immutable list of points to remove
     */
    private List<Point> findCellsToRemove(final char[][] grid, final int rows, final int cols) {
        return generateCoordinates(rows, cols)
                .filter(p -> grid[p.y()][p.x()] == TARGET_CELL)
                .filter(p -> countNeighbors(grid, p.x(), p.y(), rows, cols) < MIN_NEIGHBORS)
                .toList();
    }

    /**
     * Pure function that generates all coordinate points in the grid.
     * Uses IntStream for functional coordinate generation.
     *
     * @param rows The number of rows
     * @param cols The number of columns
     * @return A stream of all coordinate points
     */
    private Stream<Point> generateCoordinates(final int rows, final int cols) {
        return IntStream.range(0, rows)
                .boxed()
                .flatMap(y -> IntStream.range(0, cols)
                        .mapToObj(x -> Point.of(x, y)));
    }

    /**
     * Mutates the grid by removing cells at the specified points.
     * This is the only mutation point in the algorithm, isolated for clarity.
     *
     * @param grid     The grid to mutate
     * @param toRemove The list of points to remove
     */
    private void removeCells(final char[][] grid, final List<Point> toRemove) {
        toRemove.forEach(p -> grid[p.y()][p.x()] = EMPTY_CELL);
    }
}
