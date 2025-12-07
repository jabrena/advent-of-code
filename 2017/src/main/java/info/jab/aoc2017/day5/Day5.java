package info.jab.aoc2017.day5;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.ArrayList;
import java.util.List;

public class Day5 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        List<Integer> jumps = new ArrayList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                jumps.add(Integer.parseInt(line.trim()));
            }
        }
        
        int steps = 0;
        int currentIndex = 0;
        
        while (currentIndex >= 0 && currentIndex < jumps.size()) {
            int offset = jumps.get(currentIndex);
            jumps.set(currentIndex, offset + 1);
            currentIndex += offset;
            steps++;
        }
        
        return steps;
    }

    @Override
    public Integer getPart2Result(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        List<Integer> jumps = new ArrayList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                jumps.add(Integer.parseInt(line.trim()));
            }
        }
        
        int steps = 0;
        int currentIndex = 0;
        
        while (currentIndex >= 0 && currentIndex < jumps.size()) {
            int offset = jumps.get(currentIndex);
            if (offset >= 3) {
                jumps.set(currentIndex, offset - 1);
            } else {
                jumps.set(currentIndex, offset + 1);
            }
            currentIndex += offset;
            steps++;
        }
        
        return steps;
    }
}
