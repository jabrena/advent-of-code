package info.jab.aoc2017.day9;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;

public class Day9 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        var input = ResourceLines.line(fileName);
        return calculateScore(input);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        var input = ResourceLines.line(fileName);
        return countGarbageCharacters(input);
    }

    private int countGarbageCharacters(String input) {
        int garbageCount = 0;
        boolean inGarbage = false;
        boolean skipNext = false;
        
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            
            if (skipNext) {
                skipNext = false;
                continue;
            }
            
            if (c == '!') {
                skipNext = true;
                continue;
            }
            
            if (inGarbage) {
                if (c == '>') {
                    inGarbage = false;
                } else {
                    garbageCount++;
                }
                continue;
            }
            
            if (c == '<') {
                inGarbage = true;
            }
        }
        
        return garbageCount;
    }

    private int calculateScore(String input) {
        int score = 0;
        int depth = 0;
        boolean inGarbage = false;
        boolean skipNext = false;
        
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            
            if (skipNext) {
                skipNext = false;
                continue;
            }
            
            if (c == '!') {
                skipNext = true;
                continue;
            }
            
            if (inGarbage) {
                if (c == '>') {
                    inGarbage = false;
                }
                continue;
            }
            
            if (c == '<') {
                inGarbage = true;
                continue;
            }
            
            if (c == '{') {
                depth++;
                score += depth;
            } else if (c == '}') {
                depth--;
            }
        }
        
        return score;
    }
}
