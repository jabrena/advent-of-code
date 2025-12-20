package info.jab.aoc2025.day4;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

public class Day4 implements Day<Integer> {

    private final Solver<Integer> solver = new GridNeighbor2();

    @Override
    public Integer getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}
