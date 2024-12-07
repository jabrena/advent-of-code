package info.jab.aoc.day2;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * https://adventofcode.com/2024/day/2
 */
public class Day2 implements Day<Integer> {

    public static boolean isDecreasing(List<Integer> numbers) {
        for (int i = 0; i < numbers.size() - 1; i++) {
            int difference = Math.abs(numbers.get(i) - numbers.get(i + 1));
            if(difference == 0) {
                return false;
            }
            if (difference > 3) {
                return false;
            }
            if ((numbers.get(i) <= numbers.get(i + 1))) {
                return false; // Not strictly decreasing
            }
        }
        return true;
    }

    public static boolean isIncreasing(List<Integer> numbers) {
        for (int i = 0; i < numbers.size() - 1; i++) {
            int difference = Math.abs(numbers.get(i) - numbers.get(i + 1));
            if(difference == 0) {
                return false;
            }
            if (difference > 3) {
                return false;
            }
            if ((numbers.get(i) >= numbers.get(i + 1))) {
                return false; // Not strictly increasing
            }
        }
        return true; // All elements are strictly increasing
    }

    Predicate<List<Integer>> isSafe = param -> {
        return isDecreasing(param) || isIncreasing(param);
    };

    Function<String, List<Integer>> toList = param -> Arrays.asList(param.split(" ")).stream().map(Integer::parseInt).toList();

    @Override
    public Integer getPart1Result(String fileName) {
        var list = ResourceLines.list(fileName);
        return (int) list.stream().map(toList).filter(isSafe).count();
    }

    Predicate<List<Integer>> isSafe2 = param -> {
        if (isSafe.test(param)) {
            return true;
        }

        //Tricky part
        for (int skip = 0; skip < param.size(); skip++) {
            List<Integer> clean = new ArrayList<>(param);
            clean.remove(skip);
            if (isSafe.test(clean)) {
                return true;
            }
        }
        return false;
    };

    @Override
    public Integer getPart2Result(String fileName) {
        var list = ResourceLines.list(fileName);
        return (int) list.stream().map(toList).filter(isSafe2).count();
    }
}
