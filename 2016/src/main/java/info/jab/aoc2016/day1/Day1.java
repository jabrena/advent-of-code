package info.jab.aoc2016.day1;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2016/day/1
 **/
public class Day1 implements Day<Integer> {

    private final Solver<Integer> solver = new NoTimeForATaxicab();

    @Override
    public Integer getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}
