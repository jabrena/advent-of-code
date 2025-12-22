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
     *
     * @param value The value to check
     * @return true if the value is within the range, false otherwise
     */
    public boolean contains(long value) {
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
     *
     * @param line The line containing range in format "start-end"
     * @return A Range record
     * @throws IllegalArgumentException if the format is invalid or parsing fails
     */
    public static Interval from(final String line) {
        final String[] parts = line.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "Invalid range format: '%s'. Expected format: 'start-end'".formatted(line)
            );
        }
        try {
            return new Interval(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid range format: '%s'. Start and end must be valid numbers.".formatted(line),
                    e
            );
        }
    }
}

