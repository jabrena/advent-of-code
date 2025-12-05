package info.jab.aoc2024.day5;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2024/day/5
 */
public class Day5 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        Solver<Integer> printQueue = new PrintQueue2();
        return printQueue.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        Solver<Integer> printQueue = new PrintQueue2();
        return printQueue.solvePartTwo(fileName);
    }
}
