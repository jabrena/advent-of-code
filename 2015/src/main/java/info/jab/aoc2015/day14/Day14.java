package info.jab.aoc2015.day14;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

public class Day14 implements Day<Integer> {

    private final Solver<Integer> reindeerRace = new ReindeerRace();

    @Override
    public Integer getPart1Result(final String fileName) {
        return reindeerRace.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return reindeerRace.solvePartTwo(fileName);
    }
}