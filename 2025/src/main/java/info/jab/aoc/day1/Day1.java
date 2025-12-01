package info.jab.aoc.day1;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.List;

public class Day1 implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        List<String> rotations = ResourceLines.list(fileName);
        int dial = 50; // Dial starts at 50
        int count = 0; // Count how many times dial points at 0
        
        for (String rotation : rotations) {
            if (rotation == null || rotation.trim().isEmpty()) {
                continue;
            }
            char direction = rotation.charAt(0);
            int distance = Integer.parseInt(rotation.substring(1));
            
            if (direction == 'L') {
                dial = (dial - distance) % 100;
                if (dial < 0) {
                    dial += 100;
                }
            } else if (direction == 'R') {
                dial = (dial + distance) % 100;
            }
            
            if (dial == 0) {
                count++;
            }
        }
        
        return count;
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        List<String> rotations = ResourceLines.list(fileName);
        int dial = 50; // Dial starts at 50
        int count = 0; // Count how many times dial points at 0 during rotations
        
        for (String rotation : rotations) {
            if (rotation == null || rotation.trim().isEmpty()) {
                continue;
            }
            char direction = rotation.charAt(0);
            int distance = Integer.parseInt(rotation.substring(1));
            
            // Simulate each click during the rotation
            for (int i = 0; i < distance; i++) {
                if (direction == 'L') {
                    dial = (dial - 1) % 100;
                    if (dial < 0) {
                        dial += 100;
                    }
                } else { // 'R'
                    dial = (dial + 1) % 100;
                }
                
                // Count if we're at 0 after this click
                if (dial == 0) {
                    count++;
                }
            }
        }
        
        return count;
    }
}
