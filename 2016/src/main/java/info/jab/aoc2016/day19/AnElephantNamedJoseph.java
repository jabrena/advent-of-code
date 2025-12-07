package info.jab.aoc2016.day19;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

/**
 * Solver for Day 19: An Elephant Named Joseph
 * Solves the Josephus problem variant.
 */
public final class AnElephantNamedJoseph implements Solver<Integer> {

    @Override
    public Integer solvePartOne(final String fileName) {
        var lines = ResourceLines.list(fileName);
        int numberOfElves = Integer.parseInt(lines.get(0).trim());
        return solvePart1(numberOfElves);
    }

    @Override
    public Integer solvePartTwo(final String fileName) {
        var lines = ResourceLines.list(fileName);
        int numberOfElves = Integer.parseInt(lines.get(0).trim());
        return solvePart2(numberOfElves);
    }

    private Integer solvePart1(final int numberOfElves) {
        // Use Josephus formula for k=2
        // For k=2, the winner is 2*(n - 2^floor(log2(n))) + 1
        int powerOf2 = 1;
        while (powerOf2 * 2 <= numberOfElves) {
            powerOf2 *= 2;
        }
        return 2 * (numberOfElves - powerOf2) + 1;
    }

    private Integer solvePart2(final int numberOfElves) {
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

