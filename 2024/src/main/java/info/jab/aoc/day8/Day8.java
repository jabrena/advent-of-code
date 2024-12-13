package info.jab.aoc.day8;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2024/day/8
 *
 */
public class Day8 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        AntennaMap antennaMap = new AntennaMap(fileName);
        return antennaMap.countAntinodes();
    }

    @Override
    public Long getPart2Result(String fileName) {
        AntennaMap antennaMap = new AntennaMap(fileName);
        return antennaMap.countAntinodesWithHarmonics();
    }
}
