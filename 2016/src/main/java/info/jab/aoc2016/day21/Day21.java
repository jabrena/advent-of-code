package info.jab.aoc2016.day21;

import info.jab.aoc.Day;

import com.putoet.resources.ResourceLines;

import java.util.ArrayList;
import java.util.List;

/**
 * https://adventofcode.com/2016/day/21
 */
public class Day21 implements Day<String> {

    private String scramble(String password, List<String> operations) {
        char[] chars = password.toCharArray();
        
        for (String operation : operations) {
            if (operation.startsWith("swap position")) {
                // swap position X with position Y
                String[] parts = operation.split(" ");
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[5]);
                char temp = chars[x];
                chars[x] = chars[y];
                chars[y] = temp;
            } else if (operation.startsWith("swap letter")) {
                // swap letter X with letter Y
                String[] parts = operation.split(" ");
                char x = parts[2].charAt(0);
                char y = parts[5].charAt(0);
                for (int i = 0; i < chars.length; i++) {
                    if (chars[i] == x) {
                        chars[i] = y;
                    } else if (chars[i] == y) {
                        chars[i] = x;
                    }
                }
            } else if (operation.startsWith("rotate left")) {
                // rotate left X steps
                String[] parts = operation.split(" ");
                int steps = Integer.parseInt(parts[2]);
                rotateLeft(chars, steps);
            } else if (operation.startsWith("rotate right")) {
                // rotate right X steps
                String[] parts = operation.split(" ");
                int steps = Integer.parseInt(parts[2]);
                rotateRight(chars, steps);
            } else if (operation.startsWith("rotate based on position")) {
                // rotate based on position of letter X
                String[] parts = operation.split(" ");
                char letter = parts[6].charAt(0);
                int index = -1;
                for (int i = 0; i < chars.length; i++) {
                    if (chars[i] == letter) {
                        index = i;
                        break;
                    }
                }
                int rotations = 1 + index;
                if (index >= 4) {
                    rotations++;
                }
                rotateRight(chars, rotations);
            } else if (operation.startsWith("reverse positions")) {
                // reverse positions X through Y
                String[] parts = operation.split(" ");
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[4]);
                reverse(chars, x, y);
            } else if (operation.startsWith("move position")) {
                // move position X to position Y
                String[] parts = operation.split(" ");
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[5]);
                move(chars, x, y);
            }
        }
        
        return new String(chars);
    }
    
    private void rotateLeft(char[] chars, int steps) {
        steps = steps % chars.length;
        for (int i = 0; i < steps; i++) {
            char first = chars[0];
            System.arraycopy(chars, 1, chars, 0, chars.length - 1);
            chars[chars.length - 1] = first;
        }
    }
    
    private void rotateRight(char[] chars, int steps) {
        steps = steps % chars.length;
        for (int i = 0; i < steps; i++) {
            char last = chars[chars.length - 1];
            System.arraycopy(chars, 0, chars, 1, chars.length - 1);
            chars[0] = last;
        }
    }
    
    private void reverse(char[] chars, int x, int y) {
        while (x < y) {
            char temp = chars[x];
            chars[x] = chars[y];
            chars[y] = temp;
            x++;
            y--;
        }
    }
    
    private void move(char[] chars, int fromPosition, int toPosition) {
        List<Character> list = new ArrayList<>();
        for (char c : chars) {
            list.add(c);
        }
        char removed = list.remove(fromPosition);
        list.add(toPosition, removed);
        for (int i = 0; i < chars.length; i++) {
            chars[i] = list.get(i);
        }
    }

    @Override
    public String getPart1Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        return scramble("abcdefgh", lines);
    }

    private String unscramble(String password, List<String> operations) {
        char[] chars = password.toCharArray();
        
        // Process operations in reverse order
        for (int i = operations.size() - 1; i >= 0; i--) {
            String operation = operations.get(i);
            applyUnscrambleOperation(chars, operation);
        }
        
        return new String(chars);
    }

    private void applyUnscrambleOperation(char[] chars, String operation) {
        if (operation.startsWith("swap position")) {
            handleUnscrambleSwapPosition(chars, operation);
        } else if (operation.startsWith("swap letter")) {
            handleUnscrambleSwapLetter(chars, operation);
        } else if (operation.startsWith("rotate left")) {
            handleUnscrambleRotateLeft(chars, operation);
        } else if (operation.startsWith("rotate right")) {
            handleUnscrambleRotateRight(chars, operation);
        } else if (operation.startsWith("rotate based on position")) {
            handleUnscrambleRotateBasedOnPosition(chars, operation);
        } else if (operation.startsWith("reverse positions")) {
            handleUnscrambleReversePositions(chars, operation);
        } else if (operation.startsWith("move position")) {
            handleUnscrambleMovePosition(chars, operation);
        }
    }

    private void handleUnscrambleSwapPosition(char[] chars, String operation) {
        String[] parts = operation.split(" ");
        int x = Integer.parseInt(parts[2]);
        int y = Integer.parseInt(parts[5]);
        char temp = chars[x];
        chars[x] = chars[y];
        chars[y] = temp;
    }

    private void handleUnscrambleSwapLetter(char[] chars, String operation) {
        String[] parts = operation.split(" ");
        char x = parts[2].charAt(0);
        char y = parts[5].charAt(0);
        for (int j = 0; j < chars.length; j++) {
            if (chars[j] == x) {
                chars[j] = y;
            } else if (chars[j] == y) {
                chars[j] = x;
            }
        }
    }

    private void handleUnscrambleRotateLeft(char[] chars, String operation) {
        String[] parts = operation.split(" ");
        int steps = Integer.parseInt(parts[2]);
        rotateRight(chars, steps);
    }

    private void handleUnscrambleRotateRight(char[] chars, String operation) {
        String[] parts = operation.split(" ");
        int steps = Integer.parseInt(parts[2]);
        rotateLeft(chars, steps);
    }

    private void handleUnscrambleRotateBasedOnPosition(char[] chars, String operation) {
        String[] parts = operation.split(" ");
        char letter = parts[6].charAt(0);
        
        int currentIndex = findLetterIndex(chars, letter);
        if (currentIndex == -1) {
            return;
        }
        
        int originalIndex = findOriginalIndexForRotation(chars.length, currentIndex);
        if (originalIndex != -1) {
            int rotations = calculateRotations(originalIndex);
            rotateLeft(chars, rotations);
        }
    }

    private int findLetterIndex(char[] chars, char letter) {
        for (int j = 0; j < chars.length; j++) {
            if (chars[j] == letter) {
                return j;
            }
        }
        return -1;
    }

    private int findOriginalIndexForRotation(int length, int currentIndex) {
        for (int oldPos = 0; oldPos < length; oldPos++) {
            int rotations = calculateRotations(oldPos);
            int newPos = (oldPos + rotations) % length;
            if (newPos == currentIndex) {
                return oldPos;
            }
        }
        return -1;
    }

    private int calculateRotations(int position) {
        int rotations = 1 + position;
        if (position >= 4) {
            rotations++;
        }
        return rotations;
    }

    private void handleUnscrambleReversePositions(char[] chars, String operation) {
        String[] parts = operation.split(" ");
        int x = Integer.parseInt(parts[2]);
        int y = Integer.parseInt(parts[4]);
        reverse(chars, x, y);
    }

    private void handleUnscrambleMovePosition(char[] chars, String operation) {
        String[] parts = operation.split(" ");
        int x = Integer.parseInt(parts[2]);
        int y = Integer.parseInt(parts[5]);
        move(chars, y, x);
    }

    @Override
    public String getPart2Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        return unscramble("fbgdceah", lines);
    }
}
