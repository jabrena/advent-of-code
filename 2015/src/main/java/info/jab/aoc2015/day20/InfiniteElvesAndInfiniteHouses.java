package info.jab.aoc2015.day20;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

/**
 * Optimized implementation using sieve-based approach.
 * 
 * Instead of calculating divisors for each house individually (O(√H) per house),
 * we iterate through elves and add presents to all houses they visit.
 * 
 * Complexity: O(H log H) where H is the final house number found.
 * This is better than the previous O(H × √H) approach.
 * 
 * The sieve approach:
 * - For each elf e, add presents to houses e, 2e, 3e, ...
 * - Total operations: H/1 + H/2 + H/3 + ... + H/H = O(H log H) (harmonic series)
 */
public class InfiniteElvesAndInfiniteHouses implements Solver<Integer> {

    @Override
    public Integer solvePartOne(final String fileName) {
        final var input = ResourceLines.line(fileName);
        final int target = Integer.parseInt(input.trim());
        
        return findLowestHouseNumberSieve(target);
    }

    @Override
    public Integer solvePartTwo(final String fileName) {
        final var input = ResourceLines.line(fileName);
        final int target = Integer.parseInt(input.trim());
        
        return findLowestHouseNumberPart2Sieve(target);
    }
    
    /**
     * Sieve-based approach for Part 1.
     * For each elf e, add 10*e presents to houses e, 2e, 3e, ...
     * Complexity: O(H log H) where H is the final house number.
     */
    private int findLowestHouseNumberSieve(final int target) {
        // Estimate upper bound: worst case is when house has many divisors
        // For house H, max presents ≈ H * 10 * (average number of divisors)
        // Average number of divisors is O(log H), so we need roughly H ≈ target/10
        // Use a conservative upper bound with some margin
        final int estimatedMax = Math.max(target / 10, 100_000);
        final int[] presents = new int[estimatedMax + 1];
        
        // For each elf e, add presents to all houses that are multiples of e
        for (int elf = 1; elf <= estimatedMax; elf++) {
            for (int house = elf; house <= estimatedMax; house += elf) {
                presents[house] += elf * 10;
            }
        }
        
        // Find the first house that meets the target
        for (int house = 1; house <= estimatedMax; house++) {
            if (presents[house] >= target) {
                return house;
            }
        }
        
        // If not found, fall back to original method (shouldn't happen with good estimate)
        // This is a safety fallback
        return findLowestHouseNumberSequential(target);
    }
    
    /**
     * Sieve-based approach for Part 2.
     * For each elf e, add 11*e presents to houses e, 2e, 3e, ..., 50e
     * Complexity: O(H log H) but with better constant factor due to 50-house limit.
     */
    private int findLowestHouseNumberPart2Sieve(final int target) {
        // Estimate upper bound: similar to Part 1 but with 11x multiplier
        final int estimatedMax = Math.max(target / 11, 100_000);
        final int[] presents = new int[estimatedMax + 1];
        
        // For each elf e, add presents to houses e, 2e, 3e, ..., 50e
        for (int elf = 1; elf <= estimatedMax; elf++) {
            final int maxHouse = Math.min(elf * 50, estimatedMax);
            for (int house = elf; house <= maxHouse; house += elf) {
                presents[house] += elf * 11;
            }
        }
        
        // Find the first house that meets the target
        for (int house = 1; house <= estimatedMax; house++) {
            if (presents[house] >= target) {
                return house;
            }
        }
        
        // Fallback to sequential method
        return findLowestHouseNumberPart2Sequential(target);
    }
    
    /**
     * Fallback sequential method for Part 1 (original approach).
     * Used if sieve estimate was too low.
     */
    private int findLowestHouseNumberSequential(final int target) {
        int houseNumber = 1;
        
        while (true) {
            int presents = calculatePresents(houseNumber);
            if (presents >= target) {
                return houseNumber;
            }
            houseNumber++;
        }
    }
    
    /**
     * Fallback sequential method for Part 2 (original approach).
     * Used if sieve estimate was too low.
     */
    private int findLowestHouseNumberPart2Sequential(final int target) {
        int houseNumber = 1;
        
        while (true) {
            int presents = calculatePresentsPart2(houseNumber);
            if (presents >= target) {
                return houseNumber;
            }
            houseNumber++;
        }
    }
    
    /**
     * Calculate presents for a single house (Part 1).
     * Used as fallback if sieve estimate was too low.
     */
    private int calculatePresents(final int houseNumber) {
        int totalPresents = 0;
        final int sqrt = (int) Math.sqrt(houseNumber);
        
        for (int elf = 1; elf <= sqrt; elf++) {
            if (houseNumber % elf == 0) {
                totalPresents += elf * 10;
                final int pairedElf = houseNumber / elf;
                if (elf != pairedElf) {
                    totalPresents += pairedElf * 10;
                }
            }
        }
        
        return totalPresents;
    }
    
    /**
     * Calculate presents for a single house (Part 2).
     * Used as fallback if sieve estimate was too low.
     */
    private int calculatePresentsPart2(final int houseNumber) {
        int totalPresents = 0;
        final int sqrt = (int) Math.sqrt(houseNumber);
        
        for (int elf = 1; elf <= sqrt; elf++) {
            if (houseNumber % elf == 0) {
                if (houseNumber <= elf * 50) {
                    totalPresents += elf * 11;
                }
                
                final int pairedElf = houseNumber / elf;
                if (elf != pairedElf && houseNumber <= pairedElf * 50) {
                    totalPresents += pairedElf * 11;
                }
            }
        }
        
        return totalPresents;
    }
}