package info.jab.aoc.day16;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2024/day/16
 **/
public class Day16 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        //var list = ResourceLines.list(fileName);
        //Grid grid = new Grid(GridUtils.of(list));
        //ReindeerMaze reindeerMaze = new ReindeerMaze();
        //return reindeerMaze.findMinimumScore(grid.grid());
        ReindeerMaze3 reindeerMaze3 = new ReindeerMaze3(fileName);
        return reindeerMaze3.getMinPrice();
    }

    @Override
    public Integer getPart2Result(String fileName) {
        ReindeerMaze3 reindeerMaze3 = new ReindeerMaze3(fileName);
        return reindeerMaze3.getBestPathPoints();
    }
}
