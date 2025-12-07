package info.jab.aoc2016.day21;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2016/day/21
 */
public class Day21 implements Day<String> {

    private final Solver<String> solver = new ScrambledLettersAndHash();

    @Override
    public String getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public String getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}
