package info.jab.aoc2016.day6;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.HashMap;
import java.util.Map;

/**
 * Solver for Day 6: Signals and Noise
 * Error-corrects messages by finding most/least frequent characters.
 */
public final class SignalsAndNoise implements Solver<String> {

    @Override
    public String solvePartOne(final String fileName) {
        var lines = ResourceLines.list(fileName);
        return errorCorrect(lines, true); // true = most frequent
    }

    @Override
    public String solvePartTwo(final String fileName) {
        var lines = ResourceLines.list(fileName);
        return errorCorrect(lines, false); // false = least frequent
    }
    
    private String errorCorrect(final java.util.List<String> lines, final boolean findMostFrequent) {
        if (lines.isEmpty()) {
            return "";
        }
        
        int messageLength = lines.get(0).length();
        StringBuilder result = new StringBuilder();
        
        // For each column position
        for (int col = 0; col < messageLength; col++) {
            Map<Character, Integer> charCount = new HashMap<>();
            
            // Count frequency of each character in this column
            for (String line : lines) {
                if (col < line.length()) {
                    char c = line.charAt(col);
                    charCount.put(c, charCount.getOrDefault(c, 0) + 1);
                }
            }
            
            // Find the most or least frequent character based on parameter
            char targetChar = findMostFrequent ? findMostFrequent(charCount) : findLeastFrequent(charCount);
            result.append(targetChar);
        }
        
        return result.toString();
    }
    
    private char findMostFrequent(final Map<Character, Integer> charCount) {
        char mostFrequent = ' ';
        int maxCount = 0;
        
        for (Map.Entry<Character, Integer> entry : charCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }
        
        return mostFrequent;
    }
    
    private char findLeastFrequent(final Map<Character, Integer> charCount) {
        char leastFrequent = ' ';
        int minCount = Integer.MAX_VALUE;
        
        for (Map.Entry<Character, Integer> entry : charCount.entrySet()) {
            if (entry.getValue() < minCount) {
                minCount = entry.getValue();
                leastFrequent = entry.getKey();
            }
        }
        
        return leastFrequent;
    }
}

