package info.jab.aoc.day10;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.resources.ResourceLines;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2024/day/10
 *
 * Pair programming with ChatGTP
 */
public class Day10 implements Day<Integer> {

    //BFS
    //https://en.wikipedia.org/wiki/Breadth-first_search

    @Override
    public Integer getPart1Result(String fileName) {
        Solver<Integer> solver = new LavaHikingTrails2();
        return solver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        Solver<Integer> solver = new LavaHikingTrails2();
        return solver.solvePartTwo(fileName);
    }
}
