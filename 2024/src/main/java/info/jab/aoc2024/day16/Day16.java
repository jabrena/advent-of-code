package info.jab.aoc2024.day16;

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
        ReindeerMaze2 reindeerMaze = new ReindeerMaze2(fileName);
        return reindeerMaze.getBestPathPoints();
    }
}
