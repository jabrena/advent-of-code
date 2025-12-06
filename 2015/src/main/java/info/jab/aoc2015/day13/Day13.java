package info.jab.aoc2015.day13;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/13
 */
public class Day13 implements Day<Integer> {

    private final Solver<Integer> happinessCalculator = new HappinessCalculator();

    @Override
    public Integer getPart1Result(final String fileName) {
        return happinessCalculator.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return happinessCalculator.solvePartTwo(fileName);
    }
}
