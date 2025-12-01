package info.jab.aoc2024.day6;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2024/day/6
 *
 * Interesting implementations:
 * https://github.com/Zooty6/adventofcode2024/blob/master/src/main/java/dev/zooty/day6/LabMap.java
 * https://github.com/bertjan/advent-of-code-2024/blob/main/src/main/java/AoC2024Day6.java
 */
public class Day6 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        Game game = new Game(fileName);
        return game.moveGuardian();
    }

    //TODO: I need to recode the second part again.
    @Override
    public Integer getPart2Result(String fileName) {
        throw new UnsupportedOperationException();
    }

}
