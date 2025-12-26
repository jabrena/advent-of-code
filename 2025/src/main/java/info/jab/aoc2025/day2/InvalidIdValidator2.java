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
     * Optimized to use imperative loops instead of streams for better performance.
     *
     * @param fileName    The input file name
     * @param validator   A predicate that determines if an ID is invalid
     * @return The sum of all invalid IDs
     */
    private Long calculateSum(final String fileName, final LongPredicate validator) {
        final List<Range> ranges = parseRanges(fileName);
        long sum = 0;
        for (final Range range : ranges) {
            for (long id = range.start(); id <= range.end(); id++) {
                if (validator.test(id)) {
                    sum += id;
                }
            }
        }
        return sum;
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
     * Optimized to avoid String allocations by using char array.
     *
     * @param id The ID to validate
     * @return true if the ID is invalid, false otherwise
     */
    private boolean isInvalidForPartOne(final long id) {
        final char[] idChars = longToCharArray(id);
        final int length = idChars.length;

        // Invalid ID must have even length (made of two equal parts)
        if (length % 2 != 0) {
            return false;
        }

        final int halfLength = length / 2;
        // Compare characters directly without creating substrings
        for (int i = 0; i < halfLength; i++) {
            if (idChars[i] != idChars[i + halfLength]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Pure function that checks if an ID is invalid for part two.
     * An invalid ID must be composed of repeated equal parts of any length.
     * Optimized to O(L) complexity by avoiding substring creation and comparing characters directly.
     * Uses imperative loop instead of stream for better performance.
     *
     * @param id The ID to validate
     * @return true if the ID is invalid, false otherwise
     */
    private boolean isInvalidForPartTwo(final long id) {
        final char[] idChars = longToCharArray(id);
        final int length = idChars.length;

        // Try all possible part lengths that divide the length evenly
        // Only check divisors to reduce iterations from O(L) to O(√L)
        for (int partLength = 1; partLength <= length / 2; partLength++) {
            if (length % partLength == 0) {
                final int numParts = length / partLength;
                // Need at least 2 parts
                if (numParts < 2) {
                    continue;
                }

                // Check if all parts are equal by comparing characters directly
                // For each position i, check if idChars[i] == idChars[i % partLength]
                // This avoids creating substrings, reducing complexity from O(L²) to O(L)
                boolean allPartsEqual = true;
                for (int i = partLength; i < length; i++) {
                    if (idChars[i] != idChars[i % partLength]) {
                        allPartsEqual = false;
                        break;
                    }
                }
                if (allPartsEqual) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Converts a long to a char array without creating intermediate String.
     *
     * @param value The long value to convert
     * @return Char array representation
     */
    private static char[] longToCharArray(final long value) {
        if (value == 0) {
            return new char[]{'0'};
        }

        // Calculate number of digits
        int digits = 0;
        long temp = value;
        if (temp < 0) {
            temp = -temp;
        }
        while (temp > 0) {
            temp /= 10;
            digits++;
        }

        final char[] chars = new char[digits];
        temp = value < 0 ? -value : value;
        for (int i = digits - 1; i >= 0; i--) {
            chars[i] = (char) ('0' + (temp % 10));
            temp /= 10;
        }
        return chars;
    }
}

