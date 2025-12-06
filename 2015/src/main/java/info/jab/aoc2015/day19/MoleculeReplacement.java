package info.jab.aoc2015.day19;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class MoleculeReplacement implements Solver<Integer> {

    private static final String REPLACEMENT_SEPARATOR = " => ";
    private static final int MAX_STEPS = 1000;
    private static final int MAX_ATTEMPTS = 100;
    private static final String TARGET_MOLECULE = "e";


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

    /**
     * Pure function: parses input using stream API.
     */
    private ParsedInput parseInput(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        
        final Map<String, List<String>> replacements = lines.stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty() && line.contains(REPLACEMENT_SEPARATOR))
                .map(line -> {
                    final String[] parts = line.split(REPLACEMENT_SEPARATOR);
                    return new Replacement(parts[0], parts[1]);
                })
                .collect(java.util.stream.Collectors.groupingBy(
                        Replacement::from,
                        java.util.stream.Collectors.mapping(Replacement::to, java.util.stream.Collectors.toList())
                ));
        
        final String molecule = lines.stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty() && !line.contains(REPLACEMENT_SEPARATOR))
                .findFirst()
                .orElse("");
        
        return new ParsedInput(replacements, molecule);
    }
    
    /**
     * Pure function: counts distinct molecules using stream API.
     */
    private int countDistinctMolecules(final String molecule, final Map<String, List<String>> replacements) {
        return replacements.entrySet().stream()
                .flatMap(entry -> {
                    final String from = entry.getKey();
                    final List<String> toList = entry.getValue();
                    
                    // Find all positions where 'from' appears in the molecule
                    return IntStream.rangeClosed(0, molecule.length() - from.length())
                            .filter(i -> molecule.substring(i, i + from.length()).equals(from))
                            .boxed()
                            .flatMap(i -> toList.stream()
                                    .map(to -> molecule.substring(0, i) + to + molecule.substring(i + from.length()))
                            );
                })
                .collect(java.util.stream.Collectors.toSet())
                .size();
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
    
    /**
     * Builds reverse replacements using stream API.
     * Returns a mutable list to allow rotation for different ordering attempts.
     */
    private List<Replacement> buildReverseReplacements(final Map<String, List<String>> replacements) {
        return new ArrayList<>(replacements.entrySet().stream()
                .flatMap(entry -> {
                    final String from = entry.getKey();
                    return entry.getValue().stream()
                            .map(to -> new Replacement(to, from));
                })
                .sorted(Comparator
                        .comparingInt((Replacement r) -> r.from().length())
                        .reversed()
                        .thenComparing(Replacement::from)
                )
                .toList());
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
