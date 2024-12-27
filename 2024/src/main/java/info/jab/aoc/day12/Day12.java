package info.jab.aoc.day12;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.resources.ResourceLines;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2024/day/12
 *
 */
public class Day12 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        Solver<Integer> solver = new GardenGroups();
        return solver.solvePartOne(fileName);
    }

    //TODO: I need to recode the second part again.
    @Override
    public Integer getPart2Result(String fileName) {
        throw new UnsupportedOperationException();
    }
}
