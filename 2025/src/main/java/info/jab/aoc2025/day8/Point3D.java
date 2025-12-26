package info.jab.aoc2025.day8;

public record Point3D(int x, int y, int z) {

    public long distanceSquared(Point3D other) {
        long dx = this.x - other.x;
        long dy = this.y - other.y;
        long dz = this.z - other.z;
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Static factory method that creates a Point3D from a string representation.
     * Pure function: depends only on input parameter, no side effects.
     * Follows the "Consider static factory methods instead of constructors" principle.
     *
     * @param line The line containing point in format "x,y,z"
     * @return A Point3D record
     * @throws IllegalArgumentException if the format is invalid or parsing fails
     */
    public static Point3D from(String line) {
        String[] parts = line.split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "Invalid point format: '%s'. Expected format: 'x,y,z'".formatted(line)
            );
        }
        try {
            return new Point3D(
                Integer.parseInt(parts[0].trim()),
                Integer.parseInt(parts[1].trim()),
                Integer.parseInt(parts[2].trim())
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid point format: '%s'. Coordinates must be valid integers.".formatted(line),
                    e
            );
        }
    }
}

