package info.jab.aoc2023.day1;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for AOC 2023, Day 1
 * https://adventofcode.com/2023/day/1
 *
 */
public class Day1 implements Day<Long> {

    // @formatter:off

    Function<String, List<Character>> toCharList = param -> {
        return param.chars()
            .mapToObj(c -> (char) c)
            .toList();
    };

    Function<List<Character>, List<Long>> toListOfDigits = param -> {
        return param.stream()
            .filter(c -> Character.isDigit(c))
            .map(String::valueOf)
            .map(Long::valueOf)
            .toList();
    };

    Function<List<Long>, Long> applyRules = param -> {
        String result = switch (param.size()) {
            case 1 -> String.valueOf(param.getFirst()) + String.valueOf(param.getFirst());
            case 2 -> String.valueOf(param.getFirst()) + String.valueOf(param.getLast());
            default -> String.valueOf(param.getFirst()) + String.valueOf(param.getLast());
        };
        return Long.valueOf(result);
    };

    @Override
    public Long getPart1Result(String fileName) {
        return ResourceLines.list("/" + fileName).stream()
            .map(toCharList)
            .map(toListOfDigits)
            .map(applyRules)
            .reduce(0L, Long::sum);
    }

    private static final Map<String, String> map = Map.of(
        "one", "1",
        "two", "2",
        "three", "3",
        "four", "4",
        "five", "5",
        "six", "6",
        "seven", "7",
        "eight", "8",
        "nine", "9"
    );

    // @formatter:on

    private static final Pattern DIGITS_REGEX_PATTERN = Pattern.compile(
        "one|two|three|four|five|six|seven|eight|nine|\\d"
    );

    Function<String, Long> extractNumber = line -> {
        Matcher matcher = DIGITS_REGEX_PATTERN.matcher(line);

        String first = "";
        String last = "";

        while (matcher.find()) {
            if (first.isEmpty()) {
                first = matcher.group();
            }
            last = matcher.group();
            matcher.region(matcher.start() + 1, line.length());
        }
        String number = map.getOrDefault(first, first) + map.getOrDefault(last, last);
        return Long.valueOf(number);
    };

    // @formatter:off

    @Override
    public Long getPart2Result(String fileName) {
        return ResourceLines.list("/" + fileName).stream()
            .map(extractNumber)
            .reduce(0L, Long::sum);
    }
    // @formatter:on
}
