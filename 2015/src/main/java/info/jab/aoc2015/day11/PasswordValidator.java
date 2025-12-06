package info.jab.aoc2015.day11;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.stream.IntStream;

public class PasswordValidator implements Solver<String> {

    @Override
    public String solvePartOne(final String fileName) {
        String currentPassword = ResourceLines.line(fileName);
        return findNextValidPassword(currentPassword);
    }

    @Override
    public String solvePartTwo(final String fileName) {
        String currentPassword = ResourceLines.line(fileName);
        String firstPassword = findNextValidPassword(currentPassword);
        return findNextValidPassword(firstPassword);
    }

    private String findNextValidPassword(String currentPassword) {
        String nextPassword = currentPassword;
        do {
            nextPassword = incrementPassword(nextPassword);
        } while (!isValidPassword(nextPassword));
        return nextPassword;
    }

    private String incrementPassword(String password) {
        char[] chars = password.toCharArray();
        int i = chars.length - 1;
        
        while (i >= 0) {
            chars[i] = (char) (chars[i] + 1);
            if (chars[i] > 'z') {
                chars[i] = 'a';
                i--;
            } else {
                if (isProhibitedLetter(chars[i])) {
                    chars[i]++;
                    // Reset all following characters to 'a'
                    for (int j = i + 1; j < chars.length; j++) {
                        chars[j] = 'a';
                    }
                }
                break;
            }
        }
        return new String(chars);
    }

    private boolean containsProhibitedLetters(String password) {
        return password.chars()
                .mapToObj(c -> (char) c)
                .anyMatch(this::isProhibitedLetter);
    }

    private boolean containsStraightSequence(String password) {
        return IntStream.range(0, password.length() - 2)
                .anyMatch(i -> {
                    char current = password.charAt(i);
                    return password.charAt(i + 1) == current + 1 &&
                           password.charAt(i + 2) == current + 2;
                });
    }

    private boolean containsTwoDifferentPairs(String password) {
        int pairCount = 0;
        char lastPairChar = '\0';
        
        int i = 0;
        while (i < password.length() - 1) {
            if (password.charAt(i) == password.charAt(i + 1) && password.charAt(i) != lastPairChar) {
                pairCount++;
                lastPairChar = password.charAt(i);
                i += 2; // Skip both characters as they're part of the pair
            } else {
                i++;
            }
        }
        return pairCount >= 2;
    }

    private static final char[] PROHIBITED_LETTERS = {'i', 'o', 'l'};

    private boolean isProhibitedLetter(char c) {
        for (char prohibited : PROHIBITED_LETTERS) {
            if (c == prohibited) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidPassword(String password) {
        return !containsProhibitedLetters(password) &&
                containsStraightSequence(password) &&
                containsTwoDifferentPairs(password);
    }
}
