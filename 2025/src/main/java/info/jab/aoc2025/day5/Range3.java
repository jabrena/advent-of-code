package info.jab.aoc2025.day5;

import module java.base;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;

/**
 * Solver for range-based problems optimized with FastUtil.
 * Part 1: Counts IDs that are contained within any of the given ranges.
 * Part 2: Merges overlapping and adjacent ranges, then calculates total coverage.
 *
 * This implementation follows functional programming principles while leveraging FastUtil:
 * - Uses FastUtil LongArrayList for primitive long collections (avoids boxing overhead)
 * - Employs immutable collections where possible
 * - Separates pure functions from I/O operations
 * - Uses imperative loops for better performance with primitive collections
 * - Leverages method references and function composition where appropriate
 *
 * Performance optimizations:
 * - LongArrayList eliminates boxing/unboxing overhead for IDs
 * - Direct indexed access improves cache locality
 * - Reduced memory footprint compared to boxed Long collections
 */
public final class Range3 implements Solver<Long> {

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
     * Optimized with FastUtil LongArrayList to avoid boxing overhead.
     *
     * @param input The input containing intervals and IDs
     * @return The count of IDs contained in any interval
     */
    private Long countIdsInIntervals(final RangeProblemInput input) {
        final List<Interval> intervals = input.intervals();
        final LongList ids = convertToLongList(input.ids());
        long count = 0;
        final int idsSize = ids.size();
        for (int i = 0; i < idsSize; i++) {
            final long id = ids.getLong(i);
            if (containsId(intervals, id)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Converts a List of Long to FastUtil LongList for better performance.
     * Pure function for functional composition.
     *
     * @param ids The list of boxed Long values
     * @return A LongList containing the same values as primitives
     */
    private static LongList convertToLongList(final List<Long> ids) {
        final LongList result = new LongArrayList(ids.size());
        for (final Long id : ids) {
            result.add(id.longValue());
        }
        return result;
    }

    /**
     * Checks if an ID is contained within any of the given intervals.
     * Optimized with early exit and indexed access for better cache locality.
     *
     * @param intervals The list of intervals to check against
     * @param id The ID to check
     * @return true if the ID is contained in any interval, false otherwise
     */
    private static boolean containsId(final List<Interval> intervals, final long id) {
        final int size = intervals.size();
        for (int i = 0; i < size; i++) {
            if (intervals.get(i).contains(id)) {
                return true;
            }
        }
        return false;
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
     * Pure function that merges overlapping and adjacent ranges.
     * Optimized with imperative loop for better performance than functional reduce.
     * Ranges are merged if they overlap or are adjacent.
     *
     * @param sortedIntervals Ranges sorted by start value
     * @return List of merged ranges
     */
    private List<Interval> mergeIntervals(final List<Interval> sortedIntervals) {
        if (sortedIntervals.isEmpty()) {
            return List.of();
        }

        final int size = sortedIntervals.size();
        final List<Interval> merged = new ArrayList<>(size);
        Interval current = sortedIntervals.get(0);

        for (int i = 1; i < size; i++) {
            final Interval next = sortedIntervals.get(i);
            if (current.overlapsOrAdjacent(next)) {
                current = current.merge(next);
            } else {
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);
        return merged;
    }
}
