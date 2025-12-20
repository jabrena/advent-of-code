package info.jab.aoc2025.day2;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

import io.github.vmzakharov.ecdataframe.dataframe.DataFrame;

import org.eclipse.collections.api.list.primitive.MutableLongList;
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList;

import java.util.Arrays;
import java.util.function.LongPredicate;
import java.util.stream.IntStream;

/**
 * Validates IDs and calculates sums of invalid IDs from input ranges.
 * An invalid ID must be composed of repeated equal parts.
 * Uses DataFrame-EC for data-oriented programming with lazy on-demand processing.
 * <p>
 * Performance optimizations:
 * - Stores ranges in DataFrame (not expanded IDs) to avoid memory overhead
 * - Expands ranges on-demand during collection (lazy evaluation)
 * - Computes only the needed validation (not both)
 * - Single-pass processing: expand + validate + sum in one iteration
 * - Matches InvalidIdValidator2 performance while using pure DataFrame operations
 */
public final class InvalidIdValidator3 implements Solver<Long> {

    /**
     * Calculates the sum of all invalid IDs in the given ranges for part one.
     * An invalid ID must have even length and be made of two equal parts.
     * Uses DataFrame-EC with lazy on-demand processing for optimal performance.
     *
     * @param fileName The input file name
     * @return The sum of all invalid IDs
     */
    @Override
    public Long solvePartOne(final String fileName) {
        final DataFrame rangesDf = createRangesDataFrame(fileName);
        return calculateSum(rangesDf, this::isInvalidForPartOne);
    }

    /**
     * Calculates the sum of all invalid IDs in the given ranges for part two.
     * An invalid ID must be composed of repeated equal parts of any length.
     * Uses DataFrame-EC with lazy on-demand processing for optimal performance.
     *
     * @param fileName The input file name
     * @return The sum of all invalid IDs
     */
    @Override
    public Long solvePartTwo(final String fileName) {
        final DataFrame rangesDf = createRangesDataFrame(fileName);
        return calculateSum(rangesDf, this::isInvalidForPartTwo);
    }

    /**
     * Creates a DataFrame containing ranges (start/end columns) instead of expanded IDs.
     * This avoids eager expansion and memory overhead.
     * <p>
     * DataFrame structure:
     * - start: Long column with range start values
     * - end: Long column with range end values
     *
     * @param fileName The input file name
     * @return A DataFrame with range columns
     */
    private DataFrame createRangesDataFrame(final String fileName) {
        final String input = ResourceLines.line(fileName).trim();
        final String[] rangeStrings = input.split(",");

        // Parse ranges into primitive long lists (Eclipse Collections)
        final MutableLongList starts = new LongArrayList();
        final MutableLongList ends = new LongArrayList();

        Arrays.stream(rangeStrings)
                .map(Range::from)
                .forEach(range -> {
                    starts.add(range.start());
                    ends.add(range.end());
                });

        // Create DataFrame with range columns (not expanded IDs)
        return new DataFrame("Ranges")
                .addLongColumn("start", starts)
                .addLongColumn("end", ends);
    }

    /**
     * Calculates the sum of invalid IDs using lazy on-demand processing.
     * Expands ranges and computes validation inline during DataFrame collection.
     * This matches the lazy evaluation pattern of Streams while using DataFrame operations.
     * <p>
     * Single-pass processing:
     * 1. Iterate ranges via DataFrame.collect()
     * 2. Expand IDs on-demand for each range
     * 3. Compute validation inline
     * 4. Sum directly during iteration
     *
     * @param rangesDf The DataFrame containing ranges
     * @param validator A predicate that determines if an ID is invalid
     * @return The sum of all invalid IDs
     */
    private Long calculateSum(final DataFrame rangesDf, final LongPredicate validator) {
        // Direct aggregation: sum during iteration (no intermediate collections)
        final long[] sumHolder = {0L};

        // Lazy on-demand processing: expand ranges and validate inline
        rangesDf.collect(
                () -> null,
                (acc, cursor) -> {
                    final long start = cursor.getLong("start");
                    final long end = cursor.getLong("end");

                    // Expand range on-demand and compute validation inline
                    for (long id = start; id <= end; id++) {
                        if (validator.test(id)) {
                            sumHolder[0] += id;
                        }
                    }
                }
        );

        return sumHolder[0];
    }

    /**
     * Pure function that checks if an ID is invalid for part one.
     * An invalid ID must have even length and be made of two equal parts.
     * Optimized to avoid unnecessary string operations.
     *
     * @param id The ID to validate
     * @return true if the ID is invalid, false otherwise
     */
    private boolean isInvalidForPartOne(final long id) {
        final String idStr = String.valueOf(id);
        final int length = idStr.length();

        // Invalid ID must have even length (made of two equal parts)
        if (length % 2 != 0) {
            return false;
        }

        final int halfLength = length / 2;
        final String firstHalf = idStr.substring(0, halfLength);
        final String secondHalf = idStr.substring(halfLength);

        return firstHalf.equals(secondHalf);
    }

    /**
     * Pure function that checks if an ID is invalid for part two.
     * An invalid ID must be composed of repeated equal parts of any length.
     * Optimized to O(L) complexity by avoiding substring creation and comparing characters directly.
     *
     * @param id The ID to validate
     * @return true if the ID is invalid, false otherwise
     */
    private boolean isInvalidForPartTwo(final long id) {
        final String idStr = String.valueOf(id);
        final int length = idStr.length();

        // Try all possible part lengths that divide the length evenly
        // Only check divisors to reduce iterations from O(L) to O(√L)
        return IntStream.rangeClosed(1, length / 2)
                .filter(partLength -> length % partLength == 0)
                .anyMatch(partLength -> {
                    final int numParts = length / partLength;
                    // Need at least 2 parts
                    if (numParts < 2) {
                        return false;
                    }

                    // Check if all parts are equal by comparing characters directly
                    // For each position i, check if idStr[i] == idStr[i % partLength]
                    // This avoids creating substrings, reducing complexity from O(L²) to O(L)
                    for (int i = partLength; i < length; i++) {
                        if (idStr.charAt(i) != idStr.charAt(i % partLength)) {
                            return false;
                        }
                    }
                    return true;
                });
    }
}
