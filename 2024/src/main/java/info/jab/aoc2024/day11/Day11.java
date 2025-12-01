package info.jab.aoc2024.day11;

import info.jab.aoc.DayWith2Params;
import info.jab.aoc.Solver2;

/**
 * https://adventofcode.com/2024/day/11
 *
 */
public class Day11 implements DayWith2Params<Long, String, Integer> {

    @Override
    public Long getPart1Result(String fileName, Integer blinks) {
        Solver2<Long, String, Integer> solver = new PlutonianPebbles();
        return solver.solvePartOne(fileName, blinks);
    }

    @Override
    public Long getPart2Result(String fileName, Integer blinks) {
        Solver2<Long, String, Integer> solver = new PlutonianPebbles();
        return solver.solvePartTwo(fileName, blinks);
    }
}
