package jab.aoc.day4;

import jab.aoc.Day;
import jab.aoc.Utils;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Solution for AOC 2022, Day 4
 * https://adventofcode.com/2022/day/4
 *
 */
public class Day4 implements Day<Long> {

    // @formatter:off
    private Function<String, List<Integer>> toListOfIntegers = arr -> {
        String[] parts1 = arr.split("-");
        return IntStream
                .rangeClosed(Integer.parseInt(parts1[0]), Integer.parseInt(parts1[1]))
                .boxed()
                .toList();
    };

    // @formatter:on

    private record Tuple(List<Integer> part1, List<Integer> part2) {}

    private Function<String, Tuple> toTuple = param -> {
        String[] arr = param.split(",");
        return new Tuple(toListOfIntegers.apply(arr[0]), toListOfIntegers.apply(arr[1]));
    };

    @Override
    public Long getPart1Result(String fileName) {
        // @formatter:off
        Function<Tuple, Boolean> detectSubsets = tuple -> {
            if (tuple.part1().containsAll(tuple.part2())
                || tuple.part2().containsAll(tuple.part1())) {
                return true;
            }
            return false;
        };

        return Utils.readFileToList(fileName)
                .stream()
                .map(toTuple)
                .map(detectSubsets)
                .filter(value -> value)
                .count();
        // @formatter:on
    }

    @Override
    public Long getPart2Result(String fileName) {
        /**
         * Detect if 2 Arrays Overlaps
         */
        Function<Tuple, Boolean> detectOverlap = tuple -> {
            HashSet<Integer> set2 = new HashSet<>();
            set2.addAll(tuple.part1());
            set2.retainAll(tuple.part2());

            if (!set2.isEmpty()) {
                return true;
            }
            return false;
        };

        // @formatter:off
        return Utils
                .readFileToList(fileName)
                .stream()
                .map(toTuple)
                .map(detectOverlap)
                .filter(value -> value)
                .count();
        // @formatter:on
    }
}
