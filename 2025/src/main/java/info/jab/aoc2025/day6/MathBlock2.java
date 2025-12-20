package info.jab.aoc2025.day6;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;

/**
 * Solver for math block problems.
 * Processes lines of text containing mathematical expressions separated by empty columns.
 * Part 1: Processes blocks row by row, extracting numbers and operators.
 * Part 2: Processes blocks column by column (right to left), extracting numbers vertically.
 */
public final class MathBlock2 implements Solver<Long> {

    @Override
    public Long solvePartOne(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        return solve(lines, range -> processBlockPart1(lines, range));
    }

    @Override
    public Long solvePartTwo(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        return solve(lines, range -> processBlockPart2(lines, range));
    }

    /**
     * Solves the problem by processing blocks using the specified block processor.
     *
     * @param lines The input lines to process
     * @param blockProcessor The function to process each block
     * @return The sum of all block calculations
     */
    public Long solve(final List<String> lines, final ToLongFunction<ColumnRange> blockProcessor) {
        if (lines.isEmpty()) {
            return 0L;
        }

        final int maxLength = lines.stream().mapToInt(String::length).max().orElse(0);
        final List<String> paddedLines = padLines(lines, maxLength);

        return findBlockRanges(paddedLines, maxLength).stream()
                .mapToLong(blockProcessor)
                .sum();
    }

    /**
     * Processes a block for Part 1: extracts numbers and operators row by row.
     *
     * @param lines The padded lines
     * @param range The column range
     * @return The calculated result for this block
     */
    public long processBlockPart1(final List<String> lines, final ColumnRange range) {
        final List<Long> numbers = lines.stream()
                .map(line -> safeSubstring(line, range.startCol(), range.endCol()).trim())
                .filter(sub -> !sub.isEmpty())
                .filter(isNotOperatorSymbol())
                .map(this::parseLongSafely)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        final MathOperator operator = findOperatorInRange(lines, range);
        return calculate(numbers, operator);
    }

    /**
     * Processes a block for Part 2: extracts numbers column by column (right to left).
     *
     * @param lines The padded lines
     * @param range The column range
     * @return The calculated result for this block
     */
    public long processBlockPart2(final List<String> lines, final ColumnRange range) {
        final MathOperator operator = findOperatorInRange(lines, range);

        final List<Long> numbers = IntStream.range(range.startCol(), range.endCol())
                .boxed()
                .sorted((a, b) -> Integer.compare(b, a)) // Reverse order: right to left
                .map(col -> extractVerticalNumber(lines, col))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

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
        return lines.stream()
                .map(line -> {
                    final int paddingNeeded = maxLength - line.length();
                    return paddingNeeded > 0
                            ? line + " ".repeat(paddingNeeded)
                            : line;
                })
                .toList();
    }

    private boolean isSeparatorColumn(final List<String> paddedLines, final int col) {
        return paddedLines.stream()
                .allMatch(line -> line.charAt(col) == ' ');
    }

    @SuppressWarnings("null")
    private List<ColumnRange> findBlockRanges(final List<String> paddedLines, final int maxLength) {
        final List<Integer> separatorColumns = IntStream.range(0, maxLength)
                .filter(col -> isSeparatorColumn(paddedLines, col))
                .boxed()
                .toList();

        if (separatorColumns.isEmpty()) {
            return List.of(new ColumnRange(0, maxLength));
        }

        // Build ranges: blocks between separators
        // Using a mutable accumulator is acceptable when mutation is isolated and result is immutable
        final List<ColumnRange> ranges = new java.util.ArrayList<>();
        int startCol = 0;

        for (final int separatorCol : separatorColumns) {
            if (separatorCol > startCol) {
                ranges.add(new ColumnRange(startCol, separatorCol));
            }
            startCol = separatorCol + 1;
        }

        // Add the final block if there's content after the last separator
        if (startCol < maxLength) {
            ranges.add(new ColumnRange(startCol, maxLength));
        }

        return List.copyOf(ranges);
    }

    private MathOperator findOperatorInRange(final List<String> lines, final ColumnRange range) {
        return lines.stream()
                .map(line -> safeSubstring(line, range.startCol(), range.endCol()).trim())
                .filter(containsOperatorSymbol())
                .findFirst()
                .map(MathOperator::from)
                .orElse(MathOperator.NONE);
    }

    /**
     * Returns a predicate that tests if a string contains an operator symbol.
     *
     * @return A predicate that returns true if the string contains an operator symbol
     */
    private Predicate<String> containsOperatorSymbol() {
        return sub -> sub.contains(MathOperator.ADDITION.symbol())
                || sub.contains(MathOperator.MULTIPLICATION.symbol());
    }

    /**
     * Returns a predicate that tests if a string is not an operator symbol.
     *
     * @return A predicate that returns true if the string is not an operator symbol
     */
    private Predicate<String> isNotOperatorSymbol() {
        return sub -> !sub.equals(MathOperator.ADDITION.symbol())
                && !sub.equals(MathOperator.MULTIPLICATION.symbol());
    }

    private String safeSubstring(final String str, final int start, final int end) {
        if (str == null || str.length() < start) {
            return "";
        }
        final int actualEnd = Math.min(end, str.length());
        return start < actualEnd ? str.substring(start, actualEnd) : "";
    }

    private Optional<Long> parseLongSafely(final String str) {
        try {
            return Optional.of(Long.parseLong(str));
        } catch (final NumberFormatException _) {
            return Optional.empty();
        }
    }

    private Optional<Long> extractVerticalNumber(final List<String> lines, final int col) {
        final String numStr = lines.stream()
                .filter(line -> col < line.length())
                .mapToInt(line -> line.charAt(col))
                .filter(Character::isDigit)
                .mapToObj(Character::toString)
                .collect(Collectors.joining());

        return numStr.isEmpty()
                ? Optional.empty()
                : Optional.of(Long.parseLong(numStr));
    }
}
