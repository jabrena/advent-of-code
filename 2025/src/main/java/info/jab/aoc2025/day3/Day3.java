package info.jab.aoc2025.day3;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

public class Day3 implements Day<Long> {

    private final Solver<Long> solver = new MaxJoltage();

    @Override
    public Long getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Long getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}
