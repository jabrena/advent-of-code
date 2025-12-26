package info.jab.aoc2016.day9;

/**
 * https://adventofcode.com/2016/day/9
 */
public class Day9 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        return new ExplosivesInCyberspace().solvePartOne(fileName);
    }

    @Override
    public Long getPart2Result(String fileName) {
        return new ExplosivesInCyberspace().solvePartTwo(fileName);
    }
}