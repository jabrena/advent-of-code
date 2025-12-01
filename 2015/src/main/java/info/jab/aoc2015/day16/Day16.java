package info.jab.aoc2015.day16;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2015/day/16
 */
public class Day16 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return new AuntSueDetector().solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return new AuntSueDetector().solvePartTwo(fileName);
    }

    public static void main(String[] args) {
        var day = new Day16();
        String fileName = "/day16/day16-input.txt";
        
        System.out.println("Day 16 - Part 1: " + day.getPart1Result(fileName));
        System.out.println("Day 16 - Part 2: " + day.getPart2Result(fileName));
    }
}