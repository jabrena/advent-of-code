package info.jab.aoc2016.day8;

import info.jab.aoc.Day;
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
        boolean[][] screen = new boolean[height][width];
        
        for (String instruction : instructions) {
            processInstruction(screen, instruction, width, height);
        }
        
        return countLitPixels(screen);
    }
    
    private void processInstruction(boolean[][] screen, String instruction, int width, int height) {
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
    
    private void drawRect(boolean[][] screen, int width, int height) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                screen[row][col] = true;
            }
        }
    }
    
    private void rotateRow(boolean[][] screen, int row, int shift, int width) {
        boolean[] temp = new boolean[width];
        
        // Copy the row with rotation
        for (int col = 0; col < width; col++) {
            temp[(col + shift) % width] = screen[row][col];
        }
        
        // Copy back to screen
        System.arraycopy(temp, 0, screen[row], 0, width);
    }
    
    private void rotateColumn(boolean[][] screen, int column, int shift, int height) {
        boolean[] temp = new boolean[height];
        
        // Copy the column with rotation
        for (int row = 0; row < height; row++) {
            temp[(row + shift) % height] = screen[row][column];
        }
        
        // Copy back to screen
        for (int row = 0; row < height; row++) {
            screen[row][column] = temp[row];
        }
    }
    
    private int countLitPixels(boolean[][] screen) {
        int count = 0;
        for (boolean[] row : screen) {
            for (boolean pixel : row) {
                if (pixel) {
                    count++;
                }
            }
        }
        return count;
    }

    public String decodeScreen(List<String> instructions, int width, int height) {
        boolean[][] screen = new boolean[height][width];
        
        for (String instruction : instructions) {
            processInstruction(screen, instruction, width, height);
        }
        
        // Debug: print the screen (commented out for production)
        // printScreen(screen);
        
        return screenToString(screen);
    }
    
    private void printScreen(boolean[][] screen) {
        System.out.println("Screen output:");
        for (int row = 0; row < screen.length; row++) {
            for (int col = 0; col < screen[row].length; col++) {
                System.out.print(screen[row][col] ? '#' : '.');
            }
            System.out.println();
        }
        System.out.println();
    }
    
    private String screenToString(boolean[][] screen) {
        StringBuilder result = new StringBuilder();
        
        // Each letter is 5 pixels wide, so we process 5-pixel chunks
        int numLetters = screen[0].length / 5;
        
        for (int letterIndex = 0; letterIndex < numLetters; letterIndex++) {
            char letter = decodeLetter(screen, letterIndex * 5);
            result.append(letter);
        }
        
        return result.toString();
    }
    
    private char decodeLetter(boolean[][] screen, int startCol) {
        // Extract 5x6 letter pattern
        boolean[][] letterPattern = new boolean[6][5];
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                if (startCol + col < screen[0].length) {
                    letterPattern[row][col] = screen[row][startCol + col];
                }
            }
        }
        
        return recognizeLetter(letterPattern);
    }
    
    private char recognizeLetter(boolean[][] pattern) {
        // Convert pattern to string for easier matching
        StringBuilder patternStr = new StringBuilder();
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                patternStr.append(pattern[row][col] ? '#' : '.');
            }
        }
        
        String p = patternStr.toString();
        
        // Debug: print the pattern (commented out for production)
        // System.out.println("Letter pattern: " + p);
        
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