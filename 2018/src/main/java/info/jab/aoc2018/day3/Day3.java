package info.jab.aoc2018.day3;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 implements Solver<Integer> {

    private static final Pattern CLAIM_PATTERN = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");

    @Override
    public Integer solvePartOne(String fileName) {
        List<String> claims = ResourceLines.list(fileName);
        Map<String, Integer> fabric = new HashMap<>();

        for (String claim : claims) {
            Matcher matcher = CLAIM_PATTERN.matcher(claim);
            if (matcher.matches()) {
                int x = Integer.parseInt(matcher.group(2));
                int y = Integer.parseInt(matcher.group(3));
                int width = Integer.parseInt(matcher.group(4));
                int height = Integer.parseInt(matcher.group(5));

                for (int i = x; i < x + width; i++) {
                    for (int j = y; j < y + height; j++) {
                        String key = i + "," + j;
                        fabric.put(key, fabric.getOrDefault(key, 0) + 1);
                    }
                }
            }
        }

        return (int) fabric.values().stream()
                .filter(count -> count >= 2)
                .count();
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        List<String> claims = ResourceLines.list(fileName);
        Map<String, Integer> fabric = new HashMap<>();
        Map<Integer, java.util.Set<String>> claimSquares = new HashMap<>();

        for (String claim : claims) {
            Matcher matcher = CLAIM_PATTERN.matcher(claim);
            if (matcher.matches()) {
                int claimId = Integer.parseInt(matcher.group(1));
                int x = Integer.parseInt(matcher.group(2));
                int y = Integer.parseInt(matcher.group(3));
                int width = Integer.parseInt(matcher.group(4));
                int height = Integer.parseInt(matcher.group(5));

                java.util.Set<String> squares = new java.util.HashSet<>();
                for (int i = x; i < x + width; i++) {
                    for (int j = y; j < y + height; j++) {
                        String key = i + "," + j;
                        fabric.put(key, fabric.getOrDefault(key, 0) + 1);
                        squares.add(key);
                    }
                }
                claimSquares.put(claimId, squares);
            }
        }

        for (Map.Entry<Integer, java.util.Set<String>> entry : claimSquares.entrySet()) {
            int claimId = entry.getKey();
            java.util.Set<String> squares = entry.getValue();
            boolean hasOverlap = squares.stream()
                    .anyMatch(key -> fabric.get(key) > 1);
            if (!hasOverlap) {
                return claimId;
            }
        }

        return null;
    }
}
