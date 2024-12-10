package info.jab.aoc.day10;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.resources.ResourceLines;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2024/day/10
 *
 * Pair programming with ChatGTP
 */
public class Day10 implements Day<Integer> {

    //BFS
    //https://en.wikipedia.org/wiki/Breadth-first_search

    @Override
    public Integer getPart1Result(String fileName) {
        var list = ResourceLines.list(fileName);
        Grid grid = new Grid(GridUtils.of(list));
        return LavaHikingTrails.sumTrailheadScoresBFS(grid);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        var list = ResourceLines.list(fileName);
        Grid grid = new Grid(GridUtils.of(list));
        return LavaHikingTrails.sumTrailheadRatingsBFS(grid);
    }

    //Recursion approach

    public Integer getPart1Result2(String fileName) {
        var list = ResourceLines.list(fileName);
        Grid grid = new Grid(GridUtils.of(list));
        return LavaHikingTrails.sumTrailheadScores(grid);
    }

    public Integer getPart2Result2(String fileName) {
        var list = ResourceLines.list(fileName);
        Grid grid = new Grid(GridUtils.of(list));
        return LavaHikingTrails.sumTrailheadRatings(grid);
    }
}
