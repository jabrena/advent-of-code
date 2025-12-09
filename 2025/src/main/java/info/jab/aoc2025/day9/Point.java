package info.jab.aoc2025.day9;

/**
 * Represents a 2D point with integer coordinates.
 * Immutable record following functional programming principles.
 *
 * @param x The x coordinate
 * @param y The y coordinate
 */
public record Point(int x, int y) {
    /**
     * Static factory method that creates a Point from a string representation.
     * Pure function: depends only on input parameter, no side effects.
     * Follows the "Consider static factory methods instead of constructors" principle.
     *
     * @param line The line containing point in format "x,y"
     * @return A Point record
     * @throws IllegalArgumentException if the format is invalid or parsing fails
     */
    public static Point from(final String line) {
        final String[] parts = line.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "Invalid point format: '%s'. Expected format: 'x,y'".formatted(line)
            );
        }
        try {
            return new Point(
                    Integer.parseInt(parts[0].trim()),
                    Integer.parseInt(parts[1].trim())
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid point format: '%s'. Coordinates must be valid integers.".formatted(line),
                    e
            );
        }
    }
}

