package info.jab.aoc2016.day12;

/**
 * https://adventofcode.com/2016/day/12
 */
public class Day12 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return new AssembunnyInterpreter().solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return new AssembunnyInterpreter().solvePartTwo(fileName);
    }
}