package info.jab.aoc2025.day5;

import module java.base;

public record RangeProblemInput(List<Interval> intervals, List<Long> ids) {

    /**
     * Static factory method that creates a RangeProblemInput from a list of lines.
     * Pure function: depends only on input parameter, no side effects.
     * Follows the "Consider static factory methods instead of constructors" principle.
     * The I/O boundary is at the call site where ResourceLines.list() is invoked.
     *
     * @param lines The input lines to parse (format: ranges first, blank line separator, then IDs)
     * @return A RangeProblemInput record containing immutable ranges and IDs
     */
    public static RangeProblemInput from(final List<String> lines) {
        // Split lines at the blank line separator using IntStream for index-based operations
        final int separatorIndex = IntStream.range(0, lines.size())
                .filter(i -> lines.get(i).isBlank())
                .findFirst()
                .orElse(lines.size());

        final List<Interval> intervals = lines.stream()
                .limit(separatorIndex)
                .filter(line -> !line.isBlank())
                .map(Interval::from)
                .toList();

        final List<Long> ids = lines.stream()
                .skip((long) separatorIndex + 1)
                .filter(line -> !line.isBlank())
                .map(String::trim)
                .map(Long::parseLong)
                .toList();

        return new RangeProblemInput(intervals, ids);
    }
}

