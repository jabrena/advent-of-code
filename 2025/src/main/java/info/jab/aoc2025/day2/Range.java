package info.jab.aoc2025.day2;

import module java.base;

/**
 * Immutable record representing a range of IDs.
 *
 * @param start The start of the range (inclusive)
 * @param end   The end of the range (inclusive)
 */
record Range(long start, long end) {

    private static final Pattern RANGE_SEPARATOR_PATTERN = Pattern.compile("-");
    /**
     * Creates a stream of all IDs in this range.
     *
     * @return A stream of IDs from start to end (inclusive)
     */
    LongStream ids() {
        return LongStream.rangeClosed(start, end);
    }

    /**
     * Returns all IDs in this range as a list.
     *
     * @return List of IDs from start to end (inclusive)
     */
    List<Long> toIds() {
        final List<Long> out = new ArrayList<>();
        for (long n = start; n <= end; n++) {
            out.add(n);
        }
        return out;
    }

    /**
     * Returns invalid IDs in this range.
     * An ID is invalid if it has repeated parts and the number of repeats
     * is greater than 0 and less than or equal to max.
     *
     * @param max Maximum number of repeats allowed
     * @param repeatedFunction Function that calculates the number of repeats for an ID
     * @return List of invalid IDs
     */
    List<Long> invalidIds(final int max, final LongToIntFunction repeatedFunction) {
        final List<Long> out = new ArrayList<>();
        for (long id = start; id <= end; id++) {
            final int repeats = repeatedFunction.applyAsInt(id);
            if (repeats > 0 && repeats <= max) {
                out.add(id);
            }
        }
        return out;
    }

    /**
     * Static factory method that parses a range string into a Range record.
     * Pure function that creates a Range from a string in the format "start-end".
     *
     * @param rangeStr A string in the format "start-end"
     * @return A Range record
     */
    static Range from(final String rangeStr) {
        final String[] parts = RANGE_SEPARATOR_PATTERN.split(rangeStr);
        final long start = Long.parseLong(parts[0]);
        final long end = Long.parseLong(parts[1]);
        return new Range(start, end);
    }
}

