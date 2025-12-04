package info.jab.aoc2025.day4;

import com.putoet.grid.Grid;
import com.putoet.grid.Point;
import java.util.List;

/**
 * Immutable record representing the state during iterative removal.
 * Encapsulates grid state and removal tracking.
 *
 * @param grid         The current grid state
 * @param cellsToRemove The cells to remove in the next iteration
 * @param totalRemoved  The total number of cells removed so far
 */
record RemovalState(
        Grid grid,
        List<Point> cellsToRemove,
        int totalRemoved
) {
}

