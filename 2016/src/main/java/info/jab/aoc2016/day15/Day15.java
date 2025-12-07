package info.jab.aoc2016.day15;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2016/day/15
 */
public class Day15 implements Day<Integer> {

    private final Solver<Integer> solver = new TimingIsEverything();

    @Override
    public Integer getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}
