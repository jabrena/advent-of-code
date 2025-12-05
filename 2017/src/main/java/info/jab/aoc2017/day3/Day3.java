package info.jab.aoc2017.day3;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.HashMap;
import java.util.Map;

public class Day3 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        String input = ResourceLines.line(fileName);
        int squareNumber = Integer.parseInt(input.trim());
        
        if (squareNumber == 1) {
            return 0;
        }
        
        // Find which ring the square is in
        // Ring 0: 1 (1 square)
        // Ring 1: 2-9 (8 squares)
        // Ring 2: 10-25 (16 squares)
        // Ring n: (2n+1)^2 - (2n-1)^2 = 8n squares
        
        int ring = 1;
        int ringStart = 2;
        while (ringStart + 8 * ring <= squareNumber) {
            ringStart += 8 * ring;
            ring++;
        }
        
        // Position in the ring (0 to 8*ring-1)
        int positionInRing = squareNumber - ringStart;
        
        // Each side of the ring has length 2*ring
        int sideLength = 2 * ring;
        int side = positionInRing / sideLength; // 0=right, 1=top, 2=left, 3=bottom
        int offsetInSide = positionInRing % sideLength;
        
        // Calculate coordinates
        // Right side: (ring, -ring+1+offset) to (ring, ring)
        // Top side: (ring-1-offset, ring) to (-ring+1, ring)
        // Left side: (-ring, ring-1-offset) to (-ring, -ring+1)
        // Bottom side: (-ring+1+offset, -ring) to (ring-1, -ring)
        
        int x;
        int y;
        switch (side) {
            case 0: // Right side
                x = ring;
                y = -ring + 1 + offsetInSide;
                break;
            case 1: // Top side
                x = ring - 1 - offsetInSide;
                y = ring;
                break;
            case 2: // Left side
                x = -ring;
                y = ring - 1 - offsetInSide;
                break;
            default: // Bottom side (case 3)
                x = -ring + 1 + offsetInSide;
                y = -ring;
                break;
        }
        
        // Manhattan distance from (x, y) to (0, 0)
        return Math.abs(x) + Math.abs(y);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        String input = ResourceLines.line(fileName);
        int target = Integer.parseInt(input.trim());
        
        // Map to store values at each coordinate
        Map<String, Integer> grid = new HashMap<>();
        
        // Start with square 1 at (0, 0) with value 1
        grid.put("0,0", 1);
        
        if (target < 1) {
            return 1;
        }
        
        // Generate spiral pattern and calculate values
        int ring = 1;
        
        while (true) {
            int sideLength = 2 * ring;
            
            // Generate all squares in this ring
            for (int side = 0; side < 4; side++) {
                for (int offset = 0; offset < sideLength; offset++) {
                    int x;
                    int y;
                    switch (side) {
                        case 0: // Right side
                            x = ring;
                            y = -ring + 1 + offset;
                            break;
                        case 1: // Top side
                            x = ring - 1 - offset;
                            y = ring;
                            break;
                        case 2: // Left side
                            x = -ring;
                            y = ring - 1 - offset;
                            break;
                        default: // Bottom side (case 3)
                            x = -ring + 1 + offset;
                            y = -ring;
                            break;
                    }
                    
                    // Calculate sum of adjacent squares (8 directions including diagonals)
                    int sum = 0;
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            if (dx == 0 && dy == 0) {
                                continue; // Skip the current square
                            }
                            String key = (x + dx) + "," + (y + dy);
                            sum += grid.getOrDefault(key, 0);
                        }
                    }
                    
                    // Store the value
                    String key = x + "," + y;
                    grid.put(key, sum);
                    
                    // Check if we found a value larger than target
                    if (sum > target) {
                        return sum;
                    }
                }
            }
            
            ring++;
        }
    }
}
