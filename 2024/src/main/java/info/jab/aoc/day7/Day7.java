package info.jab.aoc.day7;

import info.jab.aoc.Day;

import java.util.Arrays;

import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2024/day/1
 *
 */
public class Day7 implements Day<Long> {

    private sealed interface Operator permits Add, Multiply, Concatenate {
        long apply(long a, long b);
    }

    private record Add() implements Operator {
        @Override
        public long apply(long a, long b) {
            return a + b;
        }
    }

    private record Multiply() implements Operator {
        @Override
        public long apply(long a, long b) {
            return a * b;
        }
    }

    private record Concatenate() implements Operator {
        @Override
        public long apply(long a, long b) {
            return Long.parseLong(a + String.valueOf(b));
        }
    }

    private boolean recursive(long[] numbers, long target, int pos, long current, boolean part2) {
        // Base case: check if we are at the last position
        if (pos == numbers.length - 1) {
            return current == target;
        }

        long nextNumber = numbers[pos + 1];

        // Try each operator
        Operator[] operators = part2
            ? new Operator[]{new Add(), new Multiply(), new Concatenate()}
            : new Operator[]{new Add(), new Multiply()};

        for (Operator op : operators) {
            if (recursive(numbers, target, pos + 1, op.apply(current, nextNumber), part2)) {
                return true;
            }
        }
        return false;
    }

    private Long calculateResult(String fileName, boolean allowConcat) {
        return ResourceLines.list(fileName).stream()
            .filter(line -> {
                String[] parts = line.split(":");
                long target = Long.parseLong(parts[0].trim());
                long[] numbers = Arrays.stream(parts[1].trim().split(" ")).mapToLong(Long::parseLong).toArray();
                return recursive(numbers, target, 0, numbers[0], allowConcat);
            })
            .mapToLong(line -> Long.parseLong(line.split(":")[0].trim()))
            .sum();
    }

    @Override
    public Long getPart1Result(String fileName) {
        return calculateResult(fileName, false);
    }

    @Override
    public Long getPart2Result(String fileName) {
        return calculateResult(fileName, true);
    }
}
