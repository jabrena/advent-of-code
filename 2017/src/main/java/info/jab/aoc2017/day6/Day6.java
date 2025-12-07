package info.jab.aoc2017.day6;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day6 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        String line = ResourceLines.line(fileName).trim();
        String[] parts = line.split("\\s+");
        List<Integer> banks = new ArrayList<>();
        for (String part : parts) {
            if (!part.trim().isEmpty()) {
                banks.add(Integer.parseInt(part.trim()));
            }
        }
        
        Set<List<Integer>> seen = new HashSet<>();
        int cycles = 0;
        
        while (!seen.contains(banks)) {
            seen.add(new ArrayList<>(banks));
            
            // Find the bank with the most blocks (ties go to lowest index)
            int maxIndex = 0;
            int maxValue = banks.get(0);
            for (int i = 1; i < banks.size(); i++) {
                if (banks.get(i) > maxValue) {
                    maxValue = banks.get(i);
                    maxIndex = i;
                }
            }
            
            // Redistribute
            int blocks = banks.get(maxIndex);
            banks.set(maxIndex, 0);
            int currentIndex = (maxIndex + 1) % banks.size();
            
            while (blocks > 0) {
                banks.set(currentIndex, banks.get(currentIndex) + 1);
                blocks--;
                currentIndex = (currentIndex + 1) % banks.size();
            }
            
            cycles++;
        }
        
        return cycles;
    }

    @Override
    public Integer getPart2Result(String fileName) {
        String line = ResourceLines.line(fileName).trim();
        String[] parts = line.split("\\s+");
        List<Integer> banks = new ArrayList<>();
        for (String part : parts) {
            if (!part.trim().isEmpty()) {
                banks.add(Integer.parseInt(part.trim()));
            }
        }
        
        Set<List<Integer>> seen = new HashSet<>();
        List<Integer> duplicateState = null;
        int cycles = 0;
        
        // First, find the duplicate state
        while (!seen.contains(banks)) {
            seen.add(new ArrayList<>(banks));
            
            // Find the bank with the most blocks (ties go to lowest index)
            int maxIndex = 0;
            int maxValue = banks.get(0);
            for (int i = 1; i < banks.size(); i++) {
                if (banks.get(i) > maxValue) {
                    maxValue = banks.get(i);
                    maxIndex = i;
                }
            }
            
            // Redistribute
            int blocks = banks.get(maxIndex);
            banks.set(maxIndex, 0);
            int currentIndex = (maxIndex + 1) % banks.size();
            
            while (blocks > 0) {
                banks.set(currentIndex, banks.get(currentIndex) + 1);
                blocks--;
                currentIndex = (currentIndex + 1) % banks.size();
            }
            
            cycles++;
        }
        
        // Now we found the duplicate, save it and count cycles until we see it again
        duplicateState = new ArrayList<>(banks);
        cycles = 0;
        
        do {
            // Find the bank with the most blocks (ties go to lowest index)
            int maxIndex = 0;
            int maxValue = banks.get(0);
            for (int i = 1; i < banks.size(); i++) {
                if (banks.get(i) > maxValue) {
                    maxValue = banks.get(i);
                    maxIndex = i;
                }
            }
            
            // Redistribute
            int blocks = banks.get(maxIndex);
            banks.set(maxIndex, 0);
            int currentIndex = (maxIndex + 1) % banks.size();
            
            while (blocks > 0) {
                banks.set(currentIndex, banks.get(currentIndex) + 1);
                blocks--;
                currentIndex = (currentIndex + 1) % banks.size();
            }
            
            cycles++;
        } while (!banks.equals(duplicateState));
        
        return cycles;
    }
}
