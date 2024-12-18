package info.jab.aoc.day18;

import info.jab.aoc.DayWith4Params;

/**
 * https://adventofcode.com/2024/day/18
 **/
public class Day18 implements DayWith4Params<String, String, Integer, Integer, Boolean> {

    @Override
    public String getPart1Result(String fileName, Integer gridDimension, Integer limit, Boolean debug) {
        RamRun ramRun = new RamRun();
        return ramRun.solve1(fileName, gridDimension, limit, debug);
    }

    @Override
    public String getPart2Result(String fileName, Integer gridDimension, Integer limit, Boolean debug) {
        RamRun ramRun = new RamRun();
        return ramRun.solve2(fileName, gridDimension, limit, debug);
    }
}
