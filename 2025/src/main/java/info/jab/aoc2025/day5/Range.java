package info.jab.aoc2025.day5;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
        final Input input = parse(fileName);
        return input.ids().stream()
                .filter(id -> input.ranges().stream().anyMatch(range -> range.contains(id)))
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
        final Input input = parse(fileName);
        final List<RangeData> sortedRanges = input.ranges().stream()
                .sorted(Comparator.comparingLong(RangeData::start))
                .toList();

        if (sortedRanges.isEmpty()) {
            return 0L;
        }

        final List<RangeData> mergedRanges = mergeRanges(sortedRanges);

        return mergedRanges.stream()
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
    private List<RangeData> mergeRanges(final List<RangeData> sortedRanges) {
        final List<RangeData> mergedRanges = new ArrayList<>();
        RangeData current = sortedRanges.get(0);

        for (int i = 1; i < sortedRanges.size(); i++) {
            final RangeData next = sortedRanges.get(i);
            // Merge if overlapping or adjacent
            if (next.start() <= current.end() + 1) {
                current = new RangeData(current.start(), Math.max(current.end(), next.end()));
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
    private Input parse(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        final List<RangeData> ranges = new ArrayList<>();
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
                ranges.add(new RangeData(Long.parseLong(parts[0]), Long.parseLong(parts[1])));
            } else {
                ids.add(Long.parseLong(line.trim()));
            }
        }
        return new Input(ranges, ids);
    }
}

