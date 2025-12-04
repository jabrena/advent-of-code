package info.jab.aoc2025.day4;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.grid.Point;
import com.putoet.grid.Points;
import info.jab.aoc.Solver;
import info.jab.aoc.Utils;
import java.util.List;
import java.util.stream.Stream;

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
    private static final List<Point> NEIGHBOR_DIRECTIONS = Points.directionsAll();

    /**
     * Counts '@' symbols that have fewer than 4 neighbors.
     * Uses Stream API for declarative processing.
     *
     * @param fileName The input file name
     * @return The count of '@' symbols with fewer than 4 neighbors
     */
    @Override
    public Integer solvePartOne(final String fileName) {
        final Grid grid = createGrid(fileName);

        return grid.findAll(c -> c == TARGET_CELL).stream()
                .filter(p -> countNeighbors(grid, p) < MIN_NEIGHBORS)
                .mapToInt(p -> 1)
                .sum();
    }

    /**
     * Pure function that creates a grid from file input.
     * Separates I/O from pure transformation logic.
     *
     * @param fileName The input file name
     * @return A Grid object representing the grid
     */
    private Grid createGrid(final String fileName) {
        final List<String> lines = Utils.readFileToList(fileName).stream()
                .filter(line -> !line.isEmpty())
                .toList();
        return new Grid(GridUtils.of(lines));
    }


    /**
     * Pure function that counts the number of '@' neighbors around a cell.
     * Checks all 8 adjacent cells (including diagonals).
     * Uses Point objects and Stream API for functional neighbor checking.
     *
     * @param grid The grid
     * @param point The point to check neighbors for
     * @return The count of '@' neighbors
     */
    private int countNeighbors(final Grid grid, final Point point) {
        return (int) NEIGHBOR_DIRECTIONS.stream()
                .map(point::add)
                .filter(grid::contains)
                .filter(p -> grid.get(p) == TARGET_CELL)
                .count();
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
        final Grid grid = createGrid(fileName);

        final List<Point> initialCellsToRemove = findCellsToRemove(grid);
        if (initialCellsToRemove.isEmpty()) {
            return 0;
        }

        return Stream.iterate(
                        new RemovalState(grid, initialCellsToRemove, 0),
                        state -> !state.cellsToRemove().isEmpty(),
                        state -> {
                            final int cellsRemovedThisIteration = state.cellsToRemove().size();
                            removeCells(state.grid(), state.cellsToRemove());
                            final List<Point> nextCellsToRemove = findCellsToRemove(state.grid());
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
     * Pure function that finds all cells that should be removed.
     * Returns an immutable list of points to remove.
     * Uses Stream API for declarative processing.
     *
     * @param grid The grid
     * @return An immutable list of points to remove
     */
    private List<Point> findCellsToRemove(final Grid grid) {
        return grid.findAll(c -> c == TARGET_CELL).stream()
                .filter(p -> countNeighbors(grid, p) < MIN_NEIGHBORS)
                .toList();
    }

    /**
     * Mutates the grid by removing cells at the specified points.
     * This is the only mutation point in the algorithm, isolated for clarity.
     *
     * @param grid     The grid to mutate
     * @param toRemove The list of points to remove
     */
    private void removeCells(final Grid grid, final List<Point> toRemove) {
        toRemove.forEach(p -> grid.set(p, EMPTY_CELL));
    }
}
