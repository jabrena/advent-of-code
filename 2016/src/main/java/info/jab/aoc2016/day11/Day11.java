package info.jab.aoc2016.day11;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2016/day/11
 */
public class Day11 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return new RTGFacility().solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return new RTGFacility().solvePartTwo(fileName);
    }
}