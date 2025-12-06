package info.jab.aoc2015.day21;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

public class Day21 implements Day<Integer> {

    private final Solver<Integer> rpgEquipment = new RPGEquipment();

    @Override
    public Integer getPart1Result(final String fileName) {
        return rpgEquipment.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return rpgEquipment.solvePartTwo(fileName);
    }
}