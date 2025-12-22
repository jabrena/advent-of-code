package info.jab.aoc2017.day11;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;

public class Day11 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        String input = ResourceLines.line(fileName).trim();
        String[] directions = input.split(",");
        
        // Use cube coordinates for hex grid
        int x = 0, y = 0, z = 0;
        
        for (String dir : directions) {
            switch (dir.trim()) {
                case "n" -> { y++; z--; }
                case "ne" -> { x++; z--; }
                case "se" -> { x++; y--; }
                case "s" -> { y--; z++; }
                case "sw" -> { x--; z++; }
                case "nw" -> { x--; y++; }
            }
        }
        
        // Distance in hex grid is (|x| + |y| + |z|) / 2
        return (Math.abs(x) + Math.abs(y) + Math.abs(z)) / 2;
    }

    @Override
    public Integer getPart2Result(String fileName) {
        String input = ResourceLines.line(fileName).trim();
        String[] directions = input.split(",");
        
        // Use cube coordinates for hex grid
        int x = 0, y = 0, z = 0;
        int maxDistance = 0;
        
        for (String dir : directions) {
            switch (dir.trim()) {
                case "n" -> { y++; z--; }
                case "ne" -> { x++; z--; }
                case "se" -> { x++; y--; }
                case "s" -> { y--; z++; }
                case "sw" -> { x--; z++; }
                case "nw" -> { x--; y++; }
            }
            
            // Calculate distance at each step and track maximum
            int distance = (Math.abs(x) + Math.abs(y) + Math.abs(z)) / 2;
            maxDistance = Math.max(maxDistance, distance);
        }
        
        return maxDistance;
    }
}
