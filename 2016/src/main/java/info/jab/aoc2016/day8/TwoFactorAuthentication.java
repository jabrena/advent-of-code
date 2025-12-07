package info.jab.aoc2016.day8;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Solver for Day 8: Two-Factor Authentication
 * Processes screen instructions and decodes the display.
 */
public final class TwoFactorAuthentication implements Solver<String> {

    private static final int SCREEN_WIDTH = 50;
    private static final int SCREEN_HEIGHT = 6;
    
    private static final Pattern RECT_PATTERN = Pattern.compile("rect (\\d+)x(\\d+)");
    private static final Pattern ROTATE_ROW_PATTERN = Pattern.compile("rotate row y=(\\d+) by (\\d+)");
    private static final Pattern ROTATE_COLUMN_PATTERN = Pattern.compile("rotate column x=(\\d+) by (\\d+)");

    @Override
    public String solvePartOne(final String fileName) {
        var lines = ResourceLines.list(fileName);
        int count = countLitPixels(lines, SCREEN_WIDTH, SCREEN_HEIGHT);
        return String.valueOf(count);
    }

    @Override
    public String solvePartTwo(final String fileName) {
        var lines = ResourceLines.list(fileName);
        return decodeScreen(lines, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public Integer countLitPixels(final List<String> instructions, final int width, final int height) {
        Grid screen = new Grid(GridUtils.of(0, width, 0, height, '.'));
        
        for (String instruction : instructions) {
            processInstruction(screen, instruction, width, height);
        }
        
        return (int) screen.count('#');
    }
    
    private void processInstruction(final Grid screen, final String instruction, final int width, final int height) {
        var rectMatcher = RECT_PATTERN.matcher(instruction);
        if (rectMatcher.matches()) {
            int w = Integer.parseInt(rectMatcher.group(1));
            int h = Integer.parseInt(rectMatcher.group(2));
            drawRect(screen, w, h);
            return;
        }
        
        var rotateRowMatcher = ROTATE_ROW_PATTERN.matcher(instruction);
        if (rotateRowMatcher.matches()) {
            int row = Integer.parseInt(rotateRowMatcher.group(1));
            int shift = Integer.parseInt(rotateRowMatcher.group(2));
            rotateRow(screen, row, shift, width);
            return;
        }
        
        var rotateColumnMatcher = ROTATE_COLUMN_PATTERN.matcher(instruction);
        if (rotateColumnMatcher.matches()) {
            int column = Integer.parseInt(rotateColumnMatcher.group(1));
            int shift = Integer.parseInt(rotateColumnMatcher.group(2));
            rotateColumn(screen, column, shift, height);
            return;
        }
        
        throw new IllegalArgumentException("Unknown instruction: " + instruction);
    }
    
    private void drawRect(final Grid screen, final int width, final int height) {
        for (int y = screen.minY(); y < screen.minY() + height; y++) {
            for (int x = screen.minX(); x < screen.minX() + width; x++) {
                screen.set(x, y, '#');
            }
        }
    }
    
    private void rotateRow(final Grid screen, final int row, final int shift, final int width) {
        char[] temp = new char[width];
        
        // Copy the row
        for (int col = 0; col < width; col++) {
            temp[col] = screen.get(screen.minX() + col, screen.minY() + row);
        }
        
        // Copy back with rotation
        for (int col = 0; col < width; col++) {
            screen.set(screen.minX() + col, screen.minY() + row, temp[(col - shift + width) % width]);
        }
    }
    
    private void rotateColumn(final Grid screen, final int column, final int shift, final int height) {
        char[] temp = new char[height];
        
        // Copy the column
        for (int row = 0; row < height; row++) {
            temp[row] = screen.get(screen.minX() + column, screen.minY() + row);
        }
        
        // Copy back with rotation
        for (int row = 0; row < height; row++) {
            screen.set(screen.minX() + column, screen.minY() + row, temp[(row - shift + height) % height]);
        }
    }

    private String decodeScreen(final List<String> instructions, final int width, final int height) {
        Grid screen = new Grid(GridUtils.of(0, width, 0, height, '.'));
        
        for (String instruction : instructions) {
            processInstruction(screen, instruction, width, height);
        }
        
        return screenToString(screen);
    }
    
    
    private String screenToString(final Grid screen) {
        StringBuilder result = new StringBuilder();
        
        // Each letter is 5 pixels wide, so we process 5-pixel chunks
        int numLetters = screen.width() / 5;
        
        for (int letterIndex = 0; letterIndex < numLetters; letterIndex++) {
            char letter = decodeLetter(screen, letterIndex * 5);
            result.append(letter);
        }
        
        return result.toString();
    }
    
    private char decodeLetter(final Grid screen, final int startCol) {
        // Extract 5x6 letter pattern
        char[][] letterPattern = new char[6][5];
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                if (startCol + col < screen.width()) {
                    letterPattern[row][col] = screen.get(screen.minX() + startCol + col, screen.minY() + row);
                } else {
                    letterPattern[row][col] = '.';
                }
            }
        }
        
        return recognizeLetter(letterPattern);
    }
    
    private char recognizeLetter(final char[][] pattern) {
        // Convert pattern to string for easier matching
        StringBuilder patternStr = new StringBuilder();
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                patternStr.append(pattern[row][col]);
            }
        }
        
        String p = patternStr.toString();
        
        // Define letter patterns (5x6 each) based on actual debug output
        if (p.equals(".##..#..#.#..#.####.#..#.#..#.")) return 'A';  // A
        if (p.equals("####.#....###..#....#....#....")) return 'F';  // F  
        if (p.equals("###..#..#.###..#..#.#..#.###..")) return 'B';  // B
        if (p.equals("#..#.#..#.#..#.#..#.#..#..##..")) return 'U';  // U
        if (p.equals("###..#..#.#..#.###..#....#....")) return 'P';  // P
        if (p.equals("####....#...#...#...#....####.")) return 'Z';  // Z
        if (p.equals("..##....#....#....#.#..#..##..")) return 'J';  // J
        if (p.equals(".###.#....#.....##.....#.###..")) return 'S';  // S
        
        return '?'; // Unknown pattern
    }
}

