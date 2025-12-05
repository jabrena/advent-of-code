package info.jab.aoc2016.day19;

import info.jab.aoc.Day;

import com.putoet.math.Josephus;
import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2016/day/19
 */
public class Day19 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        int numberOfElves = Integer.parseInt(lines.get(0).trim());
        return Josephus.winner(numberOfElves);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        int numberOfElves = Integer.parseInt(lines.get(0).trim());
        return solvePart2(numberOfElves);
    }

    private Integer solvePart2(int numberOfElves) {
        // Use a more efficient approach for large numbers
        // For part 2, we need to find the pattern
        // When n is a power of 3, the winner is n
        // Otherwise, we can use a formula based on the pattern
        
        if (numberOfElves == 1) {
            return 1;
        }
        
        // Find the largest power of 3 <= numberOfElves
        int powerOf3 = 1;
        while (powerOf3 * 3 <= numberOfElves) {
            powerOf3 *= 3;
        }
        
        if (numberOfElves == powerOf3) {
            return numberOfElves;
        }
        
        // For numbers between powerOf3 and 2*powerOf3
        if (numberOfElves < 2 * powerOf3) {
            return numberOfElves - powerOf3;
        }
        
        // For numbers >= 2*powerOf3
        return 2 * numberOfElves - 3 * powerOf3;
    }
}
