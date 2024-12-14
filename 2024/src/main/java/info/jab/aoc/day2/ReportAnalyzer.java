package info.jab.aoc.day2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class ReportAnalyzer {

    static private boolean isMonotonic(List<Integer> numbers, BiPredicate<Integer, Integer> comparator) {
        return IntStream.range(0, numbers.size() - 1)
            .allMatch(i -> {
                int difference = Math.abs(numbers.get(i) - numbers.get(i + 1));
                return difference != 0 && difference <= 3 && comparator.test(numbers.get(i), numbers.get(i + 1));
            });
    }

    static private boolean isDecreasing(List<Integer> numbers) {
        return isMonotonic(numbers, (a, b) -> a > b);
    }

    static private boolean isIncreasing(List<Integer> numbers) {
        return isMonotonic(numbers, (a, b) -> a < b);
    }

    static Predicate<List<Integer>> isSafePart1 = param -> {
        return isDecreasing(param) || isIncreasing(param);
    };

    static Function<String, List<Integer>> toList = param -> Arrays.asList(param.split(" ")).stream().map(Integer::parseInt).toList();

    static Predicate<List<Integer>> isSafePart2 = param -> {
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
}
