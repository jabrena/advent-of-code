package info.jab.aoc.day2;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

public class Day2 implements Day<String> {

    @Override
    public String getPart1Result(String fileName) {
        Solver<String> solver = new BathroomSecurity2();
        return solver.solvePartOne(fileName);
    }
    

    @Override
    public String getPart2Result(String fileName) {
        Solver<String> solver = new BathroomSecurity2();
        return solver.solvePartTwo(fileName);
    }
}
