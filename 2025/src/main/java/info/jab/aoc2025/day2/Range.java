package info.jab.aoc2025.day2;

import java.util.stream.LongStream;

/**
 * Immutable record representing a range of IDs.
 *
 * @param start The start of the range (inclusive)
 * @param end   The end of the range (inclusive)
 */
record Range(long start, long end) {
    /**
     * Creates a stream of all IDs in this range.
     *
     * @return A stream of IDs from start to end (inclusive)
     */
    LongStream ids() {
        return LongStream.rangeClosed(start, end);
    }

    /**
     * Static factory method that parses a range string into a Range record.
     * Pure function that creates a Range from a string in the format "start-end".
     *
     * @param rangeStr A string in the format "start-end"
     * @return A Range record
     */
    static Range from(final String rangeStr) {
        final String[] parts = rangeStr.split("-");
        final long start = Long.parseLong(parts[0]);
        final long end = Long.parseLong(parts[1]);
        return new Range(start, end);
    }
}

