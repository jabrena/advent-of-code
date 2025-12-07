package info.jab.aoc2016.day14;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2016/day/14
 * Day 14: One-Time Pad
 */
public class Day14 implements Day<Integer> {

    private final Solver<Integer> solver = new OneTimePad();

    @Override
    public Integer getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}