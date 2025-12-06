package info.jab.aoc2015.day20;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/20
 */
public class Day20 implements Day<Integer> {

    private final Solver<Integer> solver = new InfiniteElvesAndInfiniteHouses();

    @Override
    public Integer getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}
