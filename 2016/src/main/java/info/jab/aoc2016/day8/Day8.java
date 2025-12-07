package info.jab.aoc2016.day8;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2016/day/8
 * Two-Factor Authentication
 */
public class Day8 implements Day<String> {

    private final Solver<String> solver = new TwoFactorAuthentication();

    @Override
    public String getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public String getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}