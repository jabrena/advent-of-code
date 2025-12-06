package info.jab.aoc2015.day20;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

/**
 * Optimized version using sieve-based approach.
 * 
 * Instead of calculating divisors for each house individually (O(√H) per house),
 * we iterate through elves and add presents to all houses they visit.
 * 
 * Complexity:
 * - Part 1: O(H log H) - Each elf e visits H/e houses, sum of H/e for e=1..H is O(H log H)
 * - Part 2: O(H log H) - Similar but limited to 50 houses per elf
 * 
 * This is better than the current O(H×√H) approach for large H.
 * 
 * Expected improvement: ~2-3x faster for typical inputs (H ~ 800,000)
 */
public class InfiniteElvesAndInfiniteHousesOptimized implements Solver<Integer> {

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
        // Estimate upper bound: worst case is when house has all divisors up to itself
        // For house H, max presents ≈ H * 10 * (number of divisors)
        // Average number of divisors is O(log H), so we need roughly H ≈ target/10
        // Use a conservative upper bound
        final int estimatedMax = target / 10;
        final int[] presents = new int[estimatedMax + 1];
        
        // For each elf e, add presents to all houses that are multiples of e
        for (int elf = 1; elf <= estimatedMax; elf++) {
            for (int house = elf; house <= estimatedMax; house += elf) {
                presents[house] += elf * 10;
                
                // Early termination: if we found a house that meets the target, we can stop
                // But we need to check all smaller elves first to ensure correctness
                if (house <= estimatedMax && presents[house] >= target) {
                    // Continue to check if this is the first house meeting the target
                    // We'll scan after the loop to find the minimum
                }
            }
        }
        
        // Find the first house that meets the target
        for (int house = 1; house <= estimatedMax; house++) {
            if (presents[house] >= target) {
                return house;
            }
        }
        
        // If not found, fall back to original method (shouldn't happen with good estimate)
        throw new IllegalStateException("Target not found within estimated range");
    }
    
    /**
     * Sieve-based approach for Part 2.
     * For each elf e, add 11*e presents to houses e, 2e, 3e, ..., 50e
     * Complexity: O(H log H) but with better constant factor due to 50-house limit.
     */
    private int findLowestHouseNumberPart2Sieve(final int target) {
        // Estimate upper bound: similar to Part 1 but with 11x multiplier
        final int estimatedMax = target / 11;
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
        
        throw new IllegalStateException("Target not found within estimated range");
    }
}
