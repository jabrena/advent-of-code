package info.jab.aoc2024.day3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class MullItOver implements Solver<Integer> {

    /**
     * Maximum input length to prevent ReDoS attacks.
     * This limit prevents polynomial runtime due to regex backtracking with alternation patterns.
     */
    private static final int MAX_INPUT_LENGTH = 1_000_000;
    
    /**
     * Maximum number of regex matches to prevent DoS attacks.
     * This limit prevents excessive iterations in regex find() loops.
     */
    private static final int MAX_MATCHES = 100_000;

    // Define the regex patterns as constants
    private static final String REGEX = "mul\\((\\d+),(\\d+)\\)";
    private static final String REGEX2 = "(mul\\((\\d+),(\\d+)\\)|don't\\(\\)|do\\(\\))";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    private static final Pattern PATTERN2 = Pattern.compile(REGEX2);

    // Helper method to calculate sum from multiplication patterns
    private Integer calculateSumFromMultiplications(String input, Pattern pattern) {
        if (input.length() > MAX_INPUT_LENGTH) {
            throw new IllegalArgumentException("Input exceeds maximum length of " + MAX_INPUT_LENGTH);
        }
        
        Matcher matcher = pattern.matcher(input);
        List<Integer> results = new ArrayList<>();
        int matchCount = 0;

        while (matcher.find()) {
            if (matchCount >= MAX_MATCHES) {
                throw new IllegalStateException("Exceeded maximum number of matches: " + MAX_MATCHES);
            }
            matchCount++;
            
            var param1 = Integer.parseInt(matcher.group(1));
            var param2 = Integer.parseInt(matcher.group(2));
            results.add(param1 * param2);
        }

        return results.stream().reduce(0, Integer::sum);
    }

    // Helper method for part 2 that handles conditional "do" and "don't" commands
    private Integer calculateSumWithConditional(String input, Pattern pattern) {
        if (input.length() > MAX_INPUT_LENGTH) {
            throw new IllegalArgumentException("Input exceeds maximum length of " + MAX_INPUT_LENGTH);
        }
        
        Matcher matcher = pattern.matcher(input);
        boolean enabled = true;
        List<Integer> results = new ArrayList<>();
        int matchCount = 0;

        while (matcher.find()) {
            if (matchCount >= MAX_MATCHES) {
                throw new IllegalStateException("Exceeded maximum number of matches: " + MAX_MATCHES);
            }
            matchCount++;
            
            String command = matcher.group(0);

            if ("don't()".equals(command)) {
                enabled = false;
            } else if ("do()".equals(command)) {
                enabled = true;
            }

            // Process the multiplication if it's enabled and the groups are non-null
            if (matcher.group(2) != null && matcher.group(3) != null) {
                var param1 = Integer.parseInt(matcher.group(2));
                var param2 = Integer.parseInt(matcher.group(3));
                if (enabled) {
                    results.add(param1 * param2);
                }
            }
        }
        return results.stream().reduce(0, Integer::sum);
    }

    @Override
    public Integer solvePartOne(String fileName) {
        var input = ResourceLines.line(fileName);
        return calculateSumFromMultiplications(input, PATTERN);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var input = ResourceLines.line(fileName);
        return calculateSumWithConditional(input, PATTERN2);
    }
}
