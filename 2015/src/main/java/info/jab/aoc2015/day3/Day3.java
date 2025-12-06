package info.jab.aoc2015.day3;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/3
 */
public class Day3 implements Day<Integer> {

    private final Solver<Integer> housesSolver = new Houses3();

    @Override
    public Integer getPart1Result(final String fileName) {
        return housesSolver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return housesSolver.solvePartTwo(fileName);
    }
}
