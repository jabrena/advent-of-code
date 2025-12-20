package info.jab.aoc2025.day1;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

public class Day1 implements Day<Integer> {

    private final Solver<Integer> dialRotator = new DialRotator();

    @Override
    public Integer getPart1Result(final String fileName) {
        return dialRotator.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return dialRotator.solvePartTwo(fileName);
    }
}
