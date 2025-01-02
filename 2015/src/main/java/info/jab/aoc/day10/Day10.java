package info.jab.aoc.day10;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2015/day/10
 */
public class Day10 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        String input = "1321131112";
        int iterations = 40;

        String result = new LookAndSay3().applyLookAndSay(input, iterations);
        return Integer.valueOf(result.length());
    }

    @Override
    public Integer getPart2Result(String fileName) {
        String input = "1321131112";
        int iterations = 50;

        String result = new LookAndSay3().applyLookAndSay(input, iterations);
        return Integer.valueOf(result.length());
    }
}
