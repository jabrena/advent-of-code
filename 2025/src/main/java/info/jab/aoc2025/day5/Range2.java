package info.jab.aoc2025.day5;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

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
        final List<String> lines = ResourceLines.list(fileName);
        final RangeProblemInput input = RangeProblemInput.from(lines);
        return countIdsInRanges(input);
    }

    /**
     * Pure function that counts IDs contained in any range.
     * Separated from I/O for testability and functional composition.
     *
     * @param input The input containing ranges and IDs
     * @return The count of IDs contained in any range
     */
    private Long countIdsInRanges(final RangeProblemInput input) {
        return input.ids().stream()
                .filter(id -> findById(input.ranges(), id))
                .count();
    }

    /**
     * Pure function that checks if an ID is contained within any of the given ranges.
     * Separated for testability and functional composition.
     *
     * @param id The ID to check
     * @param ranges The list of ranges to check against
     * @return true if the ID is contained in any range, false otherwise
     */
    private boolean findById(final List<Interval> ranges, final Long id) {
        return ranges.stream().anyMatch(range -> range.contains(id));
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
        final List<String> lines = ResourceLines.list(fileName);
        final RangeProblemInput input = RangeProblemInput.from(lines);
        return calculateTotalCoverage(input);
    }

    /**
     * Pure function that calculates total coverage of merged ranges.
     * Separated from I/O for testability and functional composition.
     *
     * @param input The input containing ranges and IDs
     * @return The total number of values covered by merged ranges
     */
    private Long calculateTotalCoverage(final RangeProblemInput input) {
        final List<Interval> sortedRanges = input.ranges().stream()
                .sorted(Comparator.comparingLong(Interval::start))
                .toList();
        return mergeRanges(sortedRanges).stream()
                .mapToLong(Interval::size)
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
    private List<Interval> mergeRanges(final List<Interval> sortedRanges) {
        if (sortedRanges.isEmpty()) {
            return List.of();
        }

        final RangeMergeState initialState = new RangeMergeState(sortedRanges.get(0), List.of(), 0);
        final RangeMergeState finalState = IntStream.range(0, sortedRanges.size() - 1)
                .boxed()
                .reduce(
                        initialState,
                        (state, i) -> state.next(sortedRanges),
                        (s1, s2) -> s2.index() > s1.index() ? s2 : s1
                );
        return finalState.complete();
    }
}
