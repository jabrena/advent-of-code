package info.jab.aoc2016.day16;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2016/day/16
 */
public class Day16 implements Day<String> {

    private final Solver<String> solver = new DragonChecksum();

    @Override
    public String getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public String getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}
