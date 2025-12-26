package info.jab.aoc2025.day1;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;

public final class DialRotator2 implements Solver<Integer> {

    private static final int END = 100;
    private static final int INITIAL_POSITION = 50;
    private static final String LINE_SEPARATOR_REGEX = "\\r?\\n";
    private static final Pattern ROTATION_PATTERN = Pattern.compile("^([LR])\\s*(\\d+)$", Pattern.CASE_INSENSITIVE);

    private Integer position;

    /**
     * Counts how many times the dial points at 0 after each complete rotation.
     * Each rotation is applied as a single operation.
     *
     * @param fileName Input file name containing rotation sequences
     * @return The count of times the dial points at 0 after rotations
     */
    @Override
    public Integer solvePartOne(final String fileName) {
        final String input = String.join("\n", ResourceLines.list(fileName));
        return solve(input, 1);
    }

    /**
     * Counts how many times the dial points at 0 during rotations.
     * Uses modular arithmetic to count zero crossings directly.
     *
     * @param fileName Input file name containing rotation sequences
     * @return The count of times the dial points at 0 during rotations
     */
    @Override
    public Integer solvePartTwo(final String fileName) {
        final String input = String.join("\n", ResourceLines.list(fileName));
        return solve(input, 2);
    }

    /**
     * Solves the dial rotation problem for the specified part.
     *
     * @param input Input string containing rotation sequences
     * @param part Part number (1 or 2)
     * @return The count of times the dial points at 0
     */
    private int solve(final String input, final int part) {
        position = INITIAL_POSITION; // reset position for each part
        int zeros = 0;
        final List<Sequence> sequences = parse(input);

        for (final Sequence seq : sequences) {
            final int directionValue = seq.direction() == Direction.LEFT ? -1 : 1;
            final int reposition = mod(position + directionValue * seq.steps());

            if (part == 1 && reposition == 0) {
                zeros++;
            } else if (part == 2) {
                final int remainder = mod(-directionValue * position);
                final int first = remainder == 0 ? END : remainder;
                if (seq.steps() >= first) {
                    zeros += 1 + (seq.steps() - first) / END;
                }
            }

            position = reposition;
        }

        return zeros;
    }

    /**
     * Parses input string into a list of rotation sequences.
     *
     * @param input Input string containing rotation sequences
     * @return List of parsed sequences
     * @throws IllegalArgumentException if any line is invalid
     */
    private List<Sequence> parse(final String input) {
        return Stream.of(input.split(LINE_SEPARATOR_REGEX))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(line -> {
                    final Matcher matcher = ROTATION_PATTERN.matcher(line);
                    if (!matcher.matches()) {
                        throw new IllegalArgumentException("Invalid sequence line: " + line);
                    }
                    final char directionChar = Character.toUpperCase(matcher.group(1).charAt(0));
                    final Direction direction = Direction.from(directionChar);
                    final int steps = Integer.parseInt(matcher.group(2));
                    return new Sequence(direction, steps);
                })
                .collect(Collectors.toList());
    }

    /**
     * Computes the mathematical modulo operation that always returns a non-negative result.
     *
     * @param n The number to compute modulo for
     * @return Non-negative result of n mod END
     */
    private int mod(final int n) {
        return ((n % END) + END) % END;
    }

    /**
     * Record representing a rotation sequence.
     *
     * @param direction Rotation direction
     * @param steps Number of steps to rotate
     */
    private record Sequence(Direction direction, int steps) { }
}

