package info.jab.aoc2016.day7;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2016/day/7
 * Day 7: Internet Protocol Version 7
 */
public class Day7 implements Day<Integer> {

    private final Solver<Integer> solver = new InternetProtocolVersion7();

    @Override
    public Integer getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}