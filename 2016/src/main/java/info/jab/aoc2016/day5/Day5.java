package info.jab.aoc2016.day5;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2016/day/5
 */
public class Day5 implements Day<String> {

    private final Solver<String> solver = new HowAboutANiceGameOfChess();

    @Override
    public String getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public String getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}