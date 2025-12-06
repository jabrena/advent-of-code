package info.jab.aoc2015.day14;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

public class Day14 implements Day<Integer> {

    private final Solver<Integer> reindeerRaceSolver = new ReindeerRaceSolver();

    @Override
    public Integer getPart1Result(final String fileName) {
        return reindeerRaceSolver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return reindeerRaceSolver.solvePartTwo(fileName);
    }
}