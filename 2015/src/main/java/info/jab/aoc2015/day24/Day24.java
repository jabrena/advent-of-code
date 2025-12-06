package info.jab.aoc2015.day24;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/24
 */
public class Day24 implements Day<Long> {

    private final Solver<Long> packageBalancerSolver = new PackageBalancerSolver();

    @Override
    public Long getPart1Result(final String fileName) {
        return packageBalancerSolver.solvePartOne(fileName);
    }

    @Override
    public Long getPart2Result(final String fileName) {
        return packageBalancerSolver.solvePartTwo(fileName);
    }
}
