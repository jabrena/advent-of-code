package info.jab.aoc2024.day23;

/**
 * https://adventofcode.com/2024/day/23
 **/
public class Day23 implements Day<String> {

    @Override
    public String getPart1Result(String fileName) {
        LanParty lanParty = new LanParty();
        return lanParty.solvePartOne(fileName);
    }

    @Override
    public String getPart2Result(String fileName) {
        LanParty lanParty = new LanParty();
        return lanParty.solvePartTwo(fileName);
    }
}
