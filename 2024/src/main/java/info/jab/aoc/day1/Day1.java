package info.jab.aoc.day1;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2024/day/1
 */
public class Day1 implements Day<Integer> {

    Function<String, List<String>> loadFle = fileName -> ResourceLines.list(fileName);

    private static final String SEPARATOR = "\\s+";
    private static final Pattern SEPARATOR_PATTERN = Pattern.compile(SEPARATOR);

    private record ListSplitted(List<Integer> listLeft, List<Integer> listRight) {}

    Function<List<String>, ListSplitted> splitInto2Lists = param -> {
        var list1 = param.stream().map(line -> Integer.parseInt(SEPARATOR_PATTERN.split(line)[0])).sorted().toList();
        var list2 = param.stream().map(line -> Integer.parseInt(SEPARATOR_PATTERN.split(line)[1])).sorted().toList();
        return new ListSplitted(list1, list2);
    };

    //@juan-medina idea
    Function<List<String>, ListSplitted> splitInto2Lists2 = param -> {
        return param.stream()
            .map(line -> SEPARATOR_PATTERN.split(line))
            .collect(Collectors.teeing(
                Collectors.mapping(parts -> Integer.parseInt(parts[0]), Collectors.toList()),
                Collectors.mapping(parts -> Integer.parseInt(parts[1]), Collectors.toList()),
                (l1, l2) -> new ListSplitted(l1.stream().sorted().toList(), l2.stream().sorted().toList())
            ));
    };

    Function<ListSplitted, Integer> calculateDistance = parameter -> {
        return IntStream.range(0, parameter.listLeft().size())
            .map(i -> {
                int param1 = parameter.listLeft().get(i);
                int param2 = parameter.listRight().get(i);
                return Math.abs(param1 - param2);
            })
            .sum();
    };

    @Override
    public Integer getPart1Result(String fileName) {
        return loadFle.andThen(splitInto2Lists).andThen(calculateDistance).apply(fileName);
    }

    BiFunction<Integer, List<Integer>, Integer> countOccurrences = (param1, param2) -> {
        return (int) param2.stream().filter(number -> number.equals(param1)).count();
    };

    Function<ListSplitted, Integer> calculateOcurrences = parameter -> {
        return IntStream.range(0, parameter.listLeft().size())
            .map(i -> {
                int param1 = parameter.listLeft().get(i);
                int param2 = countOccurrences.apply(param1, parameter.listRight());
                return param1 * param2;
            })
            .sum();
    };

    @Override
    public Integer getPart2Result(String fileName) {
        return loadFle.andThen(splitInto2Lists2).andThen(calculateOcurrences).apply(fileName);
    }
}
