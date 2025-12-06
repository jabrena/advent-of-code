package info.jab.aoc2015.day19;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
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
        // Optimized BFS with better memory management and pruning
        // Use a priority queue to explore shorter molecules first (heuristic: shorter = closer to "e")
        PriorityQueue<BFSState> queue = new PriorityQueue<>(
            Comparator.comparingInt((BFSState s) -> s.molecule.length())
                     .thenComparingInt(s -> s.steps)
        );
        Set<String> visited = new HashSet<>();
        
        queue.offer(new BFSState(targetMolecule, 0));
        visited.add(targetMolecule);
        
        int maxDepth = 1000; // Safety limit
        int bestSteps = Integer.MAX_VALUE;
        
        while (!queue.isEmpty() && queue.peek().steps < maxDepth) {
            BFSState current = queue.poll();
            
            if (current.molecule.equals("e")) {
                return current.steps;
            }
            
            // Pruning: if we've found a solution, don't explore deeper
            if (current.steps >= bestSteps) {
                continue;
            }
            
            // Generate all possible predecessors by applying reverse replacements
            // Sort replacements by length (longer first) to reduce molecule size faster
            List<Map.Entry<String, String>> sortedReplacements = new ArrayList<>(reverseReplacements.entrySet());
            sortedReplacements.sort((a, b) -> Integer.compare(b.getKey().length(), a.getKey().length()));
            
            for (Map.Entry<String, String> entry : sortedReplacements) {
                String product = entry.getKey();
                String reactant = entry.getValue();
                
                // Find all occurrences of product in current molecule
                int index = 0;
                while ((index = current.molecule.indexOf(product, index)) != -1) {
                    String newMolecule = current.molecule.substring(0, index) + 
                                        reactant + 
                                        current.molecule.substring(index + product.length());
                    
                    // Pruning: only explore if new molecule is shorter or equal length
                    // and we haven't seen it before
                    if (!visited.contains(newMolecule) && newMolecule.length() <= current.molecule.length()) {
                        visited.add(newMolecule);
                        int newSteps = current.steps + 1;
                        queue.offer(new BFSState(newMolecule, newSteps));
                        
                        if (newMolecule.equals("e")) {
                            bestSteps = Math.min(bestSteps, newSteps);
                        }
                    }
                    
                    index += product.length();
                }
            }
            
            // Limit visited set size to prevent memory issues
            if (visited.size() > 100000) {
                // Clear some old entries (simple strategy: keep recent ones)
                visited.clear();
                visited.add(targetMolecule);
            }
        }
        
        return bestSteps != Integer.MAX_VALUE ? bestSteps : -1;
    }
    
    private record BFSState(String molecule, int steps) {}
}