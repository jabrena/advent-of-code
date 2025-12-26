package info.jab.aoc2025.day1;

import module java.base;

import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Handles dial rotation logic for a 0-99 dial.
 * The dial can be rotated left (L) or right (R) by a specified distance.
 * Optimized with:
 * - Mutable state (eliminates DialState allocations)
 * - Single-pass processing (combines filtering and processing)
 * - FastUtil ObjectArrayList for efficient iteration
 */
public final class DialRotator3 implements Solver<Integer> {

    private static final int DIAL_MAX = 100;
    private static final int INITIAL_POSITION = 50;

    /**
     * Counts how many times the dial points at 0 after each complete rotation.
     * Each rotation is applied as a single operation.
     * <p>
     * Optimized with:
     * - Mutable state to eliminate DialState record allocations
     * - Single-pass processing (filter and process in one loop)
     * - FastUtil ObjectArrayList for efficient iteration
     *
     * @param fileName Input file name containing rotation sequences
     * @return The count of times the dial points at 0 after rotations
     */
    @Override
    public Integer solvePartOne(final String fileName) {
        final List<String> allLines = ResourceLines.list(fileName);
        // Convert to FastUtil ObjectArrayList for efficient iteration
        final ObjectArrayList<String> lines = new ObjectArrayList<>(allLines);

        // Single-pass: filter and process in one loop with mutable state
        int position = INITIAL_POSITION;
        int zeroCount = 0;

        for (final String line : lines) {
            if (isValidRotation(line)) {
                final char directionChar = line.charAt(0);
                final Direction direction = Direction.from(directionChar);
                final int distance = parseIntFromOffset(line, 1);
                position = rotateDial(position, direction, distance);
                if (position == 0) {
                    zeroCount++;
                }
            }
        }

        return zeroCount;
    }

    /**
     * Counts how many times the dial points at 0 during rotations.
     * Optimized to calculate zero crossings directly without expanding rotations.
     * <p>
     * Optimized with:
     * - Mutable state to eliminate DialState record allocations
     * - Single-pass processing (filter and process in one loop)
     * - FastUtil ObjectArrayList for efficient iteration
     * Complexity: O(n) instead of O(n×d) by using modular arithmetic to count zero crossings.
     *
     * @param fileName Input file name containing rotation sequences
     * @return The count of times the dial points at 0 during rotations
     */
    @Override
    public Integer solvePartTwo(final String fileName) {
        final List<String> allLines = ResourceLines.list(fileName);
        // Convert to FastUtil ObjectArrayList for efficient iteration
        final ObjectArrayList<String> lines = new ObjectArrayList<>(allLines);

        // Single-pass: filter and process in one loop with mutable state
        int position = INITIAL_POSITION;
        int zeroCount = 0;

        for (final String line : lines) {
            if (isValidRotation(line)) {
                final char directionChar = line.charAt(0);
                final Direction direction = Direction.from(directionChar);
                final int distance = parseIntFromOffset(line, 1);
                final int zeroCrossings = countZeroCrossings(position, direction, distance);
                position = rotateDial(position, direction, distance);
                zeroCount += zeroCrossings;
            }
        }

        return zeroCount;
    }

    /**
     * Checks if a rotation string is valid (not null and not empty).
     * Optimized to avoid trim() allocation.
     *
     * @param rotation Rotation string to validate
     * @return true if rotation is valid, false otherwise
     */
    private boolean isValidRotation(final String rotation) {
        return rotation != null && !rotation.trim().isEmpty();
    }

    /**
     * Parses integer from string starting at given offset.
     * Optimized to avoid substring() allocation.
     *
     * @param str    String to parse
     * @param offset Starting offset
     * @return Parsed integer value
     */
    private static int parseIntFromOffset(final String str, final int offset) {
        return Integer.parseInt(str, offset, str.length(), 10);
    }

    /**
     * Calculates how many times position 0 is visited during a rotation.
     * Uses modular arithmetic to avoid expanding rotations into individual steps.
     * Counts when the dial is at position 0 after each step (not counting the starting position).
     *
     * @param currentPosition Current dial position (0-99)
     * @param direction Rotation direction
     * @param distance Number of positions to rotate
     * @return Number of times position 0 is visited during the rotation
     */
    int countZeroCrossings(final int currentPosition, final Direction direction, final int distance) {
        return switch (direction) {
            // RIGHT: Count multiples of DIAL_MAX in range [currentPosition+1, currentPosition+distance]
            case RIGHT -> currentPosition == 0
                    ? distance / DIAL_MAX
                    : (currentPosition + distance) / DIAL_MAX;
            // LEFT: Count when we cross 0 going backwards
            case LEFT -> {
                if (currentPosition == 0) {
                    yield distance / DIAL_MAX;
                } else if (currentPosition <= distance) {
                    yield 1 + (distance - currentPosition) / DIAL_MAX;
                } else {
                    yield 0;
                }
            }
        };
    }

    /**
     * Rotates the dial by a specified distance in the given direction.
     *
     * @param currentPosition Current dial position (0-99)
     * @param direction Rotation direction
     * @param distance Number of positions to rotate
     * @return New dial position after rotation
     */
    int rotateDial(final int currentPosition, final Direction direction, final int distance) {
        return switch (direction) {
            case LEFT -> {
                int newPosition = (currentPosition - distance) % DIAL_MAX;
                yield newPosition < 0 ? newPosition + DIAL_MAX : newPosition;
            }
            case RIGHT -> (currentPosition + distance) % DIAL_MAX;
        };
    }

    /**
     * Rotates the dial by one position in the given direction.
     *
     * @param currentPosition Current dial position (0-99)
     * @param direction Rotation direction
     * @return New dial position after one-step rotation
     */
    int rotateDialByOne(final int currentPosition, final Direction direction) {
        return rotateDial(currentPosition, direction, 1);
    }
}
