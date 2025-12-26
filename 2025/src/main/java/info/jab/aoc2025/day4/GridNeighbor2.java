package info.jab.aoc2025.day4;

import module java.base;

import com.putoet.grid.Grid;
import com.putoet.grid.GridDirections;
import com.putoet.grid.GridUtils;
import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

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
public final class GridNeighbor2 implements Solver<Integer> {

    private static final char TARGET_CELL = '@';
    private static final char EMPTY_CELL = '.';
    private static final int MIN_NEIGHBORS = 4;

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
                .filter(p -> hasFewerThanMinimumNeighbors(grid, p))
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
        final List<String> lines = ResourceLines.list(fileName).stream()
                .filter(line -> !line.isEmpty())
                .toList();
        return new Grid(GridUtils.of(lines));
    }

    /**
     * Predicate that checks if a cell has fewer than the minimum required
     * neighbors.
     * A cell is considered to have insufficient neighbors if it has fewer than
     * MIN_NEIGHBORS (4) adjacent cells containing the TARGET_CELL character.
     * This predicate encapsulates the business rule for identifying cells that
     * should be removed or counted.
     *
     * @param grid  The grid to check neighbors in
     * @param point The point to check
     * @return true if the cell has fewer than MIN_NEIGHBORS neighbors with
     *         TARGET_CELL
     */
    private boolean hasFewerThanMinimumNeighbors(final Grid grid, final Point point) {
        return GridDirections.countNeighbors(grid, point, c -> c == TARGET_CELL, true) < MIN_NEIGHBORS;
    }

    /**
     * Iteratively removes '@' symbols that have fewer than 4 neighbors
     * until no more can be removed, then returns the total count removed.
     * Uses an Imperative approach for clarity.
     *
     * @param fileName The input file name
     * @return The total number of cells removed
     */
    @Override
    public Integer solvePartTwo(final String fileName) {
        final Grid grid = createGrid(fileName);

        List<Point> cellsToRemove = findCellsToRemove(grid);
        int totalRemoved = 0;

        while (!cellsToRemove.isEmpty()) {
            final int cellsRemovedThisIteration = cellsToRemove.size();
            removeCells(grid, cellsToRemove);
            totalRemoved += cellsRemovedThisIteration;
            cellsToRemove = findCellsToRemove(grid);
        }

        return totalRemoved;
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
                .filter(p -> hasFewerThanMinimumNeighbors(grid, p))
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
