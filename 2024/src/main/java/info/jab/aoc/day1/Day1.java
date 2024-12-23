package info.jab.aoc.day1;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2024/day/1
 *
 * TODO: Refactor in a less functional style, more OOP.
 */
public class Day1 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        HistorianHysteria historianHysteria = new HistorianHysteria();
        return historianHysteria.partOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        HistorianHysteria historianHysteria = new HistorianHysteria();
        return historianHysteria.partTwo(fileName);
    }
}
