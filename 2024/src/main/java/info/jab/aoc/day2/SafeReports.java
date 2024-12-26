package info.jab.aoc.day2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class SafeReports implements Solver<Integer> {

    private boolean isMonotonic(List<Integer> numbers, BiPredicate<Integer, Integer> comparator) {
        return IntStream.range(0, numbers.size() - 1)
            .allMatch(i -> {
                int difference = Math.abs(numbers.get(i) - numbers.get(i + 1));
                return difference != 0 && difference <= 3 && comparator.test(numbers.get(i), numbers.get(i + 1));
            });
    }

    private boolean isMonotonic2(List<Integer> numbers, BiPredicate<Integer, Integer> comparator) {
        for (int i = 0; i < numbers.size() - 1; i++) {
            int difference = Math.abs(numbers.get(i) - numbers.get(i + 1));

            // Check if the difference is valid (not 0 and less than or equal to 3)
            if (difference == 0 || difference > 3) {
                return false; // If the difference is invalid, return false immediately
            }

            // Check if the comparator condition holds for adjacent numbers
            if (!comparator.test(numbers.get(i), numbers.get(i + 1))) {
                return false; // If comparator fails, return false immediately
            }
        }
        return true; // If no issues found, the sequence is monotonic
    }

    private boolean isDecreasing(List<Integer> numbers) {
        return isMonotonic2(numbers, (a, b) -> a > b);
    }

    private boolean isIncreasing(List<Integer> numbers) {
        return isMonotonic2(numbers, (a, b) -> a < b);
    }

    Predicate<List<Integer>> isSafePart1 = param -> {
        return isDecreasing(param) || isIncreasing(param);
    };

    Function<String, List<Integer>> toList = param ->
        Arrays.asList(param.split(" ")).stream().map(Integer::parseInt).toList();

    Predicate<List<Integer>> isSafePart2 = param -> {
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
    public Integer solvePartOne(String fileName) {
        var list = ResourceLines.list(fileName);
        return (int) list.stream().map(toList).filter(isSafePart1).count();
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var list = ResourceLines.list(fileName);
        return (int) list.stream().map(toList).filter(isSafePart2).count();
    }
}
