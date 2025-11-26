package info.jab.aoc.day22;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2015/day/22
 */
public class Day22 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return new WizardSimulator().solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return new WizardSimulator().solvePartTwo(fileName);
    }
}