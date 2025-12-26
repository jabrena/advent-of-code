package info.jab.aoc2025.day7;

import module java.base;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.grid.Point;
import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

public final class BeamPathCounter implements Solver<Long> {

    @Override
    public Long solvePartOne(final String fileName) {
        final Grid grid = createGrid(fileName);
        return countSplits(grid);
    }

    @Override
    public Long solvePartTwo(final String fileName) {
        final Grid grid = createGrid(fileName);
        return countPaths(grid);
    }

    private Grid createGrid(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        return new Grid(GridUtils.of(lines, CellType.EMPTY.character()));
    }

    private long countSplits(final Grid grid) {
        final Point start = grid.findFirst(c -> c == CellType.START.character())
                .orElseThrow(() -> new IllegalArgumentException("No start position S found"));

        return countSplitsRecursive(
                grid,
                Set.of(start.x()),
                start.y() + 1,
                0L
        );
    }

    private long countSplitsRecursive(
            final Grid grid,
            final Set<Integer> currentBeams,
            final int currentY,
            final long accumulatedSplits
    ) {
        if (currentBeams.isEmpty() || currentY >= grid.maxY()) {
            return accumulatedSplits;
        }

        final SplitResult result = processBeamRow(grid, currentBeams, currentY);
        return countSplitsRecursive(
                grid,
                result.nextBeams(),
                currentY + 1,
                accumulatedSplits + result.splits()
        );
    }

    private record BeamAction(int x, CellType cellType) { }

    private record SplitResult(Set<Integer> nextBeams, long splits) { }

    private SplitResult processBeamRow(
            final Grid grid,
            final Set<Integer> currentBeams,
            final int y
    ) {
        return currentBeams.stream()
                .map(x -> Point.of(x, y))
                .filter(grid::contains)
                .map(point -> {
                    final CellType cellType = CellType.from(grid.get(point));
                    return new BeamAction(point.x(), cellType);
                })
                .collect(Collectors.teeing(
                        Collectors.filtering(
                                action -> action.cellType() == CellType.SPLITTER,
                                Collectors.counting()
                        ),
                        Collectors.flatMapping(
                                action -> action.cellType() == CellType.SPLITTER
                                        ? IntStream.of(action.x() - 1, action.x() + 1).boxed()
                                        : IntStream.of(action.x()).boxed(),
                                Collectors.toUnmodifiableSet()
                        ),
                        (splits, nextBeams) -> new SplitResult(nextBeams, splits)
                ));
    }

    private long countPaths(final Grid grid) {
        final Map<Point, Long> memo = new java.util.HashMap<>();

        final Point start = grid.findFirst(c -> c == CellType.START.character())
                .orElseThrow(() -> new IllegalArgumentException("No start position S found"));

        return countPathsRecursive(grid, start.x(), start.y() + 1, memo);
    }

    private long countPathsRecursive(
            final Grid grid,
            final int x,
            final int y,
            final Map<Point, Long> memo) {
        if (y >= grid.maxY()) {
            return 1L;
        }

        final Point point = Point.of(x, y);
        if (!grid.contains(point)) {
            return 1L;
        }

        final Long cached = memo.get(point);
        if (cached != null) {
            return cached;
        }

        final CellType cellType = CellType.from(grid.get(point));
        final long result = switch (cellType) {
            case SPLITTER -> countPathsRecursive(grid, x - 1, y + 1, memo) +
                             countPathsRecursive(grid, x + 1, y + 1, memo);
            default -> countPathsRecursive(grid, x, y + 1, memo);
        };

        memo.put(point, result);
        return result;
    }
}
