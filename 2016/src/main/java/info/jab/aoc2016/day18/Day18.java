package info.jab.aoc2016.day18;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2016/day/18
 * Day 18: Like a Rogue
 */
public class Day18 implements Day<Integer> {

    private final Solver<Integer> solver = new LikeARogue();

    @Override
    public Integer getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}
