package info.jab.aoc2016.day20;

import info.jab.aoc.Day;

import com.putoet.resources.ResourceLines;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * https://adventofcode.com/2016/day/20
 */
public class Day20 implements Day<Long> {

    private record Range(long start, long end) {
        boolean overlaps(Range other) {
            return start <= other.end + 1 && other.start <= end + 1;
        }

        Range merge(Range other) {
            return new Range(Math.min(start, other.start), Math.max(end, other.end));
        }
    }

    @Override
    public Long getPart1Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        
        // Parse ranges
        List<Range> ranges = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split("-");
            long start = Long.parseLong(parts[0]);
            long end = Long.parseLong(parts[1]);
            ranges.add(new Range(start, end));
        }
        
        // Sort by start
        ranges.sort(Comparator.comparingLong(Range::start));
        
        // Merge overlapping ranges
        List<Range> merged = new ArrayList<>();
        Range current = ranges.get(0);
        
        for (int i = 1; i < ranges.size(); i++) {
            Range next = ranges.get(i);
            if (current.overlaps(next)) {
                current = current.merge(next);
            } else {
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);
        
        // Find the lowest IP not blocked
        // If the first range doesn't start at 0, then 0 is the answer
        if (merged.get(0).start > 0) {
            return 0L;
        }
        
        // Otherwise, return the first IP after the first range
        return merged.get(0).end + 1;
    }

    @Override
    public Long getPart2Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        
        // Parse ranges
        List<Range> ranges = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split("-");
            long start = Long.parseLong(parts[0]);
            long end = Long.parseLong(parts[1]);
            ranges.add(new Range(start, end));
        }
        
        // Sort by start
        ranges.sort(Comparator.comparingLong(Range::start));
        
        // Merge overlapping ranges
        List<Range> merged = new ArrayList<>();
        Range current = ranges.get(0);
        
        for (int i = 1; i < ranges.size(); i++) {
            Range next = ranges.get(i);
            if (current.overlaps(next)) {
                current = current.merge(next);
            } else {
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);
        
        // Calculate total blocked IPs
        long blocked = merged.stream()
            .mapToLong(range -> range.end - range.start + 1)
            .sum();
        
        // Total IPs from 0 to 4294967295 (inclusive) = 4294967296
        long totalIPs = 4294967296L;
        
        // Allowed IPs = Total - Blocked
        return totalIPs - blocked;
    }
}
