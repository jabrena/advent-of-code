package info.jab.aoc2025.day3;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

/**
 * Solver for calculating maximum joltage from digit sequences.
 * Finds the maximum number that can be formed by selecting digits
 * in order while maintaining a specific sequence length.
 */
public final class MaxJoltage implements Solver<Long> {

    private static final int PART_ONE_LENGTH = 2;
    private static final int PART_TWO_LENGTH = 12;

    /**
     * Solves part one by finding the maximum joltage with length 2.
     *
     * @param fileName The input file name
     * @return The total maximum joltage for part one
     */
    @Override
    public Long solvePartOne(final String fileName) {
        return solve(fileName, PART_ONE_LENGTH);
    }

    /**
     * Solves part two by finding the maximum joltage with length 12.
     *
     * @param fileName The input file name
     * @return The total maximum joltage for part two
     */
    @Override
    public Long solvePartTwo(final String fileName) {
        return solve(fileName, PART_TWO_LENGTH);
    }

    /**
     * Calculates the total maximum joltage for all lines in the file.
     * Uses Stream API for functional transformation and immutable operations.
     * Each line is represented as a Bank object.
     *
     * @param fileName The input file name
     * @param length   The required length of the joltage sequence
     * @return The sum of maximum joltages for all valid lines
     */
    private Long solve(final String fileName, final int length) {
        final String resourceName = fileName.startsWith("/") ? fileName : "/" + fileName;
        return ResourceLines.list(resourceName).stream()
                .map(Bank::from)
                .filter(bank -> bank.hasEnoughDigits(length))
                .mapToLong(bank -> bank.getMaxJoltage(length))
                .sum();
    }
}
