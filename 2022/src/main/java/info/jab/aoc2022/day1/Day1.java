package info.jab.aoc2022.day1;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Solution for AOC 2022, Day 1
 * https://adventofcode.com/2022/day/1
 *
 */
public class Day1 implements Day<Long> {

    private static final Pattern GROUP_SEPARATOR_PATTERN = Pattern.compile("\n\n");
    private static final Pattern LINE_SEPARATOR_PATTERN = Pattern.compile("\n");

    private Long processData(String fileContent, Integer limit) {
        return Arrays
            .stream(GROUP_SEPARATOR_PATTERN.split(fileContent))
            .map(group -> Arrays.stream(LINE_SEPARATOR_PATTERN.split(group)))
            .flatMapToLong(item -> LongStream.of(item.mapToLong(Long::parseLong).sum()))
            .boxed()
            .sorted(Comparator.reverseOrder())
            .limit(limit)
            .reduce(0L, Long::sum);
    }

    @Override
    public Long getPart1Result(String fileName) {
        var fileLoaded = ResourceLines.stream("/" + fileName)
                .collect(Collectors.joining("\n"));
        return this.processData(fileLoaded, 1);
    }

    @Override
    public Long getPart2Result(String fileName) {
        var fileLoaded = ResourceLines.stream("/" + fileName)
                .collect(Collectors.joining("\n"));
        return this.processData(fileLoaded, 3);
    }
}
