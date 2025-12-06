package info.jab.aoc2015.day2;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/2
 */
public class Day2 implements Day<Integer> {

    private final Solver<Integer> presentSolver = new Present2();

    @Override
    public Integer getPart1Result(final String fileName) {
        return presentSolver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return presentSolver.solvePartTwo(fileName);
    }
}
