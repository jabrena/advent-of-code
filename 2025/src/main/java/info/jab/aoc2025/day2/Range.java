package info.jab.aoc2025.day2;

import module java.base;

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
     * Returns all IDs in this range as a list.
     * Pre-allocates list with estimated size.
     *
     * @return List of IDs from start to end (inclusive)
     */
    List<Long> toIds() {
        final int size = (int) Math.min(end - start + 1, Integer.MAX_VALUE);
        final List<Long> out = new ArrayList<>(size);
        for (long n = start; n <= end; n++) {
            out.add(n);
        }
        return out;
    }

    /**
     * Returns invalid IDs in this range.
     * An ID is invalid if it has repeated parts and the number of repeats
     * is greater than 0 and less than or equal to max.
     * Pre-allocates list with estimated size.
     *
     * @param max Maximum number of repeats allowed
     * @param repeatedFunction Function that calculates the number of repeats for an ID
     * @return List of invalid IDs
     */
    List<Long> invalidIds(final int max, final LongToIntFunction repeatedFunction) {
        final int estimatedSize = (int) Math.min((end - start + 1) / 10, Integer.MAX_VALUE);
        final List<Long> out = new ArrayList<>(estimatedSize > 0 ? estimatedSize : 16);
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
     * Optimized to avoid regex overhead by using indexOf.
     *
     * @param rangeStr A string in the format "start-end"
     * @return A Range record
     */
    static Range from(final String rangeStr) {
        final int dashIdx = rangeStr.indexOf('-');
        final long start = parseLongFromString(rangeStr, 0, dashIdx);
        final long end = parseLongFromString(rangeStr, dashIdx + 1, rangeStr.length());
        return new Range(start, end);
    }

    /**
     * Parses a long from a substring without creating intermediate String.
     *
     * @param str String to parse from
     * @param start Start index (inclusive)
     * @param end End index (exclusive)
     * @return Parsed long value
     */
    private static long parseLongFromString(final String str, final int start, final int end) {
        long result = 0;
        for (int i = start; i < end; i++) {
            result = result * 10 + (str.charAt(i) - '0');
        }
        return result;
    }
}

