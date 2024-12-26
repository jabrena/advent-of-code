package info.jab.aoc.day3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

import com.putoet.resources.ResourceLines;

public class MullItOver {

    // Define the regex patterns as constants
    private static final String REGEX = "mul\\((\\d+),(\\d+)\\)";
    private static final String REGEX2 = "(mul\\((\\d+),(\\d+)\\)|don't\\(\\)|do\\(\\))";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    private static final Pattern PATTERN2 = Pattern.compile(REGEX2);

    // Helper method to calculate sum from multiplication patterns
    private Integer calculateSumFromMultiplications(String input, Pattern pattern) {
        Matcher matcher = pattern.matcher(input);
        List<Integer> results = new ArrayList<>();

        while (matcher.find()) {
            var param1 = Integer.parseInt(matcher.group(1));
            var param2 = Integer.parseInt(matcher.group(2));
            results.add(param1 * param2);
        }

        return results.stream().reduce(0, Integer::sum);
    }

    // Helper method for part 2 that handles conditional "do" and "don't" commands
    private Integer calculateSumWithConditional(String input, Pattern pattern) {
        Matcher matcher = pattern.matcher(input);
        boolean enabled = true;
        List<Integer> results = new ArrayList<>();

        while (matcher.find()) {
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

    public Integer solvePartOne(String fileName) {
        var input = ResourceLines.line(fileName);
        return calculateSumFromMultiplications(input, PATTERN);
    }

    public Integer solvePartTwo(String fileName) {
        var input = ResourceLines.line(fileName);
        return calculateSumWithConditional(input, PATTERN2);
    }
}
