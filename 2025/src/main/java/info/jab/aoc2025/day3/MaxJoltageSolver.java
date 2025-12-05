package info.jab.aoc2025.day3;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Solver for calculating maximum joltage from digit sequences.
 * Finds the maximum number that can be formed by selecting digits
 * in order while maintaining a specific sequence length.
 */
public final class MaxJoltageSolver implements Solver<Long> {

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
     *
     * @param fileName The input file name
     * @param length   The required length of the joltage sequence
     * @return The sum of maximum joltages for all valid lines
     */
    private Long solve(final String fileName, final int length) {
        final String resourceName = fileName.startsWith("/") ? fileName : "/" + fileName;
        return ResourceLines.list(resourceName).stream()
                .filter(line -> line.length() >= length)
                .mapToLong(line -> getMaxJoltage(line, length))
                .sum();
    }

    /**
     * Calculates the maximum joltage that can be formed from a line
     * by selecting digits in order to form a number of the specified length.
     * This is a pure function that transforms input to output without side effects.
     *
     * @param line   The input line containing digits
     * @param length The required length of the resulting number
     * @return The maximum joltage value
     */
    private long getMaxJoltage(final String line, final int length) {
        final List<Integer> digits = line.chars()
                .mapToObj(ch -> ch - '0')
                .toList();

        return buildMaxJoltage(digits, length, 0, 0L);
    }

    /**
     * Recursively builds the maximum joltage by selecting the maximum digit
     * at each position while ensuring enough digits remain for the remaining length.
     * This is a pure, tail-recursive function that transforms state immutably.
     *
     * @param digits      The list of digits (immutable)
     * @param remaining   The number of digits still needed
     * @param startPos    The starting position in the digits list
     * @param accumulator The accumulated joltage value
     * @return The maximum joltage value
     */
    private long buildMaxJoltage(final List<Integer> digits, final int remaining,
                                 final int startPos, final long accumulator) {
        if (remaining == 0) {
            return accumulator;
        }

        final int endPos = digits.size() - remaining;
        final MaxDigitResult maxResult = findMaxDigitInRange(digits, startPos, endPos);

        return buildMaxJoltage(
                digits,
                remaining - 1,
                maxResult.index() + 1,
                accumulator * 10 + maxResult.value()
        );
    }

    /**
     * Result record containing the maximum digit value and its index.
     * Immutable data structure for functional programming.
     *
     * @param value The maximum digit value
     * @param index The index where the maximum digit was found
     */
    private record MaxDigitResult(int value, int index) {}

    /**
     * Finds the maximum digit value and its first occurrence index in the specified range.
     * Pure function with no side effects. Combines max finding and index lookup in a single pass.
     *
     * @param digits   The list of digits
     * @param startPos The starting position (inclusive)
     * @param endPos   The ending position (inclusive)
     * @return A record containing the maximum digit value and its index
     */
    private MaxDigitResult findMaxDigitInRange(final List<Integer> digits, final int startPos, final int endPos) {
        return IntStream.rangeClosed(startPos, endPos)
                .boxed()
                .reduce(
                        new MaxDigitResult(-1, startPos),
                        (acc, i) -> {
                            final int digit = digits.get(i);
                            return (digit > acc.value())
                                    ? new MaxDigitResult(digit, i)
                                    : acc;
                        },
                        (acc1, acc2) -> acc1.value() >= acc2.value() ? acc1 : acc2
                );
    }
}

