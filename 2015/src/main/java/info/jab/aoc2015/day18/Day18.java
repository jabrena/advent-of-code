package info.jab.aoc2015.day18;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/18
 */
public class Day18 implements Day<Integer> {

    private final Solver<Integer> lightGrid = new LightGrid();

    @Override
    public Integer getPart1Result(final String fileName) {
        return lightGrid.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return lightGrid.solvePartTwo(fileName);
    }
}