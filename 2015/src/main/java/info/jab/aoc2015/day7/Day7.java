package info.jab.aoc2015.day7;

import info.jab.aoc.Day;

public class Day7 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return new Circuit().solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return new Circuit().solvePartTwo(fileName);
    }
}
