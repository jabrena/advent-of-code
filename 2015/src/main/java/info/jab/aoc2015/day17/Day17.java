package info.jab.aoc2015.day17;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

public class Day17 implements Day<Integer> {

    private final Solver<Integer> solver = new ContainerCombination();

    @Override
    public Integer getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}
