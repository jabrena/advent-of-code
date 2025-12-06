package info.jab.aoc2015.day7;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

public class Day7 implements Day<Integer> {

    private final Solver<Integer> circuit = new Circuit();

    @Override
    public Integer getPart1Result(final String fileName) {
        return circuit.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return circuit.solvePartTwo(fileName);
    }
}
