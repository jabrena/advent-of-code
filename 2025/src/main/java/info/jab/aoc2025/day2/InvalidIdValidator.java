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
        final List<String> lines = ResourceLines.list(fileName);
        final String input = String.join("", lines).trim();

        final String[] ranges = input.split(",");
        long sum = 0;

        for (final String range : ranges) {
            final String[] parts = range.split("-");
            final long start = Long.parseLong(parts[0]);
            final long end = Long.parseLong(parts[1]);

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
        final List<String> lines = ResourceLines.list(fileName);
        final String input = String.join("", lines).trim();

        final String[] ranges = input.split(",");
        long sum = 0;

        for (final String range : ranges) {
            final String[] parts = range.split("-");
            final long start = Long.parseLong(parts[0]);
            final long end = Long.parseLong(parts[1]);

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
     * Checks if an ID is invalid for part two.
     * An invalid ID must be composed of repeated equal parts of any length.
     *
     * @param id The ID to validate
     * @return true if the ID is invalid, false otherwise
     */
    private boolean isInvalidForPartTwo(final long id) {
        final String idStr = String.valueOf(id);
        final int length = idStr.length();

        // Try all possible part lengths from 1 to length/2
        for (int partLength = 1; partLength <= length / 2; partLength++) {
            // Check if length is divisible by partLength and we have at least 2 parts
            final int numParts = length / partLength;
            if (length % partLength == 0 && numParts >= 2) {
                final String firstPart = idStr.substring(0, partLength);
                boolean allPartsEqual = true;

                for (int i = 1; i < numParts; i++) {
                    final String part = idStr.substring(i * partLength, (i + 1) * partLength);
                    if (!part.equals(firstPart)) {
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
}

