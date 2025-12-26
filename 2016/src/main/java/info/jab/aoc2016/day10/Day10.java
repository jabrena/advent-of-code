package info.jab.aoc2016.day10;

/**
 * https://adventofcode.com/2016/day/10
 */
public class Day10 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return new BalanceBots().solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return new BalanceBots().solvePartTwo(fileName);
    }
}