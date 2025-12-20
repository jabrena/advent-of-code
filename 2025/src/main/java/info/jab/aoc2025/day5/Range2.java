package info.jab.aoc2025.day5;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;
import java.util.Comparator;
import java.util.List;

/**
 * Solver for range-based problems.
 * Part 1: Counts IDs that are contained within any of the given ranges.
 * Part 2: Merges overlapping and adjacent ranges, then calculates total coverage.
 *
 * This implementation follows functional programming principles:
 * - Uses Stream API for declarative transformations
 * - Employs immutable collections exclusively
 * - Separates pure functions from I/O operations
 * - Uses Stream.iterate for functional iteration
 * - Leverages method references and function composition
 */
public final class Range2 implements Solver<Long> {

    /**
     * Counts IDs that are contained within any of the given ranges.
     * Pure transformation after I/O boundary.
     *
     * @param fileName The input file name
     * @return The count of IDs contained in any range
     */
    @Override
    public Long solvePartOne(final String fileName) {
        final Input input = Input.from(ResourceLines.list(fileName));
        return countIdsInRanges(input);
    }

    /**
     * Pure function that counts IDs contained in any range.
     * Separated from I/O for testability and functional composition.
     *
     * @param input The input containing ranges and IDs
     * @return The count of IDs contained in any range
     */
    private Long countIdsInRanges(final Input input) {
        return input.ids().stream()
                .filter(id -> input.ranges().stream().anyMatch(range -> range.contains(id)))
                .count();
    }

    /**
     * Merges overlapping and adjacent ranges, then calculates the total coverage.
     * Pure transformation after I/O boundary.
     *
     * @param fileName The input file name
     * @return The total number of values covered by merged ranges
     */
    @Override
    public Long solvePartTwo(final String fileName) {
        final Input input = Input.from(ResourceLines.list(fileName));
        return calculateTotalCoverage(input);
    }

    /**
     * Pure function that calculates total coverage of merged ranges.
     * Separated from I/O for testability and functional composition.
     *
     * @param input The input containing ranges and IDs
     * @return The total number of values covered by merged ranges
     */
    private Long calculateTotalCoverage(final Input input) {
        final List<RangeData> sortedRanges = input.ranges().stream()
                .sorted(Comparator.comparingLong(RangeData::start))
                .toList();
        return mergeRanges(sortedRanges).stream()
                .mapToLong(RangeData::size)
                .sum();
    }

    /**
     * Pure function that merges overlapping and adjacent ranges using Stream.iterate.
     * Functional approach replacing imperative loop with declarative stream operations.
     * Ranges are merged if they overlap or are adjacent.
     *
     * @param sortedRanges Ranges sorted by start value
     * @return Immutable list of merged ranges
     */
    private List<RangeData> mergeRanges(final List<RangeData> sortedRanges) {
        if (sortedRanges.isEmpty()) {
            return List.of();
        }

        MergeState state = new MergeState(sortedRanges.get(0), List.of(), 0);
        for (int i = 0; i < sortedRanges.size() - 1; i++) {
            state = state.next(sortedRanges);
        }
        return state.complete();
    }
}
