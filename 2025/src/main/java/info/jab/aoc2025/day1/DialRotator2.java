package info.jab.aoc2025.day1;

import java.util.List;

import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;
import io.github.vmzakharov.ecdataframe.dataframe.DataFrame;
import org.eclipse.collections.api.factory.Lists;

/**
 * Handles dial rotation logic for a 0-99 dial using a pure DataFrame-oriented approach.
 * The dial can be rotated left (L) or right (R) by a specified distance.
 * <p>
 * This implementation uses DataFrame as the primary data abstraction:
 * - Single DataFrame for input data
 * - Single-pass processing with DataFrame collect
 * - Direct aggregation without intermediate collections
 * - Inline parsing and processing during DataFrame iteration
 */
public final class DialRotator2 implements Solver<Integer> {

    private static final int DIAL_MAX = 100;
    private static final int INITIAL_POSITION = 50;

    /**
     * Counts how many times the dial points at 0 after each complete rotation.
     * Each rotation is applied as a single operation.
     * <p>
     * Pure DataFrame approach:
     * 1. Create single DataFrame with rotation strings
     * 2. Single-pass processing: parse, validate, and process inline using DataFrame collect
     * 3. Direct aggregation: accumulate zero count directly in accumulator
     * <p>
     * Performance: O(n) single pass, O(1) extra space, no intermediate collections
     *
     * @param fileName File name containing rotation strings
     * @return The count of times the dial points at 0 after rotations
     */
    @Override
    public Integer solvePartOne(final String fileName) {
        final List<String> rotations = ResourceLines.list(fileName);

        // Create DataFrame with rotation strings
        final DataFrame df = new DataFrame("Rotations")
                .addStringColumn("rotation", Lists.mutable.ofAll(rotations));

        // Single-pass processing with direct aggregation
        // Accumulator: [position, zeroCount]
        final int[] accumulator = {INITIAL_POSITION, 0};

        df.collect(
                () -> null,
                (acc, cursor) -> {
                    final String rotationStr = cursor.getString("rotation");

                    // Inline validation and processing
                    if (isValidRotation(rotationStr)) {
                        // Parse direction and distance directly without Rotation object
                        final char directionChar = parseDirection(rotationStr);
                        final int distance = parseDistance(rotationStr);
                        final Direction direction = Direction.from(directionChar);

                        // Apply rotation and accumulate zero count directly
                        accumulator[0] = rotateDial(accumulator[0], direction, distance);
                        if (accumulator[0] == 0) {
                            accumulator[1]++;
                        }
                    }
                }
        );

        return accumulator[1];
    }

    /**
     * Counts how many times the dial points at 0 during rotations.
     * Optimized to calculate zero crossings directly without expanding rotations.
     * <p>
     * Pure DataFrame approach:
     * 1. Create single DataFrame with rotation strings
     * 2. Single-pass processing: parse, validate, and process inline using DataFrame collect
     * 3. Direct aggregation: accumulate zero crossings directly in accumulator
     * <p>
     * Complexity: O(n) single pass instead of O(n×d) by using modular arithmetic to count zero crossings.
     * Performance: O(n) time, O(1) extra space, no intermediate collections
     *
     * @param fileName File name containing rotation strings
     * @return The count of times the dial points at 0 during rotations
     */
    @Override
    public Integer solvePartTwo(final String fileName) {
        final List<String> rotations = ResourceLines.list(fileName);

        // Create DataFrame with rotation strings
        final DataFrame df = new DataFrame("Rotations")
                .addStringColumn("rotation", Lists.mutable.ofAll(rotations));

        // Single-pass processing with direct aggregation
        // Accumulator: [position, zeroCrossings]
        final int[] accumulator = {INITIAL_POSITION, 0};

        df.collect(
                () -> null,
                (acc, cursor) -> {
                    final String rotationStr = cursor.getString("rotation");

                    // Inline validation and processing
                    if (isValidRotation(rotationStr)) {
                        // Parse direction and distance directly without Rotation object
                        final char directionChar = parseDirection(rotationStr);
                        final int distance = parseDistance(rotationStr);
                        final Direction direction = Direction.from(directionChar);

                        // Calculate zero crossings and apply rotation, accumulate directly
                        accumulator[1] += countZeroCrossings(accumulator[0], direction, distance);
                        accumulator[0] = rotateDial(accumulator[0], direction, distance);
                    }
                }
        );

        return accumulator[1];
    }

    /**
     * Extracts the direction character from a rotation string.
     * Pure function: extracts direction directly without creating Rotation object.
     *
     * @param rotation Rotation string in format "L{distance}" or "R{distance}"
     * @return Direction character ('L' or 'R')
     */
    private char parseDirection(final String rotation) {
        return rotation.charAt(0);
    }

    /**
     * Parses the distance from a rotation string.
     * Pure function: parses distance directly without creating Rotation object.
     *
     * @param rotation Rotation string in format "L{distance}" or "R{distance}"
     * @return Distance as integer
     */
    private int parseDistance(final String rotation) {
        return Integer.parseInt(rotation.substring(1));
    }

    /**
     * Checks if a rotation string is valid (not null and not empty after trimming).
     * Optimized: avoids creating new string with trim(), checks length first for fast path.
     *
     * @param rotation Rotation string to validate
     * @return true if rotation is valid, false otherwise
     */
    private boolean isValidRotation(final String rotation) {
        if (rotation == null || rotation.isEmpty()) {
            return false;
        }
        // Fast path: if string has length > 0 and first char is not whitespace, it's valid
        if (!Character.isWhitespace(rotation.charAt(0))) {
            return true;
        }
        // Slow path: check if any non-whitespace character exists
        for (int i = 1; i < rotation.length(); i++) {
            if (!Character.isWhitespace(rotation.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates how many times position 0 is visited during a rotation.
     * Uses modular arithmetic to count zero crossings without expanding rotations.
     *
     * @param currentPosition Current dial position (0-99)
     * @param direction Rotation direction
     * @param distance Number of positions to rotate
     * @return Number of times position 0 is visited during the rotation
     */
    int countZeroCrossings(final int currentPosition, final Direction direction, final int distance) {
        return switch (direction) {
            // RIGHT: count multiples of DIAL_MAX in range [currentPosition+1, currentPosition+distance]
            case RIGHT -> currentPosition == 0
                    ? distance / DIAL_MAX
                    : (currentPosition + distance) / DIAL_MAX;
            // LEFT: count when we cross 0 going backwards
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
