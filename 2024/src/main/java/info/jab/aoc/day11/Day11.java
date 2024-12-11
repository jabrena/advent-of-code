package info.jab.aoc.day11;

import info.jab.aoc.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2024/day/11
 **/
public class Day11 {

    //@Override
    public Integer getPart1Result(String fileName, Integer blinks) {

        var line = ResourceLines.line(fileName);
        List<String> stones = new ArrayList<>();
        stones.addAll(Arrays.asList(line.split(" ")));
        for (int i = 0; i < blinks; i++) {
            stones = PlutonianPebbles.blink(stones);
        }
        //System.out.println(stones);
        System.out.println("Total stones: " + stones.size());
        return stones.size();
    }

    //@Override
    public Integer getPart2Result(String fileName) {
        throw new UnsupportedOperationException();
    }
}
