package info.jab.aoc2016.day16;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;

/**
 * https://adventofcode.com/2016/day/16
 */
public class Day16 implements Day<String> {

    @Override
    public String getPart1Result(String fileName) {
        String initialState = ResourceLines.line(fileName).trim();
        int diskLength = 272;
        
        // Generate data using dragon curve until we have enough
        String data = generateData(initialState, diskLength);
        
        // Calculate checksum
        return calculateChecksum(data);
    }

    @Override
    public String getPart2Result(String fileName) {
        String initialState = ResourceLines.line(fileName).trim();
        int diskLength = 35651584;
        
        // Generate data using dragon curve until we have enough
        String data = generateData(initialState, diskLength);
        
        // Calculate checksum
        return calculateChecksum(data);
    }

    private String generateData(String initialState, int requiredLength) {
        String data = initialState;
        
        while (data.length() < requiredLength) {
            String a = data;
            String b = reverseAndFlip(a);
            data = a + "0" + b;
        }
        
        // Return only the first requiredLength characters
        return data.substring(0, requiredLength);
    }

    private String reverseAndFlip(String input) {
        StringBuilder reversed = new StringBuilder(input).reverse();
        StringBuilder flipped = new StringBuilder();
        
        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            flipped.append(c == '0' ? '1' : '0');
        }
        
        return flipped.toString();
    }

    private String calculateChecksum(String data) {
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
