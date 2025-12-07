package info.jab.aoc2016.day20;

/**
 * Represents a range of IP addresses.
 */
public record Range(long start, long end) {
    boolean overlaps(final Range other) {
        return start <= other.end() + 1 && other.start() <= end + 1;
    }

    Range merge(final Range other) {
        return new Range(Math.min(start, other.start()), Math.max(end, other.end()));
    }
}

