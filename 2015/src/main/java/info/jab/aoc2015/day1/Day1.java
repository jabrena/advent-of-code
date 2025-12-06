package info.jab.aoc2015.day1;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/1
 */
public class Day1 implements Day<Integer> {

    private final Solver<Integer> lispSolver = new Lisp2();

    @Override
    public Integer getPart1Result(final String fileName) {
        return lispSolver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return lispSolver.solvePartTwo(fileName);
    }
}
