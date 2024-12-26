package info.jab.aoc.day1;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

class HistorianHysteria implements Solver<Integer> {

    private static final String SEPARATOR = "\\s+";
    private static final Pattern SEPARATOR_PATTERN = Pattern.compile(SEPARATOR);

    Function<String, List<String>> loadFle = fileName -> ResourceLines.list(fileName);

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

    Function<List<String>, ListSplitted> splitInto2Lists3 = lines -> {
        var result = lines.stream()
            .collect(
                // Supplier: Initialize a ListSplitted with two empty lists
                () -> new ListSplitted(new ArrayList<Integer>(), new ArrayList<Integer>()),
                // Accumulator: Split lines and add to respective lists
                (ListSplitted accumulator, String line) -> {
                    String[] parts = SEPARATOR_PATTERN.split(line);
                    accumulator.listLeft().add(Integer.parseInt(parts[0]));
                    accumulator.listRight().add(Integer.parseInt(parts[1]));
                },
                // Combiner: Merge lists
                (ListSplitted acc1, ListSplitted acc2) -> {
                    acc1.listLeft().addAll(acc2.listLeft());
                    acc1.listRight().addAll(acc2.listRight());
                }
            );
        // Convert lists to sorted lists
        return new ListSplitted(
            result.listLeft().stream().sorted().toList(),
            result.listRight().stream().sorted().toList()
        );
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
    public Integer solvePartOne(String fileName) {
        return loadFle.andThen(splitInto2Lists3).andThen(calculateDistance).apply(fileName);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        return loadFle.andThen(splitInto2Lists3).andThen(calculateOcurrences).apply(fileName);
    }
}
