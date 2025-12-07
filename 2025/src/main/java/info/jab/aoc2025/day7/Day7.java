package info.jab.aoc2025.day7;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class Day7 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        return solvePart1(lines);
    }

    private Long[][] memo;

    @Override
    public Long getPart2Result(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        return solvePart2(lines);
    }

    private Long solvePart2(List<String> lines) {
        int height = lines.size();
        int width = lines.stream().mapToInt(String::length).max().orElse(0);
        
        memo = new Long[height][width];
        
        int startX = -1;
        int startY = -1;
        for (int y = 0; y < height; y++) {
            String line = lines.get(y);
            int index = line.indexOf('S');
            if (index != -1) {
                startX = index;
                startY = y;
                break;
            }
        }
        
        if (startX == -1) {
            throw new IllegalArgumentException("No start position S found");
        }

        return countPaths(lines, startX, startY + 1, width, height);
    }

    private long countPaths(List<String> lines, int x, int y, int width, int height) {
        if (y >= height) {
            return 1;
        }
        if (x < 0 || x >= width) {
            return 1;
        }
        
        String line = lines.get(y);
        if (x >= line.length()) {
            return 1;
        }

        if (memo[y][x] != null) {
            return memo[y][x];
        }
        
        char c = line.charAt(x);
        long result;
        if (c == '^') {
            result = countPaths(lines, x - 1, y + 1, width, height) + 
                     countPaths(lines, x + 1, y + 1, width, height);
        } else {
            result = countPaths(lines, x, y + 1, width, height);
        }
        
        memo[y][x] = result;
        return result;
    }

    private Long solvePart1(List<String> lines) {
        int height = lines.size();
        int width = lines.get(0).length();
        
        // Find S
        int startX = -1;
        int startY = -1;
        for (int y = 0; y < height; y++) {
            String line = lines.get(y);
            int index = line.indexOf('S');
            if (index != -1) {
                startX = index;
                startY = y;
                break;
            }
        }
        
        if (startX == -1) {
            throw new IllegalArgumentException("No start position S found");
        }
        
        Set<Integer> currentBeams = new HashSet<>();
        currentBeams.add(startX);
        
        long totalSplits = 0;
        
        for (int y = startY + 1; y < height; y++) {
            if (currentBeams.isEmpty()) {
                break;
            }
            
            Set<Integer> nextBeams = new HashSet<>();
            String line = lines.get(y);
            
            for (int x : currentBeams) {
                // Check bounds before accessing character
                if (x >= 0 && x < line.length()) {
                    char c = line.charAt(x);
                    if (c == '^') {
                        totalSplits++;
                        nextBeams.add(x - 1);
                        nextBeams.add(x + 1);
                    } else {
                        nextBeams.add(x);
                    }
                }
            }
            currentBeams = nextBeams;
        }
        
        return totalSplits;
    }
}
