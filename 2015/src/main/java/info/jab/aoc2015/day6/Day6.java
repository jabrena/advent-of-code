package info.jab.aoc2015.day6;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/6
 */
public class Day6 implements Day<Long> {

    private final Solver<Long> solver = new LightCounter();

    @Override
    public Long getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Long getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}
