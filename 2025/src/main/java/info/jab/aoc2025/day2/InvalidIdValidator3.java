package info.jab.aoc2025.day2;

import module java.base;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

public final class InvalidIdValidator3 implements Solver<Long> {

    /**
     * Calculates the sum of all invalid IDs in the given ranges for part one.
     * An invalid ID must have 2 or fewer repeated parts.
     *
     * @param fileName The input file name
     * @return The sum of all invalid IDs
     */
    @Override
    public Long solvePartOne(final String fileName) {
        final String input = ResourceLines.line(fileName).trim();
        return solve(input, 1);
    }

    /**
     * Calculates the sum of all invalid IDs in the given ranges for part two.
     * An invalid ID must have 99 or fewer repeated parts.
     *
     * @param fileName The input file name
     * @return The sum of all invalid IDs
     */
    @Override
    public Long solvePartTwo(final String fileName) {
        final String input = ResourceLines.line(fileName).trim();
        return solve(input, 2);
    }

    /**
     * Solves the invalid ID validation problem for the specified part.
     *
     * @param input Input string containing comma-separated ranges
     * @param part Part number (1 or 2)
     * @return The sum of all invalid IDs
     */
    private Long solve(final String input, final int part) {
        long totalInvalid = 0;
        final List<Range> ranges = parse(input);

        for (final Range range : ranges) {
            final int maxRepeats = part == 1 ? 2 : 99;
            for (final long id : range.invalidIds(maxRepeats, this::repeated)) {
                totalInvalid += id;
            }
        }

        return totalInvalid;
    }

    /**
     * Parses input string into a list of ranges.
     *
     * @param input Input string containing comma-separated ranges
     * @return List of parsed ranges
     */
    private List<Range> parse(final String input) {
        final String[] parts = input.split(",");
        final List<Range> ranges = new ArrayList<>();
        for (final String part : parts) {
            ranges.add(Range.from(part.trim()));
        }
        return ranges;
    }

    /**
     * Calculates the number of times a chunk repeats to form the given ID string.
     * Returns 0 if the ID is not composed of repeated chunks.
     * Optimized to avoid String allocations by using char array and character comparison.
     *
     * @param id The ID to check
     * @return Number of repeats (0 if not repeated)
     */
    private int repeated(final long id) {
        final char[] idChars = longToCharArray(id);
        final int length = idChars.length;
        final int halfLength = length / 2;

        for (int len = halfLength; len >= 1; len--) {
            if (length % len != 0) {
                continue; // can't fill the whole string
            }
            final int repeats = length / len; // how many to repeat

            // Check if all parts are equal by comparing characters directly
            // For each position i, check if idChars[i] == idChars[i % len]
            boolean allPartsEqual = true;
            for (int i = len; i < length; i++) {
                if (idChars[i] != idChars[i % len]) {
                    allPartsEqual = false;
                    break;
                }
            }
            if (allPartsEqual) {
                return repeats;
            }
        }
        return 0;
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
