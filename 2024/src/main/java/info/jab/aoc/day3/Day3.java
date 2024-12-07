package info.jab.aoc.day3;

import info.jab.aoc.Day;

import com.putoet.resources.ResourceLines;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

/**
 * https://adventofcode.com/2024/day/3
 **/
public class Day3 implements Day<Integer> {

    // Define the regex as a constant
    private static final String REGEX = "mul\\((\\d+),(\\d+)\\)";
    private static final String REGEX2 = "(mul\\((\\d+),(\\d+)\\)|don't\\(\\)|do\\(\\))";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    private static final Pattern PATTERN2 = Pattern.compile(REGEX2);

    @Override
    public Integer getPart1Result(String fileName) {
        var input = ResourceLines.line(fileName);

        Matcher matcher = PATTERN.matcher(input);

        List<Integer> sum = new ArrayList<>();
        while (matcher.find()) {
            var param1 = Integer.parseInt(matcher.group(1));
            var param2 = Integer.parseInt(matcher.group(2));
            sum.add(param1 * param2);
        }

        return sum.stream().reduce(0, (a, b) -> a + b);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        var input = ResourceLines.line(fileName);

        Matcher matcher = PATTERN2.matcher(input);

        boolean enabled = true;
        List<Integer> sum = new ArrayList<>();
        while (matcher.find()) {
            String command = matcher.group(0);

            if(command.equals("don't()")) {
                enabled = false;
            }
            if(command.equals("do()")) {
                enabled = true;
            }

            if (matcher.group(2) != null && matcher.group(3) != null) {
                var param1 = Integer.parseInt(matcher.group(2));
                var param2 = Integer.parseInt(matcher.group(3));
                if(enabled) {
                    sum.add(param1 * param2);
                }
            }
        }
        return sum.stream().reduce(0, (a, b) -> a + b);
    }
}
