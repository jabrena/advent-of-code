package info.jab.aoc2015.day21;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

public class Day21 implements Day<Integer> {

    private final Solver<Integer> rpgEquipmentSolver = new RPGEquipmentSolver();

    @Override
    public Integer getPart1Result(final String fileName) {
        return rpgEquipmentSolver.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return rpgEquipmentSolver.solvePartTwo(fileName);
    }
}