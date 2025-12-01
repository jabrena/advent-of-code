package info.jab.aoc2024.day9;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2024/day/9
 *
 * Pair programming with ChatGTP
 */
public class Day9 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        Solver<Long> solver = new DiskCompactor();
        return solver.solvePartOne(fileName);
    }

    @Override
    public Long getPart2Result(String fileName) {
        Solver<Long> solver = new DiskCompactor();
        return solver.solvePartTwo(fileName);
    }
}
