package info.jab.aoc2025.day5;

import module java.base;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

/**
 * Solver for range-based problems.
 * Part 1: Counts IDs that are contained within any of the given ranges.
 * Part 2: Merges overlapping and adjacent ranges, then calculates total coverage.
 *
 * This implementation follows functional programming principles:
 * - Uses Stream API for declarative transformations
 * - Employs immutable collections where possible
 * - Separates pure functions from I/O operations
 */
public final class Range implements Solver<Long> {

    /**
     * Counts IDs that are contained within any of the given ranges.
     * Optimized to use imperative loop instead of nested streams for better performance.
     *
     * @param fileName The input file name
     * @return The count of IDs contained in any range
     */
    @Override
    public Long solvePartOne(final String fileName) {
        final RangeProblemInput input = parse(fileName);
        final List<Interval> intervals = input.intervals();
        long count = 0;
        for (final long id : input.ids()) {
            if (containsId(intervals, id)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Checks if an ID is contained within any of the given intervals.
     * Optimized with early exit.
     *
     * @param intervals The list of intervals to check against
     * @param id The ID to check
     * @return true if the ID is contained in any interval, false otherwise
     */
    private static boolean containsId(final List<Interval> intervals, final long id) {
        for (final Interval interval : intervals) {
            if (interval.contains(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Merges overlapping and adjacent ranges, then calculates the total coverage.
     *
     * @param fileName The input file name
     * @return The total number of values covered by merged ranges
     */
    @Override
    public Long solvePartTwo(final String fileName) {
        final RangeProblemInput input = parse(fileName);
        final List<Interval> sortedIntervals = input.intervals().stream()
                .sorted(Comparator.comparingLong(Interval::start))
                .toList();

        if (sortedIntervals.isEmpty()) {
            return 0L;
        }

        final List<Interval> mergedIntervals = mergeIntervals(sortedIntervals);

        return mergedIntervals.stream()
                .mapToLong(r -> r.end() - r.start() + 1)
                .sum();
    }

    /**
     * Pure function that merges overlapping and adjacent ranges.
     * Ranges are merged if they overlap or are adjacent (end + 1 >= start).
     *
     * @param sortedRanges Ranges sorted by start value
     * @return List of merged ranges
     */
    private List<Interval> mergeIntervals(final List<Interval> sortedIntervals) {
        final List<Interval> mergedRanges = new ArrayList<>();
        Interval current = sortedIntervals.get(0);

        for (int i = 1; i < sortedIntervals.size(); i++) {
            final Interval next = sortedIntervals.get(i);
            // Merge if overlapping or adjacent
            if (next.start() <= current.end() + 1) {
                current = new Interval(current.start(), Math.max(current.end(), next.end()));
            } else {
                mergedRanges.add(current);
                current = next;
            }
        }
        mergedRanges.add(current);
        return mergedRanges;
    }

    /**
     * Parses the input file into ranges and IDs.
     * The file format: ranges first (one per line, format: "start-end"),
     * followed by a blank line, then IDs (one per line).
     * Optimized to use indexOf() instead of split() and pre-allocate collections.
     *
     * @param fileName The input file name
     * @return Parsed input containing ranges and IDs
     */
    private RangeProblemInput parse(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        final int estimatedSize = lines.size() / 2;
        final List<Interval> ranges = new ArrayList<>(estimatedSize);
        final List<Long> ids = new ArrayList<>(estimatedSize);

        boolean parsingRanges = true;
        for (final String line : lines) {
            if (line.isBlank()) {
                if (parsingRanges) {
                    parsingRanges = false;
                }
                continue;
            }

            if (parsingRanges) {
                final int dashIdx = line.indexOf('-');
                final long start = parseLongFromString(line, 0, dashIdx);
                final long end = parseLongFromString(line, dashIdx + 1, line.length());
                ranges.add(new Interval(start, end));
            } else {
                ids.add(parseLongFromString(line, 0, line.length()));
            }
        }
        return new RangeProblemInput(ranges, ids);
    }

    /**
     * Parses a long from a substring without creating intermediate String.
     *
     * @param str String to parse from
     * @param start Start index (inclusive)
     * @param end End index (exclusive)
     * @return Parsed long value
     */
    private static long parseLongFromString(final String str, final int start, final int end) {
        // Skip leading whitespace
        int actualStart = start;
        while (actualStart < end && Character.isWhitespace(str.charAt(actualStart))) {
            actualStart++;
        }
        // Skip trailing whitespace
        int actualEnd = end;
        while (actualEnd > actualStart && Character.isWhitespace(str.charAt(actualEnd - 1))) {
            actualEnd--;
        }
        
        long result = 0;
        for (int i = actualStart; i < actualEnd; i++) {
            result = result * 10 + (str.charAt(i) - '0');
        }
        return result;
    }
}

