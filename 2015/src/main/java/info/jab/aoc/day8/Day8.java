package info.jab.aoc.day8;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2015/day/8
 */
public class Day8 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return new StringLiteralCalculator().solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return new StringLiteralCalculator().solvePartTwo(fileName);
    }
}
