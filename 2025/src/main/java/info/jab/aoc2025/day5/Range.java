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
     *
     * @param fileName The input file name
     * @return The count of IDs contained in any range
     */
    @Override
    public Long solvePartOne(final String fileName) {
        final RangeProblemInput input = parse(fileName);
        return input.ids().stream()
                .filter(id -> input.intervals().stream().anyMatch(range -> range.contains(id)))
                .count();
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
     *
     * @param fileName The input file name
     * @return Parsed input containing ranges and IDs
     */
    private RangeProblemInput parse(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        final List<Interval> ranges = new ArrayList<>();
        final List<Long> ids = new ArrayList<>();

        boolean parsingRanges = true;
        for (final String line : lines) {
            if (line.isBlank()) {
                if (parsingRanges) {
                    parsingRanges = false;
                }
                continue;
            }

            if (parsingRanges) {
                final String[] parts = line.split("-");
                ranges.add(new Interval(Long.parseLong(parts[0]), Long.parseLong(parts[1])));
            } else {
                ids.add(Long.parseLong(line.trim()));
            }
        }
        return new RangeProblemInput(ranges, ids);
    }
}

