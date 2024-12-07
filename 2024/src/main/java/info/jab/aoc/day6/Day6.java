package info.jab.aoc.day6;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2024/day/6
 *
 * Interesting implementations:
 * https://github.com/Zooty6/adventofcode2024/blob/master/src/main/java/dev/zooty/day6/LabMap.java
 * https://github.com/bertjan/advent-of-code-2024/blob/main/src/main/java/AoC2024Day6.java
 **/
public class Day6 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        Game game = new Game(fileName);
        var count = game.moveGuardian();
        return count;
    }

    @Override
    public Integer getPart2Result(String fileName) {
        throw new UnsupportedOperationException();
    }

}
