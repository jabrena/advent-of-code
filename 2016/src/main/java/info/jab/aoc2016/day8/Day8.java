package info.jab.aoc2016.day8;

import info.jab.aoc.Day;
import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.resources.ResourceLines;

import java.util.List;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2016/day/8
 * Two-Factor Authentication
 */
public class Day8 implements Day<String> {

    private static final int SCREEN_WIDTH = 50;
    private static final int SCREEN_HEIGHT = 6;
    
    private static final Pattern RECT_PATTERN = Pattern.compile("rect (\\d+)x(\\d+)");
    private static final Pattern ROTATE_ROW_PATTERN = Pattern.compile("rotate row y=(\\d+) by (\\d+)");
    private static final Pattern ROTATE_COLUMN_PATTERN = Pattern.compile("rotate column x=(\\d+) by (\\d+)");

    public Integer countLitPixels(List<String> instructions, int width, int height) {
        Grid screen = new Grid(GridUtils.of(0, width, 0, height, '.'));
        
        for (String instruction : instructions) {
            processInstruction(screen, instruction, width, height);
        }
        
        return (int) screen.count('#');
    }
    
    private void processInstruction(Grid screen, String instruction, int width, int height) {
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
    
    private void drawRect(Grid screen, int width, int height) {
        for (int y = screen.minY(); y < screen.minY() + height; y++) {
            for (int x = screen.minX(); x < screen.minX() + width; x++) {
                screen.set(x, y, '#');
            }
        }
    }
    
    private void rotateRow(Grid screen, int row, int shift, int width) {
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
    
    private void rotateColumn(Grid screen, int column, int shift, int height) {
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

    public String decodeScreen(List<String> instructions, int width, int height) {
        Grid screen = new Grid(GridUtils.of(0, width, 0, height, '.'));
        
        for (String instruction : instructions) {
            processInstruction(screen, instruction, width, height);
        }
        
        return screenToString(screen);
    }
    
    
    private String screenToString(Grid screen) {
        StringBuilder result = new StringBuilder();
        
        // Each letter is 5 pixels wide, so we process 5-pixel chunks
        int numLetters = screen.width() / 5;
        
        for (int letterIndex = 0; letterIndex < numLetters; letterIndex++) {
            char letter = decodeLetter(screen, letterIndex * 5);
            result.append(letter);
        }
        
        return result.toString();
    }
    
    private char decodeLetter(Grid screen, int startCol) {
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
    
    private char recognizeLetter(char[][] pattern) {
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

    @Override
    public String getPart1Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        int count = countLitPixels(lines, SCREEN_WIDTH, SCREEN_HEIGHT);
        return String.valueOf(count);
    }

    @Override
    public String getPart2Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        return decodeScreen(lines, SCREEN_WIDTH, SCREEN_HEIGHT);
    }
}