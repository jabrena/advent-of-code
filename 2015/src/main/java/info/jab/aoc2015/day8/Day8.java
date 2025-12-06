package info.jab.aoc2015.day8;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/8
 */
public class Day8 implements Day<Integer> {

    private final Solver<Integer> stringLiteralCalculator = new StringLiteralCalculator();

    @Override
    public Integer getPart1Result(final String fileName) {
        return stringLiteralCalculator.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return stringLiteralCalculator.solvePartTwo(fileName);
    }
}
