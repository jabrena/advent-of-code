package info.jab.aoc.day2;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2024/day/2
 */
public class Day2 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        Solver<Integer> solver = new SafeReports2();
        return solver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        Solver<Integer> solver = new SafeReports2();
        return solver.solvePartTwo(fileName);
    }
}
