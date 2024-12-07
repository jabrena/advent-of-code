package info.jab.aoc.day4;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2016/day/4
 **/
public class Day4 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        var soupLetter = new SoupLetter(fileName);
        return soupLetter.xmasCount();
    }

    @Override
    public Integer getPart2Result(String fileName) {
        var soupLetter = new SoupLetter(fileName);
        return soupLetter.xDashMasCount();
    }
}
