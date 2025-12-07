package info.jab.aoc2016.day21;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.ArrayList;
import java.util.List;

/**
 * Solver for Day 21: Scrambled Letters and Hash
 * Performs string scrambling and unscrambling operations.
 */
public final class ScrambledLettersAndHash implements Solver<String> {

    @Override
    public String solvePartOne(final String fileName) {
        var lines = ResourceLines.list(fileName);
        return scramble("abcdefgh", lines);
    }

    @Override
    public String solvePartTwo(final String fileName) {
        var lines = ResourceLines.list(fileName);
        return unscramble("fbgdceah", lines);
    }

    private String scramble(final String password, final List<String> operations) {
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
    
    private void rotateLeft(final char[] chars, final int steps) {
        int actualSteps = steps % chars.length;
        for (int i = 0; i < actualSteps; i++) {
            char first = chars[0];
            System.arraycopy(chars, 1, chars, 0, chars.length - 1);
            chars[chars.length - 1] = first;
        }
    }
    
    private void rotateRight(final char[] chars, final int steps) {
        int actualSteps = steps % chars.length;
        for (int i = 0; i < actualSteps; i++) {
            char last = chars[chars.length - 1];
            System.arraycopy(chars, 0, chars, 1, chars.length - 1);
            chars[0] = last;
        }
    }
    
    private void reverse(final char[] chars, final int x, final int y) {
        int start = x;
        int end = y;
        while (start < end) {
            char temp = chars[start];
            chars[start] = chars[end];
            chars[end] = temp;
            start++;
            end--;
        }
    }
    
    private void move(final char[] chars, final int fromPosition, final int toPosition) {
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

    private String unscramble(final String password, final List<String> operations) {
        char[] chars = password.toCharArray();
        
        // Process operations in reverse order
        for (int i = operations.size() - 1; i >= 0; i--) {
            String operation = operations.get(i);
            applyUnscrambleOperation(chars, operation);
        }
        
        return new String(chars);
    }

    private void applyUnscrambleOperation(final char[] chars, final String operation) {
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

    private void handleUnscrambleSwapPosition(final char[] chars, final String operation) {
        String[] parts = operation.split(" ");
        int x = Integer.parseInt(parts[2]);
        int y = Integer.parseInt(parts[5]);
        char temp = chars[x];
        chars[x] = chars[y];
        chars[y] = temp;
    }

    private void handleUnscrambleSwapLetter(final char[] chars, final String operation) {
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

    private void handleUnscrambleRotateLeft(final char[] chars, final String operation) {
        String[] parts = operation.split(" ");
        int steps = Integer.parseInt(parts[2]);
        rotateRight(chars, steps);
    }

    private void handleUnscrambleRotateRight(final char[] chars, final String operation) {
        String[] parts = operation.split(" ");
        int steps = Integer.parseInt(parts[2]);
        rotateLeft(chars, steps);
    }

    private void handleUnscrambleRotateBasedOnPosition(final char[] chars, final String operation) {
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

    private int findLetterIndex(final char[] chars, final char letter) {
        for (int j = 0; j < chars.length; j++) {
            if (chars[j] == letter) {
                return j;
            }
        }
        return -1;
    }

    private int findOriginalIndexForRotation(final int length, final int currentIndex) {
        for (int oldPos = 0; oldPos < length; oldPos++) {
            int rotations = calculateRotations(oldPos);
            int newPos = (oldPos + rotations) % length;
            if (newPos == currentIndex) {
                return oldPos;
            }
        }
        return -1;
    }

    private int calculateRotations(final int position) {
        int rotations = 1 + position;
        if (position >= 4) {
            rotations++;
        }
        return rotations;
    }

    private void handleUnscrambleReversePositions(final char[] chars, final String operation) {
        String[] parts = operation.split(" ");
        int x = Integer.parseInt(parts[2]);
        int y = Integer.parseInt(parts[4]);
        reverse(chars, x, y);
    }

    private void handleUnscrambleMovePosition(final char[] chars, final String operation) {
        String[] parts = operation.split(" ");
        int x = Integer.parseInt(parts[2]);
        int y = Integer.parseInt(parts[5]);
        move(chars, y, x);
    }
}

