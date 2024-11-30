package info.jab.aoc.day1;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.concurrent.atomic.AtomicLong;
import java.util.ArrayList;
import java.util.List;

/**
 * https://adventofcode.com/2021/day/1
 **/
public class Day1 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {

        AtomicLong previous = new AtomicLong(0);
        var sonarMeasures = ResourceLines.list(fileName, Long::parseLong);
        var counter = sonarMeasures.stream()
            .mapToLong(element -> {
                if(previous.get() == 0) {
                    previous.set(element);
                    return 0L;
                } else if (element > previous.get()) {
                    previous.set(element);
                    return 1L;
                } else {
                    previous.set(element);
                    return 0L;
                }
            })
            .sum();
        return counter;
    }

    @Override
    public Long getPart2Result(String fileName) {
        AtomicLong previous = new AtomicLong(0);
        var sonarMeasures = ResourceLines.list(fileName, Long::parseLong);
        var sonarMeasures2 = calculateThreeMeasurementSlicingWindow(sonarMeasures);
        var counter = sonarMeasures2.stream()
            .mapToLong(element -> {
                if(previous.get() == 0) {
                    previous.set(element);
                    return 0L;
                } else if (element > previous.get()) {
                    previous.set(element);
                    return 1L;
                } else {
                    previous.set(element);
                    return 0L;
                }
            })
            .sum();
        return counter;
    }

    //three-measurement sliding window
    private List<Long> calculateThreeMeasurementSlicingWindow (List<Long> param) {

        // Sliding window size
        int windowSize = 3;
        List<Long> result = new ArrayList<>();

        // Iterate through the list for sliding window sums
        for (int i = 0; i <= param.size() - windowSize; i++) {
            // Calculate the sum of the current window
            Long sum = param.get(i) + param.get(i + 1) + param.get(i + 2);
            System.out.println("Window " + (i + 1) + " sum: " + sum);
            result.add(sum);
        }

        return result;
    }

}
