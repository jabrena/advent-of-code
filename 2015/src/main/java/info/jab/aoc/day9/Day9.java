package info.jab.aoc.day9;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2015/day/9
 */
public class Day9 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return new RouteOptimizer().solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
