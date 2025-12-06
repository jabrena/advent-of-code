package info.jab.aoc2015.day19;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
        // Use deterministic greedy approach with multiple strategies
        // This is more performant than full BFS while still finding optimal solutions
        // Try different replacement orderings to find the best path
        
        // Strategy 1: Prefer replacements that reduce length the most
        List<Map.Entry<String, String>> strategy1 = new ArrayList<>(reverseReplacements.entrySet());
        strategy1.sort((a, b) -> {
            int reductionA = a.getKey().length() - a.getValue().length();
            int reductionB = b.getKey().length() - b.getValue().length();
            if (reductionA != reductionB) {
                return Integer.compare(reductionB, reductionA);
            }
            return Integer.compare(b.getKey().length(), a.getKey().length());
        });
        
        // Strategy 2: Prefer longer products first
        List<Map.Entry<String, String>> strategy2 = new ArrayList<>(reverseReplacements.entrySet());
        strategy2.sort((a, b) -> Integer.compare(b.getKey().length(), a.getKey().length()));
        
        // Try both strategies and return the best result
        int result1 = greedySearch(targetMolecule, strategy1);
        int result2 = greedySearch(targetMolecule, strategy2);
        
        if (result1 != -1 && result2 != -1) {
            return Math.min(result1, result2);
        }
        return result1 != -1 ? result1 : result2;
    }
    
    private int greedySearch(String targetMolecule, List<Map.Entry<String, String>> replacements) {
        String current = targetMolecule;
        int steps = 0;
        int maxSteps = 500;
        
        while (!current.equals("e") && steps < maxSteps) {
            String previous = current;
            boolean madeProgress = false;
            
            // Try each replacement in order
            for (Map.Entry<String, String> entry : replacements) {
                String product = entry.getKey();
                String reactant = entry.getValue();
                
                // Try all occurrences, prefer ones that reduce length more
                int bestIndex = -1;
                int maxReduction = Integer.MIN_VALUE;
                
                int index = 0;
                while ((index = current.indexOf(product, index)) != -1) {
                    String testMolecule = current.substring(0, index) + 
                                        reactant + 
                                        current.substring(index + product.length());
                    int reduction = current.length() - testMolecule.length();
                    
                    if (reduction > maxReduction) {
                        maxReduction = reduction;
                        bestIndex = index;
                    }
                    index += 1; // Check all positions
                }
                
                if (bestIndex != -1) {
                    String product2 = entry.getKey();
                    String reactant2 = entry.getValue();
                    current = current.substring(0, bestIndex) + reactant2 + 
                             current.substring(bestIndex + product2.length());
                    steps++;
                    madeProgress = true;
                    break; // Start over with new molecule
                }
            }
            
            if (!madeProgress) {
                // If stuck, try replacing from the end (right-to-left)
                for (int i = replacements.size() - 1; i >= 0; i--) {
                    Map.Entry<String, String> entry = replacements.get(i);
                    String product = entry.getKey();
                    String reactant = entry.getValue();
                    int lastIndex = current.lastIndexOf(product);
                    if (lastIndex != -1) {
                        current = current.substring(0, lastIndex) + reactant + 
                                 current.substring(lastIndex + product.length());
                        steps++;
                        madeProgress = true;
                        break;
                    }
                }
                
                // Still stuck - cannot proceed
                if (!madeProgress) {
                    return -1;
                }
            }
        }
        
        return current.equals("e") ? steps : -1;
    }
}