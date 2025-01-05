package info.jab.aoc.day13;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2015/day/13
 */
public class Day13 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String input) {
        return new HappinessCalculator().solvePartOne(input);
    }

    @Override
    public Integer getPart2Result(String input) {
        return new HappinessCalculator().solvePartTwo(input);
    }
}
