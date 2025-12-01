package info.jab.aoc2016.day22;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2016/day/22
 */
public class Day22 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return new GridComputing().solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return new GridComputing().solvePartTwo(fileName);
    }
}
