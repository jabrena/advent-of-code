package jab.aoc.day1;

import static jab.aoc.Utils.GROUP_SEPARATOR_PATTERN;
import static jab.aoc.Utils.LINE_SEPARATOR_PATTERN;

import jab.aoc.Day;
import jab.aoc.Utils;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.LongStream;

/**
 * Solution for AOC 2022, Day 1
 * https://adventofcode.com/2022/day/1
 *
 */
public class Day1 implements Day<Long> {

    private Long processData(String fileName, Integer limit) {
        return Arrays
            .stream(GROUP_SEPARATOR_PATTERN.split(fileName))
            .map(group -> Arrays.stream(LINE_SEPARATOR_PATTERN.split(group)))
            .flatMapToLong(item -> LongStream.of(item.mapToLong(Long::parseLong).sum()))
            .boxed()
            .sorted(Comparator.reverseOrder())
            .limit(limit)
            .reduce(0L, Long::sum);
    }

    @Override
    public Long getPart1Result(String fileName) {
        var fileLoaded = Utils.readFileToString(fileName);
        return this.processData(fileLoaded, 1);
    }

    @Override
    public Long getPart2Result(String fileName) {
        var fileLoaded = Utils.readFileToString(fileName);
        return this.processData(fileLoaded, 3);
    }
}
