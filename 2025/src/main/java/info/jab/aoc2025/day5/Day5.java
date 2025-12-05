package info.jab.aoc2025.day5;

import info.jab.aoc.Day;
import info.jab.aoc.Utils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day5 implements Day<Long> {

    record Range(long start, long end) {
        boolean contains(long value) {
            return value >= start && value <= end;
        }
    }

    private record Input(List<Range> ranges, List<Long> ids) {}

    private Input parse(String fileName) {
        List<String> lines = Utils.readFileToList(fileName);
        List<Range> ranges = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        
        boolean parsingRanges = true;
        for (String line : lines) {
            if (line.isBlank()) {
                if (parsingRanges) {
                    parsingRanges = false;
                }
                continue;
            }
            
            if (parsingRanges) {
                String[] parts = line.split("-");
                ranges.add(new Range(Long.parseLong(parts[0]), Long.parseLong(parts[1])));
            } else {
                ids.add(Long.parseLong(line.trim()));
            }
        }
        return new Input(ranges, ids);
    }

    @Override
    public Long getPart1Result(String fileName) {
        Input input = parse(fileName);
        return input.ids.stream()
                .filter(id -> input.ranges.stream().anyMatch(range -> range.contains(id)))
                .count();
    }

    @Override
    public Long getPart2Result(String fileName) {
        Input input = parse(fileName);
        List<Range> sortedRanges = input.ranges.stream()
            .sorted(Comparator.comparingLong(Range::start))
            .toList();

        List<Range> mergedRanges = new ArrayList<>();
        if (sortedRanges.isEmpty()) {
            return 0L;
        }

        Range current = sortedRanges.get(0);
        for (int i = 1; i < sortedRanges.size(); i++) {
            Range next = sortedRanges.get(i);
            // Merge if overlapping or adjacent
            if (next.start <= current.end + 1) { 
                 current = new Range(current.start, Math.max(current.end, next.end));
            } else {
                mergedRanges.add(current);
                current = next;
            }
        }
        mergedRanges.add(current);

        return mergedRanges.stream()
            .mapToLong(r -> r.end - r.start + 1)
            .sum();
    }
}
