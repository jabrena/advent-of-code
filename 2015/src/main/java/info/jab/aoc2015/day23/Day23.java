package info.jab.aoc2015.day23;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2015/day/23
 */
public class Day23 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return new TuringComputer().solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return new TuringComputer().solvePartTwo(fileName);
    }
}