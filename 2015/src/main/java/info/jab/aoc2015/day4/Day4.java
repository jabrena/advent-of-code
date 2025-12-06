package info.jab.aoc2015.day4;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/4
 */
public class Day4 implements Day<Integer> {

    private final Solver<Integer> adventCoinMiner = new AdventCoinMiner();

    @Override
    public Integer getPart1Result(final String fileName) {
        return adventCoinMiner.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return adventCoinMiner.solvePartTwo(fileName);
    }
}
