package info.jab.aoc2024.day13;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2024/day/13
 *
 */
public class Day13 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        ClawContraption clawContraption = new ClawContraption();
        return clawContraption.solve(fileName, false);
    }

    @Override
    public Long getPart2Result(String fileName) {
        ClawContraption clawContraption = new ClawContraption();
        return clawContraption.solve(fileName, true);
    }
}
