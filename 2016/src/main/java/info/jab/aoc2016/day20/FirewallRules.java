package info.jab.aoc2016.day20;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Solver for Day 20: Firewall Rules
 * Finds allowed IP addresses by merging and analyzing blocked ranges.
 */
public final class FirewallRules implements Solver<Long> {

    @Override
    public Long solvePartOne(final String fileName) {
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
        if (merged.get(0).start() > 0) {
            return 0L;
        }
        
        // Otherwise, return the first IP after the first range
        return merged.get(0).end() + 1;
    }

    @Override
    public Long solvePartTwo(final String fileName) {
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
            .mapToLong(range -> range.end() - range.start() + 1)
            .sum();
        
        // Total IPs from 0 to 4294967295 (inclusive) = 4294967296
        long totalIPs = 4294967296L;
        
        // Allowed IPs = Total - Blocked
        return totalIPs - blocked;
    }
}

