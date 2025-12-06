package info.jab.aoc2015.day5;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/5
 */
public class Day5 implements Day<Integer> {

    private final Solver<Integer> stringValidator = new StringValidator();

    @Override
    public Integer getPart1Result(final String fileName) {
        return stringValidator.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return stringValidator.solvePartTwo(fileName);
    }
}
