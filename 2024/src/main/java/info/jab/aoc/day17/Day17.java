package info.jab.aoc.day17;

import info.jab.aoc.Day;

import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2024/day/17
 **/
public class Day17 implements Day<String> {

    @Override
    public String getPart1Result(String fileName) {
        var list = ResourceLines.list(fileName);
        ChronospatialComputer computer = ChronospatialComputer.fromInput(list);
        computer.run();
        return computer.getOutput();
    }

    @Override
    public String getPart2Result(String fileName) {
        ChronospatialComputer2 chronospatialComputer2 = new ChronospatialComputer2();
        return chronospatialComputer2.part22(fileName);
    }
}
