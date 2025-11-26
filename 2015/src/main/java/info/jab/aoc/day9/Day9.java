package info.jab.aoc.day9;

import info.jab.aoc.Day;

/**
 * https://adventofcode.com/2015/day/9
 */
public class Day9 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        return new RouteOptimizer().solvePartOne(fileName);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        return new RouteOptimizer().solvePartTwo(fileName);
    }

    public static void main(String[] args) {
        var day9 = new Day9();
        
        System.out.println("=== Day 9: All in a Single Night ===");
        
        // Test with sample data
        System.out.println("Sample data:");
        var part1Sample = day9.getPart1Result("/day9/day9-input-sample.txt");
        var part2Sample = day9.getPart2Result("/day9/day9-input-sample.txt");
        System.out.println("Part 1 (shortest): " + part1Sample);
        System.out.println("Part 2 (longest): " + part2Sample);
        
        System.out.println("\nActual data:");
        var part1Result = day9.getPart1Result("/day9/day9-input.txt");
        var part2Result = day9.getPart2Result("/day9/day9-input.txt");
        System.out.println("Part 1 (shortest): " + part1Result);
        System.out.println("Part 2 (longest): " + part2Result);
    }
}
