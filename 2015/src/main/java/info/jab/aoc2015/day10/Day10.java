package info.jab.aoc2015.day10;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/10
 */
public class Day10 implements Day<Integer> {

    private final Solver<Integer> lookAndSay = new LookAndSay();

    @Override
    public Integer getPart1Result(final String fileName) {
        return lookAndSay.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return lookAndSay.solvePartTwo(fileName);
    }
}
