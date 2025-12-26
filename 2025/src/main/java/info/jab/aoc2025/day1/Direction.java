package info.jab.aoc2025.day1;

/**
 * Enum representing the possible rotation directions.
 */
public enum Direction {
    LEFT,
    RIGHT;

    /**
     * Converts a character to a Direction enum value.
     * Optimized to avoid Stream API overhead.
     *
     * @param symbol Character representing the direction ('L' or 'R')
     * @return Direction enum value
     * @throws IllegalArgumentException if the character is not a valid direction
     */
    public static Direction from(final char symbol) {
        return switch (Character.toUpperCase(symbol)) {
            case 'L' -> LEFT;
            case 'R' -> RIGHT;
            default -> throw new IllegalArgumentException("Direction must be 'L' or 'R', got: " + symbol);
        };
    }
}
