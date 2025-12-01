package info.jab.aoc2025.day1;

/**
 * Immutable state record representing dial position and zero count.
 *
 * @param position Current dial position (0-99)
 * @param zeroCount Number of times the dial has pointed at 0
 */
public record DialState(int position, int zeroCount) {
    /**
     * Creates initial dial state.
     *
     * @param initialPosition Initial dial position
     * @return Initial state with dial at starting position and zero count of 0
     */
    public static DialState initial(final int initialPosition) {
        return new DialState(initialPosition, 0);
    }

    /**
     * Applies a rotation and updates state.
     *
     * @param rotation Rotation to apply
     * @param rotator DialRotator instance to perform rotation
     * @return New state after rotation
     */
    public DialState applyRotation(final Rotation rotation, final DialRotator rotator) {
        final int newPosition = rotator.rotateDial(this.position, rotation.direction(), rotation.distance());
        final int newZeroCount = this.zeroCount + (newPosition == 0 ? 1 : 0);
        return new DialState(newPosition, newZeroCount);
    }

    /**
     * Applies a single-step rotation and updates state.
     *
     * @param direction Rotation direction
     * @param rotator DialRotator instance to perform rotation
     * @return New state after one-step rotation
     */
    public DialState applySingleStep(final Direction direction, final DialRotator rotator) {
        final int newPosition = rotator.rotateDialByOne(this.position, direction);
        final int newZeroCount = this.zeroCount + (newPosition == 0 ? 1 : 0);
        return new DialState(newPosition, newZeroCount);
    }
}

