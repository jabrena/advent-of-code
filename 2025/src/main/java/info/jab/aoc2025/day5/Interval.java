package info.jab.aoc2025.day5;

/**
 * Represents a range with start and end values (inclusive).
 * Immutable record following functional programming principles.
 */
public record Interval(long start, long end) {
    /**
     * Compact constructor with validation for type safety.
     * Ensures start <= end, making invalid states unrepresentable.
     */
    public Interval {
        if (start > end) {
            throw new IllegalArgumentException(
                    "Range start (%d) must be less than or equal to end (%d)".formatted(start, end)
            );
        }
    }

    /**
     * Checks if a value is contained within this range.
     * Pure function: depends only on input parameters, no side effects.
     * Final method for better inlining by JIT compiler.
     *
     * @param value The value to check
     * @return true if the value is within the range, false otherwise
     */
    public final boolean contains(final long value) {
        return value >= start && value <= end;
    }

    /**
     * Calculates the size (number of values) in this range.
     * Pure function for functional composition.
     *
     * @return The size of the range (end - start + 1)
     */
    public long size() {
        return end - start + 1;
    }

    /**
     * Checks if this range overlaps or is adjacent to another range.
     * Pure function for functional composition.
     *
     * @param other The other range to check
     * @return true if ranges overlap or are adjacent, false otherwise
     */
    public boolean overlapsOrAdjacent(Interval other) {
        return other.start() <= this.end() + 1;
    }

    /**
     * Merges this range with another range, assuming they overlap or are adjacent.
     * Pure function that returns a new immutable Range instance.
     *
     * @param other The other range to merge with
     * @return A new Range representing the merged range
     */
    public Interval merge(Interval other) {
        return new Interval(
                Math.min(this.start(), other.start()),
                Math.max(this.end(), other.end())
        );
    }

    /**
     * Static factory method that creates a Range from a string representation.
     * Pure function: depends only on input parameter, no side effects.
     * Follows the "Consider static factory methods instead of constructors" principle.
     * Optimized to use indexOf() instead of split() to avoid array allocation.
     *
     * @param line The line containing range in format "start-end"
     * @return A Range record
     * @throws IllegalArgumentException if the format is invalid or parsing fails
     */
    public static Interval from(final String line) {
        final int dashIdx = line.indexOf('-');
        if (dashIdx == -1 || dashIdx == 0 || dashIdx == line.length() - 1) {
            throw new IllegalArgumentException(
                    "Invalid range format: '%s'. Expected format: 'start-end'".formatted(line)
            );
        }
        try {
            final long start = parseLongFromString(line, 0, dashIdx);
            final long end = parseLongFromString(line, dashIdx + 1, line.length());
            return new Interval(start, end);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid range format: '%s'. Start and end must be valid numbers.".formatted(line),
                    e
            );
        }
    }

    /**
     * Parses a long from a substring without creating intermediate String.
     *
     * @param str String to parse from
     * @param start Start index (inclusive)
     * @param end End index (exclusive)
     * @return Parsed long value
     * @throws NumberFormatException if the string is not a valid number
     */
    private static long parseLongFromString(final String str, final int start, final int end) {
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
        
        long result = 0;
        for (int i = actualStart; i < actualEnd; i++) {
            final char c = str.charAt(i);
            if (c < '0' || c > '9') {
                throw new NumberFormatException("Invalid character: " + c);
            }
            result = result * 10 + (c - '0');
        }
        return result;
    }
}

