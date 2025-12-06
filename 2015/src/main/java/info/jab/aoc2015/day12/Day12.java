package info.jab.aoc2015.day12;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/12
 */
public class Day12 implements Day<Integer> {

    private final Solver<Integer> solver = new JSONAbacus();

    @Override
    public Integer getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}
