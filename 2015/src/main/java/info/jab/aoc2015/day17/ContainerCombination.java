package info.jab.aoc2015.day17;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;
import info.jab.aoc.Trampoline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Solver for container combination problems.
 * Uses functional programming principles:
 * - Pure functions for calculations
 * - Immutable data structures
 * - Functional recursion with memoization
 */
public final class ContainerCombination implements Solver<Integer> {
    
    private static final int TARGET_VOLUME = 150;
    
    @Override
    public Integer solvePartOne(final String fileName) {
        final List<Integer> containers = ResourceLines.list(fileName, Integer::parseInt);
        return Trampoline.run(countCombinationsTrampoline(
                containers, TARGET_VOLUME, 0, 0, new HashMap<>()));
    }
    
    @Override
    public Integer solvePartTwo(final String fileName) {
        final List<Integer> containers = ResourceLines.list(fileName, Integer::parseInt);
        final List<List<Integer>> validCombinations = Trampoline.run(
                findCombinationsTrampoline(containers, TARGET_VOLUME, 0, 0, List.of()));
        
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
     * Creates a trampoline computation for counting combinations with memoization.
     * Uses tail-recursive pattern converted to trampoline for stack safety.
     */
    private Trampoline<Integer> countCombinationsTrampoline(
            final List<Integer> containers,
            final int target,
            final int currentSum,
            final int index,
            final Map<String, Integer> memo) {
        
        final String key = currentSum + "," + index;
        if (memo.containsKey(key)) {
            return new Trampoline.Done<>(memo.get(key));
        }
        
        if (currentSum == target) {
            memo.put(key, 1);
            return new Trampoline.Done<>(1);
        }
        
        if (currentSum > target || index >= containers.size()) {
            memo.put(key, 0);
            return new Trampoline.Done<>(0);
        }
        
        // Create trampolines for both branches
        final Trampoline<Integer> withCurrentTrampoline = countCombinationsTrampoline(
                containers,
                target,
                currentSum + containers.get(index),
                index + 1,
                memo
        );
        
        final Trampoline<Integer> withoutCurrentTrampoline = countCombinationsTrampoline(
                containers,
                target,
                currentSum,
                index + 1,
                memo
        );
        
        // Combine results using a helper that evaluates both trampolines
        return combineCountTrampolines(key, withCurrentTrampoline, withoutCurrentTrampoline, memo);
    }
    
    /**
     * Combines two count trampolines and memoizes the result.
     */
    private Trampoline<Integer> combineCountTrampolines(
            final String key,
            final Trampoline<Integer> trampoline1,
            final Trampoline<Integer> trampoline2,
            final Map<String, Integer> memo) {
        
        // If both are Done, combine immediately
        if (trampoline1 instanceof Trampoline.Done<Integer>(var result1) 
                && trampoline2 instanceof Trampoline.Done<Integer>(var result2)) {
            final int result = result1 + result2;
            memo.put(key, result);
            return new Trampoline.Done<>(result);
        }
        
        // Otherwise, continue evaluation
        return new Trampoline.More<>(() -> {
            final Trampoline<Integer> evaluated1 = trampoline1 instanceof Trampoline.More<Integer>(var compute1) 
                    ? compute1.get() 
                    : trampoline1;
            final Trampoline<Integer> evaluated2 = trampoline2 instanceof Trampoline.More<Integer>(var compute2) 
                    ? compute2.get() 
                    : trampoline2;
            return combineCountTrampolines(key, evaluated1, evaluated2, memo);
        });
    }
    
    /**
     * Creates a trampoline computation for finding all valid combinations.
     * Uses tail-recursive pattern converted to trampoline for stack safety.
     */
    private Trampoline<List<List<Integer>>> findCombinationsTrampoline(
            final List<Integer> containers,
            final int target,
            final int currentSum,
            final int index,
            final List<Integer> currentCombination) {
        
        if (currentSum == target) {
            return new Trampoline.Done<>(List.of(currentCombination));
        }
        
        if (currentSum > target || index >= containers.size()) {
            return new Trampoline.Done<>(List.of());
        }
        
        // Include current container
        final List<Integer> withCurrent = append(currentCombination, containers.get(index));
        final Trampoline<List<List<Integer>>> withCurrentTrampoline = findCombinationsTrampoline(
                containers,
                target,
                currentSum + containers.get(index),
                index + 1,
                withCurrent
        );
        
        // Exclude current container
        final Trampoline<List<List<Integer>>> withoutCurrentTrampoline = findCombinationsTrampoline(
                containers,
                target,
                currentSum,
                index + 1,
                currentCombination
        );
        
        // Combine results functionally
        return combineListTrampolines(withCurrentTrampoline, withoutCurrentTrampoline);
    }
    
    /**
     * Combines two list trampolines by concatenating their results.
     */
    private Trampoline<List<List<Integer>>> combineListTrampolines(
            final Trampoline<List<List<Integer>>> trampoline1,
            final Trampoline<List<List<Integer>>> trampoline2) {
        
        // If both are Done, combine immediately
        if (trampoline1 instanceof Trampoline.Done<List<List<Integer>>>(var result1) 
                && trampoline2 instanceof Trampoline.Done<List<List<Integer>>>(var result2)) {
            return new Trampoline.Done<>(
                    Stream.concat(
                            result1.stream(),
                            result2.stream()
                    ).toList()
            );
        }
        
        // Otherwise, continue evaluation
        return new Trampoline.More<>(() -> {
            final Trampoline<List<List<Integer>>> evaluated1 = trampoline1 instanceof Trampoline.More<List<List<Integer>>>(var compute1) 
                    ? compute1.get() 
                    : trampoline1;
            final Trampoline<List<List<Integer>>> evaluated2 = trampoline2 instanceof Trampoline.More<List<List<Integer>>>(var compute2) 
                    ? compute2.get() 
                    : trampoline2;
            return combineListTrampolines(evaluated1, evaluated2);
        });
    }
    
    /**
     * Pure function: creates new list with appended element (immutable).
     */
    private List<Integer> append(final List<Integer> list, final int value) {
        return Stream.concat(list.stream(), Stream.of(value))
                .toList();
    }
}
