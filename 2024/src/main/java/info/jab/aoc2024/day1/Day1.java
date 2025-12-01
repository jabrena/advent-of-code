package info.jab.aoc2024.day1;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2024/day/1
 */
public class Day1 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        Solver<Integer> solver = new HistorianHysteria();
        return solver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        Solver<Integer> solver = new HistorianHysteria();
        return solver.solvePartTwo(fileName);
    }
}
