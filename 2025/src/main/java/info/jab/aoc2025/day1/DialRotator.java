package info.jab.aoc2025.day1;

import java.util.List;

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
     * Uses a functional pipeline: filter invalid inputs → parse to Rotation → reduce with state.
     *
     * @param rotations List of rotation strings in format "L{distance}" or "R{distance}"
     * @return The count of times the dial points at 0 after rotations
     */
    @Override
    public Integer solvePartOne(final String fileName) {
        final List<String> rotations = ResourceLines.list(fileName);
        return rotations.stream()
                .filter(this::isValidRotation)
                .map(Rotation::from)
                .reduce(
                        DialState.initial(INITIAL_POSITION),
                        (state, rotation) -> state.applyRotation(rotation, this),
                        (state1, state2) -> state2
                )
                .zeroCount();
    }

    /**
     * Counts how many times the dial points at 0 during rotations.
     * Optimized to calculate zero crossings directly without expanding rotations.
     * <p>
     * Uses a functional pipeline: filter invalid inputs → parse to Rotation → reduce with state.
     * Complexity: O(n) instead of O(n×d) by using modular arithmetic to count zero crossings.
     *
     * @param rotations List of rotation strings in format "L{distance}" or "R{distance}"
     * @return The count of times the dial points at 0 during rotations
     */
    @Override
    public Integer solvePartTwo(final String fileName) {
        final List<String> rotations = ResourceLines.list(fileName);

        return rotations.stream()
                .filter(this::isValidRotation)
                .map(Rotation::from)
                .reduce(
                        DialState.initial(INITIAL_POSITION),
                        (state, rotation) -> state.applyRotationWithZeroCount(rotation, this),
                        (state1, state2) -> state2
                )
                .zeroCount();
    }

    /**
     * Checks if a rotation string is valid (not null and not empty after trimming).
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
            case RIGHT -> {
                // For RIGHT rotation: we visit 0 when (currentPosition + i) % DIAL_MAX == 0
                // for i in [1, distance]. This happens when currentPosition + i is a multiple of DIAL_MAX.
                // So i = 100k - currentPosition for some k where 1 <= i <= distance.
                // This means: 1 <= 100k - currentPosition <= distance
                // Rearranging: ceil((currentPosition + 1) / 100) <= k <= floor((currentPosition + distance) / 100)
                // Since currentPosition < 100: ceil((currentPosition + 1) / 100) = 1 (if currentPosition > 0) or 0 (if currentPosition == 0)
                // So count = floor((currentPosition + distance) / 100) - (currentPosition > 0 ? 0 : 1)
                // Simplified: if currentPosition == 0, count = floor(distance / 100)
                //            else, count = floor((currentPosition + distance) / 100)
                yield currentPosition == 0 
                    ? distance / DIAL_MAX 
                    : (currentPosition + distance) / DIAL_MAX;
            }
            case LEFT -> {
                // For LEFT rotation: we visit 0 when (currentPosition - i) % DIAL_MAX == 0
                // for i in [1, distance]. This happens when currentPosition - i is <= 0 (mod DIAL_MAX).
                // So we visit 0 when currentPosition - i <= 0, i.e., i >= currentPosition.
                // But we also wrap around, so we visit 0 when currentPosition - i + 100k == 0 for some k.
                // More simply: we visit 0 when i = currentPosition + 100k for k >= 0, where 1 <= i <= distance.
                // So: 1 <= currentPosition + 100k <= distance, i.e., k ranges from max(0, ceil((1 - currentPosition) / 100)) 
                // to floor((distance - currentPosition) / 100).
                // If currentPosition == 0: we visit 0 when i = 100k for k >= 1, so count = floor(distance / 100)
                // If currentPosition > 0 and currentPosition <= distance: we visit 0 at i = currentPosition, and possibly more
                //   if currentPosition + 100k <= distance for k >= 1, so count = 1 + floor((distance - currentPosition) / 100)
                // If currentPosition > distance: we don't visit 0
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
