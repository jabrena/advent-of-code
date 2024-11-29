package jab.aoc.day3;

import static jab.aoc.Utils.getUniqueCharactersAsHashSet;

import jab.aoc.Day;
import jab.aoc.Utils;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Solution for AOC 2022, Day 3
 * https://adventofcode.com/2022/day/3
 *
 */
public class Day3 implements Day<Long> {

    private Function<String, Integer> getPriority = param -> {
        final String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return alphabet.indexOf(param) + 1;
    };

    @Override
    public Long getPart1Result(String fileName) {
        Function<String, List<String>> splitInTheMiddle = param -> {
            var mid = param.length() / 2;
            return List.of(param.substring(0, mid), param.substring(mid));
        };

        Function<List<String>, String> find = param -> {
            var group1 = param.get(0);
            var group2 = param.get(1);

            var set = new HashSet<>(getUniqueCharactersAsHashSet(group1));
            set.retainAll(getUniqueCharactersAsHashSet(group2));
            return set.stream().findFirst().get();
        };

        return Utils
            .readFileToList(fileName)
            .stream()
            .map(splitInTheMiddle::apply)
            .map(find::apply)
            .mapToLong(getPriority::apply)
            .reduce(0, Long::sum);
    }

    @Override
    public Long getPart2Result(String fileName) {
        Function<String, Collection<List<String>>> groupBy3 = param -> {
            final int chunkSize = 3;
            final AtomicInteger counter = new AtomicInteger();
            return Utils
                .readFileToList(param)
                .stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize))
                .values();
        };

        Function<List<String>, String> find = param -> {
            var group1 = param.get(0);
            var group2 = param.get(1);
            var group3 = param.get(2);

            var set = new HashSet<>(getUniqueCharactersAsHashSet(group1));
            set.retainAll(getUniqueCharactersAsHashSet(group2));
            set.retainAll(getUniqueCharactersAsHashSet(group3));
            return set.stream().findFirst().get();
        };

        // @formatter:off
        return groupBy3.apply(fileName).stream()
                .map(find::apply)
                .mapToLong(getPriority::apply)
                .reduce(0, Long::sum);
        // @formatter:on
    }
}
