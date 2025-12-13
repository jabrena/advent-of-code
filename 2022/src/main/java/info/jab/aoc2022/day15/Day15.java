package info.jab.aoc2022.day15;

import com.putoet.resources.ResourceLines;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day15 {

    // @formatter:off
    private static final String REGEX =
            "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    // @formatter:on

    private record Data(Point sp, Point bp, int d) {
    }

    private record Point(int x, int y) {
        public int dist(Point p2) {
            return Math.abs(x - p2.x) + Math.abs(y - p2.y);
        }
    }

    private static List<Data> processLines(List<String> lines) {
        return lines
            .stream()
            .map(Day15::parseLine)
            .flatMap(Optional::stream)
            .toList();
    }

    private static Optional<Data> parseLine(String line) {
        Matcher matcher = PATTERN.matcher(line);
        if (matcher.find()) {
            var sp = new Point(
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2))
            );
            var bp = new Point(
                Integer.parseInt(matcher.group(3)),
                Integer.parseInt(matcher.group(4))
            );
            int distance = sp.dist(bp);
            return Optional.of(new Data(sp, bp, distance));
        }
        return Optional.empty();
    }

    public Long getPart1Result(String fileName, Integer y) {
        //Source
        var fileLines = ResourceLines.list("/" + fileName);

        //Transform
        var processed = processLines(fileLines);

        // Optimize: Use range merging instead of checking each point
        var ranges = getRangesForRow(processed, y);
        var mergedRanges = mergeRangesPrimitive(ranges);

        // Count covered positions
        long covered = mergedRanges.stream()
            .mapToLong(range -> range[1] - range[0] + 1)
            .sum();

        // Count unique beacons on this row that are within covered ranges
        Set<Point> beaconsOnRow = new HashSet<>();
        for (var s : processed) {
            if (s.bp().y() == y) {
                for (var range : mergedRanges) {
                    if (s.bp().x() >= range[0] && s.bp().x() <= range[1]) {
                        beaconsOnRow.add(s.bp());
                        break;
                    }
                }
            }
        }

        //Sink
        return covered - beaconsOnRow.size();
    }

    public Long getPart2Result(String fileName) {
        //Source
        var fileLines = ResourceLines.list("/" + fileName);

        //Transform
        var processed = processLines(fileLines);

        // Optimize: Precompute row bounds to skip rows with no sensor coverage
        int minRow = processed.stream().mapToInt(s -> s.sp().y() - s.d).min().orElse(0);
        int maxRow = processed.stream().mapToInt(s -> s.sp().y() + s.d).max().orElse(4000000);
        int searchMax = 4000000;

        // Clamp to search bounds
        int startRow = Math.max(0, minRow);
        int endRow = Math.min(searchMax, maxRow);

        //Sink
        for (int y = startRow; y <= endRow; y++) {
            // Optimize: Use primitive arrays instead of Guava Range
            var ranges = getRangesForRow(processed, y);

            // Skip rows with no coverage
            if (ranges.isEmpty()) {
                // No coverage means entire row is a gap
                return (long) 0 * searchMax + y;
            }

            // Optimize: Merge ranges per row (O(n log n) per row)
            var mergedRanges = mergeRangesPrimitive(ranges);

            // Check for gaps in merged ranges (O(R) where R is number of merged ranges)
            var gap = findGapPrimitive(mergedRanges, searchMax);
            if (gap.isPresent()) {
                return (long) gap.getAsInt() * searchMax + y;
            }
        }

        return 0L; // not reached
    }

    /**
     * Gets ranges for a specific row using primitive arrays [start, end].
     * Optimized to avoid object allocation.
     * Complexity: O(n) where n is the number of sensors.
     */
    private List<int[]> getRangesForRow(List<Data> processed, int y) {
        var ranges = new ArrayList<int[]>();
        for (var s : processed) {
            int dx = s.d - Math.abs(y - s.sp.y());
            if (dx >= 0) {
                ranges.add(new int[]{s.sp.x() - dx, s.sp.x() + dx});
            }
        }
        return ranges;
    }

    /**
     * Merges overlapping and adjacent ranges using primitive arrays.
     * Ranges are represented as int[]{start, end}.
     * Complexity: O(n log n) where n is the number of ranges.
     */
    private List<int[]> mergeRangesPrimitive(List<int[]> ranges) {
        if (ranges.isEmpty()) {
            return ranges;
        }

        // Sort ranges by start position
        ranges.sort(Comparator.comparingInt(range -> range[0]));

        var merged = new ArrayList<int[]>();
        var current = ranges.get(0);

        for (int i = 1; i < ranges.size(); i++) {
            var next = ranges.get(i);

            // Check if ranges overlap or are adjacent
            // Adjacent ranges (e.g., [1,5] and [6,10]) can merge
            if (current[1] >= next[0] - 1) {
                // Merge: take the maximum end position
                current = new int[]{current[0], Math.max(current[1], next[1])};
            } else {
                // No overlap, add current and move to next
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);

        return merged;
    }

    /**
     * Finds a gap in the merged ranges within [0, max].
     * Uses primitive arrays for better performance.
     * Complexity: O(R) where R is the number of merged ranges.
     * Returns empty if no gap found.
     */
    private OptionalInt findGapPrimitive(List<int[]> mergedRanges, int max) {
        if (mergedRanges.isEmpty()) {
            // No ranges means the entire row is a gap, return 0
            return OptionalInt.of(0);
        }

        // Check for gap before first range
        var first = mergedRanges.get(0);
        if (first[0] > 0) {
            return OptionalInt.of(0);
        }

        // Check for gaps between ranges
        for (int i = 0; i < mergedRanges.size() - 1; i++) {
            var current = mergedRanges.get(i);
            var next = mergedRanges.get(i + 1);

            // If there's a gap between current and next
            if (current[1] < next[0] - 1) {
                int gapX = current[1] + 1;
                if (gapX <= max) {
                    return OptionalInt.of(gapX);
                }
            }
        }

        // Check for gap after last range
        var last = mergedRanges.get(mergedRanges.size() - 1);
        if (last[1] < max) {
            return OptionalInt.of(last[1] + 1);
        }

        return OptionalInt.empty();
    }
}
