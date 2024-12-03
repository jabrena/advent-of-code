package info.jab.aoc.day1;

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

    @Override
    public Integer getPart1Result(String fileName) {

        var input = ResourceLines.line(fileName);

        String regex = "mul\\((\\d+),(\\d+)\\)";

        // Compile the pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        List<Integer> sum = new ArrayList<>();
        while (matcher.find()) {
            System.out.println("Found: " + matcher.group(0));
            var param1 = matcher.group(1);
            var param2 = matcher.group(2);
            System.out.println("x: " + param1);
            System.out.println("y: " + param2);
            sum.add(Integer.parseInt(param1) * Integer.parseInt(param2));
        }

        return sum.stream().reduce(0, (a, b) -> a + b);
    }

    @Override
    public Integer getPart2Result(String fileName) {

        var input = ResourceLines.line(fileName);

        String regex = "(mul\\((\\d+),(\\d+)\\)|don't\\(\\)|do\\(\\))";

        // Compile the pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        boolean enabled = true;
        List<Integer> sum = new ArrayList<>();
        while (matcher.find()) {
            String command = matcher.group(0);
            System.out.println("Found: " + matcher.group(0));

            if(command.equals("don't()")) {
                enabled = false;
            }
            if(command.equals("do()")) {
                enabled = true;
            }

            // Print specific groups for mul(x,y)
            if (matcher.group(2) != null && matcher.group(3) != null) {
                var param1 = matcher.group(2);
                var param2 = matcher.group(3);
                System.out.println("x: " + param1);
                System.out.println("y: " + param2);
                if(enabled) {
                    sum.add(Integer.parseInt(param1) * Integer.parseInt(param2));
                }
            }
        }
        return sum.stream().reduce(0, (a, b) -> a + b);
    }
}
