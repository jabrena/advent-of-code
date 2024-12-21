package info.jab.aoc.day21;

import info.jab.aoc.Day;

import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2024/day/21
 *
 * Defeated today.
 **/
public class Day21 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        var list = ResourceLines.list(fileName);
        KeypadConundrum2 keypadConundrum = new KeypadConundrum2();
        return keypadConundrum.partOne(list);
    }

    @Override
    public Long getPart2Result(String fileName) {
        var list = ResourceLines.list(fileName);
        KeypadConundrum2 keypadConundrum = new KeypadConundrum2();
        return keypadConundrum.partTwo(list);
    }
}
