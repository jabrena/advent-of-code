package info.jab.aoc2015.day15;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

public class Day15 implements Day<Long> {

    private final Solver<Long> solver = new CookieRecipe();

    @Override
    public Long getPart1Result(final String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Long getPart2Result(final String fileName) {
        return solver.solvePartTwo(fileName);
    }
}
