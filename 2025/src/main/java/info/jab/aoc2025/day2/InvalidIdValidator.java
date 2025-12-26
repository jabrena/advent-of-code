package info.jab.aoc2025.day2;

import module java.base;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

/**
 * Validates IDs and calculates sums of invalid IDs from input ranges.
 * An invalid ID must be composed of repeated equal parts.
 */
public final class InvalidIdValidator implements Solver<Long> {

    /**
     * Calculates the sum of all invalid IDs in the given ranges for part one.
     * An invalid ID must have even length and be made of two equal parts.
     *
     * @param fileName The input file name
     * @return The sum of all invalid IDs
     */
    @Override
    public Long solvePartOne(final String fileName) {
        final String input = ResourceLines.line(fileName).trim();
        long sum = 0;

        int startIdx = 0;
        while (startIdx < input.length()) {
            final int commaIdx = input.indexOf(',', startIdx);
            final String range = commaIdx == -1 ? input.substring(startIdx) : input.substring(startIdx, commaIdx);
            startIdx = commaIdx == -1 ? input.length() : commaIdx + 1;

            final int dashIdx = range.indexOf('-');
            final long start = parseLongFromString(range, 0, dashIdx);
            final long end = parseLongFromString(range, dashIdx + 1, range.length());

            for (long id = start; id <= end; id++) {
                if (isInvalidForPartOne(id)) {
                    sum += id;
                }
            }
        }

        return sum;
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
        final String input = ResourceLines.line(fileName).trim();
        long sum = 0;

        int startIdx = 0;
        while (startIdx < input.length()) {
            final int commaIdx = input.indexOf(',', startIdx);
            final String range = commaIdx == -1 ? input.substring(startIdx) : input.substring(startIdx, commaIdx);
            startIdx = commaIdx == -1 ? input.length() : commaIdx + 1;

            final int dashIdx = range.indexOf('-');
            final long start = parseLongFromString(range, 0, dashIdx);
            final long end = parseLongFromString(range, dashIdx + 1, range.length());

            for (long id = start; id <= end; id++) {
                if (isInvalidForPartTwo(id)) {
                    sum += id;
                }
            }
        }

        return sum;
    }

    /**
     * Checks if an ID is invalid for part one.
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
     * Checks if an ID is invalid for part two.
     * An invalid ID must be composed of repeated equal parts of any length.
     * Optimized to avoid String allocations by using char array and character comparison.
     *
     * @param id The ID to validate
     * @return true if the ID is invalid, false otherwise
     */
    private boolean isInvalidForPartTwo(final long id) {
        final char[] idChars = longToCharArray(id);
        final int length = idChars.length;

        // Try all possible part lengths from 1 to length/2
        for (int partLength = 1; partLength <= length / 2; partLength++) {
            // Check if length is divisible by partLength and we have at least 2 parts
            final int numParts = length / partLength;
            if (length % partLength == 0 && numParts >= 2) {
                // Check if all parts are equal by comparing characters directly
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

    /**
     * Parses a long from a substring without creating intermediate String.
     *
     * @param str String to parse from
     * @param start Start index (inclusive)
     * @param end End index (exclusive)
     * @return Parsed long value
     */
    private static long parseLongFromString(final String str, final int start, final int end) {
        long result = 0;
        for (int i = start; i < end; i++) {
            result = result * 10 + (str.charAt(i) - '0');
        }
        return result;
    }
}

