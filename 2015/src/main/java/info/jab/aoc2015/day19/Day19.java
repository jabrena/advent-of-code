package info.jab.aoc2015.day19;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

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
        
        // For this specific problem, there's a mathematical pattern
        // Each replacement generally adds elements, so we can count backwards
        return findStepsWithRandomizedGreedy(targetMolecule, reverseReplacements);
    }
    
    private int findStepsWithRandomizedGreedy(String targetMolecule, Map<String, String> reverseReplacements) {
        // Try multiple times with different orderings to find the solution
        for (int attempt = 0; attempt < 1000; attempt++) {
            String current = targetMolecule;
            int steps = 0;
            
            // Create a shuffled list of replacement keys for this attempt
            List<String> keys = new ArrayList<>(reverseReplacements.keySet());
            java.util.Collections.shuffle(keys);
            
            while (!current.equals("e") && steps < 1000) {
                String previous = current;
                
                // Try each replacement in the shuffled order
                for (String product : keys) {
                    if (current.contains(product)) {
                        String reactant = reverseReplacements.get(product);
                        current = current.replaceFirst(Pattern.quote(product), reactant);
                        steps++;
                        break;
                    }
                }
                
                // If no replacement was made, we're stuck
                if (current.equals(previous)) {
                    break;
                }
            }
            
            if (current.equals("e")) {
                return steps;
            }
        }
        
        return -1; // Failed to find solution
    }
}
