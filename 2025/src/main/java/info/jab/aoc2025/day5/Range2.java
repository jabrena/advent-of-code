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
     * Counts IDs that are contained within any of the given intervals.
     * Pure transformation after I/O boundary.
     *
     * @param fileName The input file name
     * @return The count of IDs contained in any interval
     */
    @Override
    public Long solvePartOne(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        final RangeProblemInput input = RangeProblemInput.from(lines);
        return countIdsInIntervals(input);
    }

    /**
     * Pure function that counts IDs contained in any interval.
     * Separated from I/O for testability and functional composition.
     *
     * @param input The input containing intervals and IDs
     * @return The count of IDs contained in any interval
     */
    private Long countIdsInIntervals(final RangeProblemInput input) {
        return input.ids().stream()
                .filter(id -> existsInInterval(input.intervals(), id))
                .count();
    }

    /**
     * Pure function that checks if an ID is contained within any of the given intervals.
     * Separated for testability and functional composition.
     *
     * @param id The ID to check
     * @param ranges The list of ranges to check against
     * @return true if the ID is contained in any range, false otherwise
     */
    private boolean existsInInterval(final List<Interval> intervals, final Long id) {
        return intervals.stream().anyMatch(interval -> interval.contains(id));
    }

    /**
     * Merges overlapping and adjacent intervals, then calculates the total coverage.
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
     * Pure function that calculates total coverage of merged intervals.
     * Separated from I/O for testability and functional composition.
     *
     * @param input The input containing intervals and IDs
     * @return The total number of values covered by merged intervals
     */
    private Long calculateTotalCoverage(final RangeProblemInput input) {
        final List<Interval> sortedIntervals = input.intervals().stream()
                .sorted(Comparator.comparingLong(Interval::start))
                .toList();
        return mergeIntervals(sortedIntervals).stream()
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
    private List<Interval> mergeIntervals(final List<Interval> sortedIntervals) {
        if (sortedIntervals.isEmpty()) {
            return List.of();
        }

        final RangeMergeState initialState = new RangeMergeState(sortedIntervals.get(0), List.of(), 0);
        final RangeMergeState finalState = IntStream.range(0, sortedIntervals.size() - 1)
                .boxed()
                .reduce(
                        initialState,
                        (state, i) -> state.next(sortedIntervals),
                        (s1, s2) -> s2.index() > s1.index() ? s2 : s1
                );
        return finalState.complete();
    }
}
