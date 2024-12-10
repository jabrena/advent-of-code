package info.jab.aoc.day7;

import info.jab.aoc.Day;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2016/day/1
 *
 * Alternatives in Design:
 * https://github.com/bercee/AoC2024/blob/master/src/main/java/com/chemaxon/solvers/year2024/Day7.java
 **/
public class Day7 implements Day<Long> {

    AtomicLong counter = new AtomicLong();

    boolean recursive(boolean debug, long[] numbers, long target, int pos, long cur, boolean part2, String operator) {
        if (debug) {
            System.out.println("Running method recursively: " + counter.incrementAndGet() + " " + Arrays.toString(numbers) + " " + pos + " " + cur + " " + target + " " + operator);
        }

        if (pos == numbers.length - 1) {
            return cur == target;
        }

        if (recursive(debug, numbers, target, pos + 1, cur + numbers[pos + 1], part2, "+")) {
            return true;
        }

        if (recursive(debug, numbers, target, pos + 1, cur * numbers[pos + 1], part2, "*")) {
            return true;
        }

        if (part2) {
            String concat = String.valueOf(cur) + String.valueOf(numbers[pos + 1]);
            if (recursive(debug, numbers, target, pos + 1, Long.parseLong(concat), part2, "concat")) {
                return true;
            }
        }

        return false;
    }

    private Long getResult(String fileName, boolean isPart2) {
        var list = ResourceLines.list(fileName);
        return list.stream()
            .filter(str -> {
                var parts = str.split(":");
                var result = Long.parseLong(parts[0]);
                var operators = parts[1].trim().split(" ");
                long[] numbers = Arrays.stream(operators).mapToLong(Long::parseLong).toArray();
                return recursive(false, numbers, result, 0, numbers[0], isPart2, "");
            })
            .mapToLong(str -> Long.parseLong(str.split(":")[0]))
            .sum();
    }

    @Override
    public Long getPart1Result(String fileName) {
        return getResult(fileName, false);
    }

    @Override
    public Long getPart2Result(String fileName) {
        return getResult(fileName, true);
    }
}
