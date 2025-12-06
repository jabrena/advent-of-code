package info.jab.aoc2015.day11;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/11
 */
public class Day11 implements Day<String> {

    private final Solver<String> passwordValidator = new PasswordValidator();

    @Override
    public String getPart1Result(final String fileName) {
        return passwordValidator.solvePartOne(fileName);
    }

    @Override
    public String getPart2Result(final String fileName) {
        return passwordValidator.solvePartTwo(fileName);
    }
}
