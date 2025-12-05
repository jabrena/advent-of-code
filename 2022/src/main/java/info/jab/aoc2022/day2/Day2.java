package info.jab.aoc2022.day2;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.regex.Pattern;

/**
 * Solution for AOC 2022, Day 2
 * https://adventofcode.com/2022/day/2
 *
 */
public class Day2 implements Day<Integer> {

    private static final Pattern SPACE_SEPARATOR_PATTERN = Pattern.compile(" ");

    @Override
    public Integer getPart1Result(String fileName) {
        return ResourceLines
            .list("/" + fileName)
            .stream()
            .map(SPACE_SEPARATOR_PATTERN::split)
            .map(arr -> new Game1(Column1.fromValue(arr[0]), Column2.fromValue(arr[1])))
            .map(Game1::getScore)
            .reduce(0, Integer::sum);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return ResourceLines
            .list("/" + fileName)
            .stream()
            .map(SPACE_SEPARATOR_PATTERN::split)
            .map(arr -> new Game2(Column1.fromValue(arr[0]), Column2.fromValue(arr[1])))
            .map(Game2::getScore)
            .reduce(0, Integer::sum);
    }
}
