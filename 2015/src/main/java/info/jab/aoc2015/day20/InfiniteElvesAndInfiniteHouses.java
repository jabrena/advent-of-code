package info.jab.aoc2015.day20;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

public class InfiniteElvesAndInfiniteHouses implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        var input = ResourceLines.line(fileName);
        int target = Integer.parseInt(input.trim());
        
        return findLowestHouseNumber(target);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var input = ResourceLines.line(fileName);
        int target = Integer.parseInt(input.trim());
        
        return findLowestHouseNumberPart2(target);
    }
    
    private int findLowestHouseNumber(int target) {
        // We need to find the first house number where sum of divisors * 10 >= target
        // Start checking from house 1
        int houseNumber = 1;
        
        while (true) {
            int presents = calculatePresents(houseNumber);
            if (presents >= target) {
                return houseNumber;
            }
            houseNumber++;
        }
    }
    
    private int calculatePresents(int houseNumber) {
        int totalPresents = 0;
        
        // Optimized divisor calculation: iterate up to sqrt(houseNumber)
        // This is already O(√H) which is optimal for single house calculation
        // For multiple houses, we could use a sieve, but for sequential checking this is optimal
        int sqrt = (int) Math.sqrt(houseNumber);
        for (int elf = 1; elf <= sqrt; elf++) {
            if (houseNumber % elf == 0) {
                // elf is a divisor, so this elf visits this house
                totalPresents += elf * 10;
                
                // If elf is not the square root, then houseNumber/elf is also a divisor
                int pairedElf = houseNumber / elf;
                if (elf != pairedElf) {
                    totalPresents += pairedElf * 10;
                }
            }
        }
        
        return totalPresents;
    }
    
    private int findLowestHouseNumberPart2(int target) {
        // For Part 2: Each elf stops after 50 houses, delivers 11 * elf number presents
        int houseNumber = 1;
        
        while (true) {
            int presents = calculatePresentsPart2(houseNumber);
            if (presents >= target) {
                return houseNumber;
            }
            houseNumber++;
        }
    }
    
    private int calculatePresentsPart2(int houseNumber) {
        int totalPresents = 0;
        
        // Optimized divisor calculation: iterate up to sqrt(houseNumber)
        // An elf (divisor) visits this house only if houseNumber <= elf * 50
        // This means elf >= houseNumber / 50, so we can start from there
        int minElf = Math.max(1, (houseNumber + 49) / 50); // Ceiling division
        int sqrt = (int) Math.sqrt(houseNumber);
        
        for (int elf = 1; elf <= sqrt; elf++) {
            if (houseNumber % elf == 0) {
                // elf is a divisor
                // Check if this elf still delivers to this house (within 50 houses)
                if (houseNumber <= elf * 50) {
                    totalPresents += elf * 11;
                }
                
                // Check the paired divisor
                int pairedElf = houseNumber / elf;
                if (elf != pairedElf && houseNumber <= pairedElf * 50) {
                    totalPresents += pairedElf * 11;
                }
            }
        }
        
        return totalPresents;
    }
}