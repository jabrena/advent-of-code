package info.jab.aoc2025.day2;

import module java.base;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

/**
 * Alternative implementation based on TypeScript reference.
 * Validates IDs and calculates sums of invalid IDs from input ranges.
 * This implementation follows the algorithm from:
 * https://github.com/juan-medina/adventofcode2025/blob/master/src/days/day02.ts
 */
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
     *
     * @param id The ID to check
     * @return Number of repeats (0 if not repeated)
     */
    private int repeated(final long id) {
        final String str = String.valueOf(id);
        final int halfLength = str.length() / 2;

        for (int len = halfLength; len >= 1; len--) {
            if (str.length() % len != 0) {
                continue; // can't fill the whole string
            }
            final String chunk = str.substring(0, len);
            final int repeats = str.length() / len; // how many to repeat
            if (chunk.repeat(repeats).equals(str)) {
                return repeats;
            }
        }
        return 0;
    }
}
