package info.jab.aoc2015.day16;

import info.jab.aoc.Day;
import info.jab.aoc.Solver;

/**
 * https://adventofcode.com/2015/day/16
 */
public class Day16 implements Day<Integer> {

    private final Solver<Integer> auntSueDetector = new AuntSueDetector();

    @Override
    public Integer getPart1Result(final String fileName) {
        return auntSueDetector.solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(final String fileName) {
        return auntSueDetector.solvePartTwo(fileName);
    }

    private static final String DEFAULT_INPUT_FILE = "/day16/day16-input.txt";
    
    public static void main(String[] args) {
        var day = new Day16();
        String fileName = args.length > 0 ? args[0] : DEFAULT_INPUT_FILE;
        
        System.out.println("Day 16 - Part 1: " + day.getPart1Result(fileName));
        System.out.println("Day 16 - Part 2: " + day.getPart2Result(fileName));
    }
}