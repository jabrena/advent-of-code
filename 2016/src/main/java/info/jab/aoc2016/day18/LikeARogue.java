package info.jab.aoc2016.day18;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

/**
 * Solver for Day 18: Like a Rogue
 * Generates rows of tiles based on trap rules and counts safe tiles.
 */
public final class LikeARogue implements Solver<Integer> {

    @Override
    public Integer solvePartOne(final String fileName) {
        var lines = ResourceLines.list(fileName);
        String firstRow = lines.get(0).trim();
        return countSafeTiles(firstRow, 40);
    }

    @Override
    public Integer solvePartTwo(final String fileName) {
        var lines = ResourceLines.list(fileName);
        String firstRow = lines.get(0).trim();
        return countSafeTiles(firstRow, 400000);
    }

    /**
     * Count safe tiles across the specified number of rows
     */
    private int countSafeTiles(final String firstRow, final int totalRows) {
        int safeCount = countSafeInRow(firstRow);
        String currentRow = firstRow;
        
        for (int i = 1; i < totalRows; i++) {
            currentRow = generateNextRow(currentRow);
            safeCount += countSafeInRow(currentRow);
        }
        
        return safeCount;
    }

    /**
     * Generate the next row based on trap rules
     */
    private String generateNextRow(final String previousRow) {
        StringBuilder nextRow = new StringBuilder();
        int length = previousRow.length();
        
        for (int i = 0; i < length; i++) {
            char left = (i > 0) ? previousRow.charAt(i - 1) : '.';
            char center = previousRow.charAt(i);
            char right = (i < length - 1) ? previousRow.charAt(i + 1) : '.';
            
            // A tile is a trap if:
            // 1. Left and center are traps, but right is not
            // 2. Center and right are traps, but left is not
            // 3. Only left is a trap
            // 4. Only right is a trap
            boolean isTrap = 
                (left == '^' && center == '^' && right == '.') ||
                (left == '.' && center == '^' && right == '^') ||
                (left == '^' && center == '.' && right == '.') ||
                (left == '.' && center == '.' && right == '^');
            
            nextRow.append(isTrap ? '^' : '.');
        }
        
        return nextRow.toString();
    }

    /**
     * Count safe tiles (.) in a row
     */
    private int countSafeInRow(final String row) {
        int count = 0;
        for (char c : row.toCharArray()) {
            if (c == '.') {
                count++;
            }
        }
        return count;
    }
}

