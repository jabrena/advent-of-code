package info.jab.aoc2015.day19;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;

import java.util.ArrayList;
import java.util.Comparator;
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

    private static final String REPLACEMENT_SEPARATOR = " => ";
    private static final int MAX_ATTEMPTS = 1000;
    private static final int MAX_STEPS = 1000;

    private record ParsedInput(Map<String, List<String>> replacements, String molecule) {}

    @Override
    public Integer getPart1Result(String fileName) {
        ParsedInput input = parseInput(fileName);
        return countDistinctMolecules(input.molecule(), input.replacements());
    }

    @Override
    public Integer getPart2Result(String fileName) {
        ParsedInput input = parseInput(fileName);
        return findMinimumSteps(input.molecule(), input.replacements());
    }

    private ParsedInput parseInput(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        Map<String, List<String>> replacements = new HashMap<>();
        String molecule = "";
        
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (trimmedLine.isEmpty()) {
                continue;
            }
            
            if (trimmedLine.contains(REPLACEMENT_SEPARATOR)) {
                String[] parts = trimmedLine.split(REPLACEMENT_SEPARATOR);
                String from = parts[0];
                String to = parts[1];
                replacements.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            } else {
                molecule = trimmedLine;
            }
        }
        
        return new ParsedInput(replacements, molecule);
    }
    
    private int countDistinctMolecules(String molecule, Map<String, List<String>> replacements) {
        Set<String> distinctMolecules = new HashSet<>();
        
        for (Map.Entry<String, List<String>> entry : replacements.entrySet()) {
            String from = entry.getKey();
            List<String> toList = entry.getValue();
            
            // Find all positions where 'from' appears in the molecule
            for (int i = 0; i <= molecule.length() - from.length(); i++) {
                if (molecule.substring(i, i + from.length()).equals(from)) {
                    // For each possible replacement
                    for (String to : toList) {
                        String newMolecule = molecule.substring(0, i) + to + molecule.substring(i + from.length());
                        distinctMolecules.add(newMolecule);
                    }
                }
            }
        }
        
        return distinctMolecules.size();
    }
    
    private int findMinimumSteps(String targetMolecule, Map<String, List<String>> replacements) {
        Map<String, String> reverseReplacements = buildReverseReplacements(replacements);
        return findStepsWithDeterministicGreedy(targetMolecule, reverseReplacements);
    }
    
    private Map<String, String> buildReverseReplacements(Map<String, List<String>> replacements) {
        Map<String, String> reverseReplacements = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : replacements.entrySet()) {
            String from = entry.getKey();
            for (String to : entry.getValue()) {
                reverseReplacements.put(to, from);
            }
        }
        return reverseReplacements;
    }
    
    private int findStepsWithDeterministicGreedy(String targetMolecule, Map<String, String> reverseReplacements) {
        // Sort replacements by length (longest first) for deterministic greedy approach
        // This heuristic works well: longer replacements reduce the molecule size faster
        List<String> sortedKeys = new ArrayList<>(reverseReplacements.keySet());
        sortedKeys.sort(Comparator.comparingInt(String::length).reversed());
        
        // Try multiple attempts with slight variations (still needed for some edge cases)
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            int steps = attemptGreedyReduction(targetMolecule, reverseReplacements, sortedKeys);
            if (steps > 0) {
                return steps;
            }
            
            // On subsequent attempts, try with slightly different ordering
            // (rotate the list to try different starting points)
            if (attempt < MAX_ATTEMPTS - 1 && !sortedKeys.isEmpty()) {
                sortedKeys.add(sortedKeys.remove(0));
            }
        }
        
        throw new IllegalStateException("Failed to find solution after " + MAX_ATTEMPTS + " attempts");
    }
    
    private int attemptGreedyReduction(String targetMolecule, Map<String, String> reverseReplacements, List<String> sortedKeys) {
        String current = targetMolecule;
        int steps = 0;
        
        while (!current.equals("e") && steps < MAX_STEPS) {
            String previous = current;
            
            // Try each replacement in order (longest first)
            for (String product : sortedKeys) {
                if (current.contains(product)) {
                    String reactant = reverseReplacements.get(product);
                    // Replace first occurrence (from left to right)
                    current = current.replaceFirst(Pattern.quote(product), reactant);
                    steps++;
                    break;
                }
            }
            
            // If no replacement was made, we're stuck
            if (current.equals(previous)) {
                return -1;
            }
        }
        
        return current.equals("e") ? steps : -1;
    }
}
