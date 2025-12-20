package info.jab.aoc2025.day5;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;
import io.github.vmzakharov.ecdataframe.dataframe.DataFrame;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import org.eclipse.collections.api.list.primitive.MutableLongList;
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList;

/**
 * Solver for range-based problems using DataFrame-EC approach.
 * Part 1: Counts IDs that are contained within any of the given ranges.
 * Part 2: Merges overlapping and adjacent ranges, then calculates total coverage.
 *
 * This implementation uses DataFrame as the primary data abstraction:
 * - Single DataFrame for ranges (start/end columns)
 * - Single DataFrame for IDs (id column)
 * - Single-pass processing with DataFrame collect
 * - Direct aggregation without intermediate collections
 * - Inline processing during DataFrame iteration
 */
public final class Range3 implements Solver<Long> {

    /**
     * Counts IDs that are contained within any of the given ranges.
     * Uses DataFrame-EC for data-oriented programming with single-pass processing.
     *
     * @param fileName The input file name
     * @return The count of IDs contained in any range
     */
    @Override
    public Long solvePartOne(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        final DataFrame rangesDf = createRangesDataFrame(lines);
        final DataFrame idsDf = createIdsDataFrame(lines);

        // Single-pass processing with direct aggregation
        final long[] countHolder = {0L};

        idsDf.collect(
                () -> null,
                (acc, cursor) -> {
                    final long id = cursor.getLong("id");
                    // Check containment inline by iterating ranges DataFrame
                    // Use flag to track if ID is found in any range (only count once per ID)
                    final boolean[] found = {false};
                    rangesDf.forEach((rangeCursor) -> {
                        if (!found[0]) {
                            final long start = rangeCursor.getLong("start");
                            final long end = rangeCursor.getLong("end");
                            if (start <= id && id <= end) {
                                found[0] = true;
                            }
                        }
                    });
                    if (found[0]) {
                        countHolder[0]++;
                    }
                }
        );

        return countHolder[0];
    }

    /**
     * Merges overlapping and adjacent ranges, then calculates the total coverage.
     * Uses DataFrame-EC for data-oriented programming with single-pass processing.
     *
     * @param fileName The input file name
     * @return The total number of values covered by merged ranges
     */
    @Override
    public Long solvePartTwo(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);

        // Parse and sort ranges before creating DataFrame
        final List<RangeData> sortedRanges = parseRanges(lines).stream()
                .sorted(Comparator.comparingLong(RangeData::start))
                .toList();

        if (sortedRanges.isEmpty()) {
            return 0L;
        }

        // Create DataFrame from sorted ranges
        final DataFrame rangesDf = createRangesDataFrameFromList(sortedRanges);

        // Merge and calculate coverage in single pass
        final long[] coverageHolder = {0L};
        final RangeData[] currentRange = {null};

        rangesDf.collect(
                () -> null,
                (acc, cursor) -> {
                    final RangeData range = new RangeData(
                            cursor.getLong("start"),
                            cursor.getLong("end")
                    );
                    // Merge logic inline
                    if (currentRange[0] == null) {
                        currentRange[0] = range;
                    } else if (currentRange[0].overlapsOrAdjacent(range)) {
                        currentRange[0] = currentRange[0].merge(range);
                    } else {
                        coverageHolder[0] += currentRange[0].size();
                        currentRange[0] = range;
                    }
                }
        );

        // Add final range coverage
        if (currentRange[0] != null) {
            coverageHolder[0] += currentRange[0].size();
        }

        return coverageHolder[0];
    }

    /**
     * Creates a DataFrame containing ranges (start/end columns) from input lines.
     * Parses ranges from lines before the blank line separator.
     *
     * @param lines The input lines
     * @return A DataFrame with range columns
     */
    private DataFrame createRangesDataFrame(final List<String> lines) {
        final List<RangeData> ranges = parseRanges(lines);
        return createRangesDataFrameFromList(ranges);
    }

    /**
     * Creates a DataFrame containing ranges from a list of RangeData.
     *
     * @param ranges The list of ranges
     * @return A DataFrame with range columns
     */
    private DataFrame createRangesDataFrameFromList(final List<RangeData> ranges) {
        final MutableLongList starts = new LongArrayList();
        final MutableLongList ends = new LongArrayList();

        ranges.forEach(range -> {
            starts.add(range.start());
            ends.add(range.end());
        });

        return new DataFrame("Ranges")
                .addLongColumn("start", starts)
                .addLongColumn("end", ends);
    }

    /**
     * Creates a DataFrame containing IDs from input lines.
     * Parses IDs from lines after the blank line separator.
     *
     * @param lines The input lines
     * @return A DataFrame with ID column
     */
    private DataFrame createIdsDataFrame(final List<String> lines) {
        final int separatorIndex = IntStream.range(0, lines.size())
                .filter(i -> lines.get(i).isBlank())
                .findFirst()
                .orElse(lines.size());

        final MutableLongList ids = new LongArrayList();

        lines.stream()
                .skip((long) separatorIndex + 1)
                .filter(line -> !line.isBlank())
                .map(String::trim)
                .mapToLong(Long::parseLong)
                .forEach(id -> ids.add(id));

        return new DataFrame("IDs")
                .addLongColumn("id", ids);
    }

    /**
     * Parses ranges from input lines (before blank line separator).
     *
     * @param lines The input lines
     * @return List of parsed ranges
     */
    private List<RangeData> parseRanges(final List<String> lines) {
        final int separatorIndex = IntStream.range(0, lines.size())
                .filter(i -> lines.get(i).isBlank())
                .findFirst()
                .orElse(lines.size());

        return lines.stream()
                .limit(separatorIndex)
                .filter(line -> !line.isBlank())
                .map(RangeData::from)
                .toList();
    }
}
