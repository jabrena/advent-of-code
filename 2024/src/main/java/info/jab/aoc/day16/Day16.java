package info.jab.aoc.day16;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2024/day/16
 **/
public class Day16 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        ReindeerMaze reindeerMaze = new ReindeerMaze();
        return reindeerMaze.findMinimumScore(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        ReindeerMaze2 reindeerMaze3 = new ReindeerMaze2(fileName);
        return reindeerMaze3.getBestPathPoints();
    }
}
