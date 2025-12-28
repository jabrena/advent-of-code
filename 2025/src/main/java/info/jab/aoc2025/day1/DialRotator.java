package info.jab.aoc2025.day1;

import module java.base;

import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;

/**
 * Handles dial rotation logic for a 0-99 dial.
 * The dial can be rotated left (L) or right (R) by a specified distance.
 */
public final class DialRotator implements Solver<Integer> {

    private static final int DIAL_MAX = 100;
    private static final int INITIAL_POSITION = 50;

    /**
     * Counts how many times the dial points at 0 after each complete rotation.
     * Each rotation is applied as a single operation.
     * <p>
     * Optimized to parse on-the-fly without creating intermediate Rotation objects.
     *
     * @param fileName File name containing rotation strings
     * @return The count of times the dial points at 0 after rotations
     */
    @Override
    public Integer solvePartOne(final String fileName) {
        return solve(fileName, false);
    }

    /**
     * Counts how many times the dial points at 0 during rotations.
     * Optimized to calculate zero crossings directly without expanding rotations.
     * <p>
     * Optimized to parse on-the-fly without creating intermediate Rotation objects.
     * Complexity: O(n) instead of O(n×d) by using modular arithmetic to count zero crossings.
     *
     * @param fileName File name containing rotation strings
     * @return The count of times the dial points at 0 during rotations
     */
    @Override
    public Integer solvePartTwo(final String fileName) {
        return solve(fileName, true);
    }

    /**
     * Unified solve method that processes rotations and counts zero occurrences.
     * The only difference between part 1 and part 2 is which state method is called:
     * - Part 1: applyRotationDirect (counts zeros only after complete rotations)
     * - Part 2: applyRotationWithZeroCountDirect (counts zeros during rotations)
     * <p>
     * Optimized to parse on-the-fly without creating intermediate Rotation objects.
     *
     * @param fileName File name containing rotation strings
     * @param countDuringRotation If true, counts zeros during rotation (part 2); if false, counts after rotation (part 1)
     * @return The count of times the dial points at 0
     */
    private Integer solve(final String fileName, final boolean countDuringRotation) {
        final List<String> rotations = ResourceLines.list(fileName);
        DialState state = DialState.initial(INITIAL_POSITION);

        for (final String rotation : rotations) {
            if (isValidRotation(rotation)) {
                final char directionChar = rotation.charAt(0);
                final Direction direction = Direction.from(directionChar);
                final int distance = Integer.parseInt(rotation.substring(1));
                state = countDuringRotation
                        ? state.applyRotationWithZeroCountDirect(direction, distance, this)
                        : state.applyRotationDirect(direction, distance, this);
            }
        }
        return state.zeroCount();
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
