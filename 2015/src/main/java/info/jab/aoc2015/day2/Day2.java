package info.jab.aoc2015.day2;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2015/day/2
 */
public class Day2 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return new Present2().solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return new Present2().solvePartTwo(fileName);
    }
}
