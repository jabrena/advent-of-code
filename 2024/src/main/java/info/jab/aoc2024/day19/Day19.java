package info.jab.aoc2024.day19;

/**
 * https://adventofcode.com/2024/day/19
 **/
public class Day19 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        TowelArrangement towelArrangement = new TowelArrangement();
        return towelArrangement.solve1(fileName);
    }

    @Override
    public Long getPart2Result(String fileName) {
        TowelArrangement towelArrangement = new TowelArrangement();
        return towelArrangement.solve2(fileName);
    }
}
