package info.jab.aoc.day20;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2015/day/20
 */
public class Day20 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return new InfiniteElvesAndInfiniteHouses().solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return new InfiniteElvesAndInfiniteHouses().solvePartTwo(fileName);
    }
}