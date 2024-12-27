package info.jab.aoc.day4;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2024/day/4
 **/
public class Day4 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        Solver<Integer> solver = new SoupLetter2();
        return solver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        Solver<Integer> solver = new SoupLetter2();
        return solver.solvePartTwo(fileName);
    }
}
