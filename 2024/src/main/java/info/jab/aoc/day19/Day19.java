package info.jab.aoc.day19;

import info.jab.aoc.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2024/day/19
 **/
public class Day19 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        TowelArrangement towelArrangement = new TowelArrangement();
        return towelArrangement.solve1(fileName);
    }

    @Override
    public Long getPart2Result(String fileName) {
        TowelArrangement towelArrangement = new TowelArrangement();
        return towelArrangement.solve2(fileName);
    }
}
