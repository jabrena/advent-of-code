package info.jab.aoc2023.day4;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Solution for AOC 2023, Day 4
 * https://adventofcode.com/2023/day/4
 *
 */
class Day4 implements Day<Long> {

    // @formatter:off

    Function<String, Integer> getMatches = param -> {
        String[] numbers = param.split(":")[1].split("\\|");

        List<String> winningNumbers = Arrays.stream(numbers[0].split(" "))
            .filter(c -> !c.isBlank() && !c.equals(" "))
            .toList();

        int matches = Arrays.stream(numbers[1].split(" "))
            .filter(winningNumbers::contains)
            .mapToInt(s -> 1)
            .sum();

        return matches;
    };

    @Override
    public Long getPart1Result(String fileName) {
        var result = ResourceLines.list("/" + fileName).stream()
            .map(getMatches)
            .map(c -> ((1 << c) / 2))
            .reduce(0, (a, b) -> a + b);
        return Long.valueOf(result);
    }

    // @formatter:on

    //Recursive approach
    private long iterate(Map<Integer, Integer> cache, List<String> lines, int start, int end) {
        long counter = 0L;
        for (int i = start; i < end; i++) {
            int match = cache.computeIfAbsent(i, k -> getMatches.apply(lines.get(k)));
            if (match > 0) {
                counter += iterate(cache, lines, i + 1, i + 1 + match);
            }
            counter++;
        }
        return counter;
    }

    @Override
    public Long getPart2Result(String fileName) {
        var lines = ResourceLines.list("/" + fileName);
        return iterate(new HashMap<>(), lines, 0, lines.size());
    }
}
