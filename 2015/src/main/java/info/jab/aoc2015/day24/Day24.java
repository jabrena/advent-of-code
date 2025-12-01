package info.jab.aoc2015.day24;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.*;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2015/day/24
 */
public class Day24 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        List<Integer> packages = ResourceLines.list(fileName)
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        
        int totalWeight = packages.stream().mapToInt(Integer::intValue).sum();
        int targetWeight = totalWeight / 3;
        
        // Find all combinations that sum to targetWeight
        List<List<Integer>> validCombinations = findCombinations(packages, targetWeight);
        
        // Find minimum package count
        int minPackageCount = validCombinations.stream()
                .mapToInt(List::size)
                .min()
                .orElse(Integer.MAX_VALUE);
        
        // Filter combinations with minimum package count
        List<List<Integer>> minCombinations = validCombinations.stream()
                .filter(combo -> combo.size() == minPackageCount)
                .collect(Collectors.toList());
        
        // Find minimum quantum entanglement
        return minCombinations.stream()
                .mapToLong(this::calculateQuantumEntanglement)
                .min()
                .orElse(Long.MAX_VALUE);
    }

    @Override
    public Long getPart2Result(String fileName) {
        List<Integer> packages = ResourceLines.list(fileName)
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        
        int totalWeight = packages.stream().mapToInt(Integer::intValue).sum();
        int targetWeight = totalWeight / 4; // Divide into 4 groups instead of 3
        
        // Find all combinations that sum to targetWeight
        List<List<Integer>> validCombinations = findCombinations(packages, targetWeight);
        
        // Find minimum package count
        int minPackageCount = validCombinations.stream()
                .mapToInt(List::size)
                .min()
                .orElse(Integer.MAX_VALUE);
        
        // Filter combinations with minimum package count
        List<List<Integer>> minCombinations = validCombinations.stream()
                .filter(combo -> combo.size() == minPackageCount)
                .collect(Collectors.toList());
        
        // Find minimum quantum entanglement
        return minCombinations.stream()
                .mapToLong(this::calculateQuantumEntanglement)
                .min()
                .orElse(Long.MAX_VALUE);
    }
    
    private List<List<Integer>> findCombinations(List<Integer> packages, int targetWeight) {
        List<List<Integer>> result = new ArrayList<>();
        findCombinationsRecursive(packages, targetWeight, 0, new ArrayList<>(), result);
        return result;
    }
    
    private void findCombinationsRecursive(List<Integer> packages, int remainingWeight, 
                                         int startIndex, List<Integer> currentCombination, 
                                         List<List<Integer>> result) {
        if (remainingWeight == 0) {
            result.add(new ArrayList<>(currentCombination));
            return;
        }
        
        if (remainingWeight < 0 || startIndex >= packages.size()) {
            return;
        }
        
        // Include current package
        currentCombination.add(packages.get(startIndex));
        findCombinationsRecursive(packages, remainingWeight - packages.get(startIndex), 
                                startIndex + 1, currentCombination, result);
        currentCombination.remove(currentCombination.size() - 1);
        
        // Exclude current package
        findCombinationsRecursive(packages, remainingWeight, startIndex + 1, 
                                currentCombination, result);
    }
    
    private long calculateQuantumEntanglement(List<Integer> packages) {
        return packages.stream()
                .mapToLong(Integer::longValue)
                .reduce(1L, (a, b) -> a * b);
    }
}