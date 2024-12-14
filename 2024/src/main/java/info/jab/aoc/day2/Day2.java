package info.jab.aoc.day2;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * https://adventofcode.com/2024/day/2
 */
public class Day2 implements Day<Integer> {

    public boolean isMonotonic(List<Integer> numbers, BiPredicate<Integer, Integer> comparator) {
        return IntStream.range(0, numbers.size() - 1)
            .allMatch(i -> {
                int difference = Math.abs(numbers.get(i) - numbers.get(i + 1));
                return difference != 0 && difference <= 3 && comparator.test(numbers.get(i), numbers.get(i + 1));
            });
    }

    public boolean isDecreasing(List<Integer> numbers) {
        return isMonotonic(numbers, (a, b) -> a > b);
    }

    public boolean isIncreasing(List<Integer> numbers) {
        return isMonotonic(numbers, (a, b) -> a < b);
    }

    Predicate<List<Integer>> isSafePart1 = param -> {
        return isDecreasing(param) || isIncreasing(param);
    };

    Function<String, List<Integer>> toList = param -> Arrays.asList(param.split(" ")).stream().map(Integer::parseInt).toList();

    @Override
    public Integer getPart1Result(String fileName) {
        var list = ResourceLines.list(fileName);
        return (int) list.stream().map(toList).filter(isSafePart1).count();
    }

    Predicate<List<Integer>> isSafe2 = param -> {
        if (isSafePart1.test(param)) {
            return true;
        }

        return IntStream.range(0, param.size())
            .anyMatch(skip -> {
                List<Integer> clean = new ArrayList<>(param);
                clean.remove(skip);
                return isSafePart1.test(clean);
            });
    };

    @Override
    public Integer getPart2Result(String fileName) {
        var list = ResourceLines.list(fileName);
        return (int) list.stream().map(toList).filter(isSafe2).count();
    }
}
