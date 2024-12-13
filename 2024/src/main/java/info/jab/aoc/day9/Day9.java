package info.jab.aoc.day9;

import info.jab.aoc.Day;

import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2024/day/9
 *
 * Pair programming with ChatGTP
 */
public class Day9 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        var line = ResourceLines.line(fileName);
        var diskCompactor = new DiskCompactor();
        return diskCompactor.computeChecksum(line, false);
    }

    @Override
    public Long getPart2Result(String fileName) {
        var line = ResourceLines.line(fileName);
        var diskCompactor = new DiskCompactor();
        return diskCompactor.computeChecksum(line, true);
    }
}
