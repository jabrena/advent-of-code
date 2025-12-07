package info.jab.aoc2017.day14;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Day;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;

public class Day14 implements Day<Integer> {

    @Override
    public Integer getPart1Result(String fileName) {
        String key = ResourceLines.line(fileName).trim();
        int usedCount = 0;
        
        for (int row = 0; row < 128; row++) {
            String hashInput = key + "-" + row;
            String hash = knotHash(hashInput);
            // Convert hex to binary and count '1' bits
            for (char hexChar : hash.toCharArray()) {
                int value = Character.digit(hexChar, 16);
                usedCount += Integer.bitCount(value);
            }
        }
        
        return usedCount;
    }

    @Override
    public Integer getPart2Result(String fileName) {
        String key = ResourceLines.line(fileName).trim();
        boolean[][] grid = buildGrid(key);
        
        int regionCount = 0;
        Set<String> visited = new HashSet<>();
        
        for (int row = 0; row < 128; row++) {
            for (int col = 0; col < 128; col++) {
                if (grid[row][col] && !visited.contains(row + "," + col)) {
                    // BFS to mark all squares in this region
                    Queue<int[]> queue = new ArrayDeque<>();
                    queue.offer(new int[]{row, col});
                    visited.add(row + "," + col);
                    
                    while (!queue.isEmpty()) {
                        int[] current = queue.poll();
                        int r = current[0];
                        int c = current[1];
                        
                        // Check neighbors (up, down, left, right)
                        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
                        for (int[] dir : directions) {
                            int newRow = r + dir[0];
                            int newCol = c + dir[1];
                            String keyStr = newRow + "," + newCol;
                            
                            if (newRow >= 0 && newRow < 128 && newCol >= 0 && newCol < 128
                                    && grid[newRow][newCol] && !visited.contains(keyStr)) {
                                visited.add(keyStr);
                                queue.offer(new int[]{newRow, newCol});
                            }
                        }
                    }
                    
                    regionCount++;
                }
            }
        }
        
        return regionCount;
    }
    
    private boolean[][] buildGrid(String key) {
        boolean[][] grid = new boolean[128][128];
        
        for (int row = 0; row < 128; row++) {
            String hashInput = key + "-" + row;
            String hash = knotHash(hashInput);
            
            int col = 0;
            for (char hexChar : hash.toCharArray()) {
                int value = Character.digit(hexChar, 16);
                // Convert to binary (4 bits per hex digit)
                for (int bit = 3; bit >= 0; bit--) {
                    grid[row][col] = (value & (1 << bit)) != 0;
                    col++;
                }
            }
        }
        
        return grid;
    }

    private String knotHash(String input) {
        List<Integer> lengths = new ArrayList<>();
        for (char c : input.toCharArray()) {
            lengths.add((int) c);
        }
        // Add standard suffix
        lengths.add(17);
        lengths.add(31);
        lengths.add(73);
        lengths.add(47);
        lengths.add(23);
        
        // Initialize circular list
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            list.add(i);
        }
        
        int currentPos = 0;
        int skipSize = 0;
        
        // Run 64 rounds
        for (int round = 0; round < 64; round++) {
            for (int length : lengths) {
                // Reverse sublist
                for (int i = 0; i < length / 2; i++) {
                    int idx1 = (currentPos + i) % list.size();
                    int idx2 = (currentPos + length - 1 - i) % list.size();
                    int temp = list.get(idx1);
                    list.set(idx1, list.get(idx2));
                    list.set(idx2, temp);
                }
                
                currentPos = (currentPos + length + skipSize) % list.size();
                skipSize++;
            }
        }
        
        // Compute dense hash
        StringBuilder hash = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int xor = 0;
            for (int j = 0; j < 16; j++) {
                xor ^= list.get(i * 16 + j);
            }
            hash.append(String.format("%02x", xor));
        }
        
        return hash.toString();
    }
}
