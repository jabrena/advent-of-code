package info.jab.aoc2015.day17;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;

import java.util.ArrayList;
import java.util.List;

public class Day17 implements Day<Integer> {
    
    private static final int TARGET_VOLUME = 150;
    
    @Override
    public Integer getPart1Result(String fileName) {
        var containers = ResourceLines.list(fileName, Integer::parseInt);
        return countCombinations(containers, TARGET_VOLUME, 0, 0);
    }
    
    @Override
    public Integer getPart2Result(String fileName) {
        var containers = ResourceLines.list(fileName, Integer::parseInt);
        var validCombinations = findAllValidCombinations(containers, TARGET_VOLUME);
        
        // Find minimum number of containers needed
        int minContainers = validCombinations.stream()
                .mapToInt(List::size)
                .min()
                .orElse(0);
        
        // Count combinations that use exactly the minimum number of containers
        return (int) validCombinations.stream()
                .filter(combination -> combination.size() == minContainers)
                .count();
    }
    
    private int countCombinations(List<Integer> containers, int target, int currentSum, int index) {
        if (currentSum == target) {
            return 1;
        }
        
        if (currentSum > target || index >= containers.size()) {
            return 0;
        }
        
        // Include current container
        int withCurrent = countCombinations(containers, target, currentSum + containers.get(index), index + 1);
        
        // Exclude current container
        int withoutCurrent = countCombinations(containers, target, currentSum, index + 1);
        
        return withCurrent + withoutCurrent;
    }
    
    private List<List<Integer>> findAllValidCombinations(List<Integer> containers, int target) {
        List<List<Integer>> validCombinations = new ArrayList<>();
        findCombinations(containers, target, 0, 0, new ArrayList<>(), validCombinations);
        return validCombinations;
    }
    
    private void findCombinations(List<Integer> containers, int target, int currentSum, int index, 
                                List<Integer> currentCombination, List<List<Integer>> validCombinations) {
        if (currentSum == target) {
            validCombinations.add(new ArrayList<>(currentCombination));
            return;
        }
        
        if (currentSum > target || index >= containers.size()) {
            return;
        }
        
        // Include current container
        currentCombination.add(containers.get(index));
        findCombinations(containers, target, currentSum + containers.get(index), index + 1, 
                        currentCombination, validCombinations);
        currentCombination.remove(currentCombination.size() - 1);
        
        // Exclude current container
        findCombinations(containers, target, currentSum, index + 1, currentCombination, validCombinations);
    }
}