package info.jab.aoc2015.day5;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2015/day/5
 */
public class Day5 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return new StringValidator().solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return new StringValidator().solvePartTwo(fileName);
    }
}
