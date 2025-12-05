package info.jab.aoc2016.day23;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2016/day/23
 */
public class Day23 implements Day<Integer> {

    private Solver<Integer> solver = new AssembunnyInterpreter();

    @Override
    public Integer getPart1Result(String fileName) {
        return solver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return solver.solvePartTwo(fileName);
    }
}
