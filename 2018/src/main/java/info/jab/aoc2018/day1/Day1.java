package info.jab.aoc2018.day1;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2018/day/1
 **/
public class Day1 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        Solver<Integer> solver = new FrequencyDevice2();
        return solver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        Solver<Integer> solver = new FrequencyDevice2();
        return solver.solvePartTwo(fileName);
    }
}
