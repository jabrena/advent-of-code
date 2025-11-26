package info.jab.aoc.day5;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import com.putoet.security.MD5;

/**
 * https://adventofcode.com/2016/day/5
 */
public class Day5 implements Day<String> {

    @Override
    public String getPart1Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        String doorId = lines.get(0);
        return findPassword(doorId);
    }

    @Override
    public String getPart2Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        String doorId = lines.get(0);
        return findPasswordWithPosition(doorId);
    }
    
    private String findPassword(String doorId) {
        StringBuilder password = new StringBuilder();
        int index = 0;
        
        while (password.length() < 8) {
            String input = doorId + index;
            String hash = MD5.hash(input).toLowerCase();
            
            if (hash.startsWith("00000")) {
                // Take the 6th character (index 5)
                password.append(hash.charAt(5));
            }
            
            index++;
        }
        
        return password.toString();
    }
    
    private String findPasswordWithPosition(String doorId) {
        char[] password = new char[8];
        boolean[] filled = new boolean[8];
        int filledCount = 0;
        int index = 0;
        
        while (filledCount < 8) {
            String input = doorId + index;
            String hash = MD5.hash(input).toLowerCase();
            
            if (hash.startsWith("00000")) {
                char positionChar = hash.charAt(5);
                char valueChar = hash.charAt(6);
                
                // Check if position is valid (0-7)
                if (positionChar >= '0' && positionChar <= '7') {
                    int position = positionChar - '0';
                    
                    // Use only the first result for each position
                    if (!filled[position]) {
                        password[position] = valueChar;
                        filled[position] = true;
                        filledCount++;
                    }
                }
            }
            
            index++;
        }
        
        return new String(password);
    }
}