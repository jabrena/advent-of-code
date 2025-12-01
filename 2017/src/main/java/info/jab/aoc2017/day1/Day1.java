package info.jab.aoc2017.day1;

import info.jab.aoc.Day;
import java.util.stream.IntStream;
import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2016/day/1
 **/
public class Day1 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        var digits = ResourceLines.line(fileName);
        return IntStream.range(0, digits.length())
            .filter(i -> digits.charAt(i) == digits.charAt((i + 1) % digits.length()))
            .map(i -> Character.getNumericValue(digits.charAt(i)))
            .sum();
    }

    @Override
    public Integer getPart2Result(String fileName) {
        var digits = ResourceLines.line(fileName);
        int halfLength = digits.length() / 2;
        return IntStream.range(0, digits.length())
            .filter(i -> digits.charAt(i) == digits.charAt((i + halfLength) % digits.length()))
            .map(i -> Character.getNumericValue(digits.charAt(i)))
            .sum();
    }
}
