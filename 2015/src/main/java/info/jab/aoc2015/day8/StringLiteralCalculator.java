package info.jab.aoc2015.day8;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

class StringLiteralCalculator implements Solver<Integer> {
    
    private int calculateCodeLength(String input) {
        return input.length();
    }
    
    private int calculateMemoryLength(String input) {
        // Remove quotes from start and end
        String content = input.substring(1, input.length() - 1);
        
        int length = 0;
        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == '\\') {
                if (i + 1 < content.length()) {
                    char nextChar = content.charAt(i + 1);
                    if (nextChar == '\\' || nextChar == '"') {
                        // For \\ or \"
                        length++;
                        i++;
                    } else if (nextChar == 'x' && i + 3 < content.length()) {
                        // For \x followed by two hexadecimal characters
                        length++;
                        i += 3;
                    }
                }
            } else {
                length++;
            }
        }
        return length;
    }
    
    public int calculateTotalDifference(List<String> strings) {
        int totalCode = strings.stream()
                .mapToInt(this::calculateCodeLength)
                .sum();
                
        int totalMemory = strings.stream()
                .mapToInt(this::calculateMemoryLength)
                .sum();
        
        return totalCode - totalMemory;
    }

    // Part 2


    private String encodeString2(String input) {
        final Map<Character, String> ENCODING_RULES = Map.of(
            '\"', "\\\"",
            '\\', "\\\\"
        );
        
        return "\"" + 
               input.chars()
                   .mapToObj(c -> (char) c)
                   .map(c -> ENCODING_RULES.getOrDefault(c, String.valueOf(c)))
                   .collect(Collectors.joining()) +
               "\"";
    }
    
    private int calculateEncodedLength(String input) {
        return encodeString2(input).length();
    }
    
    public int calculateTotalDifference2(List<String> strings) {                
        int encodedLength = strings.stream()
                .mapToInt(this::calculateEncodedLength)
                .sum();

        int originalLength = strings.stream()
                .mapToInt(String::length)
                .sum();
                
        return encodedLength - originalLength;
    }

    @Override
    public Integer solvePartOne(String fileName) {
        var lines = ResourceLines.list(fileName);
        return calculateTotalDifference(lines);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var lines = ResourceLines.list(fileName);
        return calculateTotalDifference2(lines);
    }
}