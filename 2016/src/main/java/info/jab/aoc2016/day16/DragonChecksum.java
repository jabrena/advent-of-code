package info.jab.aoc2016.day16;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

/**
 * Solver for Day 16: Dragon Checksum
 * Generates data using dragon curve and calculates checksum.
 */
public final class DragonChecksum implements Solver<String> {

    @Override
    public String solvePartOne(final String fileName) {
        String initialState = ResourceLines.line(fileName).trim();
        int diskLength = 272;
        
        // Generate data using dragon curve until we have enough
        String data = generateData(initialState, diskLength);
        
        // Calculate checksum
        return calculateChecksum(data);
    }

    @Override
    public String solvePartTwo(final String fileName) {
        String initialState = ResourceLines.line(fileName).trim();
        int diskLength = 35651584;
        
        // Generate data using dragon curve until we have enough
        String data = generateData(initialState, diskLength);
        
        // Calculate checksum
        return calculateChecksum(data);
    }

    private String generateData(final String initialState, final int requiredLength) {
        String data = initialState;
        
        while (data.length() < requiredLength) {
            String a = data;
            String b = reverseAndFlip(a);
            data = a + "0" + b;
        }
        
        // Return only the first requiredLength characters
        return data.substring(0, requiredLength);
    }

    private String reverseAndFlip(final String input) {
        StringBuilder reversed = new StringBuilder(input).reverse();
        StringBuilder flipped = new StringBuilder();
        
        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            flipped.append(c == '0' ? '1' : '0');
        }
        
        return flipped.toString();
    }

    private String calculateChecksum(final String data) {
        String checksum = data;
        
        while (checksum.length() % 2 == 0) {
            StringBuilder nextChecksum = new StringBuilder();
            
            for (int i = 0; i < checksum.length(); i += 2) {
                char c1 = checksum.charAt(i);
                char c2 = checksum.charAt(i + 1);
                
                if (c1 == c2) {
                    nextChecksum.append('1');
                } else {
                    nextChecksum.append('0');
                }
            }
            
            checksum = nextChecksum.toString();
        }
        
        return checksum;
    }
}

