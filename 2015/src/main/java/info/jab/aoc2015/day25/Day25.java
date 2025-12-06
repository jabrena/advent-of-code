package info.jab.aoc2015.day25;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/25
 */
public class Day25 implements Day<Long> {

    private final Solver<Long> codeGeneratorSolver = new CodeGeneratorSolver();

    @Override
    public Long getPart1Result(final String fileName) {
        return codeGeneratorSolver.solvePartOne(fileName);
    }

    @Override
    public Long getPart2Result(final String fileName) {
        return codeGeneratorSolver.solvePartTwo(fileName);
    }
}