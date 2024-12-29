package info.jab.aoc.day1;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2015/day/1
 */
public class Day1 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return new Lisp2().solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return new Lisp2().solvePartTwo(fileName);
    }
}
