package info.jab.aoc2025.day2;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

public class Day2 implements Day<Long> {

    private final Solver<Long> validator = new InvalidIdValidator2();

    @Override
    public Long getPart1Result(final String fileName) {
        return validator.solvePartOne(fileName);
    }

    @Override
    public Long getPart2Result(final String fileName) {
        return validator.solvePartTwo(fileName);
    }
}
