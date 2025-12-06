package info.jab.aoc2015.day14;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Solver for reindeer race problems.
 * Uses functional programming principles:
 * - Pure functions for calculations
 * - Stream API for declarative transformations
 * - Immutable data structures
 */
public final class ReindeerRace implements Solver<Integer> {

    private static final int TOTAL_SECONDS = 2503;
    private static final Pattern REINDEER_PATTERN = Pattern.compile(
            "(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds\\.");


    @Override
    public Integer solvePartOne(final String fileName) {
        final List<Reindeer> reindeer = parseReindeer(fileName);
        
        return reindeer.stream()
                .mapToInt(r -> r.distanceAfter(TOTAL_SECONDS))
                .max()
                .orElse(0);
    }

    @Override
    public Integer solvePartTwo(final String fileName) {
        final List<Reindeer> reindeer = parseReindeer(fileName);
        
        // Use functional approach: calculate points for each reindeer
        // For each second, find max distance and award points to leaders
        final List<Integer> points = IntStream.range(0, reindeer.size())
                .mapToObj(reindeerIndex -> 
                    IntStream.rangeClosed(1, TOTAL_SECONDS)
                            .map(second -> {
                                final int distance = reindeer.get(reindeerIndex).distanceAfter(second);
                                final int maxDistance = reindeer.stream()
                                        .mapToInt(r -> r.distanceAfter(second))
                                        .max()
                                        .orElse(0);
                                return distance == maxDistance ? 1 : 0;
                            })
                            .sum()
                )
                .toList();
        
        return points.stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
    }
    
    /**
     * Pure function: parses reindeer from input file.
     * Uses stream API for declarative transformation.
     */
    private List<Reindeer> parseReindeer(final String fileName) {
        return ResourceLines.list(fileName).stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(line -> {
                    final var matcher = REINDEER_PATTERN.matcher(line);
                    if (matcher.matches()) {
                        return new Reindeer(
                                matcher.group(1),
                                Integer.parseInt(matcher.group(2)),
                                Integer.parseInt(matcher.group(3)),
                                Integer.parseInt(matcher.group(4))
                        );
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
