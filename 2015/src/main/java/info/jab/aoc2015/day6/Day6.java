package info.jab.aoc2015.day6;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2015/day/6
 */
public class Day6 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        return new LightCounter().solvePartOne(fileName);
    }

    @Override
    public Long getPart2Result(String fileName) {
        return new LightCounter().solvePartTwo(fileName);
    }
}
