package info.jab.aoc2025.day6;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;
import io.github.vmzakharov.ecdataframe.dataframe.DataFrame;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;

/**
 * Solver for math block problems using DataFrame-EC approach.
 * Processes lines of text containing mathematical expressions separated by empty columns.
 * Part 1: Processes blocks row by row, extracting numbers and operators.
 * Part 2: Processes blocks column by column (right to left), extracting numbers vertically.
 *
 * This implementation uses DataFrame as the primary data abstraction:
 * - Single DataFrame for blocks (startCol/endCol columns)
 * - Single-pass processing with DataFrame collect
 * - Direct aggregation without intermediate collections
 * - Inline processing during DataFrame iteration
 */
public final class MathBlock3 implements Solver<Long> {

    /**
     * Processes blocks row by row, extracting numbers and operators.
     * Uses DataFrame-EC for data-oriented programming with single-pass processing.
     *
     * @param fileName The input file name
     * @return The sum of all block calculations
     */
    @Override
    public Long solvePartOne(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        if (lines.isEmpty()) {
            return 0L;
        }

        final int maxLength = lines.stream().mapToInt(String::length).max().orElse(0);
        final List<String> paddedLines = padLines(lines, maxLength);
        final DataFrame blocksDf = createBlocksDataFrame(paddedLines, maxLength);

        // Single-pass processing with direct aggregation
        final long[] sumHolder = {0L};

        blocksDf.collect(
                () -> null,
                (acc, cursor) -> {
                    final int startCol = (int) cursor.getInt("startCol");
                    final int endCol = (int) cursor.getInt("endCol");

                    // Process block inline: extract numbers row by row
                    final List<Long> numbers = new ArrayList<>();
                    MathOperator operator = MathOperator.NONE;

                    for (final String line : paddedLines) {
                        final String sub = safeSubstring(line, startCol, endCol).trim();
                        if (!sub.isEmpty()) {
                            if (containsOperatorSymbol(sub)) {
                                operator = MathOperator.from(sub);
                            } else if (!isOperatorSymbol(sub)) {
                                parseLongSafely(sub).ifPresent(numbers::add);
                            }
                        }
                    }

                    sumHolder[0] += calculate(numbers, operator);
                }
        );

        return sumHolder[0];
    }

    /**
     * Processes blocks column by column (right to left), extracting numbers vertically.
     * Uses DataFrame-EC for data-oriented programming with single-pass processing.
     *
     * @param fileName The input file name
     * @return The sum of all block calculations
     */
    @Override
    public Long solvePartTwo(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        if (lines.isEmpty()) {
            return 0L;
        }

        final int maxLength = lines.stream().mapToInt(String::length).max().orElse(0);
        final List<String> paddedLines = padLines(lines, maxLength);
        final DataFrame blocksDf = createBlocksDataFrame(paddedLines, maxLength);

        // Single-pass processing with direct aggregation
        final long[] sumHolder = {0L};

        blocksDf.collect(
                () -> null,
                (acc, cursor) -> {
                    final int startCol = (int) cursor.getInt("startCol");
                    final int endCol = (int) cursor.getInt("endCol");

                    // Find operator first
                    final MathOperator operator = findOperatorInRange(paddedLines, startCol, endCol);

                    // Process columns right to left inline
                    final List<Long> numbers = new ArrayList<>();
                    for (int col = endCol - 1; col >= startCol; col--) {
                        extractVerticalNumber(paddedLines, col).ifPresent(numbers::add);
                    }

                    sumHolder[0] += calculate(numbers, operator);
                }
        );

        return sumHolder[0];
    }

    /**
     * Creates a DataFrame containing blocks (startCol/endCol columns) from padded lines.
     * Identifies column ranges separated by empty columns.
     *
     * @param paddedLines The padded input lines
     * @param maxLength The maximum line length
     * @return A DataFrame with block columns
     */
    private DataFrame createBlocksDataFrame(final List<String> paddedLines, final int maxLength) {
        final List<Integer> separatorColumns = IntStream.range(0, maxLength)
                .filter(col -> isSeparatorColumn(paddedLines, col))
                .boxed()
                .toList();

        final MutableIntList startCols = new IntArrayList();
        final MutableIntList endCols = new IntArrayList();

        if (separatorColumns.isEmpty()) {
            startCols.add(0);
            endCols.add(maxLength);
        } else {
            int startCol = 0;
            for (final int separatorCol : separatorColumns) {
                if (separatorCol > startCol) {
                    startCols.add(startCol);
                    endCols.add(separatorCol);
                }
                startCol = separatorCol + 1;
            }
            // Add the final block if there's content after the last separator
            if (startCol < maxLength) {
                startCols.add(startCol);
                endCols.add(maxLength);
            }
        }

        return new DataFrame("Blocks")
                .addIntColumn("startCol", startCols)
                .addIntColumn("endCol", endCols);
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

    /**
     * Finds the operator in a specific column range.
     * Pure function used during DataFrame processing.
     *
     * @param lines The padded lines
     * @param startCol The starting column (inclusive)
     * @param endCol The ending column (exclusive)
     * @return The operator found, or NONE if none found
     */
    private MathOperator findOperatorInRange(final List<String> lines, final int startCol, final int endCol) {
        for (final String line : lines) {
            final String sub = safeSubstring(line, startCol, endCol).trim();
            if (containsOperatorSymbol(sub)) {
                return MathOperator.from(sub);
            }
        }
        return MathOperator.NONE;
    }

    /**
     * Checks if a string contains an operator symbol.
     * Pure function used during DataFrame processing.
     *
     * @param sub The string to check
     * @return true if the string contains an operator symbol
     */
    private boolean containsOperatorSymbol(final String sub) {
        return sub.contains(MathOperator.ADDITION.symbol())
                || sub.contains(MathOperator.MULTIPLICATION.symbol());
    }

    /**
     * Checks if a string is not an operator symbol.
     * Pure function used during DataFrame processing.
     *
     * @param sub The string to check
     * @return true if the string is not an operator symbol
     */
    private boolean isOperatorSymbol(final String sub) {
        return sub.equals(MathOperator.ADDITION.symbol())
                || sub.equals(MathOperator.MULTIPLICATION.symbol());
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
