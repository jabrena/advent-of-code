package info.jab.aoc2025.day8;

import info.jab.aoc.Day2;
import info.jab.aoc.Solver2;

public class Day8 implements Day2<Long, String, Integer> {

    private final Solver2<Long, String, Integer> solver = new PointCluster2();

    @Override
    public Long getPart1Result(String fileName, Integer connectionLimit) {
        return solver.solvePartOne(fileName, connectionLimit);
    }

    @Override
    public Long getPart2Result(String fileName, Integer unused) {
        return solver.solvePartTwo(fileName, unused);
    }
}
