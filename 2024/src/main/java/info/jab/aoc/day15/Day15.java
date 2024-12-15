package info.jab.aoc.day15;

import info.jab.aoc.DayWith2Params;

/**
 * https://adventofcode.com/2024/day/15
 *
 * Pair programming with ChatGPT
 **/
public class Day15 implements DayWith2Params<Integer, String, Boolean> {

    @Override
    public Integer getPart1Result(String fileName, Boolean debug) {
        WarehouseScaled warehouseScaled = new WarehouseScaled();
        return warehouseScaled.solve1(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName, Boolean debug) {
        WarehouseScaled warehouseScaled = new WarehouseScaled();
        return warehouseScaled.solve2(fileName);
    }
}
