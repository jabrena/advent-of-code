package info.jab.aoc2015.day19;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/19
 */
public class Day19 implements Day<Integer> {

    private final Solver<Integer> moleculeReplacementSolver = new MoleculeReplacementSolver();

    @Override
    public Integer getPart1Result(final String fileName) {
        return moleculeReplacementSolver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return moleculeReplacementSolver.solvePartTwo(fileName);
    }
}

