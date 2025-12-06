package info.jab.aoc2015.day22;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/22
 */
public class Day22 implements Day<Integer> {

    private final Solver<Integer> wizardSimulator = new WizardSimulator();

    @Override
    public Integer getPart1Result(final String fileName) {
        return wizardSimulator.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return wizardSimulator.solvePartTwo(fileName);
    }
}