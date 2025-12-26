package info.jab.aoc2016.day13;

/**
 * https://adventofcode.com/2016/day/13
 */
public class Day13 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return new MazePathfinder().solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return new MazePathfinder().solvePartTwo(fileName);
    }
}