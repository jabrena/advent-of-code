package info.jab.aoc.day4;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2016/day/4
 **/
public class Day4 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        SoupLetter game = new SoupLetter(fileName);
        game.print();
        return game.countXMAS();
    }

    @Override
    public Integer getPart2Result(String fileName) {
        throw new UnsupportedOperationException();
    }
}
