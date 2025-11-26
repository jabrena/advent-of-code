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
        List<String> lines = Utils.readFileToList(fileName);
        return lines.stream()
            .mapToInt(line -> {
                int[] numbers = Arrays.stream(line.split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
                
                // Find two numbers where one evenly divides the other
                for (int i = 0; i < numbers.length; i++) {
                    for (int j = i + 1; j < numbers.length; j++) {
                        int a = numbers[i];
                        int b = numbers[j];
                        if (a % b == 0) {
                            return a / b;
                        }
                        if (b % a == 0) {
                            return b / a;
                        }
                    }
                }
                return 0;
            })
            .sum();
    }
}
