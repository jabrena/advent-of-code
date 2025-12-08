package info.jab.aoc2025.day8;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

public class Day8 implements Day<Long> {

    private final Solver<Long> solver = new PointCluster();

    @Override
    public Long getPart1Result(String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Long getPart2Result(String fileName) {
        return solver.solvePartTwo(fileName);
    }
}
