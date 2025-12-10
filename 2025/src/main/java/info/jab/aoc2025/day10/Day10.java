package info.jab.aoc2025.day10;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

public class Day10 implements Day<Long> {

    private final Solver<Long> solver = new ButtonPressOptimizer();

    @Override
    public Long getPart1Result(String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Long getPart2Result(String fileName) {
        return solver.solvePartTwo(fileName);
    }
}
