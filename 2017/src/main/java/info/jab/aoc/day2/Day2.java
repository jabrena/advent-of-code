package info.jab.aoc.day2;

import info.jab.aoc.Solver;
import info.jab.aoc.Utils;
import java.util.Arrays;
import java.util.List;

public class Day2 implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        List<String> lines = Utils.readFileToList(fileName);
        return lines.stream()
            .mapToInt(line -> {
                int[] numbers = Arrays.stream(line.split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
                int max = Arrays.stream(numbers).max().orElse(0);
                int min = Arrays.stream(numbers).min().orElse(0);
                return max - min;
            })
            .sum();
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
