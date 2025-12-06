package info.jab.aoc2017.day4;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day4 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        
        return (int) lines.stream()
            .filter(line -> !line.trim().isEmpty())
            .filter(this::isValidPassphrase)
            .count();
    }
    
    private boolean isValidPassphrase(String passphrase) {
        String[] words = passphrase.trim().split("\\s+");
        
        // Empty passphrase is invalid
        if (words.length == 0 || (words.length == 1 && words[0].isEmpty())) {
            return false;
        }
        
        Set<String> seen = new HashSet<>();
        
        for (String word : words) {
            if (!seen.add(word)) {
                return false; // Duplicate word found
            }
        }
        
        return true; // All words are unique
    }

    @Override
    public Integer getPart2Result(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        
        return (int) lines.stream()
            .filter(line -> !line.trim().isEmpty())
            .filter(this::isValidPassphrasePart2)
            .count();
    }
    
    private boolean isValidPassphrasePart2(String passphrase) {
        String[] words = passphrase.trim().split("\\s+");
        
        // Empty passphrase is invalid
        if (words.length == 0 || (words.length == 1 && words[0].isEmpty())) {
            return false;
        }
        
        // Create normalized forms (sorted letters) for each word
        Set<String> normalizedWords = new HashSet<>();
        
        for (String word : words) {
            // Sort the letters to create a normalized form
            char[] chars = word.toCharArray();
            Arrays.sort(chars);
            String normalized = new String(chars);
            
            // If we've seen this normalized form before, it's an anagram
            if (!normalizedWords.add(normalized)) {
                return false; // Found an anagram
            }
        }
        
        return true; // No anagrams found
    }
}
