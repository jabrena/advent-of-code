package info.jab.aoc2016.day17;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2016/day/17
 * Day 17: Two Steps Forward
 */
public class Day17 implements Day<String> {

    private final Solver<String> solver = new TwoStepsForward();

    @Override
    public String getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public String getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}
