package info.jab.aoc.day4;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2015/day/4
 */
public class Day4 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String secretKey) {
        return new AdventCoinMiner().findLowestNumber(secretKey, true);
    }

    @Override
    public Integer getPart2Result(String secretKey) {
        return new AdventCoinMiner().findLowestNumber(secretKey, false);
    }
}
