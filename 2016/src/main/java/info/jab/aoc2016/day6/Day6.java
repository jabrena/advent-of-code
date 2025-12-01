package info.jab.aoc2016.day6;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.HashMap;
import java.util.Map;

/**
 * https://adventofcode.com/2016/day/6
 * Day 6: Signals and Noise
 */
public class Day6 implements Day<String> {

    @Override
    public String getPart1Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        return errorCorrect(lines, true); // true = most frequent
    }

    @Override
    public String getPart2Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        return errorCorrect(lines, false); // false = least frequent
    }
    
    private String errorCorrect(java.util.List<String> lines, boolean findMostFrequent) {
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
    
    private char findMostFrequent(Map<Character, Integer> charCount) {
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
    
    private char findLeastFrequent(Map<Character, Integer> charCount) {
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