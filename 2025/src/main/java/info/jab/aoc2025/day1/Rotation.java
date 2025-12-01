package info.jab.aoc2025.day1;

/**
 * Record representing a dial rotation.
 *
 * @param direction Rotation direction
 * @param distance Number of positions to rotate
 */
public record Rotation(Direction direction, int distance) {
    /**
     * Creates a Rotation from a string representation.
     *
     * @param rotation Rotation string in format "L{distance}" or "R{distance}"
     * @return Parsed Rotation object
     * @throws IllegalArgumentException if rotation string is invalid
     */
    public static Rotation from(final String rotation) {
        if (rotation == null || rotation.trim().isEmpty()) {
            throw new IllegalArgumentException("Rotation string cannot be null or empty");
        }

        final char directionChar = rotation.charAt(0);
        final Direction direction = Direction.from(directionChar);
        final int distance = Integer.parseInt(rotation.substring(1));

        return new Rotation(direction, distance);
    }
}

