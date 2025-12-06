package info.jab.aoc2015.day25;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Solver for code generation problems.
 * Uses functional programming principles:
 * - Pure functions for calculations
 * - Immutable data structures
 * - Stream API for input processing
 */
public final class CodeGenerator implements Solver<Long> {

    private static final Pattern ROW_COLUMN_PATTERN = Pattern.compile("row (\\d+), column (\\d+)");
    private static final long INITIAL_CODE = 20151125L;
    private static final long MULTIPLIER = 252533L;
    private static final long MODULUS = 33554393L;

    @Override
    public Long solvePartOne(final String fileName) {
        final String input = ResourceLines.stream(fileName)
                .collect(Collectors.joining("\n"));

        final Position position = parsePosition(input);
        final long sequencePosition = calculateSequencePosition(position.row(), position.column());

        // Generate the code at that position using modular exponentiation
        // Formula: code = (20151125 * 252533^(position-1)) mod 33554393
        // This reduces complexity from O(p) to O(log p)
        final long exponent = sequencePosition - 1;
        final long power = modPow(MULTIPLIER, exponent, MODULUS);

        return (INITIAL_CODE * power) % MODULUS;
    }

    @Override
    public Long solvePartTwo(final String fileName) {
        // Day 25 typically only has part 1 in Advent of Code
        return null;
    }

    /**
     * Pure function: parses row and column from input.
     */
    private Position parsePosition(final String input) {
        final Matcher matcher = ROW_COLUMN_PATTERN.matcher(input);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Could not parse row and column from input");
        }

        return new Position(
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2))
        );
    }

    /**
     * Pure function: calculates position in diagonal sequence.
     * Formula: position = (r + c - 2) * (r + c - 1) / 2 + c
     */
    private long calculateSequencePosition(final int row, final int column) {
        final long sum = row + column;
        return (sum - 2) * (sum - 1) / 2 + column;
    }

    /**
     * Pure function: fast modular exponentiation.
     * Calculates (base^exponent) mod modulus in O(log exponent) time.
     * Uses iterative approach to avoid stack overflow.
     */
    private long modPow(final long base, final long exponent, final long modulus) {
        long result = 1L;
        long currentBase = base % modulus;
        long currentExponent = exponent;

        while (currentExponent > 0) {
            if (currentExponent % 2 == 1) {
                result = (result * currentBase) % modulus;
            }
            currentExponent = currentExponent >> 1;
            currentBase = (currentBase * currentBase) % modulus;
        }

        return result;
    }

}
