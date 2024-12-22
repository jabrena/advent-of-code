package info.jab.aoc.day22;

import info.jab.aoc.Day;

import java.math.BigInteger;

/**
 * https://adventofcode.com/2024/day/22
 **/
public class Day22 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        MonkeyMarket monkeyMarket = new MonkeyMarket();
        return monkeyMarket.solvePartOne(fileName);
    }

    @Override
    public Long getPart2Result(String fileName) {
        MonkeyMarket monkeyMarket = new MonkeyMarket();
        return monkeyMarket.solvePartTwo(fileName);
    }
}
