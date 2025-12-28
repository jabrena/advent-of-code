package info.jab.aoc2025.day7;

import module java.base;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

/**
 * Solver for beam path counting using Stream API with Gatherers.fold().
 * Uses Gatherers.fold() to process lines sequentially, maintaining beam positions.
 * Part 1: Counts splits using a mutable counter.
 * Part 2: Counts total paths using Stream API with Gatherers.fold().
 */
public final class BeamPathCounter2 implements Solver<Long> {

    @Override
    public Long solvePartOne(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        if (lines.isEmpty()) {
            return 0L;
        }

        // Find start position
        final int start = lines.getFirst().indexOf('S');
        if (start == -1) {
            return 0L;
        }

        // Mutable counter object
        final var counter = new Object() {
            int count = 0;
        };

        // Process lines using Gatherers.fold
        lines.stream()
                .gather(Gatherers.fold(
                        () -> Set.of(start),
                        (set, line) -> set.stream()
                                .flatMap(index -> {
                                    // Check bounds
                                    if (index < 0 || index >= line.length()) {
                                        return Stream.empty();
                                    }
                                    // If splitter, create two new beams and increment counter
                                    if (line.charAt(index) == '^') {
                                        counter.count++;
                                        return Stream.of(index - 1, index + 1);
                                    }
                                    // Otherwise, beam continues at same index
                                    return Stream.of(index);
                                })
                                .collect(Collectors.toSet())
                ))
                .findFirst()
                .orElseThrow();

        return (long) counter.count;
    }

    @Override
    public Long solvePartTwo(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        if (lines.isEmpty()) {
            return 0L;
        }

        final int start = lines.getFirst().indexOf('S');
        if (start == -1) {
            return 0L;
        }

        // Use Stream API with Gatherers.fold to process paths iteratively
        // Track position -> path count mapping for each row
        final Map<Integer, Long> finalPaths = lines.stream()
                .gather(Gatherers.fold(
                        () -> Map.of(start, 1L),
                        (pathCounts, line) -> pathCounts.entrySet().stream()
                                .flatMap(entry -> {
                                    final int x = entry.getKey();
                                    final long count = entry.getValue();

                                    // Check bounds
                                    if (x < 0 || x >= line.length()) {
                                        // Out of bounds: path continues (count as 1 path)
                                        return Stream.of(Map.entry(x, count));
                                    }

                                    final char cell = line.charAt(x);
                                    if (cell == '^') {
                                        // Splitter: create two paths (left and right)
                                        return Stream.of(
                                                Map.entry(x - 1, count),
                                                Map.entry(x + 1, count)
                                        );
                                    } else {
                                        // Continue straight down
                                        return Stream.of(Map.entry(x, count));
                                    }
                                })
                                .collect(Collectors.groupingBy(
                                        Map.Entry::getKey,
                                        Collectors.summingLong(Map.Entry::getValue)
                                ))
                ))
                .findFirst()
                .orElseThrow();

        // Sum all path counts (paths that reached the end)
        return finalPaths.values().stream()
                .mapToLong(Long::longValue)
                .sum();
    }
}

