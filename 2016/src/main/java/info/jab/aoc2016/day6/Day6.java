package info.jab.aoc2016.day6;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2016/day/6
 * Day 6: Signals and Noise
 */
public class Day6 implements Day<String> {

    private final Solver<String> solver = new SignalsAndNoise();

    @Override
    public String getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public String getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}