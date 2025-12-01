package info.jab.aoc2024.day7;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2024/day/7
 *
 */
public class Day7 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        Solver<Long> solver = new BridgeRepair();
        return solver.solvePartOne(fileName);
    }

    @Override
    public Long getPart2Result(String fileName) {
        Solver<Long> solver = new BridgeRepair();
        return solver.solvePartTwo(fileName);
    }
}
