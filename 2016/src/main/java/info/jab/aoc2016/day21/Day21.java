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
    
    private void move(char[] chars, int x, int y) {
        List<Character> list = new ArrayList<>();
        for (char c : chars) {
            list.add(c);
        }
        char removed = list.remove(x);
        list.add(y, removed);
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
            
            if (operation.startsWith("swap position")) {
                // swap position X with position Y - symmetric, same operation
                String[] parts = operation.split(" ");
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[5]);
                char temp = chars[x];
                chars[x] = chars[y];
                chars[y] = temp;
            } else if (operation.startsWith("swap letter")) {
                // swap letter X with letter Y - symmetric, same operation
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
            } else if (operation.startsWith("rotate left")) {
                // rotate left X steps - reverse is rotate right X steps
                String[] parts = operation.split(" ");
                int steps = Integer.parseInt(parts[2]);
                rotateRight(chars, steps);
            } else if (operation.startsWith("rotate right")) {
                // rotate right X steps - reverse is rotate left X steps
                String[] parts = operation.split(" ");
                int steps = Integer.parseInt(parts[2]);
                rotateLeft(chars, steps);
            } else if (operation.startsWith("rotate based on position")) {
                // rotate based on position of letter X - need to reverse this
                String[] parts = operation.split(" ");
                char letter = parts[6].charAt(0);
                
                // Find current index of letter
                int currentIndex = -1;
                for (int j = 0; j < chars.length; j++) {
                    if (chars[j] == letter) {
                        currentIndex = j;
                        break;
                    }
                }
                
                // Reverse the rotation: find what position would have resulted in currentIndex
                // Forward: newPos = (oldPos + 1 + oldPos + (oldPos >= 4 ? 1 : 0)) % len
                //         = (oldPos * 2 + 1 + (oldPos >= 4 ? 1 : 0)) % len
                // We need to find oldPos such that forward(oldPos) = currentIndex
                int originalIndex = -1;
                for (int oldPos = 0; oldPos < chars.length; oldPos++) {
                    int rotations = 1 + oldPos;
                    if (oldPos >= 4) {
                        rotations++;
                    }
                    int newPos = (oldPos + rotations) % chars.length;
                    if (newPos == currentIndex) {
                        originalIndex = oldPos;
                        break;
                    }
                }
                
                // Rotate left to get back to original position
                if (originalIndex != -1) {
                    int rotations = 1 + originalIndex;
                    if (originalIndex >= 4) {
                        rotations++;
                    }
                    rotateLeft(chars, rotations);
                }
            } else if (operation.startsWith("reverse positions")) {
                // reverse positions X through Y - symmetric, same operation
                String[] parts = operation.split(" ");
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[4]);
                reverse(chars, x, y);
            } else if (operation.startsWith("move position")) {
                // move position X to position Y - reverse is move position Y to position X
                String[] parts = operation.split(" ");
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[5]);
                move(chars, y, x);
            }
        }
        
        return new String(chars);
    }

    @Override
    public String getPart2Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        return unscramble("fbgdceah", lines);
    }
}
