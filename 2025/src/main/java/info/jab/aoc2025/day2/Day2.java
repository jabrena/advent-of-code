package info.jab.aoc2025.day2;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.List;

public class Day2 implements Solver<Long> {

    @Override
    public Long solvePartOne(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        String input = String.join("", lines).trim();
        
        String[] ranges = input.split(",");
        long sum = 0;
        
        for (String range : ranges) {
            String[] parts = range.split("-");
            long start = Long.parseLong(parts[0]);
            long end = Long.parseLong(parts[1]);
            
            for (long id = start; id <= end; id++) {
                if (isInvalidId(id)) {
                    sum += id;
                }
            }
        }
        
        return sum;
    }

    private boolean isInvalidId(long id) {
        String idStr = String.valueOf(id);
        int length = idStr.length();
        
        // Invalid ID must have even length (made of two equal parts)
        if (length % 2 != 0) {
            return false;
        }
        
        int halfLength = length / 2;
        String firstHalf = idStr.substring(0, halfLength);
        String secondHalf = idStr.substring(halfLength);
        
        return firstHalf.equals(secondHalf);
    }

    @Override
    public Long solvePartTwo(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        String input = String.join("", lines).trim();
        
        String[] ranges = input.split(",");
        long sum = 0;
        
        for (String range : ranges) {
            String[] parts = range.split("-");
            long start = Long.parseLong(parts[0]);
            long end = Long.parseLong(parts[1]);
            
            for (long id = start; id <= end; id++) {
                if (isInvalidIdPart2(id)) {
                    sum += id;
                }
            }
        }
        
        return sum;
    }

    private boolean isInvalidIdPart2(long id) {
        String idStr = String.valueOf(id);
        int length = idStr.length();
        
        // Try all possible part lengths from 1 to length/2
        for (int partLength = 1; partLength <= length / 2; partLength++) {
            // Check if length is divisible by partLength
            if (length % partLength != 0) {
                continue;
            }
            
            int numParts = length / partLength;
            // Need at least 2 parts
            if (numParts < 2) {
                continue;
            }
            
            String firstPart = idStr.substring(0, partLength);
            boolean allPartsEqual = true;
            
            for (int i = 1; i < numParts; i++) {
                String part = idStr.substring(i * partLength, (i + 1) * partLength);
                if (!part.equals(firstPart)) {
                    allPartsEqual = false;
                    break;
                }
            }
            
            if (allPartsEqual) {
                return true;
            }
        }
        
        return false;
    }
}
