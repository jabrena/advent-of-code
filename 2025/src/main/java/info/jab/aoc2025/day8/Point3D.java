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
     * Optimized to use indexOf() instead of split() and manual integer parsing.
     *
     * @param line The line containing point in format "x,y,z"
     * @return A Point3D record
     * @throws IllegalArgumentException if the format is invalid or parsing fails
     */
    public static Point3D from(String line) {
        final int firstComma = line.indexOf(',');
        if (firstComma == -1 || firstComma == 0) {
            throw new IllegalArgumentException(
                    "Invalid point format: '%s'. Expected format: 'x,y,z'".formatted(line)
            );
        }
        
        final int secondComma = line.indexOf(',', firstComma + 1);
        if (secondComma == -1 || secondComma == firstComma + 1 || secondComma == line.length() - 1) {
            throw new IllegalArgumentException(
                    "Invalid point format: '%s'. Expected format: 'x,y,z'".formatted(line)
            );
        }
        
        try {
            final int x = parseIntFromString(line, 0, firstComma);
            final int y = parseIntFromString(line, firstComma + 1, secondComma);
            final int z = parseIntFromString(line, secondComma + 1, line.length());
            return new Point3D(x, y, z);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid point format: '%s'. Coordinates must be valid integers.".formatted(line),
                    e
            );
        }
    }

    /**
     * Parses an integer from a substring without creating intermediate String.
     * Skips leading and trailing whitespace.
     *
     * @param str String to parse from
     * @param start Start index (inclusive)
     * @param end End index (exclusive)
     * @return Parsed integer value
     * @throws NumberFormatException if the string is not a valid number
     */
    private static int parseIntFromString(final String str, final int start, final int end) {
        // Skip leading whitespace
        int actualStart = start;
        while (actualStart < end && Character.isWhitespace(str.charAt(actualStart))) {
            actualStart++;
        }
        // Skip trailing whitespace
        int actualEnd = end;
        while (actualEnd > actualStart && Character.isWhitespace(str.charAt(actualEnd - 1))) {
            actualEnd--;
        }
        
        if (actualStart >= actualEnd) {
            throw new NumberFormatException("Empty string");
        }
        
        int result = 0;
        boolean negative = false;
        if (str.charAt(actualStart) == '-') {
            negative = true;
            actualStart++;
        } else if (str.charAt(actualStart) == '+') {
            actualStart++;
        }
        
        for (int i = actualStart; i < actualEnd; i++) {
            final char c = str.charAt(i);
            if (c < '0' || c > '9') {
                throw new NumberFormatException("Invalid character: " + c);
            }
            result = result * 10 + (c - '0');
        }
        
        return negative ? -result : result;
    }
}

