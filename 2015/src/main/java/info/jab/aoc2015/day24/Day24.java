package info.jab.aoc2015.day24;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/24
 */
public class Day24 implements Day<Long> {

    private final Solver<Long> packageBalancer = new PackageBalancer();

    @Override
    public Long getPart1Result(final String fileName) {
        return packageBalancer.solvePartOne(fileName);
    }

    @Override
    public Long getPart2Result(final String fileName) {
        return packageBalancer.solvePartTwo(fileName);
    }
}
