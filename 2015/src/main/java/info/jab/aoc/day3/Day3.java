package info.jab.aoc.day3;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2015/day/3
 */
public class Day3 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return new Houses3().solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return new Houses3().solvePartTwo(fileName);
    }
}
