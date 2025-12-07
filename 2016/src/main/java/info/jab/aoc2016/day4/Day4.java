package info.jab.aoc2016.day4;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2016/day/4
 **/
public class Day4 implements Day<Integer> {

    private final Solver<Integer> solver = new SecurityThroughObscurity();

    @Override
    public Integer getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}