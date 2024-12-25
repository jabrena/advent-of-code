package info.jab.aoc.day24;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2024/day/24
 *
 **/
public class Day24 implements Day<String> {

    @Override
    public String getPart1Result(String fileName) {
        CrossedWires crossedWires = new CrossedWires();
        return crossedWires.solvePartOne(fileName);
    }

    @Override
    public String getPart2Result(String fileName) {

        //GraphVizUtil.generate(fileName);

        CrossedWires2 crossedWires = new CrossedWires2();
        return crossedWires.solvePartTwo(fileName);
    }
}
