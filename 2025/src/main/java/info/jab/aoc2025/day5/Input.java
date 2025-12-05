package info.jab.aoc2025.day5;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Represents the parsed input containing ranges and IDs.
 * Immutable record following functional programming principles.
 * The lists are expected to be immutable (e.g., from List.of() or List.copyOf()).
 */
public record Input(List<Range> ranges, List<Long> ids) {
    /**
     * Compact constructor ensuring immutability through defensive copying.
     */
    public Input {
        // Defensive copy to ensure immutability
        ranges = List.copyOf(ranges);
        ids = List.copyOf(ids);
    }

    /**
     * Static factory method that creates an Input from a list of lines.
     * Pure function: depends only on input parameter, no side effects.
     * Follows the "Consider static factory methods instead of constructors" principle.
     * The I/O boundary is at the call site where ResourceLines.list() is invoked.
     *
     * @param lines The input lines to parse (format: ranges first, blank line separator, then IDs)
     * @return An Input record containing immutable ranges and IDs
     */
    public static Input from(final List<String> lines) {
        // Split lines at the blank line separator using IntStream for index-based operations
        final int separatorIndex = IntStream.range(0, lines.size())
                .filter(i -> lines.get(i).isBlank())
                .findFirst()
                .orElse(lines.size());

        final List<Range> ranges = lines.stream()
                .limit(separatorIndex)
                .filter(line -> !line.isBlank())
                .map(Range::from)
                .toList();

        final List<Long> ids = lines.stream()
                .skip((long) separatorIndex + 1)
                .filter(line -> !line.isBlank())
                .map(String::trim)
                .map(Long::parseLong)
                .toList();

        return new Input(ranges, ids);
    }
}

