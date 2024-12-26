package info.jab.aoc.day3;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2024/day/3
 **/
public class Day3 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        MullItOver2 mullItOver = new MullItOver2();
        return mullItOver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        MullItOver2 mullItOver = new MullItOver2();
        return mullItOver.solvePartTwo(fileName);
    }

}
