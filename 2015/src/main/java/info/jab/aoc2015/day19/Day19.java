package info.jab.aoc2015.day19;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * https://adventofcode.com/2015/day/19
 */
public class Day19 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        
        // Parse replacements and molecule
        Map<String, List<String>> replacements = new HashMap<>();
        String molecule = "";
        
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            
            if (line.contains(" => ")) {
                String[] parts = line.split(" => ");
                String from = parts[0];
                String to = parts[1];
                
                replacements.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            } else {
                molecule = line.trim();
            }
        }
        
        return countDistinctMolecules(molecule, replacements);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        
        // Parse replacements and molecule
        Map<String, List<String>> replacements = new HashMap<>();
        String targetMolecule = "";
        
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            
            if (line.contains(" => ")) {
                String[] parts = line.split(" => ");
                String from = parts[0];
                String to = parts[1];
                
                replacements.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            } else {
                targetMolecule = line.trim();
            }
        }
        
        return findMinimumSteps(targetMolecule, replacements);
    }
    
    private int countDistinctMolecules(String molecule, Map<String, List<String>> replacements) {
        Set<String> distinctMolecules = new HashSet<>();
        
        // For each replacement rule
        for (Map.Entry<String, List<String>> entry : replacements.entrySet()) {
            String from = entry.getKey();
            List<String> toList = entry.getValue();
            
            // Find all positions where 'from' appears in the molecule
            for (int i = 0; i <= molecule.length() - from.length(); i++) {
                if (molecule.substring(i, i + from.length()).equals(from)) {
                    // For each possible replacement
                    for (String to : toList) {
                        // Create new molecule by replacing at this position
                        String newMolecule = molecule.substring(0, i) + to + molecule.substring(i + from.length());
                        distinctMolecules.add(newMolecule);
                    }
                }
            }
        }
        
        return distinctMolecules.size();
    }
    
    private int findMinimumSteps(String targetMolecule, Map<String, List<String>> replacements) {
        // Create reverse replacements (from product back to reactant)
        Map<String, String> reverseReplacements = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : replacements.entrySet()) {
            String from = entry.getKey();
            for (String to : entry.getValue()) {
                reverseReplacements.put(to, from);
            }
        }
        
        // Use BFS to find the minimum steps - guaranteed optimal solution
        return findStepsWithBFS(targetMolecule, reverseReplacements);
    }
    
    private int findStepsWithBFS(String targetMolecule, Map<String, String> reverseReplacements) {
        // Use Iterative Deepening DFS (IDDFS) for memory efficiency
        // This finds optimal solution with O(d) memory instead of O(b^d) for BFS
        // where d is depth and b is branching factor
        
        // Sort replacements by length (longer first) - heuristic: prefer larger reductions
        List<Map.Entry<String, String>> sortedReplacements = new ArrayList<>(reverseReplacements.entrySet());
        sortedReplacements.sort((a, b) -> {
            int lenDiff = Integer.compare(b.getKey().length(), a.getKey().length());
            if (lenDiff != 0) return lenDiff;
            return Integer.compare(a.getValue().length(), b.getValue().length());
        });
        
        // Iterative deepening: try depth 1, 2, 3, ... until solution found
        for (int maxDepth = 1; maxDepth <= 500; maxDepth++) {
            Set<String> visited = new HashSet<>();
            int result = depthLimitedSearch(targetMolecule, "e", sortedReplacements, maxDepth, visited);
            if (result != -1) {
                return result;
            }
        }
        
        return -1; // No solution found within depth limit
    }
    
    private int depthLimitedSearch(String current, String target, List<Map.Entry<String, String>> replacements, 
                                   int remainingDepth, Set<String> visited) {
        if (current.equals(target)) {
            return 0; // Found target
        }
        
        if (remainingDepth == 0) {
            return -1; // Depth limit reached
        }
        
        // Pruning: don't revisit states at same or greater depth
        String stateKey = current + ":" + remainingDepth;
        if (visited.contains(stateKey)) {
            return -1;
        }
        visited.add(stateKey);
        
        // Try each replacement (already sorted by preference)
        for (Map.Entry<String, String> entry : replacements) {
            String product = entry.getKey();
            String reactant = entry.getValue();
            
            // Find all occurrences of product in current molecule
            int index = 0;
            while ((index = current.indexOf(product, index)) != -1) {
                String newMolecule = current.substring(0, index) + 
                                    reactant + 
                                    current.substring(index + product.length());
                
                // Pruning: only explore if new molecule is shorter (getting closer to "e")
                if (newMolecule.length() < current.length()) {
                    int result = depthLimitedSearch(newMolecule, target, replacements, remainingDepth - 1, visited);
                    if (result != -1) {
                        return result + 1;
                    }
                }
                
                index += product.length();
            }
        }
        
        return -1; // No solution found at this depth
    }
}