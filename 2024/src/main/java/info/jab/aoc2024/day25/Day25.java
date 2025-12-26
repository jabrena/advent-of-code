package info.jab.aoc2024.day25;

/**
 * https://adventofcode.com/2024/day/25
 **/
public class Day25 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        LockKeyMatcher lockKeyMatcher = new LockKeyMatcher();
        return lockKeyMatcher.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        throw new UnsupportedOperationException();
    }
}
