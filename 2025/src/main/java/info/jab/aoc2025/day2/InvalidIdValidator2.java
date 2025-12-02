package info.jab.aoc2025.day2;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

import java.util.Arrays;
import java.util.List;
import java.util.function.LongPredicate;
import java.util.stream.IntStream;

/**
 * Validates IDs and calculates sums of invalid IDs from input ranges.
 * An invalid ID must be composed of repeated equal parts.
 */
public final class InvalidIdValidator2 implements Solver<Long> {

    /**
     * Calculates the sum of all invalid IDs in the given ranges for part one.
     * An invalid ID must have even length and be made of two equal parts.
     *
     * @param fileName The input file name
     * @return The sum of all invalid IDs
     */
    @Override
    public Long solvePartOne(final String fileName) {
        return calculateSum(fileName, this::isInvalidForPartOne);
    }

    /**
     * Calculates the sum of all invalid IDs in the given ranges for part two.
     * An invalid ID must be composed of repeated equal parts of any length.
     *
     * @param fileName The input file name
     * @return The sum of all invalid IDs
     */
    @Override
    public Long solvePartTwo(final String fileName) {
        return calculateSum(fileName, this::isInvalidForPartTwo);
    }

    /**
     * Pure function that calculates the sum of invalid IDs from ranges.
     * Separates I/O (file reading) from pure computation logic.
     *
     * @param fileName    The input file name
     * @param validator   A predicate that determines if an ID is invalid
     * @return The sum of all invalid IDs
     */
    private Long calculateSum(final String fileName, final LongPredicate validator) {
        final List<Range> ranges = parseRanges(fileName);
        return ranges.stream()
                .flatMapToLong(Range::ids)
                .filter(validator)
                .sum();
    }

    /**
     * Pure function that parses input into a list of Range records.
     * Separates I/O from pure transformation logic.
     *
     * @param fileName The input file name
     * @return An immutable list of Range records
     */
    private List<Range> parseRanges(final String fileName) {
        final String input = ResourceLines.line(fileName).trim();
        return Arrays.stream(input.split(","))
                .map(Range::from)
                .toList();
    }

    /**
     * Pure function that checks if an ID is invalid for part one.
     * An invalid ID must have even length and be made of two equal parts.
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
     *
     * @param id The ID to validate
     * @return true if the ID is invalid, false otherwise
     */
    private boolean isInvalidForPartTwo(final long id) {
        final String idStr = String.valueOf(id);
        final int length = idStr.length();

        // Try all possible part lengths from 1 to length/2
        return IntStream.rangeClosed(1, length / 2)
                .filter(partLength -> length % partLength == 0)
                .anyMatch(partLength -> {
                    final int numParts = length / partLength;
                    // Need at least 2 parts
                    if (numParts < 2) {
                        return false;
                    }

                    final String firstPart = idStr.substring(0, partLength);
                    // Check if all parts are equal using stream
                    return IntStream.range(1, numParts)
                            .mapToObj(i -> idStr.substring(i * partLength, (i + 1) * partLength))
                            .allMatch(part -> part.equals(firstPart));
                });
    }
}

