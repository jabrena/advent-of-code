package info.jab.aoc2025.day6;

import module java.base;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

/**
 * Solver for math block problems.
 * Processes lines of text containing mathematical expressions separated by empty columns.
 * Part 1: Processes blocks row by row, extracting numbers and operators.
 * Part 2: Processes blocks column by column (right to left), extracting numbers vertically.
 */
public final class MathBlock implements Solver<Long> {

    private static final char SPACE = ' ';

    /**
     * @inheritDoc
     */
    @Override
    public Long solvePartOne(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        return solve(lines, this::processBlockPart1);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Long solvePartTwo(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        return solve(lines, this::processBlockPart2);
    }

    /**
     * Solves the problem by processing blocks using the specified block processor.
     *
     * @param lines The input lines to process
     * @param blockProcessor The function to process each block
     * @return The sum of all block calculations
     */
    public Long solve(final List<String> lines, final BiFunction<List<String>, Integer[], Long> blockProcessor) {
        if (lines.isEmpty()) {
            return 0L;
        }

        final int maxLength = lines.stream().mapToInt(String::length).max().orElse(0);
        final List<String> paddedLines = padLines(lines, maxLength);

        long totalSum = 0;
        int startCol = 0;

        for (int col = 0; col < maxLength; col++) {
            if (isSeparatorColumn(paddedLines, col)) {
                if (col > startCol) {
                    totalSum += blockProcessor.apply(paddedLines, new Integer[]{startCol, col});
                }
                startCol = col + 1;
            }
        }

        if (startCol < maxLength) {
            totalSum += blockProcessor.apply(paddedLines, new Integer[]{startCol, maxLength});
        }

        return totalSum;
    }

    /**
     * Processes a block for Part 1: extracts numbers and operators row by row.
     *
     * @param lines The padded lines
     * @param range The column range [startCol, endCol]
     * @return The calculated result for this block
     */
    public Long processBlockPart1(final List<String> lines, final Integer[] range) {
        final int startCol = range[0];
        final int endCol = range[1];
        final List<Long> numbers = new ArrayList<>();
        MathOperator operator = MathOperator.NONE;

        for (final String line : lines) {
            final String sub = line.substring(startCol, endCol).trim();
            if (sub.isEmpty()) {
                continue;
            }

            final MathOperator foundOperator = MathOperator.from(sub);
            if (foundOperator != MathOperator.NONE) {
                operator = foundOperator;
            } else {
                try {
                    numbers.add(Long.parseLong(sub));
                } catch (final NumberFormatException _) {
                    // Ignore invalid numbers
                }
            }
        }

        return calculate(numbers, operator);
    }

    /**
     * Processes a block for Part 2: extracts numbers column by column (right to left).
     *
     * @param lines The padded lines
     * @param range The column range [startCol, endCol]
     * @return The calculated result for this block
     */
    public Long processBlockPart2(final List<String> lines, final Integer[] range) {
        final int startCol = range[0];
        final int endCol = range[1];
        final List<Long> numbers = new ArrayList<>();
        final MathOperator operator = findOperator(lines, startCol, endCol);

        // Iterate columns right to left
        for (int col = endCol - 1; col >= startCol; col--) {
            final StringBuilder numStr = new StringBuilder();
            for (final String line : lines) {
                final char c = line.charAt(col);
                if (Character.isDigit(c)) {
                    numStr.append(c);
                }
            }

            if (!numStr.isEmpty()) {
                numbers.add(Long.parseLong(numStr.toString()));
            }
        }

        return calculate(numbers, operator);
    }

    /**
     * Calculates the result based on the operator.
     *
     * @param numbers The list of numbers to operate on
     * @param operator The operator to apply
     * @return The calculated result
     */
    public long calculate(final List<Long> numbers, final MathOperator operator) {
        if (numbers.isEmpty()) {
            return 0;
        }
        return operator.apply(numbers);
    }

    private List<String> padLines(final List<String> lines, final int maxLength) {
        final List<String> paddedLines = new ArrayList<>();
        for (final String line : lines) {
            final StringBuilder sb = new StringBuilder(line);
            while (sb.length() < maxLength) {
                sb.append(SPACE);
            }
            paddedLines.add(sb.toString());
        }
        return paddedLines;
    }

    private boolean isSeparatorColumn(final List<String> paddedLines, final int col) {
        for (final String line : paddedLines) {
            if (line.charAt(col) != SPACE) {
                return false;
            }
        }
        return true;
    }

    private MathOperator findOperator(final List<String> lines, final int startCol, final int endCol) {
        for (final String line : lines) {
            final String sub = line.substring(startCol, endCol).trim();
            final MathOperator operator = MathOperator.from(sub);
            if (operator != MathOperator.NONE) {
                return operator;
            }
        }
        return MathOperator.NONE;
    }
}
