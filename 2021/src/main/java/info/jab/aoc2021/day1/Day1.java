package info.jab.aoc2021.day1;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.List;

/**
 * https://adventofcode.com/2021/day/1
 **/
public class Day1 implements Day<Integer> {

    Function<List<Integer>, Integer> getIncrementCount = param -> {
        AtomicInteger previous = new AtomicInteger(0);
        return param.stream().mapToInt(element -> {
            if(previous.get() == 0) {
                previous.set(element);
                return 0;
            } else if (element > previous.get()) {
                previous.set(element);
                return 1;
            } else {
                previous.set(element);
                return 0;
            }
        })
        .sum();
    };

    //TODO Refactor to add into Utilities
    //three-measurement sliding window
    private List<Integer> calculateThreeMeasurementSlicingWindow (List<Integer> param) {

        // Sliding window size
        int windowSize = 3;
        List<Integer> result = new ArrayList<>();

        // Iterate through the list for sliding window sums
        for (int i = 0; i <= param.size() - windowSize; i++) {
            // Calculate the sum of the current window
            Integer sum = param.get(i) + param.get(i + 1) + param.get(i + 2);
            result.add(sum);
        }

        return result;
    }

    @Override
    public Integer getPart1Result(String fileName) {
        var sonarMeasures = ResourceLines.list(fileName, Integer::parseInt);
        return getIncrementCount.apply(sonarMeasures);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        var sonarMeasures = ResourceLines.list(fileName, Integer::parseInt);
        var sonarMeasures2 = calculateThreeMeasurementSlicingWindow(sonarMeasures);
        return getIncrementCount.apply(sonarMeasures2);
    }
}
