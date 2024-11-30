package info.jab.aoc.day1;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.concurrent.atomic.AtomicLong;

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
        throw new UnsupportedOperationException("Not implemented");
    }

}
