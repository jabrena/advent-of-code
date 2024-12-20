package info.jab.aoc.day20;

import info.jab.aoc.DayWith2Params;

/**
 * https://adventofcode.com/2024/day/20
 **/
public class Day20 implements DayWith2Params<Integer, String, Integer> {

    @Override
    public Integer getPart1Result(String fileName, Integer picoseconds) {
        RaceCondition racetrack = new RaceCondition();
        return racetrack.countCheatSaves(fileName, picoseconds, 2);
    }

    @Override
    public Integer getPart2Result(String fileName, Integer picoseconds) {
        RaceCondition racetrack = new RaceCondition();
        return racetrack.countCheatSaves(fileName, picoseconds, 20);
    }
}
