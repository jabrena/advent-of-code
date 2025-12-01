package info.jab.aoc2024.day2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Gatherers;
import java.time.temporal.ValueRange;
import java.util.stream.Collectors;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class SafeReports2 implements Solver<Integer> {

    Function<String, List<Integer>> toList = param ->
        Arrays.asList(param.split(" ")).stream().map(Integer::parseInt).toList();

    IntPredicate isValidRange = diff -> diff > 0 && diff < 4;

    Predicate<List<Integer>> isValidDifference = nums ->
        nums.stream()
            .gather(Gatherers.windowSliding(2))
            .allMatch(l -> isValidRange.test(Math.abs(l.getFirst() - l.getLast())));

    Predicate<List<Integer>> isSortedOrReverseSorted = nums ->
        nums.equals(nums.stream().sorted().toList()) ||
        nums.equals(nums.stream().sorted(Collections.reverseOrder()).toList());

    Function<List<Integer>, List<List<Integer>>> generateSubLists = nums ->
        IntStream.range(0, nums.size())
            .mapToObj(i ->
                IntStream.range(0, nums.size())
                    .filter(r -> r != i)
                    .mapToObj(nums::get)
                    .toList())
            .toList();

    Predicate<List<List<Integer>>> validLists = list ->
        list.stream()
            .anyMatch(nums ->
                isValidDifference.test(nums) &&
                isSortedOrReverseSorted.test(nums));

    @Override
    public Integer solvePartOne(String fileName) {
        var list = ResourceLines.list(fileName);
        return (int) list.stream()
                .map(toList)
                .filter(isValidDifference)
                .filter(isSortedOrReverseSorted)
                .count();
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var input = ResourceLines.list(fileName);
        return (int) input.stream()
                .map(toList)
                .map(generateSubLists)
                .filter(validLists)
                .count();
    }
}
