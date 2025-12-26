package info.jab.aoc2024.day20;

/**
 * https://adventofcode.com/2024/day/20
 **/
public class Day20 implements Day2<Integer, String, Integer> {

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
