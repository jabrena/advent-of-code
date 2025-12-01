package info.jab.aoc2025.day1;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Handles dial rotation logic for a 0-99 dial.
 * The dial can be rotated left (L) or right (R) by a specified distance.
 */
public final class DialRotator {

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
    public int countZerosAfterRotations(final List<String> rotations) {
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
     * Each rotation is simulated click-by-click.
     * <p>
     * Uses a functional pipeline: filter invalid inputs → parse to Rotation → expand to steps → reduce with state.
     *
     * @param rotations List of rotation strings in format "L{distance}" or "R{distance}"
     * @return The count of times the dial points at 0 during rotations
     */
    public int countZerosDuringRotations(final List<String> rotations) {
        return rotations.stream()
                .filter(this::isValidRotation)
                .map(Rotation::from)
                .flatMap(this::expandToSteps)
                .reduce(
                        DialState.initial(INITIAL_POSITION),
                        (state, direction) -> state.applySingleStep(direction, this),
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
     * Expands a rotation into a stream of individual direction steps.
     *
     * @param rotation Rotation to expand
     * @return Stream of Direction values, one for each step in the rotation
     */
    private Stream<Direction> expandToSteps(final Rotation rotation) {
        return IntStream.range(0, rotation.distance())
                .mapToObj(_ -> rotation.direction());
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
