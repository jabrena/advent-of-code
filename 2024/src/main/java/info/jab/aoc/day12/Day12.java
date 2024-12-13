package info.jab.aoc.day12;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.resources.ResourceLines;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2024/day/12
 *
 */
public class Day12 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        var list = ResourceLines.list(fileName);
        Grid grid = new Grid(GridUtils.of(list));
        GardenGroups gardenGroups = new GardenGroups();
        return gardenGroups.calculateTotalPricePart1(grid.grid());
    }

    //TODO: I need to recode the second part again.
    @Override
    public Integer getPart2Result(String fileName) {
        var list = ResourceLines.list(fileName);
        throw new UnsupportedOperationException();
    }
}
