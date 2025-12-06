package info.jab.aoc2025.day6;

/**
 * Immutable record representing a column range for block processing.
 *
 * @param startCol The starting column index (inclusive)
 * @param endCol The ending column index (exclusive)
 */
public record ColumnRange(int startCol, int endCol) {
    /**
     * Validates that the range is valid.
     */
    public ColumnRange {
        if (startCol < 0) {
            throw new IllegalArgumentException("startCol must be non-negative: " + startCol);
        }
        if (endCol < startCol) {
            throw new IllegalArgumentException("endCol must be >= startCol: " + endCol + " < " + startCol);
        }
    }
}

