package jab.aoc.day2;

import static jab.aoc.Utils.SPACE_SEPARATOR_PATTERN;

import jab.aoc.Day;
import jab.aoc.Utils;

/**
 * Solution for AOC 2022, Day 2
 * https://adventofcode.com/2022/day/2
 *
 */
public class Day2 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return Utils
            .readFileToList(fileName)
            .stream()
            .map(SPACE_SEPARATOR_PATTERN::split)
            .map(arr -> new Game1(Column1.fromValue(arr[0]), Column2.fromValue(arr[1])))
            .map(Game1::getScore)
            .reduce(0, Integer::sum);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return Utils
            .readFileToList(fileName)
            .stream()
            .map(SPACE_SEPARATOR_PATTERN::split)
            .map(arr -> new Game2(Column1.fromValue(arr[0]), Column2.fromValue(arr[1])))
            .map(Game2::getScore)
            .reduce(0, Integer::sum);
    }
}
