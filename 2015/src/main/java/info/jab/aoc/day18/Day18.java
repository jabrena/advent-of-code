package info.jab.aoc.day18;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2015/day/18
 */
public class Day18 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return new LightGrid().solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return new LightGrid().solvePartTwo(fileName);
    }
}