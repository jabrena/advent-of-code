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

    @Override
    public Long getPart1Result(String fileName) {
        var list = ResourceLines.list(fileName);
        return list.stream()
            .filter(str -> {
                var parts = str.split(":");
                var result = Long.parseLong(parts[0]);
                var operators = parts[1].trim().split(" ");
                long[] numbers = Arrays.stream(operators).mapToLong(Long::parseLong).toArray();
                var flag = tryOps(false, numbers, result, 0, numbers[0], false, "");

                //Learn from positive cases
                if(flag) {
                    tryOps(true, numbers, result, 0, numbers[0], false, "");
                }
                return flag;
            })
            .mapToLong(str -> Long.parseLong(str.split(":")[0]))
            .sum();
    }

    AtomicLong counter = new AtomicLong();

    boolean tryOps(boolean debug, long[] numbers, long target, int pos, long cur, boolean part2, String operator) {
        if (debug) {
            System.out.println("Running method recursively: " + counter.incrementAndGet() + " " + Arrays.toString(numbers) + " " + pos + " " + cur + " " + target + " " + operator);
        }

        if (pos == numbers.length - 1) {
            return cur == target;
        }

        if (tryOps(debug, numbers, target, pos + 1, cur + numbers[pos + 1], part2, "+")) {
            return true;
        }

        if (tryOps(debug, numbers, target, pos + 1, cur * numbers[pos + 1], part2, "*")) {
            return true;
        }

        if (part2) {
            String concat = String.valueOf(cur) + String.valueOf(numbers[pos + 1]);
            if (tryOps(debug, numbers, target, pos + 1, Long.parseLong(concat), part2, "concat")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Long getPart2Result(String fileName) {
        var list = ResourceLines.list(fileName);
        return list.stream()
            .filter(str -> {
                var parts = str.split(":");
                var result = Long.parseLong(parts[0]);
                var operators = parts[1].trim().split(" ");
                long[] numbers = Arrays.stream(operators).mapToLong(Long::parseLong).toArray();
                return tryOps(false, numbers, result, 0, numbers[0], true, "");
            })
            .mapToLong(str -> Long.parseLong(str.split(":")[0]))
            .sum();
    }
}
