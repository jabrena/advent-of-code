package info.jab.aoc2015.day19;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MoleculeReplacementSolver implements Solver<Integer> {

    private static final String REPLACEMENT_SEPARATOR = " => ";
    private static final int MAX_STEPS = 1000;
    private static final int MAX_ATTEMPTS = 100;
    private static final String TARGET_MOLECULE = "e";

    private record ParsedInput(Map<String, List<String>> replacements, String molecule) {}
    
    private record Replacement(String from, String to) {}

    @Override
    public Integer solvePartOne(final String fileName) {
        ParsedInput input = parseInput(fileName);
        return countDistinctMolecules(input.molecule(), input.replacements());
    }

    @Override
    public Integer solvePartTwo(final String fileName) {
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
        List<Replacement> reverseReplacements = buildReverseReplacements(replacements);
        
        // Try multiple attempts with different replacement orderings
        // The greedy approach works best when replacements are tried in optimal order
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            int steps = attemptGreedyReduction(targetMolecule, reverseReplacements);
            if (steps > 0) {
                return steps;
            }
            
            // Rotate the list to try different ordering on next attempt
            if (attempt < MAX_ATTEMPTS - 1 && !reverseReplacements.isEmpty()) {
                reverseReplacements.add(reverseReplacements.remove(0));
            }
        }
        
        throw new IllegalStateException("Failed to find solution after " + MAX_ATTEMPTS + " attempts");
    }
    
    private List<Replacement> buildReverseReplacements(Map<String, List<String>> replacements) {
        List<Replacement> reverseReplacements = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : replacements.entrySet()) {
            String from = entry.getKey();
            for (String to : entry.getValue()) {
                reverseReplacements.add(new Replacement(to, from));
            }
        }
        // Sort by length (longest first), then alphabetically for deterministic behavior
        reverseReplacements.sort(
            Comparator.comparingInt((Replacement r) -> r.from().length())
                .reversed()
                .thenComparing(Replacement::from)
        );
        return reverseReplacements;
    }
    
    private int attemptGreedyReduction(String targetMolecule, List<Replacement> reverseReplacements) {
        String current = targetMolecule;
        int steps = 0;
        
        while (!current.equals(TARGET_MOLECULE) && steps < MAX_STEPS) {
            String previous = current;
            
            // Try each replacement in order (longest first, then alphabetically)
            for (Replacement replacement : reverseReplacements) {
                String product = replacement.from();
                int index = current.indexOf(product);
                if (index >= 0) {
                    String reactant = replacement.to();
                    // Replace first occurrence from left to right
                    current = current.substring(0, index) + reactant + current.substring(index + product.length());
                    steps++;
                    break;
                }
            }
            
            // If no replacement was made, we're stuck
            if (current.equals(previous)) {
                return -1;
            }
        }
        
        return current.equals(TARGET_MOLECULE) ? steps : -1;
    }
}
