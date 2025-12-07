package info.jab.aoc2016.day20;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2016/day/20
 */
public class Day20 implements Day<Long> {

    private final Solver<Long> solver = new FirewallRules();

    @Override
    public Long getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Long getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}
