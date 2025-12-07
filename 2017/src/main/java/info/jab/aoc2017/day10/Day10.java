package info.jab.aoc2017.day10;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day10 implements Day<String> {

    @Override
    public String getPart1Result(String fileName) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String getPart2Result(String fileName) {
        String input = ResourceLines.line(fileName).trim();
        
        // Convert input string to ASCII bytes
        List<Integer> lengths = new ArrayList<>();
        for (char c : input.toCharArray()) {
            lengths.add((int) c);
        }
        
        // Add suffix
        lengths.addAll(List.of(17, 31, 73, 47, 23));
        
        // Initialize list with 256 elements
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            list.add(i);
        }
        
        int currentPosition = 0;
        int skipSize = 0;
        
        // Run 64 rounds
        for (int round = 0; round < 64; round++) {
            for (int length : lengths) {
                // Reverse the sublist
                reverseSublist(list, currentPosition, length);
                
                // Move current position
                currentPosition = (currentPosition + length + skipSize) % list.size();
                
                // Increase skip size
                skipSize++;
            }
        }
        
        // Convert sparse hash to dense hash
        List<Integer> denseHash = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            int xor = 0;
            for (int j = 0; j < 16; j++) {
                xor ^= list.get(i * 16 + j);
            }
            denseHash.add(xor);
        }
        
        // Convert to hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (int value : denseHash) {
            hexString.append(String.format("%02x", value));
        }
        
        return hexString.toString();
    }
    
    private void reverseSublist(List<Integer> list, int start, int length) {
        for (int i = 0; i < length / 2; i++) {
            int pos1 = (start + i) % list.size();
            int pos2 = (start + length - 1 - i) % list.size();
            
            int temp = list.get(pos1);
            list.set(pos1, list.get(pos2));
            list.set(pos2, temp);
        }
    }
}
