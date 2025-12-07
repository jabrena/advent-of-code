package info.jab.aoc2016.day3;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

public class Day3 implements Day<Integer> {

    private final Solver<Integer> solver = new TriangleValidator();

    @Override
    public Integer getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}