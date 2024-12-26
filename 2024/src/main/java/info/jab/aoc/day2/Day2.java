package info.jab.aoc.day2;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2024/day/2
 */
public class Day2 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        SafeReports2 safeReports = new SafeReports2();
        return safeReports.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        SafeReports2 safeReports = new SafeReports2();
        return safeReports.solvePartTwo(fileName);
    }
}
