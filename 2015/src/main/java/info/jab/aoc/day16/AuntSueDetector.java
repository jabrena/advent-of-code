package info.jab.aoc.day16;

import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;

import java.util.*;
import java.util.regex.Pattern;

public class AuntSueDetector implements Solver<Integer> {
    
    // The MFCSAM analysis results
    private static final Map<String, Integer> MFCSAM_ANALYSIS = Map.of(
        "children", 3,
        "cats", 7,
        "samoyeds", 2,
        "pomeranians", 3,
        "akitas", 0,
        "vizslas", 0,
        "goldfish", 5,
        "trees", 3,
        "cars", 2,
        "perfumes", 1
    );

    private static final Pattern SUE_PATTERN = Pattern.compile("Sue (\\d+): (.*)");
    private static final Pattern COMPOUND_PATTERN = Pattern.compile("(\\w+): (\\d+)");

    @Override
    public Integer solvePartOne(String fileName) {
        var lines = ResourceLines.list(fileName);
        
        for (String line : lines) {
            var sueMatcher = SUE_PATTERN.matcher(line);
            if (sueMatcher.matches()) {
                int sueNumber = Integer.parseInt(sueMatcher.group(1));
                String compounds = sueMatcher.group(2);
                
                if (matchesAnalysis(compounds, false)) {
                    return sueNumber;
                }
            }
        }
        
        return -1; // Not found
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var lines = ResourceLines.list(fileName);
        
        for (String line : lines) {
            var sueMatcher = SUE_PATTERN.matcher(line);
            if (sueMatcher.matches()) {
                int sueNumber = Integer.parseInt(sueMatcher.group(1));
                String compounds = sueMatcher.group(2);
                
                if (matchesAnalysis(compounds, true)) {
                    return sueNumber;
                }
            }
        }
        
        return -1; // Not found
    }
    
    private boolean matchesAnalysis(String compounds, boolean isPart2) {
        var compoundMatcher = COMPOUND_PATTERN.matcher(compounds);
        
        while (compoundMatcher.find()) {
            String compound = compoundMatcher.group(1);
            int value = Integer.parseInt(compoundMatcher.group(2));
            
            if (!MFCSAM_ANALYSIS.containsKey(compound)) {
                continue; // Skip unknown compounds
            }
            
            int expectedValue = MFCSAM_ANALYSIS.get(compound);
            
            if (isPart2) {
                // Part 2: cats and trees should be greater, pomeranians and goldfish should be fewer
                if ("cats".equals(compound) || "trees".equals(compound)) {
                    if (value <= expectedValue) {
                        return false;
                    }
                } else if ("pomeranians".equals(compound) || "goldfish".equals(compound)) {
                    if (value >= expectedValue) {
                        return false;
                    }
                } else {
                    if (value != expectedValue) {
                        return false;
                    }
                }
            } else {
                // Part 1: exact match
                if (value != expectedValue) {
                    return false;
                }
            }
        }
        
        return true;
    }
}