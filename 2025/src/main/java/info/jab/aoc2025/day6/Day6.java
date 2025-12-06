package info.jab.aoc2025.day6;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

public final class Day6 implements Day<Long> {

    private final Solver<Long> solver = new MathBlockSolverV2();

    @Override
    public Long getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Long getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}
