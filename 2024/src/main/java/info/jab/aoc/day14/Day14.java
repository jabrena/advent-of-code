package info.jab.aoc.day14;

import info.jab.aoc.DayWith3Params;

import java.util.List;
import java.util.ArrayList;

import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2024/day/14
 *
 * Pair programming with ChatGPT
 * Note: ChatGPT sometimes doesn´t understand the natural language and it is necessary to support it to stabilize the solution
 *
 * Inspiration to solve Part 2:
 * https://github.com/marcinbator/AoC2024-Java-solver/blob/main/src/solutions/day14/output.txt#L88C28-L88C59
 **/
public class Day14 implements DayWith3Params<Integer, String, Integer, Integer> {

    @Override
    public Integer getPart1Result(String fileName, Integer width, Integer height) {
        RobotMotion robotMotion = new RobotMotion();
        return robotMotion.part1(fileName, width, height);
    }

    @Override
    public Integer getPart2Result(String fileName, Integer width, Integer height) {
        RobotMotion robotMotion = new RobotMotion();
        return robotMotion.part2(fileName, width, height);
    }
}
