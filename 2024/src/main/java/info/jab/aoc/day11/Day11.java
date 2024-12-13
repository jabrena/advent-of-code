package info.jab.aoc.day11;

import info.jab.aoc.DayWith2Params;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2024/day/11
 *
 */
public class Day11 implements DayWith2Params<Long, String, Integer> {

    @Override
    public Long getPart1Result(String fileName, Integer blinks) {
        String line = ResourceLines.line(fileName);
        List<String> stones = Arrays.asList(line.split(" "));
        var plutonianPebbles = new PlutonianPebbles();
        for (int i = 0; i < blinks; i++) {
            stones = plutonianPebbles.blink(stones);
        }
        return (long) stones.size();
    }

    @Override
    public Long getPart2Result(String fileName, Integer blinks) {
        String line = ResourceLines.line(fileName);
        var plutonianPebbles = new PlutonianPebbles();
        return plutonianPebbles.simulateBlinks(line, blinks);
    }
}
