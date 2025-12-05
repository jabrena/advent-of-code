package info.jab.aoc2016.day2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class BathroomSecurity implements Solver<String>{

    private static final int[][] KEYPAD = {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 9}
    };

    private static final Map<Position, Character> KEYPAD_PART2 = new HashMap<>();
    
    static {
        // Initialize diamond-shaped keypad
        KEYPAD_PART2.put(new Position(0, 2), '1');
        KEYPAD_PART2.put(new Position(1, 1), '2');
        KEYPAD_PART2.put(new Position(1, 2), '3');
        KEYPAD_PART2.put(new Position(1, 3), '4');
        KEYPAD_PART2.put(new Position(2, 0), '5');
        KEYPAD_PART2.put(new Position(2, 1), '6');
        KEYPAD_PART2.put(new Position(2, 2), '7');
        KEYPAD_PART2.put(new Position(2, 3), '8');
        KEYPAD_PART2.put(new Position(2, 4), '9');
        KEYPAD_PART2.put(new Position(3, 1), 'A');
        KEYPAD_PART2.put(new Position(3, 2), 'B');
        KEYPAD_PART2.put(new Position(3, 3), 'C');
        KEYPAD_PART2.put(new Position(4, 2), 'D');
    }

    private static record Position(int row, int col) {
        
        Position move(char direction) {
            Position newPos = new Position(row, col);
            switch (direction) {
                case 'U' -> newPos = new Position(row - 1, col);
                case 'D' -> newPos = new Position(row + 1, col);
                case 'L' -> newPos = new Position(row, col - 1);
                case 'R' -> newPos = new Position(row, col + 1);
                default -> {
                    // Ignore unknown directions
                }
            }
            return newPos;
        }
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3;
    }


    @Override
    public String solvePartOne(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        StringBuilder code = new StringBuilder();
        
        // Start at 5 (position 1,1)
        int row = 1;
        int col = 1;
        
        for (String line : lines) {
            for (char move : line.toCharArray()) {
                int newRow = row;
                int newCol = col;
                
                switch (move) {
                    case 'U': newRow--; break;
                    case 'D': newRow++; break;
                    case 'L': newCol--; break;
                    case 'R': newCol++; break;
                    default: {
                        // Ignore unknown moves
                        break;
                    }
                }
                
                // Only move if valid position
                if (isValidPosition(newRow, newCol)) {
                    row = newRow;
                    col = newCol;
                }
            }
            
            // Add current digit to code
            code.append(KEYPAD[row][col]);
        }
        
        return code.toString();
    }

    @Override
    public String solvePartTwo(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        StringBuilder code = new StringBuilder();
        Position current = new Position(2, 0); // Start at '5'
        
        for (String line : lines) {
            for (char move : line.toCharArray()) {
                Position next = current.move(move);
                if (KEYPAD_PART2.containsKey(next)) {
                    current = next;
                }
            }
            code.append(KEYPAD_PART2.get(current));
        }
        
        return code.toString();
    }

}