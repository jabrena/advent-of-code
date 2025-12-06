package info.jab.aoc2015.day17;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Solver for container combination problems.
 * Uses functional programming principles:
 * - Pure functions for calculations
 * - Immutable data structures
 * - Functional recursion with memoization
 */
public final class ContainerCombinationSolver implements Solver<Integer> {
    
    private static final int TARGET_VOLUME = 150;
    
    @Override
    public Integer solvePartOne(final String fileName) {
        final List<Integer> containers = ResourceLines.list(fileName, Integer::parseInt);
        return countCombinationsMemoized(containers, TARGET_VOLUME, 0, 0, new HashMap<>());
    }
    
    @Override
    public Integer solvePartTwo(final String fileName) {
        final List<Integer> containers = ResourceLines.list(fileName, Integer::parseInt);
        final List<List<Integer>> validCombinations = findAllValidCombinations(containers, TARGET_VOLUME);
        
        // Find minimum number of containers needed using stream
        final int minContainers = validCombinations.stream()
                .mapToInt(List::size)
                .min()
                .orElse(0);
        
        // Count combinations that use exactly the minimum number of containers
        return (int) validCombinations.stream()
                .filter(combination -> combination.size() == minContainers)
                .count();
    }
    
    /**
     * Pure function: counts combinations using memoization.
     * Uses functional recursion with immutable memo map.
     */
    private int countCombinationsMemoized(
            final List<Integer> containers,
            final int target,
            final int currentSum,
            final int index,
            final Map<String, Integer> memo) {
        
        final String key = currentSum + "," + index;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        
        if (currentSum == target) {
            return 1;
        }
        
        if (currentSum > target || index >= containers.size()) {
            return 0;
        }
        
        // Include current container
        final int withCurrent = countCombinationsMemoized(
                containers,
                target,
                currentSum + containers.get(index),
                index + 1,
                memo
        );
        
        // Exclude current container
        final int withoutCurrent = countCombinationsMemoized(
                containers,
                target,
                currentSum,
                index + 1,
                memo
        );
        
        final int result = withCurrent + withoutCurrent;
        memo.put(key, result);
        return result;
    }
    
    /**
     * Pure function: finds all valid combinations functionally.
     */
    private List<List<Integer>> findAllValidCombinations(final List<Integer> containers, final int target) {
        return findCombinations(containers, target, 0, 0, List.of());
    }
    
    /**
     * Pure recursive function: generates combinations using immutable lists.
     */
    private List<List<Integer>> findCombinations(
            final List<Integer> containers,
            final int target,
            final int currentSum,
            final int index,
            final List<Integer> currentCombination) {
        
        if (currentSum == target) {
            return List.of(currentCombination);
        }
        
        if (currentSum > target || index >= containers.size()) {
            return List.of();
        }
        
        // Include current container
        final List<Integer> withCurrent = append(currentCombination, containers.get(index));
        final List<List<Integer>> withCurrentResults = findCombinations(
                containers,
                target,
                currentSum + containers.get(index),
                index + 1,
                withCurrent
        );
        
        // Exclude current container
        final List<List<Integer>> withoutCurrentResults = findCombinations(
                containers,
                target,
                currentSum,
                index + 1,
                currentCombination
        );
        
        // Combine results functionally
        return java.util.stream.Stream.concat(
                withCurrentResults.stream(),
                withoutCurrentResults.stream()
        ).toList();
    }
    
    /**
     * Pure function: creates new list with appended element (immutable).
     */
    private List<Integer> append(final List<Integer> list, final int value) {
        return java.util.stream.Stream.concat(list.stream(), java.util.stream.Stream.of(value))
                .toList();
    }
}
