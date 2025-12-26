package info.jab.aoc2025.day2;

import module java.base;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;


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

